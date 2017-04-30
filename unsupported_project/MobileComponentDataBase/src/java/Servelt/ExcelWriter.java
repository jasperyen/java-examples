/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servelt;

import Components.AntennasSet;
import Components.AntennasSet.Antennas;
import Components.AudioCodecSet;
import Components.AudioCodecSet.AudioCodec;
import Components.BatterySet;
import Components.BatterySet.Battery;
import Components.ButtonSet;
import Components.ButtonSet.Button;
import Components.CameraSet;
import Components.CameraSet.Camera;
import Components.CardReaderSet;
import Components.CardReaderSet.CardReader;
import Components.ChargerSet;
import Components.ChargerSet.Charger;
import Components.ClickPadSet;
import Components.ClickPadSet.ClickPad;
import Components.Component;
import Components.ComponentSet;
import Components.CpuSet;
import Components.CpuSet.CPU;
import Components.EthernetSet;
import Components.EthernetSet.Ethernet;
import Components.ExternalStorageCardSet;
import Components.ExternalStorageCardSet.ExternalStorageCard;
import Components.GraphicSet;
import Components.GraphicSet.Graphic;
import Components.IoPortSet;
import Components.IoPortSet.IoPort;
import Components.KbcEbcSet;
import Components.KbcEbcSet.KBCEBC;
import Components.KeyboardSet;
import Components.KeyboardSet.Keyboard;
import Components.LcdPanelSet;
import Components.LcdPanelSet.LcdPanel;
import Components.MeasurementSet;
import Components.MeasurementSet.Measurement;
import Components.MemorySet;
import Components.MemorySet.Memory;
import Components.MicSet;
import Components.MicSet.Mic;
import Components.ODDSet;
import Components.ODDSet.ODD;
import Components.OSSet;
import Components.OSSet.OS;
import Components.PanelInterfaceBridgeSet;
import Components.PanelInterfaceBridgeSet.PanelInterfaceBridge;
import Components.SensorSet;
import Components.SensorSet.Sensor;
import Components.SpeakerSet;
import Components.SpeakerSet.Speaker;
import Components.StorageSet;
import Components.StorageSet.Storage;
import Components.TouchPanelSet;
import Components.TouchPanelSet.TouchPanel;
import Components.WlanSet;
import Components.WlanSet.WLAN;
import Components.WwanSet;
import Components.WwanSet.WWAN;
import static DataBaseInfo.DataBaseConn.DB2password;
import static DataBaseInfo.DataBaseConn.DB2url;
import static DataBaseInfo.DataBaseConn.DB2user;
import static DataBaseInfo.DataBaseConn.DBpassword;
import static DataBaseInfo.DataBaseConn.DBurl;
import static DataBaseInfo.DataBaseConn.DBuser;
import DataBaseInfo.DataBaseName.db;
import DataBaseInfo.DataBaseName.db2;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Jasper-Yen
 */
public class ExcelWriter {
    private XSSFWorkbook workbook ;
    private Logger logger;
    private String filepath = "C:\\workspace\\usr\\";
    private String filename = "Template.xlsx";
    private String template = "Product_Specification_template.xlsx";
    
    public String getFilepath() {
        return filepath;
    }

    public String getFilename() {
        return filename;
    }
    
    
    
    private void setMainCell (XSSFCell cell, String name) {
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(HSSFColor.ORANGE.index);
        style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        
        XSSFFont font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setFont(font);
        
        cell.setCellValue(name);
        cell.setCellStyle(style);
    }
    
    private void setAttrCell (XSSFCell cell, String name) {
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(HSSFColor.YELLOW.index);
        style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        
        XSSFFont font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setFont(font);
        
        cell.setCellValue(name);
        cell.setCellStyle(style);
    }
    
    private void setDesCell (XSSFCell cell, String name) {
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(HSSFColor.GREEN.index);
        style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        
        XSSFFont font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setFont(font);
        
        cell.setCellValue(name);
        cell.setCellStyle(style);
    }
    
    private void mapToCell (XSSFRow row, final String kind, Map<Integer, String> map) {
        for (Iterator iter = map.entrySet().iterator(); iter.hasNext(); ) {
            Entry<Integer, String> entry = (Entry<Integer, String>) iter.next();
            XSSFCell cell = row.createCell(entry.getKey());
            switch (kind) {
                case "main" :
                    setMainCell (cell, entry.getValue());
                    break;
                case "attr" : 
                    setAttrCell(cell, entry.getValue());
                    break;
                case "des" : 
                    setDesCell(cell, entry.getValue());
                    break;
            }
        }
    }
    
    private Map<String, String> getSearchMap (Map<String, String> map) {
        Map<String, String> searchMap = new TreeMap<String, String>();
        for (Iterator iter = map.entrySet().iterator(); iter.hasNext(); ) {
            Entry<String, String> entry = (Entry<String, String>) iter.next();
            if (entry.getValue() != null)
                searchMap.put(entry.getKey(), entry.getValue());
        }
        return searchMap;
    }
    
