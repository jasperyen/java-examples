/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBaseInfo;

import DataBaseInfo.DataBaseName.db;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jasper-Yen
 */
public interface DataBaseConn {
    public static final String DBurl = "jdbc:mysql://127.0.0.1:3306/mobile_component?useUnicode=true&characterEncoding=utf-8&useSSL=false";
    public static final String DBuser = "root";
    public static final String DBpassword = "";
    
    public static final String DB2url = "jdbc:mysql://127.0.0.1:3306/mobile_project?useUnicode=true&characterEncoding=utf-8&useSSL=false";
    public static final String DB2user = "root";
    public static final String DB2password = "";
    
    
    
    public default String getTableNameByAttr(String name) {
        switch (name) {
            case db.vendor_Table.TABLE_NAME : 
                return db.vendor_Table.Vendor;
                
            case db.cpu_code_name_Table.TABLE_NAME :
                return db.cpu_code_name_Table.CodeName;
                
            case db.wwan_type_Table.TABLE_NAME :
                return db.wwan_type_Table.Type;
                
            case db.wlan_max_speed_Table.TABLE_NAME :
                return db.wlan_max_speed_Table.Max_Speed;
                
            case db.panel_size_Table.TABLE_NAME :
                return db.panel_size_Table.Size;
                
            case db.touch_panel_ic_vendor_Table.TABLE_NAME :
                return db.touch_panel_ic_vendor_Table.IC_Vendor;
                
            case db.touch_panel_ic_Table.TABLE_NAME :
                return db.touch_panel_ic_Table.IC;
                
            case db.camera_pixel_Table.TABLE_NAME :
                return db.camera_pixel_Table.Pixel_Mega;
                
            case db.camera_sensor_ic_Table.TABLE_NAME :
                return db.camera_sensor_ic_Table.Sensor_IC;
                
            case db.sensor_type_Table.TABLE_NAME :
                return db.sensor_type_Table.Type;
                
            case db.memory_type_Table.TABLE_NAME :
                return db.memory_type_Table.Type;
                
            case db.memory_frequency_Table.TABLE_NAME :
                return db.memory_frequency_Table.Frequency;
              
            case db.memory_capacity_Table.TABLE_NAME :
                return db.memory_capacity_Table.Capacity;
              
            case db.keyboard_type_Table.TABLE_NAME :
                return db.keyboard_type_Table.Type;
              
            case db.keyboard_os_Table.TABLE_NAME :
                return db.keyboard_os_Table.OS;
                
        }
        return "";
    }

    public default void registerJDBC(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DataBaseInfo.DataBaseConn.class.getName()).log(Level.WARNING, "Can't register JDBC driver ! " + ex.toString());
        }
    }
    
}
