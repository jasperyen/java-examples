/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Components;

import DataBaseInfo.DataBaseName.db;
import DataBaseInfo.DataBaseName.db2;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Jasper-Yen
 */
public class ButtonSet extends ComponentSet {
    
    public ButtonSet() {
        setComponentName(Button.ComponentName);
        setTableName(Button.TableName);
        setAllKeyList(Button.AllKeyList);
        
        Map<Integer, String> map = new TreeMap();
        map.put(0, "Type");
        setMainColumn(map);
        
        map = new TreeMap();
        map.put(1, "Description");
        setGeneraColumn(map);
                
        map = new TreeMap();
        setForeignColumn(map);
    }
    
    
    @Override
    public Button newComponent(){
        return new Button();
    }
    
    @Override
    protected Map<String, String> getCheckTableName() {
        Map tableMap = new TreeMap<String, String>();
        return tableMap;
    }
    
    public static class Button extends Component {
        public static final String ComponentName;
        public static final String MainKey;
        public static final String TableName;
        public static final String ProjectTableName;
        public static final List<String> GeneralKeyList;
        public static final List<String> ForeignKeyList;
        public static final List<String>AllKeyList;
        
        private String Type = "";
        private String Description = "";

        static {
            ComponentName = "Button";
            MainKey = db.button_Table.Type;
            TableName = db.button_Table.TABLE_NAME;
            ProjectTableName = db2.project_button_Table.TABLE_NAME;

            List<String> list = new ArrayList();
            list.add(db.button_Table.Type);
            list.add(db.button_Table.Description);
            AllKeyList = list;

            list = new ArrayList();
            ForeignKeyList = list;

            list = new ArrayList();
            list.add(db.button_Table.Description);
            GeneralKeyList = list;
        }
        
        public Button() {
            setComponentName(ComponentName);
            setTableName(TableName);
            setAllKeyList(AllKeyList);
            
            initializeComList();
        }

        public String getType() {
            return Type;
        }

        public void setType(String Type) {
            this.Type = Type;
            comList.set(0, Type);
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String Description) {
            this.Description = Description;
            comList.set(1, Description);
        }
        
        
        @Override
        public void setComponentByColumn (int col, String data) {
            switch (col){
                    case 0 :
                        setType(data);
                        break;
                    case 1 :
                        setDescription(data);
                        break;
                }
        }
        
        @Override
        public List  getWrongComponentList(Map<String, List<String>> checkMap) {
            List wrongList = new ArrayList<String>();
            
            if (Type.equals(""))
                wrongList.add(MainKey);
            
            return wrongList;
        }
            
        @Override
        public String getComponentTableRow(List<String> wrongList){
            return "                <tr><td>"+ Type +"</td><td>"+Description +"</td></tr>\n";
        }
        
    }
    
}
