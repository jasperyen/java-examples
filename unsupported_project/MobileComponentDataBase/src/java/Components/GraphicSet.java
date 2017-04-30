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
public class GraphicSet extends ComponentSet {

    public GraphicSet() {
        setComponentName(Graphic.ComponentName);
        setTableName(Graphic.TableName);
        setAllKeyList(Graphic.AllKeyList);
        
        Map<Integer, String> map = new TreeMap();
        map.put(1, "Model Name");
        setMainColumn(map);
        
        map = new TreeMap();
        map.put(2, "Max Freq. (GHz)");
        map.put(3, "Memory Type");
        map.put(4, "vRAM size");
        map.put(5, "Description");
        setGeneraColumn(map);
                
        map = new TreeMap();
        map.put(0, "Vendor");
        setForeignColumn(map);
    }
    
    @Override
    public Graphic newComponent(){
        return new Graphic();
    }
    
    @Override
    protected Map<String, String> getCheckTableName() {
        Map tableMap = new TreeMap<String, String>();
        tableMap.put(db.vendor_Table.TABLE_NAME, db.vendor_Table.Vendor);
        return tableMap;
    }
    
    public static class Graphic extends Component {
        public static final String ComponentName;
        public static final String MainKey;
        public static final String TableName;
        public static final String ProjectTableName;
        public static final List<String> GeneralKeyList;
        public static final List<String> ForeignKeyList;
        public static final List<String>AllKeyList;
        
        private String Vendor = "";
        private String ModelName = "";
        private String MaxCoreFreq = "";
        private String MemoryType = "";
        private String vRAM_size = "";
        private String Description = "";
    
        static {
            ComponentName = "Graphic";
            MainKey = db.graphic_Table.ModelName;
            TableName = db.graphic_Table.TABLE_NAME;
            ProjectTableName = db2.project_graphic_Table.TABLE_NAME;
            
            List<String> list = new ArrayList();
            list.add(db.graphic_Table.Vendor);
            list.add(db.graphic_Table.ModelName);
            list.add(db.graphic_Table.MaxCoreFreq);
            list.add(db.graphic_Table.MemoryType);
            list.add(db.graphic_Table.vRAM_size);
            list.add(db.graphic_Table.Description);
            AllKeyList = list;

            list = new ArrayList();
            list.add(db.graphic_Table.Vendor);
            ForeignKeyList = list;

            list = new ArrayList();
            list.add(db.graphic_Table.MaxCoreFreq);
            list.add(db.graphic_Table.MemoryType);
            list.add(db.graphic_Table.vRAM_size);
            list.add(db.graphic_Table.Description);
            GeneralKeyList = list;
        }

        public Graphic() {
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

        public String getModelName() {
            return ModelName;
        }

        public void setModelName(String ModelName) {
            this.ModelName = ModelName;
            comList.set(1, ModelName);
        }

        public String getMaxCoreFreq() {
            return MaxCoreFreq;
        }

        public void setMaxCoreFreq(String MaxCoreFreq) {
            this.MaxCoreFreq = MaxCoreFreq;
            comList.set(2, MaxCoreFreq);
        }

        public String getMemoryType() {
            return MemoryType;
        }

        public void setMemoryType(String MemoryType) {
            this.MemoryType = MemoryType;
            comList.set(3, MemoryType);
        }

        public String getvRAM_size() {
            return vRAM_size;
        }

        public void setvRAM_size(String vRAM_size) {
            this.vRAM_size = vRAM_size;
            comList.set(4, vRAM_size);
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String Description) {
            this.Description = Description;
            comList.set(5, Description);
        }
        
        @Override
        public void setComponentByColumn (int col, String data) {
            switch (col){
                    case 0 :
                        setVendor(data);
                        break;
                    case 1 :
                        setModelName(data);
                        break;
                    case 2 :
                        setMaxCoreFreq(data);
                        break;
                    case 3 :
                        setMemoryType(data);
                        break;
                    case 4 :
                        setvRAM_size(data);
                        break;
                    case 5 :
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
                Entry<String, List<String>> entry = (Entry<String, List<String>>) iter.next();
                boolean boo = false;
                switch (entry.getKey()) {
                    case db.vendor_Table.Vendor : 
                        for (Object obj : entry.getValue()) {
                            if (Vendor.equals((String)obj))
                                boo = true;
                        }
                        if (!boo)
                            wrongList.add(db.graphic_Table.Vendor);
                        break;
                }
            }
            return wrongList;
        }
        
        @Override
        public String getComponentTableRow(List<String> wrongList){
            String ventd = "<td>";
            for (Object obj : wrongList) {
                switch ((String)obj) {
                    case db.graphic_Table.Vendor : {
                       ventd = "<td style=\"background-color:#FFD78C;\">";
                    }
                    break;
                }
            }
            return "                <tr>" + ventd+ Vendor +"</td><td>"+ ModelName +"</td><td>"+
                        MaxCoreFreq +"</td><td>"+ MemoryType +"</td><td>"+ vRAM_size +"</td><td>"+ Description +"</td></tr>\n";
        }
        
    }
    
}
