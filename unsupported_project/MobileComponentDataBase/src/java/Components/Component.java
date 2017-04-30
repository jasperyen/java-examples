/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

/**
 *
 * @author Jasper-Yen
 */
public abstract class Component {    
    protected List<String> comList = new ArrayList<String>();
    
    private String ComponentName;
    private String TableName;
    private List<String>AllKeyList;

    protected String getComponentName() {
        return ComponentName;
    }

    protected void setComponentName(String ComponentName) {
        this.ComponentName = ComponentName;
    }

    protected String getTableName() {
        return TableName;
    }

    protected void setTableName(String TableName) {
        this.TableName = TableName;
    }

    protected List<String> getAllKeyList() {
        return AllKeyList;
    }

    protected void setAllKeyList(List<String> AllKeyList) {
        this.AllKeyList = AllKeyList;
    }
    
    
    
    protected void initializeComList () {
        for (int i = 0; i < AllKeyList.size(); i++) {
            comList.add("");
        }
    }
    
    /**
     * 傳入欄位數及資料, 設定Component
     * 
     * @param col
     * @param data 
     */
    public abstract void setComponentByColumn (int col, String data);
    
    
    /**
     * 傳入checkMap 檢查資訊 檢查此Component是否正確 
     * 並回傳錯誤資訊
     * 
     * @param Map<String, List<String>> checkMap 檢查資訊
     * @return List<String> 錯誤資訊
     */
    public abstract List<String>  getWrongComponentList(Map<String, List<String>> checkMap);
    
    
    /**
     * toString
     * 
     * @return String 
     */
    public String ComponentToString(){
        String str = "---------------------"+ ComponentName +"---------------------";
        
        for (int i = 0; i < AllKeyList.size(); i++) {
            str = str +  "\n"+ AllKeyList.get(i) +" : " + comList.get(i);
        }
        
        return str;
    }
    
    
    /**
     * 
     * @param wrongList 錯誤資訊
     * @return String <tr> 將錯誤訊息上色後回傳Component
     */
    public abstract String getComponentTableRow(List<String> wrongList);
    
    
    /**
     * 
     * @param i 第i個Input
     * @return String <input> 將Component轉換為 hidden input
     */
    public String getComponentFormInput(int i) {
        String str = "";

        for (int j = 0; j < AllKeyList.size(); j++) {
            str = str + "            <input type=\"hidden\" name=\""+ TableName + "_" + AllKeyList.get(j) +"_"+i+"\" value=\""+ comList.get(j) +"\">\n";
        }
        
        return str;
    }
    
    
    /**
     * 
     * @return List<String> 將Component依項目加入List<String>
     */
    public List<String> getComponent() {
     return comList;
    }
    
    
    /**
     * 
     * @param 傳入List<String> Component訊息 並加入
     */
    public void setByList(List<String> list){
        for (int i = 0; i < AllKeyList.size(); i++) {
            //System.out.println(i + " ~ " + list.get(i));
            setComponentByColumn(i, list.get(i));
        }
    }
    
}
