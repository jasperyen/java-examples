/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Components;

import static DataBaseInfo.DataBaseConn.DBpassword;
import static DataBaseInfo.DataBaseConn.DBurl;
import static DataBaseInfo.DataBaseConn.DBuser;
import DataBaseInfo.DataBaseName.db;
import Servelt.MySQLSyntax;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Jasper-Yen
 */
public abstract class ComponentSet {
    private String ComponentName;
    private List<String>AllKeyList;
    private String TableName;
    
    private Map<Integer, String> MainColumn;
    private Map<Integer, String> GeneraColumn;
    private Map<Integer, String> ForeignColumn;
    
    private List<Component> comsList = new ArrayList<Component>();

    
    
    Logger logger;
    
    public ComponentSet() {
        logger = Logger.getLogger(this.getClass().getName());
    }

    public String getComponentName() {
        return ComponentName;
    }

    protected void setComponentName(String ComponentName) {
        this.ComponentName = ComponentName;
    }

    protected List<String> getAllKeyList() {
        return AllKeyList;
    }

    protected void setAllKeyList(List<String> AllKeyList) {
        this.AllKeyList = AllKeyList;
    }

    protected String getTableName() {
        return TableName;
    }

    protected void setTableName(String TableName) {
        this.TableName = TableName;
    }

    protected void setMainColumn(Map<Integer, String> MainColumn) {
        this.MainColumn = MainColumn;
    }

    protected void setGeneraColumn(Map<Integer, String> GeneraColumn) {
        this.GeneraColumn = GeneraColumn;
    }

    protected void setForeignColumn(Map<Integer, String> ForeignColumn) {
        this.ForeignColumn = ForeignColumn;
    }
    
    
    
    /**
     * 
     * @return Component newComponent()
     */
    public abstract Component newComponent();
    
    
    /**
     * 傳入Component, 並將其加入ComponentSet
     * 
     * @param Component com 
     */
    public void addComponent(Component com){
        comsList.add(com);
    }
    
    
    /**
     * 以List<String>型態傳入Component, 並將其加入ComponentSet
     * 
     * @param list 
     */
    public void addComponentbyList(List<String> list){
        Component com = newComponent();
        com.setByList(list);
        addComponent(com);
        //System.out.println(com.ComponentToString());
    }

    
    /**
     * 將ComponentSet以List<Component>型態回傳
     * 
     * @return List<Component>
     */
    public List<Component> getComList() {
        return comsList;
    }

    /**
     * 
     * @return Map<Integer, String> MainKey所在欄位, 名稱
     */
    public Map<Integer, String> getMainColumn() {
        return MainColumn;
    }

    /**
     * 
     * @return Map<Integer, String> GeneraKey所在欄位, 名稱
     */
    public Map<Integer, String> getGeneraColumn() {
        return GeneraColumn;
    }

    /**
     * 
     * @return Map<Integer, String> FoerignKey所在欄位, 名稱
     */
    public Map<Integer, String> getForeignColumn() {
        return ForeignColumn;
    }
    
    
    
    /**
     * 傳入Map<String, String> searchMap 搜尋條件 
     * 並將結果加入此 ComponentSet
     * 回傳 ResultSet
     * 
     * @param Statement stm
     * @param Map<String, String> searchMap 搜尋條件
     * @return ResultSet searchMap搜尋結果
     * @throws SQLException 
     */
    public ResultSet addComponentBySearchMap(Statement stm, Map<String, String> searchMap) throws SQLException {
        ResultSet result = MySQLSyntax.getResultSet(stm, getTableName(), searchMap);
        Component com;
        
        while (result.next()) {
            com = newComponent();
            for (int i = 1; i <= result.getMetaData().getColumnCount(); i++) {
                com.setComponentByColumn(i-1, result.getString(i));
            }
            addComponent(com);
        }
        
        result.beforeFirst();
        return result;
    }
    
    
    /**
     * 回傳所有需要檢查之表格名稱, 欄位名稱
     * 
     * @return Map<String, String> 表格名稱, 欄位名稱
     */
    protected abstract Map<String, String> getCheckTableName();
    
    
    /**
     * 回傳所有需要檢查表格之欄位名稱, 欄位資料List<String>
     * 
     * @return Map<String, String> 欄位名稱, 欄位資料
     */
    private Map<String, List<String>> getCheckMap() {
        Map checkMap = new TreeMap<String, List<String>>();
        Map<String, String> tableMap = getCheckTableName();
        
        try( Connection conn = DriverManager.getConnection(DBurl, DBuser, DBpassword) ){
            Statement stm = conn.createStatement();
            ResultSet result;
            List checkList;
            
            for (Iterator iter = tableMap.entrySet().iterator(); iter.hasNext(); ) {
                Entry<String, String> entry = (Entry<String, String>) iter.next();
                result = MySQLSyntax.getResultSet(stm, entry.getKey());
                checkList= new ArrayList<String>();
                while (result.next()) {
                    checkList.add(result.getString(entry.getValue()));
                }
                checkMap.put(entry.getValue(), checkList);
            }
        }catch(SQLException ex){
            logger.log(Level.WARNING, "In SQLException (getCheckMap) : " + ex.toString());
            ex.printStackTrace();
        }

        return checkMap;
    }
    
    
    /**
     * 檢查所有Component是否正確 並回傳table
     * 
     * @return String HTML <table> 檢查過的ComponentSet 
     */
    public String getComponentSetTable(){
        Map<String, List<String>> checkMap = getCheckMap();
        
        String inputStr = "";
        
        String corrStr = "            <p>是否將修改下列"+ getComponentName() +"資料庫內資料 ? </p>\n" + 
                                     "            <table border=\"1\">\n"+
                                     "                <tr>";
        String wrongStr = "            <p>下列"+ getComponentName() +"中有錯誤的資訊 無法新增至資料庫內 </p>\n" +
                                          "            <table border=\"1\">\n"+
                                          "                <tr>";
        
        for(Object obj : getAllKeyList()){
            corrStr = corrStr + "<td>" + (String)obj + "</td>";
            wrongStr = wrongStr + "<td>" + (String)obj + "</td>";
        }
        corrStr = corrStr + "</tr>\n";
        wrongStr = wrongStr +"</tr>\n";
        
        int i = 0;
        boolean wrong = false;
        for (Component com : comsList) {
            List wrongList = com.getWrongComponentList(checkMap);
            //System.out.println(com.ComponentToString());
            
            if (wrongList.isEmpty()) {
                corrStr = corrStr + com.getComponentTableRow(wrongList);
                inputStr = inputStr + com.getComponentFormInput(i++);
            }
            else{
                wrongStr = wrongStr + com.getComponentTableRow(wrongList);
                wrong = true;
            }
        }
        
        corrStr = corrStr + "            </table>\n";
        wrongStr = wrongStr + "            </table>\n";
        
        if (wrong)
            return inputStr + corrStr + wrongStr ;
        else
            return inputStr + corrStr;
    }
    
}
