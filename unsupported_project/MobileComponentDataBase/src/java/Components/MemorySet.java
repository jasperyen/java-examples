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
public class MemorySet extends ComponentSet {
    
    public MemorySet() {
        setComponentName(Memory.ComponentName);
        setTableName(Memory.TableName);
        setAllKeyList(Memory.AllKeyList);
        
        
        Map<Integer, String> map = new TreeMap();
        map.put(4, "Memory Name");
        setMainColumn(map);
        
        map = new TreeMap();
        map.put(5, "Description");
        setGeneraColumn(map);
                
        map = new TreeMap();
        map.put(0, "Vendor");
        map.put(1, "Type");
        map.put(2, "Frequency");
        map.put(3, "Capacity");
        setForeignColumn(map);
    }
    
    @Override
    public Memory newComponent(){
        return new Memory();
    }
    
    @Override
    protected Map<String, String> getCheckTableName() {
        Map tableMap = new TreeMap<String, String>();
        tableMap.put(db.vendor_Table.TABLE_NAME, db.memory_Table.Vendor);
        tableMap.put(db.memory_type_Table.TABLE_NAME, db.memory_Table.Type);
        tableMap.put(db.memory_frequency_Table.TABLE_NAME, db.memory_Table.Frequency);
        tableMap.put(db.memory_capacity_Table.TABLE_NAME, db.memory_Table.Capacity);
        return tableMap;
    }
    
    public static class Memory extends Component {
        public static final String ComponentName;
        public static final String MainKey;
        public static final String TableName;
        public static final String ProjectTableName;
        public static final List<String> GeneralKeyList;
        public static final List<String> ForeignKeyList;
        public static final List<String>AllKeyList;
        
        private String MemoryName = "";
        private String Type = "";
        private String Frequency = "";
        private String Capacity = "";
        private String Description = "";
        private String Vendor = "";
        
        static {
            ComponentName = "Memory";
            MainKey = db.memory_Table.MemoryName;
            TableName = db.memory_Table.TABLE_NAME;
            ProjectTableName = db2.project_memory_Table.TABLE_NAME;
            
            
            List<String> list = new ArrayList();
            list.add(db.memory_Table.MemoryName);
            list.add(db.memory_Table.Type);
            list.add(db.memory_Table.Frequency);
            list.add(db.memory_Table.Capacity);
            list.add(db.memory_Table.Description);
            list.add(db.memory_Table.Vendor);
            AllKeyList = list;

            list = new ArrayList();
            list.add(db.memory_Table.Type);
            list.add(db.memory_Table.Frequency);
            list.add(db.memory_Table.Capacity);
            list.add(db.memory_Table.Vendor);
            ForeignKeyList = list;

            list = new ArrayList();
            list.add(db.memory_Table.Description);
            GeneralKeyList = list;
        }
        
        public Memory() {
            setComponentName(ComponentName);
            setTableName(TableName);
            setAllKeyList(AllKeyList);
            
            initializeComList();
        }

        public String getMemoryName() {
            return MemoryName;
        }

        public void setMemoryName(String MemoryName) {
            this.MemoryName = MemoryName;
            comList.set(4, MemoryName);
        }

        public String getType() {
            return Type;
        }

        public void setType(String Type) {
            this.Type = Type;
            comList.set(1, Type);
        }

        public String getFrequency() {
            return Frequency;
        }

        public void setFrequency(String Frequency) {
            this.Frequency = Frequency;
            comList.set(2, Frequency);
        }

        public String getCapacity() {
            return Capacity;
        }

        public void setCapacity(String Capacity) {
            this.Capacity = Capacity;
            comList.set(3, Capacity);
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String Description) {
            this.Description = Description;
            comList.set(5, Description);
        }

        public String getVendor() {
            return Vendor;
        }

        public void setVendor(String Vendor) {
            this.Vendor = Vendor;
            comList.set(0, Vendor);
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
                        setFrequency(data);
                        break;
                    case 3 :
                        setCapacity(data);
                        break;
                    case 4 :
                        setMemoryName(data);
                        break;
                    case 5 :
                        setDescription(data);
                        break;
                }
        }
        
        @Override
        public List  getWrongComponentList(Map<String, List<String>> checkMap) {
            List wrongList = new ArrayList<String>();
            
            if (MemoryName.equals(""))
                wrongList.add(MainKey);
            
            for (Iterator iter = checkMap.entrySet().iterator(); iter.hasNext(); ) {
                Map.Entry<String, List<String>> entry = (Map.Entry<String, List<String>>) iter.next();
                boolean boo = false;
                switch (entry.getKey()) {
                    case db.memory_type_Table.Type : 
                        for (Object obj : entry.getValue()) {
                            if (Type.equals((String)obj))
                                boo = true;
                        }
                        if (!boo)
                            wrongList.add(db.memory_Table.Type);
                        break;
                    case db.memory_frequency_Table.Frequency : 
                        for (Object obj : entry.getValue()) {
                            if (Frequency.equals((String)obj))
                                boo = true;
                        }
                        if (!boo)
                            wrongList.add(db.memory_Table.Frequency);
                        break;
                    case db.memory_capacity_Table.Capacity : 
                        for (Object obj : entry.getValue()) {
                            if (Capacity.equals((String)obj))
                                boo = true;
                        }
                        if (!boo)
                            wrongList.add(db.memory_Table.Capacity);
                        break;
                    case db.vendor_Table.Vendor : 
                        for (Object obj : entry.getValue()) {
                            if (Vendor.equals((String)obj))
                                boo = true;
                        }
                        if (!boo)
                            wrongList.add(db.memory_Table.Vendor);
                        break;
                }
            }
            return wrongList;
        }
        
        @Override
        public String getComponentTableRow(List<String> wrongList){
            String ttd = "<td>";
            String ftd = "<td>";
            String ctd = "<td>";
            String vtd = "<td>";
            
            for (Object obj : wrongList) {
                switch ((String)obj) {
                    case db.memory_Table.Type : {
                       ttd = "<td style=\"background-color:#FFD78C;\">";
                    }
                    break;
                    case db.memory_Table.Frequency : {
                        ftd = "<td style=\"background-color:#FFD78C;\">";
                    }
                    break;
                    case db.memory_Table.Capacity : {
                        ctd = "<td style=\"background-color:#FFD78C;\">";
                    }
                    break;
                    case db.memory_Table.Vendor : {
                        vtd = "<td style=\"background-color:#FFD78C;\">";
                    }
                    break;
                }
            }
            return "                <tr><td>" + MemoryName +"</td>" + vtd + Vendor +"</td>"  + ttd + Type +"</td>" + ftd+ Frequency +"</td>"+ ctd +
                        Capacity +"</td><td>" + Description +"</td></tr>\n";
        }
    
    }
}
