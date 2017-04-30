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
public class LcdPanelSet extends ComponentSet {
    
    public LcdPanelSet() {
        setComponentName(LcdPanel.ComponentName);
        setTableName(LcdPanel.TableName);
        setAllKeyList(LcdPanel.AllKeyList);
        
        Map<Integer, String> map = new TreeMap();
        map.put(2, "Model Name");
        setMainColumn(map);
        
        map = new TreeMap();
        map.put(3, "Resolution");
        map.put(4, "Type");
        map.put(5, "Interface");
        map.put(6, "Nits");
        map.put(7, "Touch");
        setGeneraColumn(map);
                
        map = new TreeMap();
        map.put(0, "Vendor");
        map.put(1, "Size");
        setForeignColumn(map);
    }
    
    @Override
    public LcdPanel newComponent(){
        return new LcdPanel();
    }
    
    @Override
    protected Map<String, String> getCheckTableName() {
        Map tableMap = new TreeMap<String, String>();
        tableMap.put(db.vendor_Table.TABLE_NAME, db.vendor_Table.Vendor);
        tableMap.put(db.panel_size_Table.TABLE_NAME, db.panel_size_Table.Size);
        return tableMap;
    }
    
    public static class LcdPanel extends Component {
        public static final String ComponentName;
        public static final String MainKey;
        public static final String TableName;
        public static final String ProjectTableName;
        public static final List<String> GeneralKeyList;
        public static final List<String> ForeignKeyList;
        public static final List<String>AllKeyList;
        
        private String Vendor = "";
        private String Size = "";
        private String ModelName = "";
        private String Resolution = "";
        private String Type = "";
        private String Nits = "";
        private String Touch = "";
        private String Interface = "";

        static {
            ComponentName = "LCD Panel";
            MainKey = db.lcd_panel_Table.ModelName;
            TableName = db.lcd_panel_Table.TABLE_NAME;
            ProjectTableName = db2.project_lcd_panel_Table.TABLE_NAME;

            List<String> list = new ArrayList();
            list.add(db.lcd_panel_Table.Vendor);
            list.add(db.lcd_panel_Table.Size);
            list.add(db.lcd_panel_Table.ModelName);
            list.add(db.lcd_panel_Table.Resolution);
            list.add(db.lcd_panel_Table.Type);
            list.add(db.lcd_panel_Table.Nits);
            list.add(db.lcd_panel_Table.Touch);
            list.add(db.lcd_panel_Table.Interface);
            AllKeyList = list;

            list = new ArrayList();
            list.add(db.lcd_panel_Table.Vendor);
            list.add(db.lcd_panel_Table.Size);
            ForeignKeyList = list;

            list = new ArrayList();
            list.add(db.lcd_panel_Table.Resolution);
            list.add(db.lcd_panel_Table.Type);
            list.add(db.lcd_panel_Table.Nits);
            list.add(db.lcd_panel_Table.Touch);
            list.add(db.lcd_panel_Table.Interface);
            GeneralKeyList = list;
        }
        
        public LcdPanel() {
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

        public String getModelName() {
            return ModelName;
        }

        public void setModelName(String ModelName) {
            this.ModelName = ModelName;
            comList.set(2, ModelName);
        }

        public String getResolution() {
            return Resolution;
        }

        public void setResolution(String Resolution) {
            this.Resolution = Resolution;
            comList.set(3, Resolution);
        }

        public String getType() {
            return Type;
        }

        public void setType(String Type) {
            this.Type = Type;
            comList.set(4, Type);
        }

        public String getNits() {
            return Nits;
        }

        public void setNits(String Nits) {
            this.Nits = Nits;
            comList.set(6, Nits);
        }

        public String getTouch() {
            return Touch;
        }

        public void setTouch(String Touch) {
            this.Touch = Touch;
            comList.set(7, Touch);
        }

        public String getInterface() {
            return Interface;
        }

        public void setInterface(String Interface) {
            this.Interface = Interface;
            comList.set(5, Interface);
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
                        setModelName(data);
                        break;
                    case 3 :
                        setResolution(data);
                        break;
                    case 4 :
                        setType(data);
                        break;
                    case 5 :
                        setInterface(data);
                        break;
                    case 6 :
                        setNits(data);
                        break;
                    case 7 :
                        setTouch(data);
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
                            wrongList.add(db.lcd_panel_Table.Vendor);
                        break;
                    case db.panel_size_Table.Size : 
                        for (Object obj : entry.getValue()) {
                            if (Size.equals((String)obj))
                                boo = true;
                        }
                        if (!boo)
                            wrongList.add(db.lcd_panel_Table.Size);
                        break;
                }
            }
            return wrongList;
        }
        
        @Override
        public String getComponentTableRow(List<String> wrongList){
            String vtd = "<td>";
            String std = "<td>";
            for (Object obj : wrongList) {
                switch ((String)obj) {
                    case db.lcd_panel_Table.Vendor : {
                       vtd = "<td style=\"background-color:#FFD78C;\">";
                    }
                    break;
                    case db.lcd_panel_Table.Size : {
                        std = "<td style=\"background-color:#FFD78C;\">";
                    }
                    break;
                }
            }
            return "                <tr>" + vtd+ Vendor +"</td>" + std + Size +"</td><td>"+ ModelName +"</td><td>"+
                        Resolution +"</td><td>"+ Type +"</td><td>"+ Interface +"</td><td>"+ Nits +"</td><td>"+ Touch +"</td></tr>\n";
        }
        
    }
    
}
