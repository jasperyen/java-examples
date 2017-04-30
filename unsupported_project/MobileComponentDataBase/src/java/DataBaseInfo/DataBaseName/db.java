/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBaseInfo.DataBaseName;

/**
 *
 * @author Jasper-Yen
 */
public class db {
    
    public db() {}

    
    public static class user_Table{
        public static final String TABLE_NAME = "user";
        public static final String ID = "ID";
        public static final String PWD = "PWD";
    }
    
    
    public static class log_Table{
        public static final String TABLE_NAME = "log";
        public static final String Log = "Log";
        public static final String Time = "Time";
    }
    
    
    public static class vendor_Table{
        public static final String TABLE_NAME = "vendor";
        public static final String Vendor = "Vendor";
    }
    
    
    public static class cpu_Table {
        public static final String TABLE_NAME = "cpu";
        public static final String Vendor = "Vendor";
        public static final String CodeName = "CodeName";
        public static final String ModelName = "ModelName";
        public static final String MaxCoreFreq = "MaxCoreFreq";
        public static final String CoreThreats = "CoreThreats";
        public static final String Gfx = "Gfx";
        public static final String TDP = "TDP";
    }
    
    
    public static class cpu_code_name_Table{
        public static final String TABLE_NAME = "cpu_code_name";
        public static final String CodeName = "CodeName";
    }
    
    
    public static class memory_Table{
        public static final String TABLE_NAME = "memory";
        public static final String MemoryName = "MemoryName";
        public static final String Type = "Type";
        public static final String Frequency = "Frequency";
        public static final String Capacity = "Capacity";
        public static final String Description = "Description";
        public static final String Vendor = "Vendor";
    }
    
    
    public static class memory_type_Table{
        public static final String TABLE_NAME = "memory_type";
        public static final String Type = "Type";
    }
    
    
    public static class memory_frequency_Table{
        public static final String TABLE_NAME = "memory_frequency";
        public static final String Frequency = "Frequency";
    }
    
    
    public static class memory_capacity_Table{
        public static final String TABLE_NAME = "memory_capacity";
        public static final String Capacity = "Capacity";
    }
    
    
    public static class sensor_Table{
        public static final String TABLE_NAME = "sensor";
        public static final String Vendor = "Vendor";
        public static final String Type = "Type";
        public static final String ModelName = "ModelName";
        public static final String Description = "Description";
    }
    
    
    public static class sensor_type_Table{
        public static final String TABLE_NAME = "sensor_type";
        public static final String Type = "Type";
    }
   
    
    public static class ethernet_Table{
        public static final String TABLE_NAME = "ethernet";
        public static final String Vendor = "Vendor";
        public static final String ModelName = "ModelName";
        public static final String Description = "Description";
    }

    
    public static class storage_Table{
        public static final String TABLE_NAME = "storage";
        public static final String Vendor = "Vendor";
        public static final String Model = "Model";
        public static final String Capacity = "Capacity";
        public static final String Interface = "Interface";
        public static final String Rpm = "Rpm";
        public static final String Speed = "Speed";
        public static final String zHight = "zHight";
        public static final String Type = "Type";
    }
    
    
    public static class wwan_Table{
        public static final String TABLE_NAME = "wwan";
        public static final String Vendor = "Vendor";
        public static final String Type = "Type";
        public static final String ModelName = "ModelName";
        public static final String Description = "Description";
    }
    
    
    public static class wwan_type_Table{
        public static final String TABLE_NAME = "wwan_type";
        public static final String Type = "Type";
    }
    
    
    public static class wlan_Table{
        public static final String TABLE_NAME = "wlan";
        public static final String Vendor = "Vendor";
        public static final String Max_Speed = "Max_Speed";
        public static final String ModelName = "ModelName";
        public static final String Protocal = "Protocal";
        public static final String nTnR = "nTnR";
        public static final String Band = "Band";
        public static final String Bluetooth = "Bluetooth";
    }
    
    
    public static class wlan_max_speed_Table{
        public static final String TABLE_NAME = "wlan_max_speed";
        public static final String Max_Speed = "Max_Speed";
    }
    
    
    public static class kbc_ebc_Table{
        public static final String TABLE_NAME = "kbc_ebc";
        public static final String Vendor = "Vendor";
        public static final String Model = "Model";
        public static final String Description = "Description";
    }
    
    
    public static class click_pad_Table{
        public static final String TABLE_NAME = "click_pad";
        public static final String Vendor = "Vendor";
        public static final String ModelName = "ModelName";
        public static final String Description = "Description";
    }
    
    
    public static class lcd_panel_Table{
        public static final String TABLE_NAME = "lcd_panel";
        public static final String Vendor = "Vendor";
        public static final String Size = "Size";
        public static final String ModelName = "ModelName";
        public static final String Resolution = "Resolution";
        public static final String Type = "Type";
        public static final String Nits = "Nits";
        public static final String Touch = "Touch";
        public static final String Interface = "Interface";
    }
    
    
    public static class panel_size_Table{
        public static final String TABLE_NAME = "panel_size";
        public static final String Size = "Size";
    }
    
    
    public static class touch_panel_Table{
        public static final String TABLE_NAME = "touch_panel";
        public static final String Vendor = "Vendor";
        public static final String Size = "Size";
        public static final String IC_Vendor = "IC_Vendor";
        public static final String IC = "IC";
        public static final String ModelName = "ModelName";
        public static final String Type = "BundleType";
        public static final String Multi_Touch = "Multi_Touch";
        public static final String Stylus = "Stylus";
    }
    
    
    public static class touch_panel_ic_vendor_Table{
        public static final String TABLE_NAME = "touch_panel_ic_vendor";
        public static final String IC_Vendor = "IC_Vendor";
    }
    
    
    public static class touch_panel_ic_Table{
        public static final String TABLE_NAME = "touch_panel_ic";
        public static final String IC = "IC";
    }
    
    
    public static class camera_Table{
        public static final String TABLE_NAME = "camera";
        public static final String Vendor = "Vendor";
        public static final String Pixel_Mega = "Pixel_Mega";
        public static final String Sensor_IC = "Sensor_IC";
        public static final String Model = "Model";
        public static final String Lens = "Lens";
        public static final String AF_FF = "AF_FF";
        public static final String Description = "Description";
    }
    
    
    public static class camera_pixel_Table{
        public static final String TABLE_NAME = "camera_pixel";
        public static final String Pixel_Mega = "Pixel_Mega";
    }
    
    
    public static class camera_sensor_ic_Table{
        public static final String TABLE_NAME = "camera_sensor_ic";
        public static final String Sensor_IC = "Sensor_IC";
    }
    
    
    public static class button_Table{
        public static final String TABLE_NAME = "button";
        public static final String Type = "Type";
        public static final String Description = "Description";
    }
    
    
    public static class card_reader_Table{
        public static final String TABLE_NAME = "card_reader";
        public static final String Model = "Model";
        public static final String Description = "Description";
        public static final String Vendor = "Vendor";
    }
    
    
    public static class antennas_Table{
        public static final String TABLE_NAME = "antennas";
        public static final String Type = "Type";
        public static final String Description = "Description";
    }
    
    
    public static class keyboard_Table{
        public static final String TABLE_NAME = "keyboard";
        public static final String Vendor = "Vendor";
        public static final String Type = "Type";
        public static final String OS = "OS";
        public static final String ModelName = "ModelName";
        public static final String Description = "Description";
        public static final String Layout = "Layout";
    }
    
    
    public static class keyboard_type_Table{
        public static final String TABLE_NAME = "keyboard_type";
        public static final String Type = "Type";
    }
    
    
    public static class keyboard_os_Table{
        public static final String TABLE_NAME = "keyboard_os";
        public static final String OS = "OS";
    }
    
    
    
