/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Components;

import DataBaseInfo.DataBaseName.db;
import DataBaseInfo.DataBaseName.db2;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 *
 * @author Jasper-Yen
 */
public class KeyboardSet extends ComponentSet {

    public KeyboardSet() {
        setComponentName(Keyboard.ComponentName);
        setTableName(Keyboard.TableName);
        setAllKeyList(Keyboard.AllKeyList);
        
        Map<Integer, String> map = new TreeMap();
        map.put(3, "Model Name");
        setMainColumn(map);
        
        map = new TreeMap();
        map.put(4, "Description");
        map.put(5, "Layout");
        setGeneraColumn(map);
                
        map = new TreeMap();
        map.put(0, "Vendor");
        map.put(1, "Type");
        map.put(2, "OS");
        setForeignColumn(map);
    }
    
    @Override
    public Keyboard newComponent(){
        return new Keyboard();
    }
    
    @Override
    protected Map<String, String> getCheckTableName() {
        Map tableMap = new TreeMap<String, String>();
        tableMap.put(db.vendor_Table.TABLE_NAME, db.vendor_Table.Vendor);
        tableMap.put(db.keyboard_type_Table.TABLE_NAME, db.keyboard_type_Table.Type);
        tableMap.put(db.keyboard_os_Table.TABLE_NAME, db.keyboard_os_Table.OS);
        return tableMap;
    }
    
    public static class Keyboard extends Component {
        public static final String ComponentName;
        public static final String MainKey;
        public static final String TableName;
        public static final String ProjectTableName;
        public static final List<String> GeneralKeyList;
        public static final List<String> ForeignKeyList;
        public static final List<String>AllKeyList;
        
        private String Vendor = "";
        private String Type = "";
        private String OS = "";
        private String ModelName = "";
        private String Description = "";
        private String Layout = "";

        static {
            ComponentName = "Keyboard";
            MainKey = db.keyboard_Table.ModelName;
            TableName = db.keyboard_Table.TABLE_NAME;
            ProjectTableName = db2.project_keyboard_Table.TABLE_NAME;

            List<String> list = new ArrayList();
            list.add(db.keyboard_Table.Vendor);
            list.add(db.keyboard_Table.Type);
            list.add(db.keyboard_Table.OS);
            list.add(db.keyboard_Table.ModelName);
            list.add(db.keyboard_Table.Description);
            list.add(db.keyboard_Table.Layout);
            AllKeyList = list;

            list = new ArrayList();
            list.add(db.keyboard_Table.Vendor);
            list.add(db.keyboard_Table.Type);
            list.add(db.keyboard_Table.OS);
            ForeignKeyList = list;

            list = new ArrayList();
            list.add(db.keyboard_Table.Description);
            list.add(db.keyboard_Table.Layout);
            GeneralKeyList = list;
        }

        public Keyboard() {
            setComponentName(ComponentName);
            setTableName(TableName);
            setAllKeyList(AllKeyList);
            
            initializeComList();
        }

        public String getVendor() {
            return Vendor;
        }

        public void setVendor(String Vendor) {
            this.Vendor = Vendor;
            comList.set(0, Vendor);
        }

        public String getType() {
            return Type;
        }

        public void setType(String Type) {
            this.Type = Type;
            comList.set(1, Type);
        }

        public String getOS() {
            return OS;
        }

        public void setOS(String OS) {
            this.OS = OS;
            comList.set(2, OS);
        }

        public String getModelName() {
            return ModelName;
        }

        public void setModelName(String ModelName) {
            this.ModelName = ModelName;
            comList.set(3, ModelName);
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String Description) {
            this.Description = Description;
            comList.set(4, Description);
        }

        public String getLayout() {
            return Layout;
        }

        public void setLayout(String Layout) {
            this.Layout = Layout;
            comList.set(5, Layout);
        }
        
        
        @Override
        public void setComponentByColumn (int col, String data) {
            switch (col){
                    case 0 :
                        setVendor(data);
                        break;
                    case 1 :
                        setType(data);
                        break;
                    case 2 :
                        setOS(data);
                        break;
                    case 3 :
                        setModelName(data);
                        break;
                    case 4 :
                        setDescription(data);
                        break;
                    case 5 :
                        setLayout(data);
                        break;
                }
        }
        
        @Override
        public List  getWrongComponentList(Map<String, List<String>> checkMap) {
            List wrongList = new ArrayList<String>();
            
            if (ModelName.equals(""))
                wrongList.add(MainKey);
            
            for (Iterator iter = checkMap.entrySet().iterator(); iter.hasNext(); ) {
                Entry<String, List<String>> entry = (Entry<String, List<String>>) iter.next();
                boolean boo = false;
                switch (entry.getKey()) {
                    case db.vendor_Table.Vendor : 
                        for (Object obj : entry.getValue()) {
                            if (Vendor.equals((String)obj))
                                boo = true;
                        }
                        if (!boo)
                            wrongList.add(db.keyboard_Table.Vendor);
                        break;
                    case db.keyboard_type_Table.Type : 
                        for (Object obj : entry.getValue()) {
                            if (Type.equals((String)obj))
                                boo = true;
                        }
                        if (!boo)
                            wrongList.add(db.keyboard_Table.Type);
                        break;
                    case db.keyboard_os_Table.OS : 
                        for (Object obj : entry.getValue()) {
                            if (OS.equals((String)obj))
                                boo = true;
                        }
                        if (!boo)
                            wrongList.add(db.keyboard_Table.OS);
                        break;
                }
            }
            return wrongList;
        }
        
        @Override
        public String getComponentTableRow(List<String> wrongList){
            String vtd = "<td>";
            String ttd = "<td>";
            String otd = "<td>";
            for (Object obj : wrongList) {
                switch ((String)obj) {
                    case db.keyboard_Table.Vendor : {
                       vtd = "<td style=\"background-color:#FFD78C;\">";
                    }
                    break;
                    case db.keyboard_type_Table.Type : {
                        ttd = "<td style=\"background-color:#FFD78C;\">";
                    }
                    break;
                    case db.keyboard_os_Table.OS : {
                        otd = "<td style=\"background-color:#FFD78C;\">";
                    }
                    break;
                }
            }
            return "                <tr>" + vtd+ Vendor +"</td>" + ttd + Type +"</td>" + otd + OS +"</td><td>"+
                        ModelName +"</td><td>"+ Description +"</td><td>"+ Layout +"</td></tr>\n";
        }
        
        
    }
    
}
