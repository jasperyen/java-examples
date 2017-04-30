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
public class CameraSet extends ComponentSet {
    
    

    public CameraSet() {
        setComponentName(Camera.ComponentName);
        setTableName(Camera.TableName);
        setAllKeyList(Camera.AllKeyList);
        
        Map<Integer, String> map = new TreeMap();
        map.put(3, "Model");
        setMainColumn(map);
        
        map = new TreeMap();
        map.put(4, "Lens");
        map.put(5, "AF/FF");
        map.put(6, "Description");
        setGeneraColumn(map);
        
        map = new TreeMap();
        map.put(0, "Vendor");
        map.put(1, "Pixel (Mega)");
        map.put(2, "Sensor IC");
        setForeignColumn(map);
    }
    
    @Override
    public Camera newComponent(){
        return new Camera();
    }
    
    @Override
    protected Map<String, String> getCheckTableName() {
        Map tableMap = new TreeMap<String, String>();
        tableMap.put(db.vendor_Table.TABLE_NAME, db.vendor_Table.Vendor);
        tableMap.put(db.camera_pixel_Table.TABLE_NAME, db.camera_pixel_Table.Pixel_Mega);
        tableMap.put(db.camera_sensor_ic_Table.TABLE_NAME, db.camera_sensor_ic_Table.Sensor_IC);
        return tableMap;
    }
    
    public static class Camera extends Component {
        public static final String ComponentName;
        public static final String MainKey;
        public static final String TableName;
        public static final String ProjectTableName;
        public static final List<String> GeneralKeyList;
        public static final List<String> ForeignKeyList;
        public static final List<String>AllKeyList;
    
        private String Vendor = "";
        private String Pixel_Mega = "";
        private String Sensor_IC = "";
        private String Model = "";
        private String Lens = "";
        private String AF_FF = "";
        private String Description = "";

        static {
            ComponentName = "Camera";
            MainKey = db.camera_Table.Model;
            TableName = db.camera_Table.TABLE_NAME;
            ProjectTableName = db2.project_camera_Table.TABLE_NAME;
            
            
            List<String> list = new ArrayList();
            list.add(db.camera_Table.Vendor);
            list.add(db.camera_Table.Pixel_Mega);
            list.add(db.camera_Table.Sensor_IC);
            list.add(db.camera_Table.Model);
            list.add(db.camera_Table.Lens);
            list.add(db.camera_Table.AF_FF);
            list.add(db.camera_Table.Description);
            AllKeyList = list;

            list = new ArrayList();
            list.add(db.camera_Table.Vendor);
            list.add(db.camera_Table.Pixel_Mega);
            list.add(db.camera_Table.Sensor_IC);
            ForeignKeyList = list;

            list = new ArrayList();
            list.add(db.camera_Table.Lens);
            list.add(db.camera_Table.AF_FF);
            list.add(db.camera_Table.Description);
            GeneralKeyList = list;
        }
        
        public Camera() {
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

        public String getPixel_Mega() {
            return Pixel_Mega;
        }

        public void setPixel_Mega(String Pixel_Mega) {
            this.Pixel_Mega = Pixel_Mega;
            comList.set(1, Pixel_Mega);
        }

        public String getSensor_IC() {
            return Sensor_IC;
        }

        public void setSensor_IC(String Sensor_IC) {
            this.Sensor_IC = Sensor_IC;
            comList.set(2, Sensor_IC);
        }

        public String getModel() {
            return Model;
        }

        public void setModel(String Model) {
            this.Model = Model;
            comList.set(3, Model);
        }

        public String getLens() {
            return Lens;
        }

        public void setLens(String Lens) {
            this.Lens = Lens;
            comList.set(4, Lens);
        }

        public String getAF_FF() {
            return AF_FF;
        }

        public void setAF_FF(String AF_FF) {
            this.AF_FF = AF_FF;
            comList.set(5, AF_FF);
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String Description) {
            this.Description = Description;
            comList.set(6, Description);
        }
        
        
        @Override
        public void setComponentByColumn (int col, String data) {
            switch (col){
                    case 0 :
                        setVendor(data);
                        break;
                    case 1 :
                        setPixel_Mega(data);
                        break;
                    case 2 :
                        setSensor_IC(data);
                        break;
                    case 3 :
                        setModel(data);
                        break;
                    case 4 :
                        setLens(data);
                        break;
                    case 5 :
                        setAF_FF(data);
                        break;
                    case 6 :
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
                Map.Entry<String, List<String>> entry = (Map.Entry<String, List<String>>) iter.next();
                boolean boo = false;
                switch (entry.getKey()) {
                    case db.vendor_Table.Vendor : 
                        for (Object obj : entry.getValue()) {
                            if (Vendor.equals((String)obj))
                                boo = true;
                        }
                        if (!boo)
                            wrongList.add(db.camera_Table.Vendor);
                        break;
                    case db.camera_pixel_Table.Pixel_Mega : 
                        for (Object obj : entry.getValue()) {
                            if (Pixel_Mega.equals((String)obj))
                                boo = true;
                        }
                        if (!boo)
                            wrongList.add(db.camera_Table.Pixel_Mega);
                        break;
                    case db.camera_sensor_ic_Table.Sensor_IC : 
                        for (Object obj : entry.getValue()) {
                            if (Sensor_IC.equals((String)obj))
                                boo = true;
                        }
                        if (!boo)
                            wrongList.add(db.camera_Table.Sensor_IC);
                        break;
                }
            }
            return wrongList;
        }
    
        
        @Override
        public String getComponentTableRow(List<String> wrongList){
            String vtd = "<td>";
            String ptd = "<td>";
            String std = "<td>";
            
            for (Object obj : wrongList) {
                switch ((String)obj) {
                    case db.camera_Table.Vendor : {
                       vtd = "<td style=\"background-color:#FFD78C;\">";
                    }
                    break;
                    case db.camera_Table.Pixel_Mega : {
                        ptd = "<td style=\"background-color:#FFD78C;\">";
                    }
                    break;
                    case db.camera_Table.Sensor_IC : {
                        std = "<td style=\"background-color:#FFD78C;\">";
                    }
                    break;
                }
            }
            return "                <tr>" + vtd+ Vendor +"</td>" + ptd + Pixel_Mega +"</td>" + std + Sensor_IC +"</td><td>"+
                        Model +"</td><td>"+ Lens +"</td><td>"+ AF_FF +"</td><td>"+ Description +"</td></tr>\n";
        }
    }
    
}
