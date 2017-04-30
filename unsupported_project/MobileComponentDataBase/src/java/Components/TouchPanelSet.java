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
public class TouchPanelSet extends ComponentSet {

    public TouchPanelSet() {
        setComponentName(TouchPanel.ComponentName);
        setTableName(TouchPanel.TableName);
        setAllKeyList(TouchPanel.AllKeyList);
        
        Map<Integer, String> map = new TreeMap();
        map.put(4, "Model Name");
        setMainColumn(map);
        
        map = new TreeMap();
        map.put(5, "Bundle Type");
        map.put(6, "Multi Touch");
        map.put(7, "Stylus");
        setGeneraColumn(map);
                
        map = new TreeMap();
        map.put(0, "Vendor");
        map.put(1, "Size");
        map.put(2, "IC Vendor");
        map.put(3, "IC");
        setForeignColumn(map);
    }
    
    @Override
    public TouchPanel newComponent(){
        return new TouchPanel();
    }
    
    @Override
    protected Map<String, String> getCheckTableName() {
        Map tableMap = new TreeMap<String, String>();
        tableMap.put(db.vendor_Table.TABLE_NAME, db.vendor_Table.Vendor);
        tableMap.put(db.panel_size_Table.TABLE_NAME, db.panel_size_Table.Size);
        tableMap.put(db.touch_panel_ic_vendor_Table.TABLE_NAME, db.touch_panel_ic_vendor_Table.IC_Vendor);
        tableMap.put(db.touch_panel_ic_Table.TABLE_NAME, db.touch_panel_ic_Table.IC);
        return tableMap;
    }
    
    public static class TouchPanel extends Component {
        public static final String ComponentName;
        public static final String MainKey;
        public static final String TableName;
        public static final String ProjectTableName;
        public static final List<String> GeneralKeyList;
        public static final List<String> ForeignKeyList;
        public static final List<String>AllKeyList;
        
        private String Vendor = "";
        private String Size = "";
        private String IC_Vendor = "";
        private String IC = "";
        private String ModelName = "";
        private String Type = "";
        private String Multi_Touch = "";
        private String Stylus = "";

        static {
            ComponentName = "Touch Panel";
            MainKey = db.touch_panel_Table.ModelName;
            TableName = db.touch_panel_Table.TABLE_NAME;
            ProjectTableName = db2.project_touch_panel_Table.TABLE_NAME;
            
            List<String> list = new ArrayList();
            list.add(db.touch_panel_Table.Vendor);
            list.add(db.touch_panel_Table.Size);
            list.add(db.touch_panel_Table.IC_Vendor);
            list.add(db.touch_panel_Table.IC);
            list.add(db.touch_panel_Table.ModelName);
            list.add(db.touch_panel_Table.Type);
            list.add(db.touch_panel_Table.Multi_Touch);
            list.add(db.touch_panel_Table.Stylus);
            AllKeyList = list;

            list = new ArrayList();
            list.add(db.touch_panel_Table.Vendor);
            list.add(db.touch_panel_Table.Size);
            list.add(db.touch_panel_Table.IC_Vendor);
            list.add(db.touch_panel_Table.IC);
            ForeignKeyList = list;

            list = new ArrayList();
            list.add(db.touch_panel_Table.Type);
            list.add(db.touch_panel_Table.Multi_Touch);
            list.add(db.touch_panel_Table.Stylus);
            GeneralKeyList = list;
        }
        
        public TouchPanel() {
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

        public String getSize() {
            return Size;
        }

        public void setSize(String Size) {
            this.Size = Size;
            comList.set(1, Size);
        }

        public String getIC_Vendor() {
            return IC_Vendor;
        }

        public void setIC_Vendor(String IC_Vendor) {
            this.IC_Vendor = IC_Vendor;
            comList.set(2, IC_Vendor);
        }

        public String getIC() {
            return IC;
        }

        public void setIC(String IC) {
            this.IC = IC;
            comList.set(3, IC);
        }

        public String getModelName() {
            return ModelName;
        }

        public void setModelName(String ModelName) {
            this.ModelName = ModelName;
            comList.set(4, ModelName);
        }

        public String getType() {
            return Type;
        }

        public void setType(String Type) {
            this.Type = Type;
            comList.set(5, Type);
        }

        public String getMulti_Touch() {
            return Multi_Touch;
        }

        public void setMulti_Touch(String Multi_Touch) {
            this.Multi_Touch = Multi_Touch;
            comList.set(6, Multi_Touch);
        }

        public String getStylus() {
            return Stylus;
        }

        public void setStylus(String Stylus) {
            this.Stylus = Stylus;
            comList.set(7, Stylus);
        }
        
        @Override
        public void setComponentByColumn (int col, String data) {
            switch (col){
                    case 0 :
                        setVendor(data);
                        break;
                    case 1 :
                        setSize(data);
                        break;
                    case 2 :
                        setIC_Vendor(data);
                        break;
                    case 3 :
                        setIC(data);
                        break;
                    case 4 :
                        setModelName(data);
                        break;
                    case 5 :
                        setType(data);
                        break;
                    case 6 :
                        setMulti_Touch(data);
                        break;
                    case 7 :
                        setStylus(data);
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
                            wrongList.add(db.touch_panel_Table.Vendor);
                        break;
                    case db.panel_size_Table.Size : 
                        for (Object obj : entry.getValue()) {
                            if (Size.equals((String)obj))
                                boo = true;
                        }
                        if (!boo)
                            wrongList.add(db.touch_panel_Table.Size);
                        break;
                    case db.touch_panel_ic_vendor_Table.IC_Vendor : 
                        for (Object obj : entry.getValue()) {
                            if (IC_Vendor.equals((String)obj))
                                boo = true;
                        }
                        if (!boo)
                            wrongList.add(db.touch_panel_Table.IC_Vendor);
                        break;
                    case db.touch_panel_ic_Table.IC : 
                        for (Object obj : entry.getValue()) {
                            if (IC.equals((String)obj))
                                boo = true;
                        }
                        if (!boo)
                            wrongList.add(db.touch_panel_Table.IC);
                        break;
                }
            }
            return wrongList;
        }
        
        @Override
        public String getComponentTableRow(List<String> wrongList){
            String vtd = "<td>";
            String std = "<td>";
            String ivtd = "<td>";
            String itd = "<td>";
            for (Object obj : wrongList) {
                switch ((String)obj) {
                    case db.touch_panel_Table.Vendor : {
                       vtd = "<td style=\"background-color:#FFD78C;\">";
                    }
                    break;
                    case db.touch_panel_Table.Size : {
                        std = "<td style=\"background-color:#FFD78C;\">";
                    }
                    break;
                    case db.touch_panel_Table.IC_Vendor : {
                        ivtd = "<td style=\"background-color:#FFD78C;\">";
                    }
                    break;
                    case db.touch_panel_Table.IC : {
                        itd = "<td style=\"background-color:#FFD78C;\">";
                    }
                    break;
                }
            }
            return "                <tr>" + vtd+ Vendor +"</td>" + std + Size +"</td>" + ivtd + IC_Vendor +"</td>"+ itd +
                        IC +"</td><td>"+ ModelName +"</td><td>"+ Type +"</td><td>"+ Multi_Touch +"</td><td>"+ Stylus +"</td></tr>\n";
        }
        
    }
    
}