    private ComponentSet requestToCom (HttpServletRequest request) {
        Map map = new TreeMap<String, String>();
        ComponentSet set = null;
        
        switch (request.getParameter("category")) {
            
            case db.cpu_Table.TABLE_NAME :{
                set = new CpuSet();
                map.put(db.cpu_Table.Vendor, request.getParameter(db.vendor_Table.TABLE_NAME));
                map.put(db.cpu_Table.CodeName, request.getParameter(db.cpu_code_name_Table.TABLE_NAME));
                break;
            }
            
            case db.memory_Table.TABLE_NAME :{
                set = new MemorySet();
                map.put(db.memory_Table.Vendor, request.getParameter(db.vendor_Table.TABLE_NAME));
                map.put(db.memory_Table.Type, request.getParameter(db.memory_type_Table.TABLE_NAME));
                map.put(db.memory_Table.Frequency, request.getParameter(db.memory_frequency_Table.TABLE_NAME));
                map.put(db.memory_Table.Capacity, request.getParameter(db.memory_capacity_Table.TABLE_NAME));
                break;
            }
            
            case db.sensor_Table.TABLE_NAME :{
                set = new SensorSet();
                map.put(db.sensor_Table.Vendor, request.getParameter(db.vendor_Table.TABLE_NAME));
                map.put(db.sensor_Table.Type, request.getParameter(db.sensor_type_Table.TABLE_NAME));
                break;
            }
            
            case db.ethernet_Table.TABLE_NAME :{
                set = new EthernetSet();
                map.put(db.ethernet_Table.Vendor, request.getParameter(db.vendor_Table.TABLE_NAME));
                break;
            }
            
            case db.storage_Table.TABLE_NAME :{
                set = new StorageSet();
                map.put(db.storage_Table.Vendor, request.getParameter(db.vendor_Table.TABLE_NAME));
                break;
            }
            
            case db.wwan_Table.TABLE_NAME :{
                set = new WwanSet();
                map.put(db.wwan_Table.Vendor, request.getParameter(db.vendor_Table.TABLE_NAME));
                map.put(db.wwan_Table.Type, request.getParameter(db.wwan_type_Table.TABLE_NAME));
                break;
            }
            
            case db.wlan_Table.TABLE_NAME :{
                set = new WlanSet();
                map.put(db.wlan_Table.Vendor, request.getParameter(db.vendor_Table.TABLE_NAME));
                map.put(db.wlan_Table.Max_Speed, request.getParameter(db.wlan_max_speed_Table.TABLE_NAME));
                break;
            }
            
            case db.kbc_ebc_Table.TABLE_NAME :{
                set = new KbcEbcSet();
                map.put(db.kbc_ebc_Table.Vendor, request.getParameter(db.vendor_Table.TABLE_NAME));
                break;
            }
            
            case db.click_pad_Table.TABLE_NAME :{
                set = new ClickPadSet();
                map.put(db.click_pad_Table.Vendor, request.getParameter(db.vendor_Table.TABLE_NAME));
                break;
            }
            
            case db.lcd_panel_Table.TABLE_NAME :{
                set = new LcdPanelSet();
                map.put(db.lcd_panel_Table.Vendor, request.getParameter(db.vendor_Table.TABLE_NAME));
                map.put(db.lcd_panel_Table.Size, request.getParameter(db.panel_size_Table.TABLE_NAME));
                break;
            }
            
            case db.touch_panel_Table.TABLE_NAME :{
                set = new TouchPanelSet();
                map.put(db.touch_panel_Table.Vendor, request.getParameter(db.vendor_Table.TABLE_NAME));
                map.put(db.touch_panel_Table.Size, request.getParameter(db.panel_size_Table.TABLE_NAME));
                map.put(db.touch_panel_Table.IC_Vendor, request.getParameter(db.touch_panel_ic_vendor_Table.TABLE_NAME));
                map.put(db.touch_panel_Table.IC, request.getParameter(db.touch_panel_ic_Table.TABLE_NAME));
                break;
            }
            
            case db.camera_Table.TABLE_NAME :{
                set = new CameraSet();
                map.put(db.camera_Table.Vendor, request.getParameter(db.vendor_Table.TABLE_NAME));
                map.put(db.camera_Table.Pixel_Mega, request.getParameter(db.camera_pixel_Table.TABLE_NAME));
                map.put(db.camera_Table.Sensor_IC, request.getParameter(db.camera_sensor_ic_Table.TABLE_NAME));
                break;
            }
            
            case db.button_Table.TABLE_NAME :{
                set = new ButtonSet();
                break;
            }
            
            case db.card_reader_Table.TABLE_NAME :{
                set = new CardReaderSet();
                map.put(db.card_reader_Table.Vendor, request.getParameter(db.vendor_Table.TABLE_NAME));
                break;
            }
            
            case db.antennas_Table.TABLE_NAME :{
                set = new AntennasSet();
                break;
            }
            
            case db.keyboard_Table.TABLE_NAME :{
                set = new KeyboardSet();
                map.put(db.keyboard_Table.Vendor, request.getParameter(db.vendor_Table.TABLE_NAME));
                map.put(db.keyboard_Table.Type, request.getParameter(db.keyboard_type_Table.TABLE_NAME));
                map.put(db.keyboard_Table.OS, request.getParameter(db.keyboard_os_Table.TABLE_NAME));
                break;
            }
            
            case db.battery_Table.TABLE_NAME :{
                set = new BatterySet();
                map.put(db.battery_Table.Vendor, request.getParameter(db.vendor_Table.TABLE_NAME));
                break;
            }
            
            case db.charger_Table.TABLE_NAME :{
                set = new ChargerSet();
                map.put(db.charger_Table.Vendor, request.getParameter(db.vendor_Table.TABLE_NAME));
                break;
            }
            
            case db.measurement_Table.TABLE_NAME :{
                set = new MeasurementSet();
                break;
            }
            
            case db.graphic_Table.TABLE_NAME :{
                set = new MeasurementSet();
                map.put(db.graphic_Table.Vendor, request.getParameter(db.vendor_Table.TABLE_NAME));
                break;
            }
            
            case db.audio_codec_Table.TABLE_NAME :{
                set = new MeasurementSet();
                map.put(db.audio_codec_Table.Vendor, request.getParameter(db.vendor_Table.TABLE_NAME));
                break;
            }
            
            case db.panel_interface_bridge_Table.TABLE_NAME :{
                set = new MeasurementSet();
                map.put(db.panel_interface_bridge_Table.Vendor, request.getParameter(db.vendor_Table.TABLE_NAME));
                break;
            }
            
            case db.external_storage_Table.TABLE_NAME :{
                set = new MeasurementSet();
                map.put(db.external_storage_Table.Vendor, request.getParameter(db.vendor_Table.TABLE_NAME));
                break;
            }
            
            case db.odd_Table.TABLE_NAME :{
                set = new MeasurementSet();
                map.put(db.odd_Table.Vendor, request.getParameter(db.vendor_Table.TABLE_NAME));
                break;
            }
            
            case db.speaker_Table.TABLE_NAME :{
                set = new MeasurementSet();
                map.put(db.speaker_Table.Vendor, request.getParameter(db.vendor_Table.TABLE_NAME));
                break;
            }
            
            case db.mic_Table.TABLE_NAME :{
                set = new MeasurementSet();
                map.put(db.mic_Table.Vendor, request.getParameter(db.vendor_Table.TABLE_NAME));
                break;
            }
            
            case db.io_port_Table.TABLE_NAME :{
                set = new MeasurementSet();
                break;
            }
            
            case db.os_Table.TABLE_NAME :{
                set = new MeasurementSet();
                break;
            }
            
        }
        
        try( Connection conn = DriverManager.getConnection(DBurl, DBuser, DBpassword) ){
            Statement stm = conn.createStatement();
            set.addComponentBySearchMap(stm, getSearchMap(map));
        } 
        catch(SQLException ex){
            logger.log(Level.WARNING, "In SQLException (requestToCom) : " + ex.toString());
            ex.printStackTrace();
        }
        return set;
    }
    
    private XSSFSheet createSheet (XSSFWorkbook workbook, ComponentSet comSet) {
        XSSFSheet sheet = workbook.createSheet(comSet.getComponentName());
        XSSFRow row = sheet.createRow(0);
        mapToCell(row, "main", comSet.getMainColumn());
        mapToCell(row, "attr", comSet.getForeignColumn());
        mapToCell(row, "des", comSet.getGeneraColumn());
        
        return sheet;
    }
    
