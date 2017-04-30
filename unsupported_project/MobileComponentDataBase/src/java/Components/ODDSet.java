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
public class ODDSet extends ComponentSet {

    public ODDSet() {
        setComponentName(ODD.ComponentName);
        setTableName(ODD.TableName);
        setAllKeyList(ODD.AllKeyList);
        
        Map<Integer, String> map = new TreeMap();
        map.put(1, "Model");
        setMainColumn(map);
        
        map = new TreeMap();
        map.put(2, "z-Hight");
        map.put(3, "Interface");
        map.put(4, "Description");
        setGeneraColumn(map);
                
        map = new TreeMap();
        map.put(0, "Vendor");
        setForeignColumn(map);
    }
    
    
    @Override
    public ODD newComponent(){
        return new ODD();
    }
    
    
    @Override
    protected Map<String, String> getCheckTableName() {
        Map tableMap = new TreeMap<String, String>();
        tableMap.put(db.vendor_Table.TABLE_NAME, db.vendor_Table.Vendor);
        return tableMap;
    }
    
    
    public static class ODD extends Component{
        public static final String ComponentName;
        public static final String MainKey;
        public static final String TableName;
        public static final String ProjectTableName;
        public static final List<String> GeneralKeyList;
        public static final List<String> ForeignKeyList;
        public static final List<String>AllKeyList;
        
        private String Vendor = "";
        private String Model = "";
        private String zHight = "";
        private String Interface = "";
        private String Description = "";
        
        static {
            ComponentName = "ODD";
            MainKey = db.odd_Table.Model;
            TableName = db.odd_Table.TABLE_NAME;
            ProjectTableName = db2.project_odd_Table.TABLE_NAME;

            List<String> list = new ArrayList();
            list.add(db.odd_Table.Vendor);
            list.add(db.odd_Table.Model);
            list.add(db.odd_Table.zHigh);
            list.add(db.odd_Table.Interface);
            list.add(db.odd_Table.Description);
            AllKeyList = list;

            list = new ArrayList();
            list.add(db.cpu_Table.Vendor);
            ForeignKeyList = list;

            list = new ArrayList();
            list.add(db.odd_Table.zHigh);
            list.add(db.odd_Table.Interface);
            list.add(db.odd_Table.Description);
            GeneralKeyList = list;
        }

        public ODD() {
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

        public String getzHight() {
            return zHight;
        }

        public void setzHight(String zHight) {
            this.zHight = zHight;
            comList.set(2, zHight);
        }

        public String getInterface() {
            return Interface;
        }

        public void setInterface(String Interface) {
            this.Interface = Interface;
            comList.set(3, Interface);
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String Description) {
            this.Description = Description;
            comList.set(4, Description);
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
                        setzHight(data);
                        break;
                    case 3 :
                        setInterface(data);
                        break;
                    case 4 :
                        setDescription(data);
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
                            wrongList.add(db.odd_Table.Vendor);
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
                    case db.odd_Table.Vendor : {
                       ventd = "<td style=\"background-color:#FFD78C;\">";
                    }
                    break;
                }
            }
            return "                <tr>" + ventd+ Vendor +"</td><td>"+ Model +"</td><td>"+
                        zHight +"</td><td>"+ Interface +"</td><td>"+ Description +"</td></tr>\n";
        }
        
    }
    
}
