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
public class StorageSet extends ComponentSet{
     

    public StorageSet() {
        setComponentName(Storage.ComponentName);
        setTableName(Storage.TableName);
        setAllKeyList(Storage.AllKeyList);
        
        Map<Integer, String> map = new TreeMap();
        map.put(1, "Model");
        setMainColumn(map);
        
        map = new TreeMap();
        map.put(2, "Capacity (GB)");
        map.put(3, "Interface");
        map.put(4, "Rpm");
        map.put(5, "Speed");
        map.put(6, "z-Hight");
        map.put(7, "Type");
        setGeneraColumn(map);
                
        map = new TreeMap();
        map.put(0, "Vendor");
        setForeignColumn(map);
    }
    
    @Override
    public Storage newComponent(){
        return new Storage();
    }
    
    @Override
    protected Map<String, String> getCheckTableName() {
        Map tableMap = new TreeMap<String, String>();
        tableMap.put(db.vendor_Table.TABLE_NAME, db.vendor_Table.Vendor);
        return tableMap;
    }
    
    public static class Storage extends Component {
        public static final String ComponentName;
        public static final String MainKey;
        public static final String TableName;
        public static final String ProjectTableName;
        public static final List<String> GeneralKeyList;
        public static final List<String> ForeignKeyList;
        public static final List<String>AllKeyList;
        
        private String Vendor = "";
        private String Model = "";
        private String Capacity = "";
        private String Interface = "";
        private String Rpm = "";
        private String Speed = "";
        private String zHight = "";
        private String Type = "";

        static {
            ComponentName = "Storage";
            MainKey = db.storage_Table.Model;
            TableName = db.storage_Table.TABLE_NAME;
            ProjectTableName = db2.project_storage_Table.TABLE_NAME;
            
            List<String> list = new ArrayList();
            list.add(db.storage_Table.Vendor);
            list.add(db.storage_Table.Model);
            list.add(db.storage_Table.Capacity);
            list.add(db.storage_Table.Interface);
            list.add(db.storage_Table.Rpm);
            list.add(db.storage_Table.Speed);
            list.add(db.storage_Table.zHight);
            list.add(db.storage_Table.Type);
            AllKeyList = list;

            list = new ArrayList();
            list.add(db.storage_Table.Vendor);
            ForeignKeyList = list;

            list = new ArrayList();
            list.add(db.storage_Table.Capacity);
            list.add(db.storage_Table.Interface);
            list.add(db.storage_Table.Rpm);
            list.add(db.storage_Table.Speed);
            list.add(db.storage_Table.zHight);
            list.add(db.storage_Table.Type);
            GeneralKeyList = list;
        }
        
        public Storage() {
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

        public String getModel() {
            return Model;
        }

        public void setModel(String Model) {
            this.Model = Model;
            comList.set(1, Model);
        }

        public String getCapacity() {
            return Capacity;
        }

        public void setCapacity(String Capacity) {
            this.Capacity = Capacity;
            comList.set(2, Capacity);
        }

        public String getInterface() {
            return Interface;
        }

        public void setInterface(String Interface) {
            this.Interface = Interface;
            comList.set(3, Interface);
        }

        public String getRpm() {
            return Rpm;
        }

        public void setRpm(String Rpm) {
            this.Rpm = Rpm;
            comList.set(4, Rpm);
        }

        public String getSpeed() {
            return Speed;
        }

        public void setSpeed(String Speed) {
            this.Speed = Speed;
            comList.set(5, Speed);
        }

        public String getzHight() {
            return zHight;
        }

        public void setzHight(String zHight) {
            this.zHight = zHight;
            comList.set(6, zHight);
        }

        public String getType() {
            return Type;
        }

        public void setType(String Type) {
            this.Type = Type;
            comList.set(7, Type);
        }
        
        @Override
        public void setComponentByColumn (int col, String data) {
            switch (col){
                    case 0 :
                        setVendor(data);
                        break;
                    case 1 :
                        setModel(data);
                        break;
                    case 2 :
                        setCapacity(data);
                        break;
                    case 3 :
                        setInterface(data);
                        break;
                    case 4 :
                        setRpm(data);
                        break;
                    case 5 :
                        setSpeed(data);
                        break;
                    case 6 :
                        setzHight(data);
                        break;
                    case 7 :
                        setType(data);
                        break;
                }
        }
        
        @Override
        public List  getWrongComponentList(Map<String, List<String>> checkMap) {
            List wrongList = new ArrayList<String>();
            
            if (Model.equals(""))
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
                            wrongList.add(db.storage_Table.Vendor);
                        break;
                }
            }
            return wrongList;
        }
        
        @Override
        public String getComponentTableRow(List<String> wrongList){
            String vtd = "<td>";
            for (Object obj : wrongList) {
                switch ((String)obj) {
                    case db.storage_Table.Vendor : {
                       vtd = "<td style=\"background-color:#FFD78C;\">";
                    }
                    break;
                }
            }
            return "                <tr>" + vtd+ Vendor +"</td><td>"+ Model +"</td><td>"+ Capacity +"</td><td>"+ Interface +"</td><td>"+ Rpm +"</td>"
                    + "<td>"+ Speed +"</td><td>"+ zHight +"</td><td>"+ Type +"</td></tr>\n";
        }
    }
}
