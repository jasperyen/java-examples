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
public class ChargerSet extends ComponentSet {

    public ChargerSet() {
        setComponentName(Charger.ComponentName);
        setTableName(Charger.TableName);
        setAllKeyList(Charger.AllKeyList);
        
        Map<Integer, String> map = new TreeMap();
        map.put(1, "Model Name");
        setMainColumn(map);
        
        map = new TreeMap();
        map.put(2, "OUTPUT");
        map.put(3, "INPUT");
        map.put(4, "Type");
        map.put(5, "Description");
        setGeneraColumn(map);
                
        map = new TreeMap();
        map.put(0, "Vendor");
        setForeignColumn(map);
    }
    
    @Override
    public Charger newComponent(){
        return new Charger();
    }
    
    @Override
    protected Map<String, String> getCheckTableName() {
        Map tableMap = new TreeMap<String, String>();
        tableMap.put(db.vendor_Table.TABLE_NAME, db.vendor_Table.Vendor);
        return tableMap;
    }
    
    
    public static class Charger extends Component {
        public static final String ComponentName;
        public static final String MainKey;
        public static final String TableName;
        public static final String ProjectTableName;
        public static final List<String> GeneralKeyList;
        public static final List<String> ForeignKeyList;
        public static final List<String>AllKeyList;
        
        private String Vendor = "";
        private String ModelName = "";
        private String INPUT = "";
        private String OUTPUT = "";
        private String Type = "";
        private String Description = "";
        
        static {
            ComponentName = "Charger";
            MainKey = db.charger_Table.ModelName;
            TableName = db.charger_Table.TABLE_NAME;
            ProjectTableName = db2.project_charger_Table.TABLE_NAME;

            List<String> list = new ArrayList();
            list.add(db.charger_Table.Vendor);
            list.add(db.charger_Table.ModelName);
            list.add(db.charger_Table.INPUT);
            list.add(db.charger_Table.OUTPUT);
            list.add(db.charger_Table.Type);
            list.add(db.charger_Table.Description);
            AllKeyList = list;

            list = new ArrayList();
            list.add(db.charger_Table.Vendor);
            ForeignKeyList = list;

            list = new ArrayList();
            list.add(db.charger_Table.INPUT);
            list.add(db.charger_Table.OUTPUT);
            list.add(db.charger_Table.Type);
            list.add(db.charger_Table.Description);
            GeneralKeyList = list;
        }

        public Charger() {
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

        public String getINPUT() {
            return INPUT;
        }

        public void setINPUT(String INPUT) {
            this.INPUT = INPUT;
            comList.set(2, INPUT);
        }

        public String getOUTPUT() {
            return OUTPUT;
        }

        public void setOUTPUT(String OUTPUT) {
            this.OUTPUT = OUTPUT;
            comList.set(3, OUTPUT);
        }

        public String getType() {
            return Type;
        }

        public void setType(String Type) {
            this.Type = Type;
            comList.set(4, Type);
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
                        setINPUT(data);
                        break;
                    case 3 :
                        setOUTPUT(data);
                        break;
                    case 4 :
                        setType(data);
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
                            wrongList.add(db.charger_Table.Vendor);
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
                    case db.charger_Table.Vendor : {
                       vtd = "<td style=\"background-color:#FFD78C;\">";
                    }
                    break;
                }
            }
            return "                <tr>" + vtd+ Vendor +"</td><td>"+ ModelName +"</td><td>"+
                        INPUT +"</td><td>"+ OUTPUT +"</td><td>"+ Type +"</td><td>"+ Description +"</td></tr>\n";
        }
    
    }
    
}
