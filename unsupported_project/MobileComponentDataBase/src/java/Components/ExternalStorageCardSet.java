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
public class ExternalStorageCardSet extends ComponentSet {

    public ExternalStorageCardSet() {
        setComponentName(ExternalStorageCard.ComponentName);
        setTableName(ExternalStorageCard.TableName);
        setAllKeyList(ExternalStorageCard.AllKeyList);
        
        Map<Integer, String> map = new TreeMap();
        map.put(1, "Model");
        setMainColumn(map);
        
        map = new TreeMap();
        map.put(2, "Type");
        map.put(3, "Capacity (GB)");
        map.put(4, "Interface");
        map.put(5, "Speed");
        map.put(6, "Dimension");
        setGeneraColumn(map);
                
        map = new TreeMap();
        map.put(0, "Vendor");
        setForeignColumn(map);
    }
    
    @Override
    public ExternalStorageCard newComponent(){
        return new ExternalStorageCard();
    }
    
    
    @Override
    protected Map<String, String> getCheckTableName() {
        Map tableMap = new TreeMap<String, String>();
        tableMap.put(db.vendor_Table.TABLE_NAME, db.vendor_Table.Vendor);
        return tableMap;
    }
    
    public static class ExternalStorageCard extends Component {
        public static final String ComponentName;
        public static final String MainKey;
        public static final String TableName;
        public static final String ProjectTableName;
        public static final List<String> GeneralKeyList;
        public static final List<String> ForeignKeyList;
        public static final List<String>AllKeyList;
        
        private String Vendor = "";
        private String Model = "";
        private String Type = "";
        private String Capacity = "";
        private String Interface = "";
        private String Speed = "";
        private String Dimension = "";
        
        static {
            ComponentName = "External storage card";
            MainKey = db.external_storage_Table.Model;
            TableName = db.external_storage_Table.TABLE_NAME;
            ProjectTableName = db2.project_external_storage_card_Table.TABLE_NAME;

            List<String> list = new ArrayList();
            list.add(db.external_storage_Table.Vendor);
            list.add(db.external_storage_Table.Model);
            list.add(db.external_storage_Table.Type);
            list.add(db.external_storage_Table.Capacity);
            list.add(db.external_storage_Table.Interface);
            list.add(db.external_storage_Table.Speed);
            list.add(db.external_storage_Table.Dimension);
            AllKeyList = list;

            list = new ArrayList();
            list.add(db.external_storage_Table.Vendor);
            ForeignKeyList = list;

            list = new ArrayList();
            list.add(db.external_storage_Table.Type);
            list.add(db.external_storage_Table.Capacity);
            list.add(db.external_storage_Table.Interface);
            list.add(db.external_storage_Table.Speed);
            list.add(db.external_storage_Table.Dimension);
            GeneralKeyList = list;
        }

        public ExternalStorageCard() {
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

        public String getType() {
            return Type;
        }

        public void setType(String Type) {
            this.Type = Type;
            comList.set(2, Type);
        }

        public String getCapacity() {
            return Capacity;
        }

        public void setCapacity(String Capacity) {
            this.Capacity = Capacity;
            comList.set(3, Capacity);
        }

        public String getInterface() {
            return Interface;
        }

        public void setInterface(String Interface) {
            this.Interface = Interface;
            comList.set(4, Interface);
        }

        public String getSpeed() {
            return Speed;
        }

        public void setSpeed(String Speed) {
            this.Speed = Speed;
            comList.set(5, Speed);
        }

        public String getDimension() {
            return Dimension;
        }

        public void setDimension(String Dimension) {
            this.Dimension = Dimension;
            comList.set(6, Dimension);
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
                        setType(data);
                        break;
                    case 3 :
                        setCapacity(data);
                        break;
                    case 4 :
                        setInterface(data);
                        break;
                    case 5 :
                        setSpeed(data);
                        break;
                    case 6 :
                        setDimension(data);
                        break;
                }
        }
        
        
        @Override
        public List  getWrongComponentList(Map<String, List<String>> checkMap) {
            List wrongList = new ArrayList<String>();
            
            if (Model.equals(""))
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
                            wrongList.add(db.external_storage_Table.Vendor);
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
                    case db.external_storage_Table.Vendor : {
                       ventd = "<td style=\"background-color:#FFD78C;\">";
                    }
                    break;
                }
            }
            return "                <tr>" + ventd+ Vendor +"</td><td>"+ Model +"</td><td>"+
                        Type +"</td><td>"+ Capacity +"</td><td>"+ Interface +"</td><td>"+ Speed +"</td><td>"+ Dimension +"</td></tr>\n";
        }
        
    }
}
