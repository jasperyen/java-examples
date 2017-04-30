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
public class WlanSet extends ComponentSet {
    
    public WlanSet() {
        setComponentName(WLAN.ComponentName);
        setTableName(WLAN.TableName);
        setAllKeyList(WLAN.AllKeyList);
        
        Map<Integer, String> map = new TreeMap();
        map.put(2, "Model Name");
        setMainColumn(map);
        
        map = new TreeMap();
        map.put(3, "Protocal");
        map.put(4, "nT,nR");
        map.put(5, "Band");
        map.put(6, "Bluetooth");
        setGeneraColumn(map);
                
        map = new TreeMap();
        map.put(0, "Vendor");
        map.put(1, "Max Speed (Mbps)");
        setForeignColumn(map);
    }
    
    @Override
    public WLAN newComponent(){
        return new WLAN();
    }
    
    
    @Override
    protected Map<String, String> getCheckTableName() {
        Map tableMap = new TreeMap<String, String>();
        tableMap.put(db.vendor_Table.TABLE_NAME, db.vendor_Table.Vendor);
        tableMap.put(db.wlan_max_speed_Table.TABLE_NAME, db.wlan_max_speed_Table.Max_Speed);
        return tableMap;
    }
    
    public static class WLAN extends Component {
        public static final String ComponentName;
        public static final String MainKey;
        public static final String TableName;
        public static final String ProjectTableName;
        public static final List<String> GeneralKeyList;
        public static final List<String> ForeignKeyList;
        public static final List<String>AllKeyList;
        
        private String Vendor = "";
        private String Max_Speed = "";
        private String ModelName = "";
        private String Protocal = "";
        private String nTnR = "";
        private String Band = "";
        private String Bluetooth = "";

        static {
            ComponentName = "WLAN";
            MainKey = db.wlan_Table.ModelName;
            TableName = db.wlan_Table.TABLE_NAME;
            ProjectTableName = db2.project_wlan_Table.TABLE_NAME;
            
            List<String> list = new ArrayList();
            list.add(db.wlan_Table.Vendor);
            list.add(db.wlan_Table.Max_Speed);
            list.add(db.wlan_Table.ModelName);
            list.add(db.wlan_Table.Protocal);
            list.add(db.wlan_Table.nTnR);
            list.add(db.wlan_Table.Band);
            list.add(db.wlan_Table.Bluetooth);
            AllKeyList = list;

            list = new ArrayList();
            list.add(db.wlan_Table.Vendor);
            list.add(db.wlan_Table.Max_Speed);
            ForeignKeyList = list;

            list = new ArrayList();
            list.add(db.wlan_Table.Protocal);
            list.add(db.wlan_Table.nTnR);
            list.add(db.wlan_Table.Band);
            list.add(db.wlan_Table.Bluetooth);
            GeneralKeyList = list;
        }
        
        public WLAN() {
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

        public String getMax_Speed() {
            return Max_Speed;
        }

        public void setMax_Speed(String Max_Speed) {
            this.Max_Speed = Max_Speed;
            comList.set(1, Max_Speed);
        }

        public String getModelName() {
            return ModelName;
        }

        public void setModelName(String ModelName) {
            this.ModelName = ModelName;
            comList.set(2, ModelName);
        }

        public String getProtocal() {
            return Protocal;
        }

        public void setProtocal(String Protocal) {
            this.Protocal = Protocal;
            comList.set(3, Protocal);
        }

        public String getnTnR() {
            return nTnR;
        }

        public void setnTnR(String nTnR) {
            this.nTnR = nTnR;
            comList.set(4, nTnR);
        }

        public String getBand() {
            return Band;
        }

        public void setBand(String Band) {
            this.Band = Band;
            comList.set(5, Band);
        }

        public String getBluetooth() {
            return Bluetooth;
        }

        public void setBluetooth(String Bluetooth) {
            this.Bluetooth = Bluetooth;
            comList.set(6, Bluetooth);
        }
        
        @Override
        public void setComponentByColumn (int col, String data) {
            switch (col){
                    case 0 :
                        setVendor(data);
                        break;
                    case 1 :
                        setMax_Speed(data);
                        break;
                    case 2 :
                        setModelName(data);
                        break;
                    case 3 :
                        setProtocal(data);
                        break;
                    case 4 :
                        setnTnR(data);
                        break;
                    case 5 :
                        setBand(data);
                        break;
                    case 6 :
                        setBluetooth(data);
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
                            wrongList.add(db.wlan_Table.Vendor);
                        break;
                    case db.wlan_max_speed_Table.Max_Speed : 
                        for (Object obj : entry.getValue()) {
                            if (Max_Speed.equals((String)obj))
                                boo = true;
                        }
                        if (!boo)
                            wrongList.add(db.wlan_Table.Max_Speed);
                        break;
                }
            }
            return wrongList;
        }
        
        @Override
        public String getComponentTableRow(List<String> wrongList){
            String vtd = "<td>";
            String mtd = "<td>";
            for (Object obj : wrongList) {
                switch ((String)obj) {
                    case db.wlan_Table.Vendor : {
                       vtd = "<td style=\"background-color:#FFD78C;\">";
                    }
                    break;
                    case db.wlan_Table.Max_Speed : {
                        mtd = "<td style=\"background-color:#FFD78C;\">";
                    }
                    break;
                }
            }
            return "                <tr>" + vtd+ Vendor +"</td>" + mtd + Max_Speed +"</td><td>"+ ModelName +"</td><td>"+
                        Protocal +"</td><td>"+ nTnR +"</td><td>"+ Band +"</td><td>"+ Bluetooth +"</td></tr>\n";
        }
    
    } 
    
}
