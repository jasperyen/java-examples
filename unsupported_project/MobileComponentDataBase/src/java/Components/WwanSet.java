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
import java.util.TreeMap;

/**
 *
 * @author Jasper-Yen
 */
public class WwanSet extends ComponentSet {

    public WwanSet() {
        setComponentName(WWAN.ComponentName);
        setTableName(WWAN.TableName);
        setAllKeyList(WWAN.AllKeyList);
        
        Map<Integer, String> map = new TreeMap();
        map.put(2, "Model Name");
        setMainColumn(map);
        
        map = new TreeMap();
        map.put(3, "Description");
        setGeneraColumn(map);
                
        map = new TreeMap();
        map.put(0, "Vendor");
        map.put(1, "Type");
        setForeignColumn(map);
    }
    
    @Override
    public WWAN newComponent(){
        return new WWAN();
    }
    
    @Override
    protected Map<String, String> getCheckTableName() {
        Map tableMap = new TreeMap<String, String>();
        tableMap.put(db.vendor_Table.TABLE_NAME, db.vendor_Table.Vendor);
        tableMap.put(db.wwan_type_Table.TABLE_NAME, db.wwan_type_Table.Type);
        return tableMap;
    }
    
    public static class WWAN extends Component {
        public static final String ComponentName;
        public static final String MainKey;
        public static final String TableName;
        public static final String ProjectTableName;
        public static final List<String> GeneralKeyList;
        public static final List<String> ForeignKeyList;
        public static final List<String>AllKeyList;
        
        private String Vendor = "";
        private String Type = "";
        private String ModelName = "";
        private String Description = "";

        static {
            ComponentName = "WWAN";
            MainKey = db.wwan_Table.ModelName;
            TableName = db.wwan_Table.TABLE_NAME;
            ProjectTableName = db2.project_wwan_Table.TABLE_NAME;
            
            List<String> list = new ArrayList();
            list.add(db.wwan_Table.Vendor);
            list.add(db.wwan_Table.Type);
            list.add(db.wwan_Table.ModelName);
            list.add(db.wwan_Table.Description);
            AllKeyList = list;

            list = new ArrayList();
            list.add(db.wwan_Table.Vendor);
            list.add(db.wwan_Table.Type);
            ForeignKeyList = list;

            list = new ArrayList();
            list.add(db.wwan_Table.Description);
            GeneralKeyList = list;
        }
        
        public WWAN() {
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

        public String getModelName() {
            return ModelName;
        }

        public void setModelName(String ModelName) {
            this.ModelName = ModelName;
            comList.set(2, ModelName);
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String Description) {
            this.Description = Description;
            comList.set(3, Description);
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
                        setModelName(data);
                        break;
                    case 3 :
                        setDescription(data);
                        break;
                }
        }
        
        @Override
        public List  getWrongComponentList(Map<String, List<String>> checkMap) {
            List wrongList = new ArrayList<String>();
            
            if (ModelName.equals(""))
                wrongList.add(MainKey);
            
            for (Iterator iter = checkMap.entrySet().iterator(); iter.hasNext(); ) {
                Map.Entry<String, List<String>> entry = (Map.Entry<String, List<String>>) iter.next();
                boolean boo = false;
                switch (entry.getKey()) {
                    case db.vendor_Table.Vendor : 
                        for (Object obj : entry.getValue()) {
                            if (Vendor.equals((String)obj))
                                boo = true;
                        }
                        if (!boo)
                            wrongList.add(db.wwan_Table.Vendor);
                        break;
                    case db.wwan_type_Table.Type : 
                        for (Object obj : entry.getValue()) {
                            if (Type.equals((String)obj))
                                boo = true;
                        }
                        if (!boo)
                            wrongList.add(db.wwan_Table.Type);
                        break;
                }
            }
            return wrongList;
        }
        
        
        @Override
        public String getComponentTableRow(List<String> wrongList){
            String vtd = "<td>";
            String ttd = "<td>";
            for (Object obj : wrongList) {
                switch ((String)obj) {
                    case db.wwan_Table.Vendor : {
                       vtd = "<td style=\"background-color:#FFD78C;\">";
                    }
                    break;
                    case db.wwan_Table.Type : {
                        ttd = "<td style=\"background-color:#FFD78C;\">";
                    }
                    break;
                }
            }
            return "                <tr>" + vtd+ Vendor +"</td>" + ttd + Type +"</td><td>"+ ModelName +"</td><td>"+
                        Description +"</td></tr>\n";
        }
    
    }
    
}