    private void ProjectResultMap(Statement stm, Map<String, List<String>> map, String pjname, String cname, String tname, String mainkey) throws SQLException {
        
        ResultSet result = MySQLSyntax.getResultSet(stm, tname, db2.project_Table.ProjectName, pjname);
        List list = new ArrayList<String>();
        while ( result.next() ) {
            list.add( result.getString(mainkey) );
        }
        
        if (!list.isEmpty()) {
            list.add(0, mainkey);
            map.put(cname, list);
        }
    }
    
    public ExcelWriter(String pjname, String id) {
        logger = Logger.getLogger(ExcelWriter.class.getName());
    
        String cate = null;
        Map<String, List<String>> map = new TreeMap<String, List<String>>();
        List<String> typ = new ArrayList<String>();
        List<String> des = new ArrayList<String>();
        
        Map<String, List<String[]>> emap = new TreeMap<String, List<String[]>>();
        String[] stra;
        
        try( Connection conn = DriverManager.getConnection(DB2url, DB2user, DB2password) ){
            Statement stm = conn.createStatement();
           
            ResultSet result = MySQLSyntax.getResultSet(stm, db2.project_Table.TABLE_NAME, db2.project_Table.ProjectName, pjname);
            if ( result.next() ) {
                cate = result.getString(db2.project_Table.Category);
            }
            
            result = MySQLSyntax.getResultSet(stm, db2.description_Table.TABLE_NAME, db2.description_Table.ProjectName, pjname);
            while (result.next()) {
                stra = new String[2];
                stra[0] = result.getString(db2.description_Table.Type);
                stra[1] = result.getString(db2.description_Table.Description);
                
                if (!emap.containsKey("Others")) {
                    emap.put("Others", new ArrayList<String[]>());
                }
                emap.get("Others").add(stra);
            }
            
            
            ProjectResultMap(stm, map, pjname, Antennas.TableName, db2.project_antennas_Table.TABLE_NAME, Antennas.MainKey);
            ProjectResultMap(stm, map, pjname, Battery.TableName, db2.project_battery_Table.TABLE_NAME, Battery.MainKey);
            ProjectResultMap(stm, map, pjname, Button.TableName, db2.project_button_Table.TABLE_NAME, Button.MainKey);
            ProjectResultMap(stm, map, pjname, Camera.TableName, db2.project_camera_Table.TABLE_NAME, Camera.MainKey);
            ProjectResultMap(stm, map, pjname, CardReader.TableName, db2.project_card_reader_Table.TABLE_NAME, CardReader.MainKey);
            ProjectResultMap(stm, map, pjname, Charger.TableName, db2.project_charger_Table.TABLE_NAME, Charger.MainKey);
            ProjectResultMap(stm, map, pjname, ClickPad.TableName, db2.project_click_pad_Table.TABLE_NAME, ClickPad.MainKey);
            ProjectResultMap(stm, map, pjname, CPU.TableName, db2.project_cpu_Table.TABLE_NAME, CPU.MainKey);
            ProjectResultMap(stm, map, pjname, Ethernet.TableName, db2.project_ethernet_Table.TABLE_NAME, Ethernet.MainKey);
            ProjectResultMap(stm, map, pjname, KBCEBC.TableName, db2.project_kbc_ebc_Table.TABLE_NAME, KBCEBC.MainKey);
            ProjectResultMap(stm, map, pjname, Keyboard.TableName, db2.project_keyboard_Table.TABLE_NAME, Keyboard.MainKey);
            ProjectResultMap(stm, map, pjname, LcdPanel.TableName, db2.project_lcd_panel_Table.TABLE_NAME, LcdPanel.MainKey);
            ProjectResultMap(stm, map, pjname, Measurement.TableName, db2.project_measurement_Table.TABLE_NAME, Measurement.MainKey);
            ProjectResultMap(stm, map, pjname, Memory.TableName, db2.project_memory_Table.TABLE_NAME, Memory.MainKey);
            ProjectResultMap(stm, map, pjname, Sensor.TableName, db2.project_sensor_Table.TABLE_NAME, Sensor.MainKey);
            ProjectResultMap(stm, map, pjname, Storage.TableName, db2.project_storage_Table.TABLE_NAME, Storage.MainKey);
            ProjectResultMap(stm, map, pjname, TouchPanel.TableName, db2.project_touch_panel_Table.TABLE_NAME, TouchPanel.MainKey);
            ProjectResultMap(stm, map, pjname, WLAN.TableName, db2.project_wlan_Table.TABLE_NAME, WLAN.MainKey);
            ProjectResultMap(stm, map, pjname, WWAN.TableName, db2.project_wwan_Table.TABLE_NAME, WWAN.MainKey);
            ProjectResultMap(stm, map, pjname, Graphic.TableName, db2.project_graphic_Table.TABLE_NAME, Graphic.MainKey);
            ProjectResultMap(stm, map, pjname, AudioCodec.TableName, db2.project_audio_codec_Table.TABLE_NAME, AudioCodec.MainKey);
            ProjectResultMap(stm, map, pjname, PanelInterfaceBridge.TableName, db2.project_panel_interface_bridge_Table.TABLE_NAME, PanelInterfaceBridge.MainKey);
            ProjectResultMap(stm, map, pjname, ExternalStorageCard.TableName, db2.project_external_storage_card_Table.TABLE_NAME, ExternalStorageCard.MainKey);
            ProjectResultMap(stm, map, pjname, ODD.TableName, db2.project_odd_Table.TABLE_NAME, ODD.MainKey);
            ProjectResultMap(stm, map, pjname, Speaker.TableName, db2.project_speaker_Table.TABLE_NAME, Speaker.MainKey);
            ProjectResultMap(stm, map, pjname, Mic.TableName, db2.project_mic_Table.TABLE_NAME, Mic.MainKey);
            ProjectResultMap(stm, map, pjname, IoPort.TableName, db2.project_io_port_Table.TABLE_NAME, IoPort.MainKey);
            ProjectResultMap(stm, map, pjname, OS.TableName, db2.project_os_Table.TABLE_NAME, OS.MainKey);
            
        }catch(SQLException ex){
            logger.log(Level.WARNING, "In SQLException (getSearchProjectResultPage) : " + ex.toString());
            ex.printStackTrace();
        }
        
        
        
        List<String[]> list;
        String str;
        
        
        try( Connection conn = DriverManager.getConnection(DBurl, DBuser, DBpassword) ){
            Statement stm = conn.createStatement();
            ResultSet result = null;
            
           
            
            for (Entry<String, List<String>> entry : map.entrySet()) {
                
                
                switch ( entry.getKey() ) {
                    
                    case db.antennas_Table.TABLE_NAME : {
                        for (String s : entry.getValue()) {
                            result = MySQLSyntax.getResultSet(stm, db.antennas_Table.TABLE_NAME, Antennas.MainKey, s);
                            if (result.next()) {
                                stra = new String[2];
                                stra[0] = result.getString(db.antennas_Table.Type);
                                stra[1] = result.getString(db.antennas_Table.Description);
                                if (!emap.containsKey("Antenna"))
                                    emap.put("Antenna",  new ArrayList<String[]>());
                            
                               emap.get("Antenna").add(stra);       
                            }
                        }
                        break;
                    }
                    
                    case db.audio_codec_Table.TABLE_NAME : {
                        for (String s : entry.getValue()) {
                            result = MySQLSyntax.getResultSet(stm, db.audio_codec_Table.TABLE_NAME, AudioCodec.MainKey, s);
                            if (result.next()) {
                                stra = new String[2];
                                stra[0] = result.getString(db.audio_codec_Table.Vendor);
                                stra[1] = result.getString(db.audio_codec_Table.Model) + "\n" + result.getString(db.audio_codec_Table.Description);
                                if (!emap.containsKey("Audio Codec"))
                                    emap.put("Audio Codec",  new ArrayList<String[]>());
                            
                               emap.get("Audio Codec").add(stra);       
                            }
                        }
                        break;
                    }
                    
                    case db.battery_Table.TABLE_NAME : {
                        for (String s : entry.getValue()) {
                            result = MySQLSyntax.getResultSet(stm, db.battery_Table.TABLE_NAME, Battery.MainKey, s);
                            if (result.next()) {
                                stra = new String[2];
                                stra[0] = result.getString(db.battery_Table.Vendor);
                                stra[1] = result.getString(db.battery_Table.ModelName) + " " + result.getString(db.battery_Table.Capacity)
                                    + " " + result.getString(db.battery_Table.Cell) + "\n" + result.getString(db.battery_Table.Description);
                                if (!emap.containsKey("Battery Pack"))
                                    emap.put("Battery Pack",  new ArrayList<String[]>());
                            
                               emap.get("Battery Pack").add(stra);       
                            }
                        }
                        break;
                    }
                    
                    case db.button_Table.TABLE_NAME : {
                        for (String s : entry.getValue()) {
                            result = MySQLSyntax.getResultSet(stm, db.button_Table.TABLE_NAME, Button.MainKey, s);
                            if (result.next()) {
                                stra = new String[2];
                                stra[0] = result.getString(db.button_Table.Type);
                                stra[0] = result.getString(db.button_Table.Description);
                                if (!emap.containsKey("Button"))
                                    emap.put("Button",  new ArrayList<String[]>());
                            
                               emap.get("Button").add(stra);       
                            }
                        }
                        break;
                    }
                    
                    case db.camera_Table.TABLE_NAME : {
                        for (String s : entry.getValue()) {
                            result = MySQLSyntax.getResultSet(stm, db.camera_Table.TABLE_NAME, Camera.MainKey, s);
                            if (result.next()) {
                                stra = new String[2];
                                stra[0] = result.getString(db.camera_Table.Vendor);
                                stra[1] = result.getString(db.camera_Table.Model) + " " + result.getString(db.camera_Table.Sensor_IC)
                                    + " " + result.getString(db.camera_Table.Pixel_Mega) + " " + result.getString(db.camera_Table.AF_FF) 
                                    + " " + result.getString(db.camera_Table.Lens) + "\n" + result.getString(db.camera_Table.Description);
                                if (!emap.containsKey("Web CAM"))
                                    emap.put("Web CAM",  new ArrayList<String[]>());
                            
                               emap.get("Web CAM").add(stra);       
                            }
                        }
                        break;
                    }
                    
                    case db.card_reader_Table.TABLE_NAME : {
                        for (String s : entry.getValue()) {
                            result = MySQLSyntax.getResultSet(stm, db.card_reader_Table.TABLE_NAME, CardReader.MainKey, s);
                            if (result.next()) {
                                stra = new String[2];
                                stra[0] = result.getString(db.card_reader_Table.Vendor);
                                stra[1] = result.getString(db.card_reader_Table.Model) + "\n" + result.getString(db.card_reader_Table.Description);
                                if (!emap.containsKey("Card reader"))
                                    emap.put("Card reader",  new ArrayList<String[]>());
                            
                               emap.get("Card reader").add(stra);       
                            }
                        }
                        break;
                    }
                    
                    case db.charger_Table.TABLE_NAME : {
                        for (String s : entry.getValue()) {
                            result = MySQLSyntax.getResultSet(stm, db.charger_Table.TABLE_NAME, Charger.MainKey, s);
                            if (result.next()) {
                                stra = new String[2];
                                stra[0] = result.getString(db.charger_Table.Vendor);
                                stra[1] = result.getString(db.charger_Table.ModelName) + " " + result.getString(db.charger_Table.Type)
                                    + " " + result.getString(db.charger_Table.OUTPUT) + " " + result.getString(db.charger_Table.INPUT) 
                                    + "\n" + result.getString(db.charger_Table.Description);
                                if (!emap.containsKey("Adaptor"))
                                    emap.put("Adaptor",  new ArrayList<String[]>());
                            
                               emap.get("Adaptor").add(stra);       
                            }
                        }
                        break;
                    }
                    
                    case db.click_pad_Table.TABLE_NAME : {
                        for (String s : entry.getValue()) {
                            result = MySQLSyntax.getResultSet(stm, db.click_pad_Table.TABLE_NAME, ClickPad.MainKey, s);
                            if (result.next()) {
                                stra = new String[2];
                                stra[0] = result.getString(db.click_pad_Table.Vendor);
                                stra[1] = result.getString(db.click_pad_Table.ModelName) + "\n" + result.getString(db.click_pad_Table.Description);
                                if (!emap.containsKey("Pointing Device"))
                                    emap.put("Pointing Device",  new ArrayList<String[]>());
                            
                               emap.get("Pointing Device").add(stra);       
                            }
                        }
                        break;
                    }
                    
                    case db.cpu_Table.TABLE_NAME : {
                        for (String s : entry.getValue()) {
                            result = MySQLSyntax.getResultSet(stm, db.cpu_Table.TABLE_NAME, CPU.MainKey, s);
                            if (result.next()) {
                                stra = new String[2];
                                stra[0] = result.getString(db.cpu_Table.Vendor) + "/" + result.getString(db.cpu_Table.CodeName);
                                stra[1] = result.getString(db.cpu_Table.ModelName) + " " + result.getString(db.cpu_Table.MaxCoreFreq)
                                    + " " + result.getString(db.cpu_Table.CoreThreats) + " " + result.getString(db.cpu_Table.Gfx) 
                                    + " " + result.getString(db.cpu_Table.TDP);
                                if (!emap.containsKey("CPU/SoC"))
                                    emap.put("CPU/SoC",  new ArrayList<String[]>());
                            
                               emap.get("CPU/SoC").add(stra);       
                            }
                        }
                        break;
                    }
                    
                    case db.ethernet_Table.TABLE_NAME : {
                        for (String s : entry.getValue()) {
                            result = MySQLSyntax.getResultSet(stm, db.ethernet_Table.TABLE_NAME, Ethernet.MainKey, s);
                            if (result.next()) {
                                stra = new String[2];
                                stra[0] = result.getString(db.ethernet_Table.Vendor);
                                stra[1] = result.getString(db.ethernet_Table.ModelName) + "\n" + result.getString(db.ethernet_Table.Description);
                                if (!emap.containsKey("LAN Controller"))
                                    emap.put("LAN Controller",  new ArrayList<String[]>());
                            
                               emap.get("LAN Controller").add(stra);
                            }
                        }
                        break;
                    }
                    
                    case db.external_storage_Table.TABLE_NAME : {
                        for (String s : entry.getValue()) {
                            result = MySQLSyntax.getResultSet(stm, db.external_storage_Table.TABLE_NAME, ExternalStorageCard.MainKey, s);
                            if (result.next()) {
                                stra = new String[2];
                                stra[0] = result.getString(db.external_storage_Table.Vendor);
                                stra[1] = result.getString(db.external_storage_Table.Type) + " " + result.getString(db.external_storage_Table.Model)
                                    + " " + result.getString(db.external_storage_Table.Dimension) + " " + result.getString(db.external_storage_Table.Interface) 
                                    + " " + result.getString(db.external_storage_Table.Speed) + " " + result.getString(db.external_storage_Table.Capacity);
                                if (!emap.containsKey("Extra storage card"))
                                    emap.put("Extra storage card",  new ArrayList<String[]>());
                            
                               emap.get("Extra storage card").add(stra);       
                            }
                        }
                        break;
                    }
                    
                    case db.graphic_Table.TABLE_NAME : {
                        for (String s : entry.getValue()) {
                            result = MySQLSyntax.getResultSet(stm, db.graphic_Table.TABLE_NAME, Graphic.MainKey, s);
                            if (result.next()) {
                                stra = new String[2];
                                stra[0] = result.getString(db.graphic_Table.Vendor);
                                stra[1] = result.getString(db.graphic_Table.ModelName) + " " + result.getString(db.graphic_Table.MemoryType)
                                    + " " + result.getString(db.graphic_Table.vRAM_size) + "\n" + result.getString(db.graphic_Table.Description);
                                if (!emap.containsKey("Graphic"))
                                    emap.put("Graphic",  new ArrayList<String[]>());
                            
                               emap.get("Graphic").add(stra);       
                            }
                        }
                        break;
                    }
                    
                    case db.io_port_Table.TABLE_NAME : {
                        for (String s : entry.getValue()) {
                            result = MySQLSyntax.getResultSet(stm, db.io_port_Table.TABLE_NAME, IoPort.MainKey, s);
                            if (result.next()) {
                                stra = new String[2];
                                stra[0] = "";
                                stra[1] = result.getString(db.io_port_Table.Standard) + " " + result.getString(db.io_port_Table.ConnectorType)
                                    + "\n" + result.getString(db.io_port_Table.Description);
                                if (!emap.containsKey("I/O Port"))
                                    emap.put("I/O Port",  new ArrayList<String[]>());
                            
                               emap.get("I/O Port").add(stra);       
                            }
                        }
                        break;
                    }
                    
                    case db.kbc_ebc_Table.TABLE_NAME : {
                        for (String s : entry.getValue()) {
                            result = MySQLSyntax.getResultSet(stm, db.kbc_ebc_Table.TABLE_NAME, KBCEBC.MainKey, s);
                            if (result.next()) {
                                stra = new String[2];
                                stra[0] = result.getString(db.kbc_ebc_Table.Vendor);
                                stra[1] = result.getString(db.kbc_ebc_Table.Model) + "\n" + result.getString(db.kbc_ebc_Table.Description);
                                if (!emap.containsKey("KBC/EBC"))
                                    emap.put("KBC/EBC",  new ArrayList<String[]>());
                            
                               emap.get("KBC/EBC").add(stra);       
                            }
                        }
                        break;
                    }
                    
                    case db.keyboard_Table.TABLE_NAME : {
                        for (String s : entry.getValue()) {
                            result = MySQLSyntax.getResultSet(stm, db.keyboard_Table.TABLE_NAME, Keyboard.MainKey, s);
                            if (result.next()) {
                                stra = new String[2];
                                stra[0] = result.getString(db.keyboard_Table.Vendor);
                                stra[1] = result.getString(db.keyboard_Table.ModelName) + " " + result.getString(db.keyboard_Table.Type)
                                    + " " + result.getString(db.keyboard_Table.OS) + " " + result.getString(db.keyboard_Table.Layout)
                                    + "\n" + result.getString(db.keyboard_Table.Description);
                                if (!emap.containsKey("Keyboard"))
                                    emap.put("Keyboard",  new ArrayList<String[]>());
                            
                               emap.get("Keyboard").add(stra);       
                            }
                        }
                        break;
                    }
                    
                    case db.lcd_panel_Table.TABLE_NAME : {
                        for (String s : entry.getValue()) {
                            result = MySQLSyntax.getResultSet(stm, db.lcd_panel_Table.TABLE_NAME, LcdPanel.MainKey, s);
                            if (result.next()) {
                                stra = new String[2];
                                stra[0] = result.getString(db.lcd_panel_Table.Vendor);
                                stra[1] = result.getString(db.lcd_panel_Table.ModelName) + " " + result.getString(db.lcd_panel_Table.Size)
                                    + " " + result.getString(db.lcd_panel_Table.Resolution) + " " + result.getString(db.lcd_panel_Table.Type) 
                                    + " " + result.getString(db.lcd_panel_Table.Nits)+ " " + result.getString(db.lcd_panel_Table.Touch)
                                    + " " + result.getString(db.lcd_panel_Table.Interface);
                                if (!emap.containsKey("LCD"))
                                    emap.put("LCD",  new ArrayList<String[]>());
                            
                               emap.get("LCD").add(stra);       
                            }
                        }
                        break;
                    }
                    
                    case db.measurement_Table.TABLE_NAME : {
                        for (String s : entry.getValue()) {
                            result = MySQLSyntax.getResultSet(stm, db.measurement_Table.TABLE_NAME, Measurement.MainKey, s);
                            if (result.next()) {
                                stra = new String[2];
                                stra[0] = result.getString(db.measurement_Table.Type);
                                stra[1] = result.getString(db.measurement_Table.Dimension) + " " + result.getString(db.measurement_Table.Weight);
                                if (!emap.containsKey("Physical Outline"))
                                    emap.put("Physical Outline",  new ArrayList<String[]>());
                            
                               emap.get("Physical Outline").add(stra);       
                            }
                        }
                        break;
                    }
                    
                    case db.memory_Table.TABLE_NAME : {
                        for (String s : entry.getValue()) {
                            result = MySQLSyntax.getResultSet(stm, db.memory_Table.TABLE_NAME, Memory.MainKey, s);
                            if (result.next()) {
                                stra = new String[2];
                                stra[0] = result.getString(db.memory_Table.Vendor);
                                stra[1] = result.getString(db.memory_Table.Type) + " " + result.getString(db.memory_Table.Frequency)
                                    + " " + result.getString(db.memory_Table.Capacity) + "\n" + result.getString(db.memory_Table.Description);
                                if (!emap.containsKey("Memory"))
                                    emap.put("Memory",  new ArrayList<String[]>());
                            
                               emap.get("Memory").add(stra);       
                            }
                        }
                        break;
                    }
                    
                    case db.mic_Table.TABLE_NAME : {
                        for (String s : entry.getValue()) {
                            result = MySQLSyntax.getResultSet(stm, db.mic_Table.TABLE_NAME, Mic.MainKey, s);
                            if (result.next()) {
                                stra = new String[2];
                                stra[0] = "Mic";
                                stra[1] = result.getString(db.mic_Table.ModelName) + "\n" + result.getString(db.mic_Table.Description);
                                if (!emap.containsKey("Audio"))
                                    emap.put("Audio",  new ArrayList<String[]>());
                            
                               emap.get("Audio").add(stra);       
                            }
                        }
                        break;
                    }
                    
                    case db.speaker_Table.TABLE_NAME : {
                        for (String s : entry.getValue()) {
                            result = MySQLSyntax.getResultSet(stm, db.speaker_Table.TABLE_NAME, Speaker.MainKey, s);
                            if (result.next()) {
                                stra = new String[2];
                                stra[0] = "Speaker";
                                stra[1] =result.getString(db.speaker_Table.ModelName) + "\n" + result.getString(db.mic_Table.Description);
                                if (!emap.containsKey("Audio"))
                                    emap.put("Audio",  new ArrayList<String[]>());
                            
                               emap.get("Audio").add(stra);       
                            }
                        }
                        break;
                    }
                    
                    case db.odd_Table.TABLE_NAME : {
                        for (String s : entry.getValue()) {
                            result = MySQLSyntax.getResultSet(stm, db.odd_Table.TABLE_NAME, ODD.MainKey, s);
                            if (result.next()) {
                                stra = new String[2];
                                stra[0] = result.getString(db.odd_Table.Vendor);
                                stra[1] = result.getString(db.odd_Table.Model) + " " + result.getString(db.odd_Table.Interface)
                                    + " " + result.getString(db.odd_Table.zHigh) + "\n" + result.getString(db.odd_Table.Description);
                                if (!emap.containsKey("ODD"))
                                    emap.put("ODD",  new ArrayList<String[]>());
                            
                               emap.get("ODD").add(stra);       
                            }
                        }
                        break;
                    }
                    
                    case db.os_Table.TABLE_NAME : {
                        for (String s : entry.getValue()) {
                            result = MySQLSyntax.getResultSet(stm, db.os_Table.TABLE_NAME, OS.MainKey, s);
                            if (result.next()) {
                                stra = new String[2];
                                stra[0] = result.getString(db.os_Table.Type);
                                stra[1] = result.getString(db.os_Table.Description);
                                if (!emap.containsKey("OS"))
                                    emap.put("OS",  new ArrayList<String[]>());
                            
                               emap.get("OS").add(stra);       
                            }
                        }
                        break;
                    }
                    
                    case db.panel_interface_bridge_Table.TABLE_NAME : {
                        for (String s : entry.getValue()) {
                            result = MySQLSyntax.getResultSet(stm, db.panel_interface_bridge_Table.TABLE_NAME, PanelInterfaceBridge.MainKey, s);
                            if (result.next()) {
                                stra = new String[2];
                                stra[0] = result.getString(db.panel_interface_bridge_Table.Vendor);
                                stra[1] = result.getString(db.panel_interface_bridge_Table.ModelName) + "\n" + result.getString(db.panel_interface_bridge_Table.Description);
                                if (!emap.containsKey("Bridge IC"))
                                    emap.put("Bridge IC",  new ArrayList<String[]>());
                            
                               emap.get("Bridge IC").add(stra);       
                            }
                        }
                        break;
                    }
                    
                    case db.sensor_Table.TABLE_NAME : {
                        for (String s : entry.getValue()) {
                            result = MySQLSyntax.getResultSet(stm, db.sensor_Table.TABLE_NAME, Sensor.MainKey, s);
                            if (result.next()) {
                                stra = new String[2];
                                stra[0] = result.getString(db.sensor_Table.Type);
                                stra[1] = result.getString(db.sensor_Table.Vendor) + " " + result.getString(db.sensor_Table.ModelName)
                                     + "\n" + result.getString(db.sensor_Table.Description);
                                if (!emap.containsKey("Sensor"))
                                    emap.put("Sensor",  new ArrayList<String[]>());
                            
                               emap.get("Sensor").add(stra);       
                            }
                        }
                        break;
                    }
                    
                    case db.storage_Table.TABLE_NAME : {
                        for (String s : entry.getValue()) {
                            result = MySQLSyntax.getResultSet(stm, db.storage_Table.TABLE_NAME, Storage.MainKey, s);
                            if (result.next()) {
                                stra = new String[2];
                                stra[0] = result.getString(db.storage_Table.Vendor);
                                stra[1] = result.getString(db.storage_Table.Type) + " " + result.getString(db.storage_Table.Model)
                                     + " " + result.getString(db.storage_Table.Capacity) + " " + result.getString(db.storage_Table.Interface)
                                     + " " + result.getString(db.storage_Table.zHight) + " " + result.getString(db.storage_Table.Rpm) + " " + result.getString(db.storage_Table.Speed);
                                if (!emap.containsKey("Storage"))
                                    emap.put("Storage",  new ArrayList<String[]>());
                            
                               emap.get("Storage").add(stra);       
                            }
                        }
                        break;
                    }
                    
                    case db.touch_panel_Table.TABLE_NAME : {
                        for (String s : entry.getValue()) {
                            result = MySQLSyntax.getResultSet(stm, db.touch_panel_Table.TABLE_NAME, TouchPanel.MainKey, s);
                            if (result.next()) {
                                stra = new String[2];
                                stra[0] = result.getString(db.touch_panel_Table.Vendor);
                                stra[1] = result.getString(db.touch_panel_Table.ModelName) + " " + result.getString(db.touch_panel_Table.IC)
                                     + " " + result.getString(db.touch_panel_Table.Type) + " " + result.getString(db.touch_panel_Table.Multi_Touch)
                                        + " " + result.getString(db.touch_panel_Table.Stylus);
                                if (!emap.containsKey("Touch solution"))
                                    emap.put("Touch solution",  new ArrayList<String[]>());
                            
                               emap.get("Touch solution").add(stra);       
                            }
                        }
                        break;
                    }
                    
                    case db.wlan_Table.TABLE_NAME : {
                        for (String s : entry.getValue()) {
                            result = MySQLSyntax.getResultSet(stm, db.wlan_Table.TABLE_NAME, WLAN.MainKey, s);
                            if (result.next()) {
                                stra = new String[2];
                                stra[0] = result.getString(db.wlan_Table.Vendor);
                                stra[1] = result.getString(db.wlan_Table.ModelName) + " " + result.getString(db.wlan_Table.Protocal)
                                     + " " + result.getString(db.wlan_Table.nTnR) + " " + result.getString(db.wlan_Table.Band)
                                        + " " + result.getString(db.wlan_Table.Bluetooth) + " " + result.getString(db.wlan_Table.Max_Speed);
                                if (!emap.containsKey("Wireless LAN and Bluetooth Combo"))
                                    emap.put("Wireless LAN and Bluetooth Combo",  new ArrayList<String[]>());
                            
                               emap.get("Wireless LAN and Bluetooth Combo").add(stra);       
                            }
                        }
                        break;
                    }
                    
                    case db.wwan_Table.TABLE_NAME : {
                        for (String s : entry.getValue()) {
                            result = MySQLSyntax.getResultSet(stm, db.wwan_Table.TABLE_NAME, WWAN.MainKey, s);
                            if (result.next()) {
                                stra = new String[2];
                                stra[0] = result.getString(db.wwan_Table.Vendor);
                                stra[1] = result.getString(db.wwan_Table.ModelName) + " " + result.getString(db.wwan_Table.Type)
                                     + "\n" + result.getString(db.wwan_Table.Description);
                                if (!emap.containsKey("WWAN"))
                                    emap.put("WWAN",  new ArrayList<String[]>());
                            
                               emap.get("WWAN").add(stra);       
                            }
                        }
                        break;
                    }
                }
            }
         }catch(SQLException ex){
            logger.log(Level.WARNING, "In SQLException (ExcelWriter) : " + ex.toString());
            ex.printStackTrace();
        }
        
        
        try {
            workbook = new XSSFWorkbook(new File(filepath + template));
            
            
            XSSFSheet sheet = workbook.getSheetAt(0);
            XSSFRow row;
            XSSFCell cell;
            
            int y, k;
            
            y = 45;
            
            
            
            List<Entry<String, List<String[]>>> elist = new ArrayList<Entry<String, List<String[]>>>();
            
            if (emap.containsKey("CPU/SoC"))
                elist.add( new SimpleEntry<String, List<String[]>>("CPU/SoC", emap.get("CPU/SoC")) );
             if (emap.containsKey("Graphic"))
                elist.add( new SimpleEntry<String, List<String[]>>("Graphic", emap.get("Graphic")) );
            if (emap.containsKey("Memory"))
                elist.add( new SimpleEntry<String, List<String[]>>("Memory", emap.get("Memory")) );
            if (emap.containsKey("Audio Codec"))
                elist.add( new SimpleEntry<String, List<String[]>>("Audio Codec", emap.get("Audio Codec")) );
            if (emap.containsKey("KBC/EBC"))
                elist.add( new SimpleEntry<String, List<String[]>>("KBC/EBC", emap.get("KBC/EBC")) );
            if (emap.containsKey("LAN Controller"))
                elist.add( new SimpleEntry<String, List<String[]>>("LAN Controller", emap.get("LAN Controller")) );
            if (emap.containsKey("Wireless LAN and Bluetooth Combo"))
                elist.add( new SimpleEntry<String, List<String[]>>("Wireless LAN and Bluetooth Combo", emap.get("Wireless LAN and Bluetooth Combo")) );
            if (emap.containsKey("WWAN"))
                elist.add( new SimpleEntry<String, List<String[]>>("WWAN", emap.get("WWAN")) );
            if (emap.containsKey("Antenna"))
                elist.add( new SimpleEntry<String, List<String[]>>("Antenna", emap.get("Antenna")) );
            if (emap.containsKey("Pointing Device"))
                elist.add( new SimpleEntry<String, List<String[]>>("Pointing Device", emap.get("Pointing Device")) );
            if (emap.containsKey("LCD"))
                elist.add( new SimpleEntry<String, List<String[]>>("LCD", emap.get("LCD")) );
            if (emap.containsKey("Touch solution"))
                elist.add( new SimpleEntry<String, List<String[]>>("Touch solution", emap.get("Touch solution")) );
            if (emap.containsKey("Bridge IC"))
                elist.add( new SimpleEntry<String, List<String[]>>("Bridge IC", emap.get("Bridge IC")) );
            if (emap.containsKey("Storage"))
                elist.add( new SimpleEntry<String, List<String[]>>("Storage", emap.get("Storage")) );
            if (emap.containsKey("Extra storage card"))
                elist.add( new SimpleEntry<String, List<String[]>>("Extra storage card", emap.get("Extra storage card")) );
            if (emap.containsKey("ODD"))
                elist.add( new SimpleEntry<String, List<String[]>>("ODD", emap.get("ODD")) );
            if (emap.containsKey("Card reader"))
                elist.add( new SimpleEntry<String, List<String[]>>("Card reader", emap.get("Card reader")) );
            if (emap.containsKey("Audio")) 
                elist.add( new SimpleEntry<String, List<String[]>>("Audio", emap.get("Audio")) );
            if (emap.containsKey("Web CAM")) 
                elist.add( new SimpleEntry<String, List<String[]>>("Web CAM", emap.get("Web CAM")) );
            if (emap.containsKey("Sensor"))
                elist.add( new SimpleEntry<String, List<String[]>>("Sensor", emap.get("Sensor")) );
            if (emap.containsKey("Battery Pack"))
                elist.add( new SimpleEntry<String, List<String[]>>("Battery Pack", emap.get("Battery Pack")) );
            if (emap.containsKey("Adaptor"))
                elist.add( new SimpleEntry<String, List<String[]>>("Adaptor", emap.get("Adaptor")) );
            if (emap.containsKey("I/O Port"))
                elist.add( new SimpleEntry<String, List<String[]>>("I/O Port", emap.get("I/O Port")) );
            if (emap.containsKey("Keyboard"))
                elist.add( new SimpleEntry<String, List<String[]>>("Keyboard", emap.get("Keyboard")) );
            if (emap.containsKey("Button"))
                elist.add( new SimpleEntry<String, List<String[]>>("Button", emap.get("Button")) );
            if (emap.containsKey("OS"))
                elist.add( new SimpleEntry<String, List<String[]>>("OS", emap.get("OS")) );
            if (emap.containsKey("Physical Outline"))
                elist.add( new SimpleEntry<String, List<String[]>>("Physical Outline", emap.get("Physical Outline")) );
            if (emap.containsKey("Others"))
                elist.add( new SimpleEntry<String, List<String[]>>("Others", emap.get("Others")) );
            
            
            for (Entry<String, List<String[]>> entry : elist) {
                for (int i = 0; i < entry.getValue().size(); i++) {
                    sheet.createRow(y + i);
                }
                
                
                //System.out.println(entry.getValue().size());
                
                
                row = sheet.getRow(y);
                cell = row.createCell(0);
                
                cell.setCellValue(entry.getKey());
                sheet.addMergedRegion(new CellRangeAddress(y,  y + entry.getValue().size() - 1, 0, 1));
                
                k = 0;
                for (String[] s : entry.getValue()) {
                    //System.out.println(s[0] + s[1]);
                    row = sheet.getRow(y + k);
                    cell = row.createCell(2);
                    cell.setCellValue(s[0]);
                    sheet.addMergedRegion(new CellRangeAddress(y + k,  y + k, 2, 3));
                    
                    cell = row.createCell(4);
                    cell.setCellValue(s[1]);
                    sheet.addMergedRegion(new CellRangeAddress(y + k,  y + k, 4, 10));
                    
                    k++;
                }
                
                y = y + entry.getValue().size();
            }
            
            filename = pjname + ".xlsx";
            
            try (FileOutputStream out = new FileOutputStream(filepath + filename)) {
                workbook.write(out);
            }
            
        } catch (Exception ex) {
            logger.log(Level.WARNING, "In Exception (ExcelWriter) : " + ex.toString());
            ex.printStackTrace();
        }

    }
    
    
    public ExcelWriter() {
        this(null);
    }
    