    public static class battery_Table{
        public static final String TABLE_NAME = "battery";
        public static final String Vendor = "Vendor";
        public static final String ModelName = "ModelName";
        public static final String Capacity = "Capacity";
        public static final String Cell = "Cell";
        public static final String Description = "Description";
    }
    
    
    
    public static class charger_Table{
        public static final String TABLE_NAME = "charger";
        public static final String Vendor = "Vendor";
        public static final String ModelName = "ModelName";
        public static final String INPUT = "INPUT";
        public static final String OUTPUT = "OUTPUT";
        public static final String Type = "Type";
        public static final String Description = "Description";
    }
    
    
    
    public static class measurement_Table{
        public static final String TABLE_NAME = "measurement";
        public static final String Project = "Project";
        public static final String Type = "Type";
        public static final String Dimension = "Dimension";
        public static final String Weight = "Weight";
    }
    
    
    
    public static class graphic_Table{
        public static final String TABLE_NAME = "graphic";
        public static final String Vendor = "Vendor";
        public static final String ModelName = "ModelName";
        public static final String MaxCoreFreq = "MaxCoreFreq";
        public static final String MemoryType = "MemoryType";
        public static final String vRAM_size = "vRAM_size";
        public static final String Description = "Description";
    }
    
    
    
    public static class audio_codec_Table{
        public static final String TABLE_NAME = "audio_codec";
        public static final String Vendor = "Vendor";
        public static final String Model = "Model";
        public static final String Description = "Description";
    }
    
    
    
    public static class panel_interface_bridge_Table{
        public static final String TABLE_NAME = "panel_interface_bridge";
        public static final String Vendor = "Vendor";
        public static final String ModelName = "ModelName";
        public static final String Description = "Description";
    }
    
    
    
    public static class external_storage_Table{
        public static final String TABLE_NAME = "external_storage_card";
        public static final String Vendor = "Vendor";
        public static final String Model = "Model";
        public static final String Type = "Type";
        public static final String Capacity = "Capacity";
        public static final String Interface = "Interface";
        public static final String Speed = "Speed";
        public static final String Dimension = "Dimension";
    }
    
    
    
    public static class odd_Table{
        public static final String TABLE_NAME = "odd";
        public static final String Vendor = "Vendor";
        public static final String Model = "Model";
        public static final String zHigh = "zHigh";
        public static final String Interface = "Interface";
        public static final String Description = "Description";
    }
    
    
    
    public static class speaker_Table{
        public static final String TABLE_NAME = "speaker";
        public static final String Vendor = "Vendor";
        public static final String ModelName = "ModelName";
        public static final String Description = "Description";
    }
    
    
    
    
    public static class mic_Table{
        public static final String TABLE_NAME = "mic";
        public static final String Vendor = "Vendor";
        public static final String ModelName = "ModelName";
        public static final String Description = "Description";
    }
    
    
    
    public static class io_port_Table{
        public static final String TABLE_NAME = "io_port";
        public static final String Standard = "Standard";
        public static final String ConnectorType = "ConnectorType";
        public static final String Description = "Description";
    }
    
    
    
    public static class os_Table{
        public static final String TABLE_NAME = "os";
        public static final String Type = "Type";
        public static final String Description = "Description";
    }
}