    public ExcelWriter(HttpServletRequest request) {
        logger = Logger.getLogger(ExcelWriter.class.getName());
        workbook = new XSSFWorkbook();
        Map<String, XSSFSheet> sheetMap = new TreeMap<String, XSSFSheet>();
        XSSFRow row;
        
        sheetMap.put(CPU.ComponentName, createSheet(workbook, new CpuSet()));
        sheetMap.put(Antennas.ComponentName, createSheet(workbook, new AntennasSet()));
        sheetMap.put(Button.ComponentName, createSheet(workbook, new ButtonSet()));
        sheetMap.put(Camera.ComponentName, createSheet(workbook, new CameraSet()));
        sheetMap.put(CardReader.ComponentName, createSheet(workbook, new CardReaderSet()));
        sheetMap.put(ClickPad.ComponentName, createSheet(workbook, new ClickPadSet()));
        sheetMap.put(Ethernet.ComponentName, createSheet(workbook, new EthernetSet()));
        sheetMap.put(KBCEBC.ComponentName, createSheet(workbook, new KbcEbcSet()));
        sheetMap.put(LcdPanel.ComponentName, createSheet(workbook, new LcdPanelSet()));
        sheetMap.put(Memory.ComponentName, createSheet(workbook, new MemorySet()));
        sheetMap.put(Sensor.ComponentName, createSheet(workbook, new SensorSet()));
        sheetMap.put(Storage.ComponentName, createSheet(workbook, new StorageSet()));
        sheetMap.put(TouchPanel.ComponentName, createSheet(workbook, new TouchPanelSet()));
        sheetMap.put(WLAN.ComponentName, createSheet(workbook, new WlanSet()));
        sheetMap.put(WWAN.ComponentName, createSheet(workbook, new WwanSet()));
        sheetMap.put(Keyboard.ComponentName, createSheet(workbook, new KeyboardSet()));
        sheetMap.put(Battery.ComponentName, createSheet(workbook, new BatterySet()));
        sheetMap.put(Charger.ComponentName, createSheet(workbook, new ChargerSet()));
        sheetMap.put(Measurement.ComponentName, createSheet(workbook, new MeasurementSet()));
        sheetMap.put(Graphic.ComponentName, createSheet(workbook, new GraphicSet()));
        sheetMap.put(AudioCodec.ComponentName, createSheet(workbook, new AudioCodecSet()));
        sheetMap.put(PanelInterfaceBridge.ComponentName, createSheet(workbook, new PanelInterfaceBridgeSet()));
        sheetMap.put(ExternalStorageCard.ComponentName, createSheet(workbook, new ExternalStorageCardSet()));
        sheetMap.put(ODD.ComponentName, createSheet(workbook, new ODDSet()));
        sheetMap.put(Speaker.ComponentName, createSheet(workbook, new SpeakerSet()));
        sheetMap.put(Mic.ComponentName, createSheet(workbook, new MicSet()));
        sheetMap.put(IoPort.ComponentName, createSheet(workbook, new IoPortSet()));
        sheetMap.put(OS.ComponentName, createSheet(workbook, new OSSet()));
        
        
        if (request != null) {
            ComponentSet set = requestToCom(request);
            filename = request.getParameter("category") + ".xlsx";
            
            int i = 1;
            
            for (Iterator<Component> iter = set.getComList().iterator(); iter.hasNext(); ) {
                row = sheetMap.get(set.getComponentName()).createRow(i++);
                int j = 0;
                
                for (Iterator<String> str = iter.next().getComponent().iterator(); str.hasNext();  ) {
                    String data = str.next();
                    row.createCell(j);
                     row.getCell(j).setCellValue(data);
                     j++;
                }
                
            }
        }
        
        autoStyle(workbook);
        
        try (FileOutputStream out = new FileOutputStream(filepath + filename)) {
            workbook.write(out);
        }
        catch (Exception ex) {
            logger.log(Level.WARNING, ex.toString());
            ex.printStackTrace();
        }
        
    }
    
    private void autoStyle (XSSFWorkbook workbook) {
    
        int snum = workbook.getNumberOfSheets();
        int cnum;

        for (int i = 0; i < snum; i++) {
            int rnum = workbook.getSheetAt(i).getLastRowNum();
            cnum = 0;
            for (int j =0; j < rnum; j++ ) {
                if ( workbook.getSheetAt(i).getRow(j) != null )
                    if (cnum < workbook.getSheetAt(i).getRow(j).getLastCellNum())
                        cnum = workbook.getSheetAt(i).getRow(j).getLastCellNum();
            }
            for (int j = 0; j < cnum; j++)
                workbook.getSheetAt(i).autoSizeColumn(j);
            
        }
        
    }
    
}
