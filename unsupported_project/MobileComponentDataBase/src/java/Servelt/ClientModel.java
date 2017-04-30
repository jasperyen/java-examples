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
import static java.lang.System.out;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import DataBaseInfo.DataBaseName.db;
import DataBaseInfo.DataBaseConn;
import DataBaseInfo.DataBaseName.db2;
import java.util.logging.Level;
import java.util.logging.Logger;
import Servelt.MySQLSyntax;
import java.util.Arrays;

/**
 *Model of client
 * 連線模型 負責處理不同連線之要求 
 * 
 * @author Jasper-Yen
 */
public class ClientModel implements DataBaseConn {

    Logger logger;
     
    
    /**
     * 建構子  
     * 載入JDBC, Logger
     */
    public ClientModel() {
        registerJDBC();
        logger = Logger.getLogger(ClientModel.class.getName());
    }
 
    
    /**
     * 取得登入頁面
     * 
     * @return HTML 登入頁面 
     */
    public String getLoginPage(){
        return getLoginPage(null);
    }
    
    
    /**
     * 取得登入頁面並夾帶彈出式訊息
     * 
     * @param alertData 彈出式訊息內容
     * @return String HTML 登入頁面
     */
    private String getLoginPage (String alertData){
        
        if ( alertData != null)
            alertData = "<script language=\"javascript\"> alert('"+ alertData +"'); </script>";
        else
            alertData = "";
        
        return "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "    <head>\n" +
                    "        <title>ECS PM component database management system</title>\n" +
                    "        <meta charset=\"UTF-8\">\n" +
                    "        <meta name=\"viewport\" content=\"width=device-width; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;\">\n" +
                    "        <style>\n" +
                    "                #background{\n" +
                    "                    position: absolute;\n" +
                    "                    top: 0;\n" +
                    "                    height : 100vh;\n" +
                    "                    width : 100vw;\n" +
                    "                    background-size: 100% auto;\n" +
                    "                    background-position: center;\n" +
                    "                    background-repeat: no-repeat;\n" +
                    "                 }\n" +
                    "                #block {\n" +
                    "                    margin: 30vh auto;\n" +
                    "                    width: 40vw;\n" +
                    "                    height: 40vh;\n" +
                    "                    text-align: center;\n" +
                    "                }\n" +
                    "        </style>\n" +
                    alertData + 
                    "    </head>\n" +
                    "<body style = \"overflow-x:hidden; overflow-y:hidden;\">\n" +
                    "            <div id='background'>\n" +
                    "                    <div id='block'>\n" +
                    "                    <span style=\"font-family:cursive;font-size:1.2cm\">Login</span>\n" +
                    "                    <form action=\"\" method=\"POST\">\n" +
                    "                        <table style=\"margin: 0px auto\">\n" +
                    "                            <tr>\n" +
                    "                                    <td>ID</td>\n" +
                    "                                    <td><input type=\"text\" name=\"ID\" required=\"required\"></td>\n" +
                    "                            </tr>\n" +
                    "                            <tr>\n" +
                    "                                    <td>Password</td>\n" +
                    "                                    <td><input type=\"password\" name=\"PWD\" required=\"required\"></td>\n" +
                    "                            </tr>\n" +
                    "                            </table>\n" +
                    "                        <input type=\"submit\" name=\"Login\" value=\"Login\">\n" +
                    "                    </form>\n" +
                    "                    </div>\n" +
                    "            </div>\n" +
                    "    </body>\n" +
                    "</html>\n" ;
        
    }
    
    
    /**
     * 
     * 檢查登入帳號密碼
     * 登入成功後設定 Cookie ID
     * 
     * @param response HttpServletResponse response
     * @param ID ID
     * @param PWD Password
     * @return HTML 主要頁面框架
     */
    public String checkLogin (HttpServletResponse response, String ID, String PWD) {
        try( Connection conn = DriverManager.getConnection(DBurl, DBuser, DBpassword) ){
                Statement stm = conn.createStatement();
                
                ResultSet result = MySQLSyntax.getResultSet(stm, db.user_Table.TABLE_NAME, db.user_Table.ID, ID);
                
                
                if (result.next()) {
                    if ( result.getString(db.user_Table.PWD).equals(PWD) ){
                        Cookie cookie = new Cookie("user", ID);
                        response.addCookie(cookie);
                        return getMainPage ();
                    }
                    else{
                      return getLoginPage("密碼錯誤 ! ");
                    }
                }
                else {
                    return getLoginPage("帳號錯誤 ! ");
                }

            }catch(SQLException ex){
                logger.log(Level.WARNING, " In SQLException (checkLogin) : " + ex.toString());
                ex.printStackTrace();
                return getLoginPage("資料庫連線錯誤 ! ");
            }
    }
    
    
    /**
     * 取得主要頁面框架
     * 
     * @return HTML 主要頁面框架 
     */
    public String getMainPage (){
        return "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "    <head>\n" +
                    "        <title>ECS PM component database management system</title>\n" +
                    "        <meta charset=\"UTF-8\">\n" +
                    "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    </head>\n" +
                    "    <frameset rows = \"70px, *\" frameborder=0 framespacing=0>\n" +
                    "        <frame src=\"WEB-DATA/header.html\">\n" +
                    "            \n" +
                    "        <frameset cols = \"150px, *\" frameborder=0 framespacing=0>\n" +
                    "            <frame src=\"WEB-DATA/nav.html\">\n" +
                    "            <frame name=\"main\" src=\"main?action=search\">\n" +
                    "        </frameset>\n" +
                    "    </frameset>\n" +
                    "</html>";
    }
    
    
    /**
     * 取得搜尋Component頁面
     * 
     * @return HTML 搜尋頁面
     */
    public String getSearchPage(){
        return getSearchPage(null);
    }
    
    
    private String getSelectionOption (Statement stm, String table, String select) throws SQLException {
        String str = "";
        ResultSet result = MySQLSyntax.getResultSet(stm, table);
        while(result.next()){
            str = str + "                                <option value=\""+ result.getString(select) +"\">"+ result.getString(select) +"</option>\n";
        }
        return str;
    }

    
    /**
     * 取得搜尋Component頁面並夾帶彈出式訊息
     * 
     * @param alertData 彈出訊息
     * @return HTML 搜尋頁面
     */
    private String getSearchPage(String alertData) {
        Map<String, String> selection = new TreeMap<String, String>();
        
        if ( alertData != null)
            alertData = "<script language=\"javascript\"> alert('"+ alertData +"'); </script>";
        else
            alertData = "";

        String CategoryStr;
        CategoryStr = "                                <option value=\""+ CPU.TableName +"\">"+ CPU.ComponentName +"</option>\n" +
                                   "                                <option value=\""+ Camera.TableName +"\">"+ Camera.ComponentName +"</option>\n" +
                                   "                                <option value=\""+ Ethernet.TableName +"\">"+ Ethernet.ComponentName +"</option>\n" +
                                   "                                <option value=\""+ Storage.TableName +"\">"+ Storage.ComponentName +"</option>\n" +
                                   "                                <option value=\""+ Button.TableName +"\">"+ Button.ComponentName +"</option>\n" +
                                   "                                <option value=\""+ Antennas.TableName +"\">"+ Antennas.ComponentName +"</option>\n" +
                                   "                                <option value=\""+ CardReader.TableName +"\">"+ CardReader.ComponentName +"</option>\n" +
                                   "                                <option value=\""+ Memory.TableName +"\">"+ Memory.ComponentName +"</option>\n" +
                                   "                                <option value=\""+ Sensor.TableName +"\">"+ Sensor.ComponentName +"</option>\n" +
                                   "                                <option value=\""+ WWAN.TableName+"\">"+ WWAN.ComponentName +"</option>\n" +
                                   "                                <option value=\""+ WLAN.TableName +"\">"+ WLAN.ComponentName +"</option>\n" +
                                   "                                <option value=\""+ KBCEBC.TableName +"\">"+ KBCEBC.ComponentName +"</option>\n" +
                                   "                                <option value=\""+ ClickPad.TableName +"\">"+ ClickPad.ComponentName +"</option>\n" +
                                   "                                <option value=\""+ LcdPanel.TableName +"\">"+ LcdPanel.ComponentName +"</option>\n" +
                                   "                                <option value=\""+ TouchPanel.TableName +"\">"+ TouchPanel.ComponentName +"</option>\n" +
                                   "                                <option value=\""+ Keyboard.TableName +"\">"+ Keyboard.ComponentName +"</option>\n" +
                                   "                                <option value=\""+ Battery.TableName +"\">"+ Battery.ComponentName +"</option>\n" +
                                   "                                <option value=\""+ Charger.TableName +"\">"+ Charger.ComponentName +"</option>\n" +
                                   "                                <option value=\""+ Measurement.TableName +"\">"+ Measurement.ComponentName +"</option>\n" +
                                   "                                <option value=\""+ Graphic.TableName +"\">"+ Graphic.ComponentName +"</option>\n" +
                                   "                                <option value=\""+ AudioCodec.TableName +"\">"+ AudioCodec.ComponentName +"</option>\n" +
                                   "                                <option value=\""+ PanelInterfaceBridge.TableName +"\">"+ PanelInterfaceBridge.ComponentName +"</option>\n" +
                                   "                                <option value=\""+ ExternalStorageCard.TableName +"\">"+ ExternalStorageCard.ComponentName +"</option>\n" +
                                   "                                <option value=\""+ ODD.TableName +"\">"+ ODD.ComponentName +"</option>\n" +
                                   "                                <option value=\""+ Speaker.TableName +"\">"+ Speaker.ComponentName +"</option>\n" +
                                   "                                <option value=\""+ Mic.TableName +"\">"+ Mic.ComponentName +"</option>\n" +
                                   "                                <option value=\""+ IoPort.TableName +"\">"+ IoPort.ComponentName +"</option>\n" +
                                   "                                <option value=\""+ OS.TableName +"\">"+ OS.ComponentName +"</option>\n";
        
        
        selection.put(db.vendor_Table.TABLE_NAME, db.vendor_Table.Vendor);
        selection.put(db.wwan_type_Table.TABLE_NAME, db.wwan_type_Table.Type);
        selection.put(db.wlan_max_speed_Table.TABLE_NAME, db.wlan_max_speed_Table.Max_Speed);
        selection.put(db.panel_size_Table.TABLE_NAME, db.panel_size_Table.Size);
        selection.put(db.touch_panel_ic_vendor_Table.TABLE_NAME, db.touch_panel_ic_vendor_Table.IC_Vendor);
        selection.put(db.touch_panel_ic_Table.TABLE_NAME, db.touch_panel_ic_Table.IC);
        selection.put(db.cpu_code_name_Table.TABLE_NAME, db.cpu_code_name_Table.CodeName);
        selection.put(db.camera_pixel_Table.TABLE_NAME, db.camera_pixel_Table.Pixel_Mega);
        selection.put(db.camera_sensor_ic_Table.TABLE_NAME, db.camera_sensor_ic_Table.Sensor_IC);
        selection.put(db.sensor_type_Table.TABLE_NAME, db.sensor_type_Table.Type);
        selection.put(db.memory_type_Table.TABLE_NAME, db.memory_type_Table.Type);
        selection.put(db.memory_frequency_Table.TABLE_NAME, db.memory_frequency_Table.Frequency);
        selection.put(db.memory_capacity_Table.TABLE_NAME, db.memory_capacity_Table.Capacity);
        selection.put(db.keyboard_type_Table.TABLE_NAME, db.keyboard_type_Table.Type);
        selection.put(db.keyboard_os_Table.TABLE_NAME, db.keyboard_os_Table.OS);
        
        String SelctionStr = "";
        
        try( Connection conn = DriverManager.getConnection(DBurl, DBuser, DBpassword) ){
            Statement stm = conn.createStatement();
            
            for (Iterator<Entry<String, String>> iter = selection.entrySet().iterator(); iter.hasNext();) {
                Entry<String, String> entry = iter.next();
                SelctionStr = SelctionStr + 
                        "                    <tr id=\""+ entry.getKey() +"\" style=\"display: none\">\n" +
                        "                        <td>"+ entry.getValue() +" : </td>\n" +
                        "                        <td>\n" +
                        "                            <select name = \""+ entry.getKey() +"\">\n" +
                        "                                <option value=\"all\">ALL</option>\n" +
                        getSelectionOption(stm, entry.getKey(), entry.getValue()) + 
                        "                            </select>\n" +
                        "                        </td>\n" +
                        "                    </tr>\n";
            } 
            
        }catch(SQLException ex){
            logger.log(Level.WARNING, " In SQLException (getSearchPage) : " + ex.toString());
            ex.printStackTrace();
            return getLoginPage("資料庫連線錯誤 ! ");
        }

        return "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "    <head>\n" +
                    "        <title>TODO supply a title</title>\n" +
                    "        <meta charset=\"UTF-8\">\n" +
                    "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <style>\n" +
                    "        #section {\n" +
                    //"            background-color:#F0F8FF;\n" +
                    "            position : absolute;\n" +
                    "            top : 0;\n" +
                    "            left : 0;\n" +
                    "            right : 0;\n" +
                    //"            bottom : 0;\n" +
                    "            text-align: left;\n" +
                    "            float:left;\n" +
                    "            padding:5px;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    alertData+
                    "    <script src=\"WEB-DATA/search-js.js\" type=\"text/javascript\" defer></script>\n" +
                    "    </head>\n" +
                    "    <body>\n" +
                    "        <div id = \"section\">\n" +
                    "            <form id=\"selectionForm\" method=\"POST\">\n" +
                    "                <table>\n" +
                    "                    <tr>\n" +
                    "                        <td>Category : </td>\n" +
                    "                        <td>\n" +
                    "                            <select onchange=\"changeCate(this)\" id = \"category\" name = \"category\">\n" +
                    "                                <option value=\"\">-----</option>\n" +
                    CategoryStr +
                    "                            </select>\n" +
                    "                        </td>\n" +
                    "                    </tr>\n" +
                    SelctionStr +
                    "                    <tr>\n" +
                    "                        <td><input type=\"submit\" name=\"searchAll\"  value=\"送出\"></td>\n" +
                    "                    </tr>\n" +
                    "                </table>\n" +
                    "            </form>\n" +
                    "        </div>\n" +
                    "    </body>\n" +
                    "</html>";
    }
    
    
    /**
     * 
     * 將傳入map轉換為 可供MySQLSyntax搜尋之SearchMap
     * 
     * @param Map<String, String> map 
     * @return Map<String, String> SearchMap
     */
    private Map<String, String> getSearchMap (Map<String, String> map){
        Map<String, String> searchMap = new TreeMap<String, String>();
        for (Iterator iter = map.entrySet().iterator(); iter.hasNext(); ) {
            Entry<String, String> entry = (Entry<String, String>) iter.next();
            if (!entry.getValue().equals("all"))
                searchMap.put(entry.getKey(), entry.getValue());
        }
        return searchMap;
    }
    
    
    /**
     * 
     * 轉換 ResultSet 為 HTML<table>
     * 
     * @param ResultSet 搜尋結果
     * @return String HTML <table>
     * @throws SQLException 
     */
    private String getTableByResult(ResultSet result, Map<String, List<String>> map, String main) throws SQLException{
        String str = "                <tr>\n" +
                              "                    <td><input type = \"submit\" name = \"getModify\" value = \"修改, 刪除\"></td>\n";
        for (int i = 1; i <= result.getMetaData().getColumnCount(); i++) {
            str = str + "                    <td>"+ result.getMetaData().getColumnName(i) +"</td>\n";
            if (i == result.getMetaData().getColumnCount())
                str = str + "                    <td>ProjectName</td>\n";
        }
        str = str + "                </tr>\n";
        
        int j = 0;
        while (result.next()) {
            str = str + "                <tr>\n";
            for (int i = 1; i <= result.getMetaData().getColumnCount(); i++) {
                str = str + "                    <input type = \"hidden\" name = \"" + result.getMetaData().getColumnName(i) + "\" value = \"" + result.getString(i) +"\">\n";
                if (i == 1)
                    str = str + "                    <td><input type=\"checkbox\" value=\""+ j++ +"\" name=\"modifyNum\"></td>\n";
                    
                str = str + "                    <td>"+ result.getString(i) +"</td>\n";
                
                if (i == result.getMetaData().getColumnCount()) {
                    str = str + "                    <td>";
                    if (map.containsKey( result.getString(main) )) {
                        for (String s : map.get(  result.getString(main))) {
                            str = str + s + ", ";
                        }
                    }
                    str = str + "</td>\n";
                }
                    
            }
            str = str + "                </tr>\n";
        }
        return str;
    }
    
    
    private Map<String, List<String>> projectUseComponent (String table, String main) {
         Map<String, List<String>> map = new TreeMap<String, List<String>>();
         List<String> list;
         String key;
         
        try( Connection conn = DriverManager.getConnection(DB2url, DB2user, DB2password) ){
            Statement stm = conn.createStatement();
            
            ResultSet result = MySQLSyntax.getResultSet(stm, table);
            
            while (result.next()) {
                key = result.getString(main);
                
                if (map.containsKey(key)) 
                    map.get(key).add(result.getString("ProjectName"));
                
                else{
                    list = new ArrayList<String>();
                    list.add(result.getString("ProjectName"));
                    map.put(key, list);
                }
            }
            
            
        } catch(SQLException ex){
            logger.log(Level.WARNING, "In SQLException (projectUseComponent) : " + ex.toString());
            ex.printStackTrace();
        }
        
        return map;
    }
    
    
    /**
     *  取得搜尋結果頁面
     * 
     * @param ID UserID
     * @param request HttpServletRequest request
     * @return HTML 搜尋結果頁面
     */
    public String getSearchResultPages(String ID, HttpServletRequest request) {
        String str = "";
        String dstr = "";
        
        Map<String, String> map = new TreeMap<String, String>();
        Map<String, List<String>> promap = null;
        String main = null;
        String table = request.getParameter("category");
        
        switch (table) {
            
            case db.cpu_Table.TABLE_NAME :{
                map.put(db.cpu_Table.Vendor, request.getParameter(db.vendor_Table.TABLE_NAME));
                map.put(db.cpu_Table.CodeName, request.getParameter(db.cpu_code_name_Table.TABLE_NAME));
                promap = projectUseComponent(CPU.ProjectTableName, CPU.MainKey);
                main = CPU.MainKey;
                break;
            }
            
            case db.memory_Table.TABLE_NAME :{
                map.put(db.memory_Table.Vendor, request.getParameter(db.vendor_Table.TABLE_NAME));
                map.put(db.memory_Table.Type, request.getParameter(db.memory_type_Table.TABLE_NAME));
                map.put(db.memory_Table.Frequency, request.getParameter(db.memory_frequency_Table.TABLE_NAME));
                map.put(db.memory_Table.Capacity, request.getParameter(db.memory_capacity_Table.TABLE_NAME));
                promap = projectUseComponent(Memory.ProjectTableName, Memory.MainKey);
                main = Memory.MainKey;
                break;
            }
            
            case db.sensor_Table.TABLE_NAME :{
                map.put(db.sensor_Table.Vendor, request.getParameter(db.vendor_Table.TABLE_NAME));
                map.put(db.sensor_Table.Type, request.getParameter(db.sensor_type_Table.TABLE_NAME));
                promap = projectUseComponent(Sensor.ProjectTableName, Sensor.MainKey);
                main = Sensor.MainKey;
                break;
            }
            
            case db.ethernet_Table.TABLE_NAME :{
                map.put(db.ethernet_Table.Vendor, request.getParameter(db.vendor_Table.TABLE_NAME));
                promap = projectUseComponent(Ethernet.ProjectTableName, Ethernet.MainKey);
                main = Ethernet.MainKey;
                break;
            }
            
            case db.storage_Table.TABLE_NAME :{
                map.put(db.storage_Table.Vendor, request.getParameter(db.vendor_Table.TABLE_NAME));
                promap = projectUseComponent(Storage.ProjectTableName, Storage.MainKey);
                main = Storage.MainKey;
                break;
            }
            
            case db.wwan_Table.TABLE_NAME :{
                map.put(db.wwan_Table.Vendor, request.getParameter(db.vendor_Table.TABLE_NAME));
                map.put(db.wwan_Table.Type, request.getParameter(db.wwan_type_Table.TABLE_NAME));
                promap = projectUseComponent(WWAN.ProjectTableName, WWAN.MainKey);
                main = WWAN.MainKey;
                break;
            }
            
            case db.wlan_Table.TABLE_NAME :{
                map.put(db.wlan_Table.Vendor, request.getParameter(db.vendor_Table.TABLE_NAME));
                map.put(db.wlan_Table.Max_Speed, request.getParameter(db.wlan_max_speed_Table.TABLE_NAME));
                promap = projectUseComponent(WLAN.ProjectTableName, WLAN.MainKey);
                main = WLAN.MainKey;
                break;
            }
            
            case db.kbc_ebc_Table.TABLE_NAME :{
                map.put(db.kbc_ebc_Table.Vendor, request.getParameter(db.vendor_Table.TABLE_NAME));
                promap = projectUseComponent(KBCEBC.ProjectTableName, KBCEBC.MainKey);
                main = KBCEBC.MainKey;
                break;
            }
            
            case db.click_pad_Table.TABLE_NAME :{
                map.put(db.click_pad_Table.Vendor, request.getParameter(db.vendor_Table.TABLE_NAME));
                promap = projectUseComponent(ClickPad.ProjectTableName, ClickPad.MainKey);
                main = ClickPad.MainKey;
                break;
            }
            
            case db.lcd_panel_Table.TABLE_NAME :{
                map.put(db.lcd_panel_Table.Vendor, request.getParameter(db.vendor_Table.TABLE_NAME));
                map.put(db.lcd_panel_Table.Size, request.getParameter(db.panel_size_Table.TABLE_NAME));
                promap = projectUseComponent(LcdPanel.ProjectTableName, LcdPanel.MainKey);
                main = LcdPanel.MainKey;
                break;
            }
            
            case db.touch_panel_Table.TABLE_NAME :{
                map.put(db.touch_panel_Table.Vendor, request.getParameter(db.vendor_Table.TABLE_NAME));
                map.put(db.touch_panel_Table.Size, request.getParameter(db.panel_size_Table.TABLE_NAME));
                map.put(db.touch_panel_Table.IC_Vendor, request.getParameter(db.touch_panel_ic_vendor_Table.TABLE_NAME));
                map.put(db.touch_panel_Table.IC, request.getParameter(db.touch_panel_ic_Table.TABLE_NAME));
                promap = projectUseComponent(TouchPanel.ProjectTableName, TouchPanel.MainKey);
                main = TouchPanel.MainKey;
                break;
            }
            
            case db.camera_Table.TABLE_NAME :{
                map.put(db.camera_Table.Vendor, request.getParameter(db.vendor_Table.TABLE_NAME));
                map.put(db.camera_Table.Pixel_Mega, request.getParameter(db.camera_pixel_Table.TABLE_NAME));
                map.put(db.camera_Table.Sensor_IC, request.getParameter(db.camera_sensor_ic_Table.TABLE_NAME));
                promap = projectUseComponent(Camera.ProjectTableName, Camera.MainKey);
                main = Camera.MainKey;
                break;
            }
            
            case db.button_Table.TABLE_NAME :{
                promap = projectUseComponent(Button.ProjectTableName, Button.MainKey);
                main = Button.MainKey;
                break;
            }
            
            case db.card_reader_Table.TABLE_NAME :{
                map.put(db.card_reader_Table.Vendor, request.getParameter(db.vendor_Table.TABLE_NAME));
                promap = projectUseComponent(CardReader.ProjectTableName, CardReader.MainKey);
                main = CardReader.MainKey;
                break;
            }
            
            case db.antennas_Table.TABLE_NAME :{
                promap = projectUseComponent(Antennas.ProjectTableName, Antennas.MainKey);
                main = Antennas.MainKey;
                break;
            }
            
            case db.keyboard_Table.TABLE_NAME :{
                map.put(db.keyboard_Table.Vendor, request.getParameter(db.keyboard_Table.TABLE_NAME));
                map.put(db.keyboard_Table.Type, request.getParameter(db.keyboard_type_Table.TABLE_NAME));
                map.put(db.keyboard_Table.OS, request.getParameter(db.keyboard_os_Table.TABLE_NAME));
                promap = projectUseComponent(Keyboard.ProjectTableName, Keyboard.MainKey);
                main = Keyboard.MainKey;
                break;
            }
            
            case db.battery_Table.TABLE_NAME :{
                map.put(db.battery_Table.Vendor, request.getParameter(db.vendor_Table.TABLE_NAME));
                promap = projectUseComponent(Battery.ProjectTableName, Battery.MainKey);
                main = Battery.MainKey;
                break;
            }
            
            case db.charger_Table.TABLE_NAME :{
                map.put(db.charger_Table.Vendor, request.getParameter(db.vendor_Table.TABLE_NAME));
                promap = projectUseComponent(Charger.ProjectTableName, Charger.MainKey);
                main = Charger.MainKey;
                break;
            }
            
            case db.measurement_Table.TABLE_NAME :{
                promap = projectUseComponent(Measurement.ProjectTableName, Measurement.MainKey);
                main = Measurement.MainKey;
                break;
            }
            
            case db.graphic_Table.TABLE_NAME :{
                map.put(db.graphic_Table.Vendor, request.getParameter(db.vendor_Table.TABLE_NAME));
                promap = projectUseComponent(Graphic.ProjectTableName, Graphic.MainKey);
                main = Graphic.MainKey;
                break;
            }
            
            case db.audio_codec_Table.TABLE_NAME :{
                map.put(db.audio_codec_Table.Vendor, request.getParameter(db.vendor_Table.TABLE_NAME));
                promap = projectUseComponent(AudioCodec.ProjectTableName, AudioCodec.MainKey);
                main = AudioCodec.MainKey;
                break;
            }
            
            case db.panel_interface_bridge_Table.TABLE_NAME :{
                map.put(db.panel_interface_bridge_Table.Vendor, request.getParameter(db.vendor_Table.TABLE_NAME));
                promap = projectUseComponent(PanelInterfaceBridge.ProjectTableName, PanelInterfaceBridge.MainKey);
                main = PanelInterfaceBridge.MainKey;
                break;
            }
            
            case db.external_storage_Table.TABLE_NAME :{
                map.put(db.external_storage_Table.Vendor, request.getParameter(db.vendor_Table.TABLE_NAME));
                promap = projectUseComponent(ExternalStorageCard.ProjectTableName, ExternalStorageCard.MainKey);
                main = ExternalStorageCard.MainKey;
                break;
            }
            
            case db.odd_Table.TABLE_NAME :{
                map.put(db.odd_Table.Vendor, request.getParameter(db.vendor_Table.TABLE_NAME));
                promap = projectUseComponent(ODD.ProjectTableName, ODD.MainKey);
                main = ODD.MainKey;
                break;
            }
            
            case db.speaker_Table.TABLE_NAME :{
                map.put(db.speaker_Table.Vendor, request.getParameter(db.vendor_Table.TABLE_NAME));
                promap = projectUseComponent(Speaker.ProjectTableName, Speaker.MainKey);
                main = Speaker.MainKey;
                break;
            }
            
            case db.mic_Table.TABLE_NAME :{
                map.put(db.mic_Table.Vendor, request.getParameter(db.vendor_Table.TABLE_NAME));
                promap = projectUseComponent(Mic.ProjectTableName, Mic.MainKey);
                main = Mic.MainKey;
                break;
            }
            
            case db.io_port_Table.TABLE_NAME :{
                promap = projectUseComponent(IoPort.ProjectTableName, IoPort.MainKey);
                main = IoPort.MainKey;
                break;
            }
            
            case db.os_Table.TABLE_NAME :{
                promap = projectUseComponent(OS.ProjectTableName, OS.MainKey);
                main = OS.MainKey;
                break;
            }
            
        }
        
        try( Connection conn = DriverManager.getConnection(DBurl, DBuser, DBpassword) ){
            Statement stm = conn.createStatement();
            
            ResultSet result = MySQLSyntax.getResultSet(stm, table, getSearchMap(map));
            str = str + getTableByResult(result, promap, main);
            
        } catch(SQLException ex){
            logger.log(Level.WARNING, "In SQLException (getSearchResultPages) : " + ex.toString());
            ex.printStackTrace();
            return getSearchPage("資料庫連線錯誤 ! ");
        }
        
        for (Iterator iter = getSearchMap(map).entrySet().iterator(); iter.hasNext(); ) {
            Entry<String, String> entry = (Entry<String, String>) iter.next();
                dstr = dstr + "                <input type = \"hidden\" name = \""+ entry.getKey() +"\" value = \""+ entry.getValue() +"\">\n";
        }
        
        return "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "    <head>\n" +
                    "        <title>TODO supply a title</title>\n" +
                    "        <meta charset=\"UTF-8\">\n" +
                    "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <style>\n" +
                    "        #section {\n" +
                    //"            background-color:#F0F8FF;\n" +
                    "            position : absolute;\n" +
                    "            top : 0;\n" +
                    "            left : 0;\n" +
                    "            right : 0;\n" +
                    //"            bottom : 0;\n" +
                    "            text-align: left;\n" +
                    "            float:left;\n" +
                    "            padding:5px;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "    </head>\n" +
                    "    <body>\n" +
                    "        <div id = \"section\">\n" +
                    "            <form  method=\"POST\" action = \"download\">\n"+
                    "                <input type = \"submit\" name = \"getExcel\" value = \"下載Excel\">\n" +
                    "                <input type = \"hidden\" name = \"category\" value = \""+ request.getParameter("category") +"\">\n" +
                    dstr+
                    "            </form>\n"+
                    "            <table border=\"1\">\n" +
                    "                <form  method=\"POST\">\n" +
                    "                    <input type = \"hidden\" name = \"category\" value = \""+ request.getParameter("category") +"\">\n" +
                    str +
                    "                </form>\n"+
                    "            </table>\n" +
                    "        </div>\n" +
                    "    </body>\n" +
                    "</html>";
    }
    
    
    
    private String getModifyTableForm (List<String> nameList, String mainKey, HttpServletRequest request, String category) {
                
        String str = "                <tr><td><input type = \"hidden\" name = \"category\" value = \""+ category +"\"></td></tr>\n";
        
        String[] snum = request.getParameterValues("modifyNum");
        
        if (snum != null) {
            
            str = str + "                <tr><td>Delete</td><td>"+ mainKey +"</td>";
            for (String name : nameList){
                if (!name.equals(mainKey))
                    str = str + "<td>"+ name +"</td>";
            }
            str = str + "</tr>\n";

            int j=0;
            for (String s : snum) {
                int i = Integer.parseInt(s);
                str = str + "                <tr>\n" +
                                    "                    <td><input type = \"checkbox\" onclick = \"hideDelete( this, \'class_" + j + "\')\" name = \"delete\" value = \"" + request.getParameterValues(mainKey)[i] + "\" ></td>\n" + 
                                    "                    <input type = \"hidden\" name = \""+ mainKey +"\" value = \"" + request.getParameterValues(mainKey)[i] + "\" >\n" + 
                                    "                    <td>"+ request.getParameterValues(mainKey)[i] +"</td>\n";

                for (String name : nameList){
                    String[] vals = request.getParameterValues(name);
                    if (!name.equals(mainKey))
                        str = str + "                    <td><textarea class = \"class_"+ j +"\" name = \""+ name +"\">"+ vals[i] +"</textarea></td>\n";
                }
                str = str + "                </tr>\n";
                j++;
            }
        }
        
        int newNum = 10;
        
        str = str + "                <tr>\n" +
                           "                   <td>新增</td>\n" +
                           "                   <td>\n" +
                           "                       <select onchange=\"changeNew('selectNew')\" id = \"selectNew\">\n";
        for (int i = 0; i < newNum; i++) {
            str = str + "                           <option value=\""+ i +"\">"+ i +"</option>\n";
        }
        str = str + "                   </td>\n" +
                           "                </tr>\n" +
                           "                <tr>\n";
        for (String name : nameList){
            str = str + "                   <td>"+ name +"</td>\n";
        }
        str = str + "                </tr>\n";
        
        for (int i = 0; i <newNum - 1; i++) {
            str = str + "                <tr>\n";
            for (String name : nameList) {
                str = str + "                    <td><input type = \"text\" class = \"new_"+ i +"\" name = \""+ name +"\" value = \"\"></td>\n";
            }
            str = str + "                </tr>\n";
        }
        
        return str;
    }
    
    
    /**
     * 取得修改Component頁面
     * 
     * @param ID userID
     * @param request HttpServletRequest
     * @return HTML修改頁面
     */
    public String getModifyPages(String ID, HttpServletRequest request) {
        String str = "";
        
        switch( request.getParameter("category") ){
            
            case db.cpu_Table.TABLE_NAME : {
                str = getModifyTableForm(CPU.AllKeyList, CPU.MainKey, request, CPU.TableName);
                break;
            }
            
             case db.memory_Table.TABLE_NAME :{
                str = getModifyTableForm(Memory.AllKeyList, Memory.MainKey, request, Memory.TableName);
                break;
            }
            
            case db.sensor_Table.TABLE_NAME :{
                str = getModifyTableForm(Sensor.AllKeyList, Sensor.MainKey, request, Sensor.TableName);
                break;
            }
            
            case db.ethernet_Table.TABLE_NAME :{
                str = getModifyTableForm(Ethernet.AllKeyList, Ethernet.MainKey, request, Ethernet.TableName);
                break;
            }
            
            case db.storage_Table.TABLE_NAME :{
                str = getModifyTableForm(Storage.AllKeyList, Storage.MainKey, request, Storage.TableName);
                break;
            }
            
            case db.wwan_Table.TABLE_NAME :{
                str = getModifyTableForm(WWAN.AllKeyList, WWAN.MainKey, request, WWAN.TableName);
                break;
            }
            
            case db.wlan_Table.TABLE_NAME :{
                str = getModifyTableForm(WLAN.AllKeyList, WLAN.MainKey, request, WLAN.TableName);
                break;
            }
            
            case db.kbc_ebc_Table.TABLE_NAME :{
                str = getModifyTableForm(KBCEBC.AllKeyList, KBCEBC.MainKey, request, KBCEBC.TableName);
                break;
            }
            
            case db.click_pad_Table.TABLE_NAME :{
                str = getModifyTableForm(ClickPad.AllKeyList, ClickPad.MainKey, request, ClickPad.TableName);
                break;
            }
            
            case db.lcd_panel_Table.TABLE_NAME :{
                str = getModifyTableForm(LcdPanel.AllKeyList, LcdPanel.MainKey, request, LcdPanel.TableName);
                break;
            }
            
            case db.touch_panel_Table.TABLE_NAME :{
                str = getModifyTableForm(TouchPanel.AllKeyList, TouchPanel.MainKey, request, TouchPanel.TableName);
                break;
            }
            
            case db.camera_Table.TABLE_NAME :{
                str = getModifyTableForm(Camera.AllKeyList, Camera.MainKey, request, Camera.TableName);
                break;
            }
            
            
            case db.button_Table.TABLE_NAME :{
                str = getModifyTableForm(Button.AllKeyList, Button.MainKey, request, Button.TableName);
                break;
            }
            
            case db.card_reader_Table.TABLE_NAME :{
                str = getModifyTableForm(CardReader.AllKeyList, CardReader.MainKey, request, CardReader.TableName);
                break;
            }
            
            case db.antennas_Table.TABLE_NAME :{
                str = getModifyTableForm(Antennas.AllKeyList, Antennas.MainKey, request, Antennas.TableName);
                break;
            }   
            
            case db.keyboard_Table.TABLE_NAME :{
                str = getModifyTableForm(Keyboard.AllKeyList, Keyboard.MainKey, request, Keyboard.TableName);
                break;
            }   
            
            case db.battery_Table.TABLE_NAME :{
                str = getModifyTableForm(Battery.AllKeyList, Battery.MainKey, request, Battery.TableName);
                break;
            }   
            
            case db.charger_Table.TABLE_NAME :{
                str = getModifyTableForm(Charger.AllKeyList, Charger.MainKey, request, Charger.TableName);
                break;
            }   
            
            
            case db.measurement_Table.TABLE_NAME :{
                str = getModifyTableForm(Measurement.AllKeyList, Measurement.MainKey, request, Measurement.TableName);
                break;
            }   
            
            
            case db.graphic_Table.TABLE_NAME :{
                str = getModifyTableForm(Graphic.AllKeyList, Graphic.MainKey, request, Graphic.TableName);
                break;
            }   
            
            
            case db.audio_codec_Table.TABLE_NAME :{
                str = getModifyTableForm(AudioCodec.AllKeyList, AudioCodec.MainKey, request, AudioCodec.TableName);
                break;
            }   
            
            
            case db.panel_interface_bridge_Table.TABLE_NAME :{
                str = getModifyTableForm(PanelInterfaceBridge.AllKeyList, PanelInterfaceBridge.MainKey, request, PanelInterfaceBridge.TableName);
                break;
            }   
            
            
            case db.external_storage_Table.TABLE_NAME :{
                str = getModifyTableForm(ExternalStorageCard.AllKeyList, ExternalStorageCard.MainKey, request, ExternalStorageCard.TableName);
                break;
            }   
            
            
            case db.odd_Table.TABLE_NAME :{
                str = getModifyTableForm(ODD.AllKeyList, ODD.MainKey, request, ODD.TableName);
                break;
            }   
            
            
            case db.speaker_Table.TABLE_NAME :{
                str = getModifyTableForm(Speaker.AllKeyList, Speaker.MainKey, request, Speaker.TableName);
                break;
            }   
            
            
            case db.mic_Table.TABLE_NAME :{
                str = getModifyTableForm(Mic.AllKeyList, Mic.MainKey, request, Mic.TableName);
                break;
            }   
            
            
            case db.io_port_Table.TABLE_NAME :{
                str = getModifyTableForm(IoPort.AllKeyList, IoPort.MainKey, request, IoPort.TableName);
                break;
            }   
            
            
            case db.os_Table.TABLE_NAME :{
                str = getModifyTableForm(OS.AllKeyList, OS.MainKey, request, OS.TableName);
                break;
            }   
                
        }
        
        return "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "    <head>\n" +
                    "        <title>TODO supply a title</title>\n" +
                    "        <meta charset=\"UTF-8\">\n" +
                    "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <script src=\"WEB-DATA/modifySearch-js.js\" type=\"text/javascript\" defer></script>\n" +
                    "    <style>\n" +
                    "        #section {\n" +
                    //"            background-color:#F0F8FF;\n" +
                    "            position : absolute;\n" +
                    "            top : 0;\n" +
                    "            left : 0;\n" +
                    "            right : 0;\n" +
                    //"            bottom : 0;\n" +
                    "            text-align: left;\n" +
                    "            float:left;\n" +
                    "            padding:5px;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "    </head>\n" +
                    "    <body>\n" +
                    "        <div id = \"section\">\n" +
                    "            <table>\n" +
                    "                <form  method=\"POST\">\n" +
                    str +
                    "                    <tr><td><input type = \"submit\" name = \"checkModify\" value = \"submit\"></tr></td>\n" + 
                    "                </form>\n"+
                    "            </table>\n" +
                    "        </div>\n" +
                    "    </body>\n" +
                    "</html>";
    }
    

    /**
     * 
     * 產生確認修改Component 的 table
     * 
     * @param coms
     * @param table
     * @param mainKey
     * @param attr
     * @param request
     * @return 
     */
    public String checkModifyTable(ComponentSet coms, String table, String mainKey, List<String> attr, HttpServletRequest request){
        
        String dstr = "                <table>\n" +
                                "                    <tr><td>是否刪除下列"+ table +" ? </tr></td>\n";
        List<String> list;
                
        int i = 0;
        for ( String str : request.getParameterValues(mainKey) ) {
            if ( request.getParameterValues("delete") != null && Arrays.asList( request.getParameterValues("delete") ).contains(str) ) {
                dstr = dstr + "                    <tr><td>"+ str +"</td></tr>\n";
                dstr = dstr + "                    <input type = \"hidden\" name = \"delete\" value = \""+ str +"\">\n";
            }
            else if (!str.equals("")) {
                list = new ArrayList<String>();
                for (String a : attr) {
                     list.add(request.getParameterValues(a)[i]);
                }
                coms.addComponentbyList(list);
            }
            i++;
        }
        dstr = dstr + "                </table>\n";
        
        if ( request.getParameterValues("delete") != null )
            return  dstr + coms.getComponentSetTable();
        else
            return coms.getComponentSetTable();
    }
    
    
    /**
     * 取得確認修改Component網頁
     * 
     * @param ID
     * @param request
     * @return HTML 確認修改網頁
     */
    public String checkModifyPage(String ID, HttpServletRequest request) {
        
        String str = "";
        
        switch (request.getParameter("category")) {
            case db.cpu_Table.TABLE_NAME :
                str = checkModifyTable(new CpuSet(), CPU.TableName, CPU.MainKey, CPU.AllKeyList, request);
                break;
           
            case db.memory_Table.TABLE_NAME :{
                str = checkModifyTable(new MemorySet(), Memory.TableName, Memory.MainKey, Memory.AllKeyList, request);
                break;
            }
            
            case db.sensor_Table.TABLE_NAME :{
                str = checkModifyTable(new SensorSet(), Sensor.TableName, Sensor.MainKey, Sensor.AllKeyList, request);
                break;
            }
            
            case db.ethernet_Table.TABLE_NAME :{
                str = checkModifyTable(new EthernetSet(), Ethernet.TableName, Ethernet.MainKey, Ethernet.AllKeyList, request);
                break;
            }
            
            case db.storage_Table.TABLE_NAME :{
                str = checkModifyTable(new StorageSet(), Storage.TableName, Storage.MainKey, Storage.AllKeyList, request);
                break;
            }
            
            case db.wwan_Table.TABLE_NAME :{
                str = checkModifyTable(new WwanSet(), WWAN.TableName, WWAN.MainKey, WWAN.AllKeyList, request);
                break;
            }
            
            case db.wlan_Table.TABLE_NAME :{
                str = checkModifyTable(new WlanSet(), WLAN.TableName, WLAN.MainKey, WLAN.AllKeyList, request);
                break;
            }
            
            case db.kbc_ebc_Table.TABLE_NAME :{
                str = checkModifyTable(new KbcEbcSet(), KBCEBC.TableName, KBCEBC.MainKey, KBCEBC.AllKeyList, request);
                break;
            }
            
            case db.click_pad_Table.TABLE_NAME :{
                str = checkModifyTable(new ClickPadSet(), ClickPad.TableName, ClickPad.MainKey, ClickPad.AllKeyList, request);
                break;
            }
            
            case db.lcd_panel_Table.TABLE_NAME :{
                str = checkModifyTable(new LcdPanelSet(), LcdPanel.TableName, LcdPanel.MainKey, LcdPanel.AllKeyList, request);
                break;
            }
            
            case db.touch_panel_Table.TABLE_NAME :{
                str = checkModifyTable(new TouchPanelSet(), TouchPanel.TableName, TouchPanel.MainKey, TouchPanel.AllKeyList, request);
                break;
            }
            
            case db.camera_Table.TABLE_NAME :{
                str = checkModifyTable(new CameraSet(), Camera.TableName, Camera.MainKey, Camera.AllKeyList, request);
                break;
            }
            
            case db.button_Table.TABLE_NAME :{
                str = checkModifyTable(new ButtonSet(), Button.TableName, Button.MainKey, Button.AllKeyList, request);
                break;
            }
            
            case db.card_reader_Table.TABLE_NAME :{
                str = checkModifyTable(new CardReaderSet(), CardReader.TableName, CardReader.MainKey, CardReader.AllKeyList, request);
                break;
            }
            
            case db.antennas_Table.TABLE_NAME :{
                str = checkModifyTable(new AntennasSet(), Antennas.TableName, Antennas.MainKey, Antennas.AllKeyList, request);
                break;
            }   
            
            case db.keyboard_Table.TABLE_NAME :{
                str = checkModifyTable(new KeyboardSet(), Keyboard.TableName, Keyboard.MainKey, Keyboard.AllKeyList, request);
                break;
            }   
            
            case db.battery_Table.TABLE_NAME :{
                str = checkModifyTable(new BatterySet(), Battery.TableName, Battery.MainKey, Battery.AllKeyList, request);
                break;
            }   
            
            case db.charger_Table.TABLE_NAME :{
                str = checkModifyTable(new ChargerSet(), Charger.TableName, Charger.MainKey, Charger.AllKeyList, request);
                break;
            }   
            
            case db.measurement_Table.TABLE_NAME :{
                str = checkModifyTable(new MeasurementSet(), Measurement.TableName, Measurement.MainKey, Measurement.AllKeyList, request);
                break;
            }   
            
            case db.graphic_Table.TABLE_NAME :{
                str = checkModifyTable(new GraphicSet(), Graphic.TableName, Graphic.MainKey, Graphic.AllKeyList, request);
                break;
            }   
            
            case db.audio_codec_Table.TABLE_NAME :{
                str = checkModifyTable(new AudioCodecSet(), AudioCodec.TableName, AudioCodec.MainKey, AudioCodec.AllKeyList, request);
                break;
            }   
            
            case db.panel_interface_bridge_Table.TABLE_NAME :{
                str = checkModifyTable(new PanelInterfaceBridgeSet(), PanelInterfaceBridge.TableName, PanelInterfaceBridge.MainKey, PanelInterfaceBridge.AllKeyList, request);
                break;
            }   
            
            case db.external_storage_Table.TABLE_NAME :{
                str = checkModifyTable(new ExternalStorageCardSet(), ExternalStorageCard.TableName, ExternalStorageCard.MainKey, ExternalStorageCard.AllKeyList, request);
                break;
            }   
            
            case db.odd_Table.TABLE_NAME :{
                str = checkModifyTable(new ODDSet(), ODD.TableName, ODD.MainKey, ODD.AllKeyList, request);
                break;
            }   
            
            case db.speaker_Table.TABLE_NAME :{
                str = checkModifyTable(new SpeakerSet(), Speaker.TableName, Speaker.MainKey, Speaker.AllKeyList, request);
                break;
            }   
            
            case db.mic_Table.TABLE_NAME :{
                str = checkModifyTable(new MicSet(), Mic.TableName, Mic.MainKey, Mic.AllKeyList, request);
                break;
            }   
            
            case db.io_port_Table.TABLE_NAME :{
                str = checkModifyTable(new IoPortSet(), IoPort.TableName, IoPort.MainKey, IoPort.AllKeyList, request);
                break;
            }   
            
            case db.os_Table.TABLE_NAME :{
                str = checkModifyTable(new OSSet(), OS.TableName, OS.MainKey, OS.AllKeyList, request);
                break;
            }   
            
        }
        
        
        return "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "    <head>\n" +
                    "        <title>TODO supply a title</title>\n" +
                    "        <meta charset=\"UTF-8\">\n" +
                    "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <style>\n" +
                    "        #section {\n" +
                    //"            background-color:#F0F8FF;\n" +
                    "            position : absolute;\n" +
                    "            top : 0;\n" +
                    "            left : 0;\n" +
                    "            right : 0;\n" +
                    //"            bottom : 0;\n" +
                    "            text-align: left;\n" +
                    "            float:left;\n" +
                    "            padding:5px;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "    </head>\n" +
                    "    <body>\n" +
                    "        <div id = \"section\">\n" +
                    "            <form  method=\"POST\">\n" +
                    "                <input type = \"hidden\" name = \"category\" value = \""+ request.getParameter("category") +"\">\n" +
                    str +
                    "                <input type = \"submit\" name = \"toModify\" value = \"submit\">\n" + 
                    "            </form>\n"+
                    "        </div>\n" +
                    "    </body>\n" +
                    "</html>";
    }
    
    
    /**
     * 執行刪除Component動作
     * 
     * @param ID
     * @param values
     * @param stm
     * @param table
     * @param mainKey
     * @throws SQLException 
     */
    public void toDelete (String ID, String[] values, Statement stm, String table, String mainKey) throws SQLException {
        if (values == null) 
            return;
        
        for (String value : values) {
            MySQLSyntax.deleteDB(stm, table, mainKey, value);
            String log = ID + " delete " + table + " : " + value;
            MySQLSyntax.inserLog(stm, log);
        }
        
    }
    
    
    
    /**
     * 執行修改Component動作
     * 
     * @param ID
     * @param request
     * @return 修改結果
     */
    public String modifyPages(String ID, HttpServletRequest request) {
        
         try( Connection conn = DriverManager.getConnection(DBurl, DBuser, DBpassword) ){
            Statement stm = conn.createStatement();
            switch (request.getParameter("category")) {

                case db.cpu_Table.TABLE_NAME : {
                    toDelete(ID, request.getParameterValues("delete"), stm, CPU.TableName, CPU.MainKey);    
                    break;
                }
                
                case db.memory_Table.TABLE_NAME :{
                    toDelete(ID, request.getParameterValues("delete"), stm, Memory.TableName, Memory.MainKey);    
                    break;
                }

                case db.sensor_Table.TABLE_NAME :{
                    toDelete(ID, request.getParameterValues("delete"), stm, Sensor.TableName, Sensor.MainKey);
                    break;
                }

                case db.ethernet_Table.TABLE_NAME :{
                    toDelete(ID, request.getParameterValues("delete"), stm, Ethernet.TableName, Ethernet.MainKey);
                    break;
                }

                case db.storage_Table.TABLE_NAME :{
                    toDelete(ID, request.getParameterValues("delete"), stm, Storage.TableName, Storage.MainKey);
                    break;
                }

                case db.wwan_Table.TABLE_NAME :{
                    toDelete(ID, request.getParameterValues("delete"), stm, WWAN.TableName, WWAN.MainKey);
                    break;
                }

                case db.wlan_Table.TABLE_NAME :{
                    toDelete(ID, request.getParameterValues("delete"), stm, WLAN.TableName, WLAN.MainKey);
                    break;
                }

                case db.kbc_ebc_Table.TABLE_NAME :{
                    toDelete(ID, request.getParameterValues("delete"), stm, KBCEBC.TableName, KBCEBC.MainKey);
                    break;
                }

                case db.click_pad_Table.TABLE_NAME :{
                    toDelete(ID, request.getParameterValues("delete"), stm, ClickPad.TableName, ClickPad.MainKey);
                    break;
                }

                case db.lcd_panel_Table.TABLE_NAME :{
                    toDelete(ID, request.getParameterValues("delete"), stm, LcdPanel.TableName, LcdPanel.MainKey);
                    break;
                }

                case db.touch_panel_Table.TABLE_NAME :{
                    toDelete(ID, request.getParameterValues("delete"), stm, TouchPanel.TableName, TouchPanel.MainKey);
                    break;
                }

                case db.camera_Table.TABLE_NAME :{
                    toDelete(ID, request.getParameterValues("delete"), stm, Camera.TableName, Camera.MainKey);
                    break;
                }

                case db.button_Table.TABLE_NAME :{
                    toDelete(ID, request.getParameterValues("delete"), stm, Button.TableName, Button.MainKey);
                    break;
                }

                case db.card_reader_Table.TABLE_NAME :{
                    toDelete(ID, request.getParameterValues("delete"), stm, CardReader.TableName, CardReader.MainKey);
                    break;
                }
                
                case db.antennas_Table.TABLE_NAME :{
                    toDelete(ID, request.getParameterValues("delete"), stm, Antennas.TableName, Antennas.MainKey);
                    break;
                }   
                
                case db.keyboard_Table.TABLE_NAME :{
                    toDelete(ID, request.getParameterValues("delete"), stm, Keyboard.TableName, Keyboard.MainKey);
                    break;
                }   
                
                case db.battery_Table.TABLE_NAME :{
                    toDelete(ID, request.getParameterValues("delete"), stm, Battery.TableName, Battery.MainKey);
                    break;
                }   
                
                case db.charger_Table.TABLE_NAME :{
                    toDelete(ID, request.getParameterValues("delete"), stm, Charger.TableName, Charger.MainKey);
                    break;
                }   
                
                case db.measurement_Table.TABLE_NAME :{
                    toDelete(ID, request.getParameterValues("delete"), stm, Measurement.TableName, Measurement.MainKey);
                    break;
                }   
                
                case db.graphic_Table.TABLE_NAME :{
                    toDelete(ID, request.getParameterValues("delete"), stm, Graphic.TableName, Graphic.MainKey);
                    break;
                }   
                
                case db.audio_codec_Table.TABLE_NAME :{
                    toDelete(ID, request.getParameterValues("delete"), stm, AudioCodec.TableName, AudioCodec.MainKey);
                    break;
                }   
                
                case db.panel_interface_bridge_Table.TABLE_NAME :{
                    toDelete(ID, request.getParameterValues("delete"), stm, PanelInterfaceBridge.TableName, PanelInterfaceBridge.MainKey);
                    break;
                }   
                
                case db.external_storage_Table.TABLE_NAME :{
                    toDelete(ID, request.getParameterValues("delete"), stm, ExternalStorageCard.TableName, ExternalStorageCard.MainKey);
                    break;
                }   
                
                case db.odd_Table.TABLE_NAME :{
                    toDelete(ID, request.getParameterValues("delete"), stm, ODD.TableName, ODD.MainKey);
                    break;
                }   
                
                case db.speaker_Table.TABLE_NAME :{
                    toDelete(ID, request.getParameterValues("delete"), stm, Speaker.TableName, Speaker.MainKey);
                    break;
                }   
                
                case db.mic_Table.TABLE_NAME :{
                    toDelete(ID, request.getParameterValues("delete"), stm, Mic.TableName, Mic.MainKey);
                    break;
                }   
                
                case db.io_port_Table.TABLE_NAME :{
                    toDelete(ID, request.getParameterValues("delete"), stm, IoPort.TableName, IoPort.MainKey);
                    break;
                }   
                
                case db.os_Table.TABLE_NAME :{
                    toDelete(ID, request.getParameterValues("delete"), stm, OS.TableName, OS.MainKey);
                    break;
                }   
                
            }
        } catch(SQLException ex){
            logger.log(Level.WARNING, "In SQLException (modifyPages) : " + ex.toString());
            ex.printStackTrace();
            return getSearchPage("資料庫連線錯誤 ! ");
        }
        
        
        return uploadData(ID, request);
    }
    
    
    /**
     * 取得搜尋Attr頁面
     * 
     * @return 搜尋Attr頁面
     */
    public String getModifyAttrPage() {
        return "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "    <head>\n" +
                    "        <title>TODO supply a title</title>\n" +
                    "        <meta charset=\"UTF-8\">\n" +
                    "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <style>\n" +
                    "        #section {\n" +
                    //"            background-color:#F0F8FF;\n" +
                    "            position : absolute;\n" +
                    "            top : 0;\n" +
                    "            left : 0;\n" +
                    "            right : 0;\n" +
                    //"            bottom : 0;\n" +
                    "            text-align: left;\n" +
                    "            float:left;\n" +
                    "            padding:5px;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "    </head>\n" +
                    "    <body>\n" +
                    "        <div id = \"section\">\n" +
                    "            <form method=\"POST\">\n" +
                    "                <table>\n" +
                    "                    <tr>\n" +
                    "                        <td>\n" +
                    "                            選擇產品屬性 : \n" +
                    "                        </td>\n" +
                    "                        <td>\n" +
                    "                            <select name = \"attr\">\n" +
                    "                                <option value=\"\">-----</option>\n" +
                    "                                <option value=\""+db.vendor_Table.TABLE_NAME+"\">Vendor</option>\n" +
                    "                                <option value=\""+db.wwan_type_Table.TABLE_NAME+"\">WWAN Type</option>\n" +
                    "                                <option value=\""+db.wlan_max_speed_Table.TABLE_NAME+"\">WLAN Max Speed</option>\n" +
                    "                                <option value=\""+db.panel_size_Table.TABLE_NAME+"\">Panel Size</option>\n" +
                    "                                <option value=\""+db.touch_panel_ic_vendor_Table.TABLE_NAME+"\">Touch Panel IC Vendor</option>\n" +
                    "                                <option value=\""+db.touch_panel_ic_Table.TABLE_NAME+"\">Touch Panel IC</option>\n" +
                    "                                <option value=\""+db.cpu_code_name_Table.TABLE_NAME+"\">CPU Code Name</option>\n" +
                    "                                <option value=\""+db.camera_pixel_Table.TABLE_NAME+"\">Camera Pixel</option>\n" +
                    "                                <option value=\""+db.camera_sensor_ic_Table.TABLE_NAME+"\">Camera Sensor IC</option>\n" +
                    "                                <option value=\""+db.sensor_type_Table.TABLE_NAME+"\">Sensor Type</option>\n" +
                    "                                <option value=\""+db.memory_type_Table.TABLE_NAME+"\">Memory Type</option>\n" +
                    "                                <option value=\""+db.memory_frequency_Table.TABLE_NAME+"\">Memory Frequency</option>\n" +
                    "                                <option value=\""+db.memory_capacity_Table.TABLE_NAME+"\">Memory Capacity</option>\n" +
                    "                                <option value=\""+db.keyboard_type_Table.TABLE_NAME+"\">Keyboard Type</option>\n" +
                    "                                <option value=\""+db.keyboard_os_Table.TABLE_NAME+"\">Keyboard OS</option>\n" +
                    "                            </select>\n" +
                    "                        </td>\n" +
                    "                    </tr>\n" +
                    "                    <tr>\n" +
                    "                        <td>\n" +
                    "                            <input type=\"submit\" name=\"getAttribute\" value=\"submit\">\n" +
                    "                        </td>\n" +
                    "                    </tr>\n" +
                    "                </table>\n" +
                    "            </form>\n" +
                    "        </div>\n" +
                    "    </body>\n" +
                    "</html>";
    }
    
    
    /**
     * 取得上傳頁面
     * 
     * @param ID
     * @return 下載頁面
     */
    public String getUploadPages(String ID) {
        return "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "    <head>\n" +
                    "        <title>TODO supply a title</title>\n" +
                    "        <meta charset=\"UTF-8\">\n" +
                    "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <style>\n" +
                    "        #section {\n" +
                    //"            background-color:#F0F8FF;\n" +
                    "            position : absolute;\n" +
                    "            top : 0;\n" +
                    "            left : 0;\n" +
                    "            right : 0;\n" +
                    //"            bottom : 0;\n" +
                    "            text-align: left;\n" +
                    "            float:left;\n" +
                    "            padding:5px;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "    </head>\n" +
                    "    <body>\n" +
                    "        <div id = \"section\">\n" +
                    "            <form action=\"upload\" method=\"POST\" id=\"fileForm\" enctype=\"multipart/form-data\">  \n" +
                    "                <input type=\"file\" name=\"ExcelFile\" accept=\"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet\"  value=\"\"> \n" +
                    "                <input type=\"hidden\" name=\"ID\" value=\""+ ID +"\">\n" +
                    "                <input type=\"submit\" name=\"upload\" value=\"上傳\">\n" +
                    "           </form>\n" +
                    "           <input type=\"button\" value=\"下載Excel範本\" onclick=\"location.href='download?FileName=getTemplate'\">" + 
                    "        </div>\n" +
                    "    </body>\n" +
                    "</html>";
    }
    
    
    /**
     * 取得修改Attr頁面
     * 
     * @param attr
     * @param select
     * @param table
     * @return 修改Attr頁面
     */
    private String getAttrPage (List attr, String select, String table) {
        String str ="";
        if (!attr.isEmpty()) {
            int i = 0;
            for (Object obj : attr) {
                str += 
                    "                    <tr>\n" +
                    "                        <td>\n" +
                    "                            "+ (String)obj +"\n" +
                    "                        </td>\n" +
                    "                        <td>\n" +
                    "                            <input type=\"checkbox\" onclick=\"clickDel(this, \'modify_"+i+"\');\" name=\"delete\" value=\""+ (String)obj +"\">\n" +
                    "                        </td>\n" +
                    "                        <td>\n" +
                    "                            <input type=\"text\" id=\"modify_"+i+"\" name=\"modify_"+ (String)obj +"\" value=\"\">\n" +
                    "                        </td>\n" +
                    "                    </tr>\n";
                i++;
            }
        }
        else {
            str =                     
                    "                    <tr>\n" +
                    "                        <td>\n" +
                    "                           No Record ! " +
                    "                        </td>\n" +
                    "                    </tr>\n";
        }
        
        String strSel =      "                    <tr>\n" +
                                        "                        <td>\n" +
                                        "                            <select onchange=\"changeNew(\'selectNew\')\" id = \"selectNew\">\n" + 
                                        "                                <option value=\"0\">0</option>\n";
        String strNew = "";
        
        for (int i = 0, j = 1; i <10; i++, j++) {
        strSel = strSel + "                                <option value=\""+ j +"\">"+ j +"</option>\n";
                
         strNew = strNew + "                    <tr>\n" +
                                            "                        <td></td>\n" +
                                            "                        <td>\n" +
                                            "                            <input type=\"hidden\" id=\"new_"+ i +"\" name=\"new\" value=\"\">\n" +
                                            "                        </td>\n" +
                                            "                    </tr>\n";
        }
        
        strSel = strSel + "                        </td>\n" +
                                      "                    </tr>\n";
        
        return "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "    <head>\n" +
                    "        <title>TODO supply a title</title>\n" +
                    "        <meta charset=\"UTF-8\">\n" +
                    "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <style>\n" +
                    "        #section {\n" +
                    //"            background-color:#F0F8FF;\n" +
                    "            position : absolute;\n" +
                    "            top : 0;\n" +
                    "            left : 0;\n" +
                    "            right : 0;\n" +
                    //"            bottom : 0;\n" +
                    "            text-align: left;\n" +
                    "            float:left;\n" +
                    "            padding:5px;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "    <script src=\"WEB-DATA/modifyAttr-js.js\" type=\"text/javascript\"></script>\n" +
                    "    </head>\n" +
                    "    <body>\n" +
                    "        <div id = \"section\">\n" +
                    "            <form method=\"POST\">\n" +
                    "                <input type=\"hidden\" name=\"attr\" value=\""+ table +"\">\n" +
                    "                <table>\n" +
                    "                    <tr>\n" +
                    "                        <td>\n" +
                    "                            新增\n" +
                    "                        </td>\n" +
                    "                        <td>\n" +
                    "                            New "+ select +"\n" +
                    "                        </td>\n" +
                    "                    </tr>\n" +
                    strSel + 
                    strNew+
                    "                    <tr>\n" +
                    "                        <td>\n" +
                    "                            "+ select +"\n" +
                    "                        </td>\n" +
                    "                        <td>\n" +
                    "                            刪除\n" +
                    "                        </td>\n" +
                    "                        <td>\n" +
                    "                            更改\n" +
                    "                        </td>\n" +
                    "                    </tr>\n" +
                    str +
                    "                    <tr>\n" +
                    "                        <td>\n" +
                    "                            <input type=\"submit\" name =\"checkModifyAttr\" value=\"submit\">\n" +
                    "                        </td>\n" +
                    "                    </tr>\n" +
                    "                </table>\n" +
                    "            </form>\n" +
                    "        </div>\n" +
                    "    </body>\n" +
                    "</html>";
    }
    
    
    /**
     * 取得修改Attr方法
     * 
     * @param ID
     * @param attr
     * @return 修改Attr頁面
     */
    public String getAttrPage (String ID , String attr) {
        List attrList = new ArrayList<String>();
        String select = null;
        String table = attr;
        
        select = getTableNameByAttr(attr);

        try( Connection conn = DriverManager.getConnection(DBurl, DBuser, DBpassword) ){
                Statement stm = conn.createStatement();
                ResultSet result = MySQLSyntax.getResultSet(stm, table);

                while (result.next()) {
                    attrList.add(result.getString(select));
                }

            }catch(SQLException ex){
                logger.log(Level.WARNING, " In SQLException (getAttrPage) : " + ex.toString());
                ex.printStackTrace();
                return getSearchPage("資料庫連線錯誤 ! ");
            }
        
        return getAttrPage(attrList, select, table);
    }
    
    
    
    public String getCatePage () {
        List attrList = new ArrayList<String>();
        String table = db2.category_Table.TABLE_NAME;
        String select = db2.category_Table.Category;
        

        try( Connection conn = DriverManager.getConnection(DB2url, DB2user, DB2password) ){
                Statement stm = conn.createStatement();
                ResultSet result = MySQLSyntax.getResultSet(stm, table);

                while (result.next()) {
                    attrList.add(result.getString(select));
                }

            }catch(SQLException ex){
                logger.log(Level.WARNING, " In SQLException (getCatePage) : " + ex.toString());
                ex.printStackTrace();
                return getSearchPage("資料庫連線錯誤 ! ");
            }
        
        return getAttrPage(attrList, "Category", table);
    }
    
    
    /**
     * 檢查修改Attr頁面
     * 
     * @param table
     * @param newAttr
     * @param noNew
     * @param modify
     * @param del
     * @return 修改Attr頁面
     */
    private String checkModifyAttrPage (String table, List<String> newAttr, List<String> noNew, Map<String, String> modify, List<String> del) {
        String newStr = "";
        String noNewStr = "";
        String modStr = "";
        String delStr = "";
        String p = "";
        String form = "";
        if (noNew.size()>0)
            p = "                <p>以下的資料已存在資料庫中, 無法再新增 !</p>\n";
        for (Object obj : newAttr) {
            newStr = newStr + "                    <tr><td>Add</td><td>"+ (String)obj +"</td></tr>\n";
            form = form + "            <input type=\"hidden\" name=\"newAttr\" value=\""+ (String)obj +"\">\n";
        }
        for (Object obj : noNew) {
            noNewStr = noNewStr + "                    <tr><td>"+ (String)obj +"</td></tr>\n";
        }
        for (Object obj : del) {
            delStr = delStr + "                    <tr><td>Delete</td><td>"+ (String)obj +"</td></tr>\n";
            form = form + "            <input type=\"hidden\" name=\"delAttr\" value=\""+ (String)obj +"\">\n";
        }

        for (Iterator iter = modify.entrySet().iterator(); iter.hasNext(); ) {
            Entry<String, String> entry = (Entry<String, String>)iter.next();
            modStr = modStr + "                    <tr><td>Modify</td><td>"+ entry.getKey() +"</td><td>-></td><td>"+ entry.getValue() +"</td></tr>\n";
            form = form + "            <input type=\"hidden\" name=\"modify_"+ entry.getKey() +"\" value=\""+ entry.getValue() +"\">\n";
        }
        
        return "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "    <head>\n" +
                    "        <title>TODO supply a title</title>\n" +
                    "        <meta charset=\"UTF-8\">\n" +
                    "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <style>\n" +
                    "        #section {\n" +
                    //"            background-color:#F0F8FF;\n" +
                    "            position : absolute;\n" +
                    "            top : 0;\n" +
                    "            left : 0;\n" +
                    "            right : 0;\n" +
                    //"            bottom : 0;\n" +
                    "            text-align: left;\n" +
                    "            float:left;\n" +
                    "            padding:5px;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "    </head>\n" +
                    "    <body>\n" +
                    "        <div id = \"section\">\n" +
                    "            <h3>是否進行下列更改 ? </h3>\n" +
                    "            <form method=\"POST\">\n" +
                    "            <input type=\"hidden\" name=\"attr\" value=\""+ table +"\">\n" +
                    form+
                    "                <table>\n" +
                    newStr +
                    delStr +
                    modStr +
                    "                </table>\n" +
                    p +
                    "                <table>\n" +
                    noNewStr +
                    "                    <tr>\n" +
                    "                        <td><input type=\"submit\" name=\"modifyAttr\" value=\"submit\"></td>\n" +
                    "                        <td><input type=\"button\" value=\"cancel\" onclick=\"history.go(-1)\"></td>\n" +
                    "                    </tr>\n" +
                    "                </table>\n" +
                    "            </form>\n" +
                    "        </div>\n" +
                    "    </body>\n" +
                    "</html>";
    }
    
    
    /**
     * 檢查修改Attr頁面方法
     * 
     * @param ID
     * @param request
     * @return 修改Attr頁面
     */
    public String checkModifyAttrPage (String ID, HttpServletRequest request) {
        
        if (request.getParameter("attr").equals(db2.category_Table.TABLE_NAME))
            return checkModifyCatePage(ID, request) ;
        
        List del = new ArrayList<String>();
        Map modify = new TreeMap<String, String>();
        List newAttr = new ArrayList<String>();
        List noNew = new ArrayList<String>();
        
        String table = request.getParameter("attr");
        String select = getTableNameByAttr(table);
        
        Enumeration<String> eum =  request.getParameterNames();
        
        try( Connection conn = DriverManager.getConnection(DBurl, DBuser, DBpassword) ){
            Statement stm = conn.createStatement();
            while(eum.hasMoreElements()){
                String name = eum.nextElement();
                //String value = request.getParameter(name);
                if (name.equals("new")) {
                    for (String value : request.getParameterValues(name)) {
                        if (value.equals(""))
                            continue;
                        ResultSet result = MySQLSyntax.getResultSet(stm, table, select, value);
                        
                        if (result.next())
                            noNew.add(value);
                        else
                            newAttr.add(value);
                    }
                }
                else if (name.startsWith("modify_")&& !request.getParameter(name).equals("")) {
                    String value = request.getParameter(name);
                    String where = name.split("_", 2)[1];
                    ResultSet result = MySQLSyntax.getResultSet(stm, table, select, value);
                    
                    if (result.next())
                        noNew.add(value);
                    else
                        modify.put(where, value);
                }
                else if (name.equals("delete")) {
                    for (String value : request.getParameterValues(name)) {
                        del.add(value);
                    }
                }
            }
        }catch(SQLException ex){
            out.println(" In SQLException ! ");
            ex.printStackTrace();
            return getLoginPage("資料庫連線錯誤 ! ");
        }
        return checkModifyAttrPage(table, newAttr, noNew, modify, del);
    }
    
    
    private String checkModifyCatePage (String ID, HttpServletRequest request) {
    
        List del = new ArrayList<String>();
        Map modify = new TreeMap<String, String>();
        List newAttr = new ArrayList<String>();
        List noNew = new ArrayList<String>();
        
        String table = db2.category_Table.TABLE_NAME;
        String select = db2.category_Table.Category;
        
        Enumeration<String> eum =  request.getParameterNames();
        
        
        try( Connection conn = DriverManager.getConnection(DB2url, DB2user, DB2password) ){
            Statement stm = conn.createStatement();
            while(eum.hasMoreElements()){
                String name = eum.nextElement();
                
                if (name.equals("new")) {
                    for (String value : request.getParameterValues(name)) {
                        if (value.equals(""))
                            continue;
                        ResultSet result = MySQLSyntax.getResultSet(stm, table, select, value);
                        
                        if (result.next())
                            noNew.add(value);
                        else
                            newAttr.add(value);
                    }
                }
                else if (name.startsWith("modify_")&& !request.getParameter(name).equals("")) {
                    String value = request.getParameter(name);
                    String where = name.split("_", 2)[1];
                    ResultSet result = MySQLSyntax.getResultSet(stm, table, select, value);
                    
                    if (result.next())
                        noNew.add(value);
                    else
                        modify.put(where, value);
                }
                else if (name.equals("delete")) {
                    for (String value : request.getParameterValues(name)) {
                        del.add(value);
                    }
                }
            }
        }catch(SQLException ex){
            out.println(" In SQLException (checkModifyCatePage) ! ");
            ex.printStackTrace();
            return getLoginPage("資料庫連線錯誤 ! ");
        }
        
        
        return checkModifyAttrPage(table, newAttr, noNew, modify, del);
    }
    
    
    /**
     * 執行修改Attr頁面
     * 
     * @param ID
     * @param request
     * @return 修改Attr結果
     */
    public String modifyAttrPage(String ID, HttpServletRequest request){
        
        if (request.getParameter("attr").equals(db2.category_Table.TABLE_NAME))
            return modifyCatePage(ID, request);
            
        String table = request.getParameter("attr");
        
        Enumeration<String> eum =  request.getParameterNames();
        String select = getTableNameByAttr(table);
        
        try( Connection conn = DriverManager.getConnection(DBurl, DBuser, DBpassword) ){
            Statement stm = conn.createStatement();
            
            while(eum.hasMoreElements()){
                String name = eum.nextElement();
                if (name.equals("newAttr")) {
                    String[] str = request.getParameterValues(name);
                    for (String value : str) {
                        MySQLSyntax.insertDB(stm, table, value);
                        String log = ID + " add " + select + " : " + value;
                        MySQLSyntax.inserLog(stm, log);
                    }
                }
                else if (name.equals("delAttr")) {
                    String[] str = request.getParameterValues(name);
                    for (String value : str) {
                        MySQLSyntax.deleteDB(stm, table, select, value);
                        String log = ID + " delete " + select + " : " + value;
                        MySQLSyntax.inserLog(stm, log);
                    }
                }
                else if (name.startsWith("modify_")) {
                    MySQLSyntax.updateDB(stm, table, select, name.split("_", 2)[1], request.getParameter(name));
                    String log = ID + " modify " + select + "_" + name.split("_", 2)[1] + " : " + request.getParameter(name);
                    MySQLSyntax.inserLog(stm, log);
                }
            }
            
        } catch(SQLException ex){
            logger.log(Level.WARNING, "Modify record failed (modifyAttrPage) : " + ex.toString());
            ex.printStackTrace();
            return getSearchPage("資料庫連線錯誤 ! ");
        }
        
        return getSearchPage("資料屬性修改成功 !");
    }
    
    
    private String modifyCatePage(String ID, HttpServletRequest request){
    
        String table = db2.category_Table.TABLE_NAME;
        String select = db2.category_Table.Category;
        Enumeration<String> eum =  request.getParameterNames();
        
        try( Connection conn = DriverManager.getConnection(DB2url, DB2user, DB2password) ){
            Statement stm = conn.createStatement();
            
            while(eum.hasMoreElements()){
                String name = eum.nextElement();
                if (name.equals("newAttr")) {
                    String[] str = request.getParameterValues(name);
                    for (String value : str) {
                        MySQLSyntax.insertDB(stm, table, value);
                        String log = ID + " add " + select + " : " + value;
                        MySQLSyntax.inserLog(stm, log);
                    }
                }
                else if (name.equals("delAttr")) {
                    String[] str = request.getParameterValues(name);
                    for (String value : str) {
                        MySQLSyntax.deleteDB(stm, table, select, value);
                        String log = ID + " delete " + select + " : " + value;
                        MySQLSyntax.inserLog(stm, log);
                    }
                }
                else if (name.startsWith("modify_")) {
                    MySQLSyntax.updateDB(stm, table, select, name.split("_", 2)[1], request.getParameter(name));
                    String log = ID + " modify " + select + "_" + name.split("_", 2)[1] + " : " + request.getParameter(name);
                    MySQLSyntax.inserLog(stm, log);
                }
            }
            
        } catch(SQLException ex){
            logger.log(Level.WARNING, "Modify record failed (modifyCatePage) : " + ex.toString());
            ex.printStackTrace();
            return getSearchPage("資料庫連線錯誤 ! ");
        }
        
        return getSearchPage("產品目錄修改成功 !");
    }
    
    
    /**
     * 
     * 判斷上傳檔案資料執行之動作
     * 
     * @param boo
     * @param i
     * @param request
     * @param attrList
     * @param tableName
     * @param stm
     * @param where
     * @param ID
     * @throws SQLException 
     */
    private void insertORmodify (boolean boo, int i, HttpServletRequest request, List<String> attrList,String tableName, Statement stm, Map<String, String> where, String ID) throws SQLException {
        String log;
        if (boo) {
            Map set = new TreeMap<String, String>();
            for (Iterator<String> iter = attrList.iterator(); iter.hasNext(); ) {
                String str = iter.next();
                set.put(str, request.getParameter(tableName + "_" + str + "_" + i));
            }
            MySQLSyntax.updateDB(stm, tableName, where, set);
            log = ID + " modify "+ tableName +", ";
        }
        else {
            List val = new ArrayList<String>();
            List wh = new ArrayList<String>();
            for (Iterator<String> iter = attrList.iterator(); iter.hasNext(); ) {
                String str = iter.next();
                wh.add(str);
                val.add(request.getParameter(tableName + "_" + str + "_" + i));
            }      
            MySQLSyntax.insertDB(stm, tableName, wh, val);
            log = ID + " insert "+ tableName +", ";
        }
        for (Iterator<String> iter = attrList.iterator(); iter.hasNext(); ) {
                String str = iter.next();
                log = log + str + " : " + request.getParameter(tableName + "_" + str + "_" + i) + ", ";
        }      
        MySQLSyntax.inserLog(stm, log);
    }
    
    private void checkUploadData (String table, String mainKey, List<String> AllKeyList, HttpServletRequest request, String ID, Statement stm) throws SQLException {
        String str = "";
        Map where;
        ResultSet result = null;
        
        str = table + "_" + mainKey + "_";
        for (int i = 0; request.getParameter(str + i) != null; i++) {
            
            
            //logger.log(Level.INFO, str + i);
            
            where = new TreeMap<String, String>();

            where.put(mainKey, request.getParameter(str + i));
            result = MySQLSyntax.getResultSet(stm, table, where);

            insertORmodify (result.next(), i, request, AllKeyList, table, stm, where, ID); 
        }
    
    }
    
    
    /**
     * 
     * 將上傳檔案之資料存入資料庫
     * 
     * @param ID
     * @param request
     * @return 上傳結果
     */
    public String uploadData(String ID, HttpServletRequest request) {
        try( Connection conn = DriverManager.getConnection(DBurl, DBuser, DBpassword) ){
            Statement stm = conn.createStatement();
            
            checkUploadData (CPU.TableName, CPU.MainKey, CPU.AllKeyList, request, ID,  stm);
            checkUploadData (Antennas.TableName, Antennas.MainKey, Antennas.AllKeyList, request, ID,  stm);
            checkUploadData (Button.TableName, Button.MainKey, Button.AllKeyList, request, ID,  stm);
            checkUploadData (Camera.TableName, Camera.MainKey, Camera.AllKeyList, request, ID,  stm);
            checkUploadData (CardReader.TableName, CardReader.MainKey, CardReader.AllKeyList, request, ID,  stm);
            checkUploadData (ClickPad.TableName, ClickPad.MainKey, ClickPad.AllKeyList, request, ID,  stm);
            checkUploadData (Ethernet.TableName, Ethernet.MainKey, Ethernet.AllKeyList, request, ID,  stm);
            checkUploadData (KBCEBC.TableName, KBCEBC.MainKey, KBCEBC.AllKeyList, request, ID,  stm);
            checkUploadData (LcdPanel.TableName, LcdPanel.MainKey, LcdPanel.AllKeyList, request, ID,  stm);
            checkUploadData (Memory.TableName, Memory.MainKey, Memory.AllKeyList, request, ID,  stm);
            checkUploadData (Sensor.TableName, Sensor.MainKey, Sensor.AllKeyList, request, ID,  stm);
            checkUploadData (Storage.TableName, Storage.MainKey, Storage.AllKeyList, request, ID,  stm);
            checkUploadData (TouchPanel.TableName, TouchPanel.MainKey, TouchPanel.AllKeyList, request, ID,  stm);
            checkUploadData (WLAN.TableName, WLAN.MainKey, WLAN.AllKeyList, request, ID,  stm);
            checkUploadData (WWAN.TableName, WWAN.MainKey, WWAN.AllKeyList, request, ID,  stm);
            checkUploadData (Keyboard.TableName, Keyboard.MainKey, Keyboard.AllKeyList, request, ID,  stm);
            checkUploadData (Battery.TableName, Battery.MainKey, Battery.AllKeyList, request, ID,  stm);
            checkUploadData (Charger.TableName, Charger.MainKey, Charger.AllKeyList, request, ID,  stm);
            checkUploadData (Measurement.TableName, Measurement.MainKey, Measurement.AllKeyList, request, ID,  stm);
            checkUploadData (Graphic.TableName, Graphic.MainKey, Graphic.AllKeyList, request, ID,  stm);
            checkUploadData (AudioCodec.TableName, AudioCodec.MainKey, AudioCodec.AllKeyList, request, ID,  stm);
            checkUploadData (PanelInterfaceBridge.TableName, PanelInterfaceBridge.MainKey, PanelInterfaceBridge.AllKeyList, request, ID,  stm);
            checkUploadData (ExternalStorageCard.TableName, ExternalStorageCard.MainKey, ExternalStorageCard.AllKeyList, request, ID,  stm);
            checkUploadData (ODD.TableName, ODD.MainKey, ODD.AllKeyList, request, ID,  stm);
            checkUploadData (Speaker.TableName, Speaker.MainKey, Speaker.AllKeyList, request, ID,  stm);
            checkUploadData (Mic.TableName, Mic.MainKey, Mic.AllKeyList, request, ID,  stm);
            checkUploadData (IoPort.TableName, IoPort.MainKey, IoPort.AllKeyList, request, ID,  stm);
            checkUploadData (OS.TableName, OS.MainKey, OS.AllKeyList, request, ID,  stm);
            
        }catch(SQLException ex){
            logger.log(Level.WARNING, "Modify record failed (uploadData) : " + ex.toString());
            ex.printStackTrace();
            return getSearchPage("資料庫連線錯誤 ! ");
        }
        return getSearchPage("資料修改成功 ! ");
    }
    
    
    /**
     * 
     * 取得紀錄頁面
     * 
     * @param timeList
     * @param logList
     * @return 紀錄頁面
     */
    private String getLogPage(List<String> timeList, List<String> logList, String page, String action){
        String str = "";
        
        String next = "<a href=\"main?action="+ action +"&page="+ ( Integer.parseInt(page)+1 ) +"\">下一頁</a>";
        String last = "";
        
        if (Integer.parseInt(page) > 1)
            last = "<a href=\"main?action="+ action +"&page="+ (Integer.parseInt(page) - 1) +"\">上一頁</a>";
        
        for (int i =0; i < timeList.size(); i++) {
            str = str +  "                    <tr>\n" +
                                "                        <td>\n" +
                                "                            "+ timeList.get(i) +"\n" +
                                "                        </td>\n" +
                                "                        <td>\n" +
                                "                            "+ logList.get(i) +"\n" +
                                "                        </td>\n" +
                                "                    </tr>\n";
        }
        
        return "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "    <head>\n" +
                    "        <title>TODO supply a title</title>\n" +
                    "        <meta charset=\"UTF-8\">\n" +
                    "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <style>\n" +
                    "        #section {\n" +
                    //"            background-color:#F0F8FF;\n" +
                    "            position : absolute;\n" +
                    "            top : 0;\n" +
                    "            left : 0;\n" +
                    "            right : 0;\n" +
                    //"            bottom : 0;\n" +
                    "            text-align: left;\n" +
                    "            float:left;\n" +
                    "            padding:5px;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "    </head>\n" +
                    "    <body>\n" +
                    "        <div id = \"section\">\n" +
                    "            <form method=\"POST\">\n" +
                    "                <table>\n" +
                    "                    <tr>\n" +
                    "                        <td>"+ last +"</td>\n" +
                    "                        <td>"+ next +"</td>\n" +
                    "                    </tr>\n" +
                    "                    <tr>\n" +
                    "                        <td>\n" +
                    "                            Time\n" +
                    "                        </td>\n" +
                    "                        <td>\n" +
                    "                            Log\n" +
                    "                        </td>\n" +
                    "                    </tr>\n" +
                                            str +
                    "                </table>\n" +
                    "            </form>\n" +
                    "        </div>\n" +
                    "    </body>\n" +
                    "</html>";
    }
    
    
    /**
     * 
     * 取得紀錄頁面方法
     * 
     * @return 紀錄頁面
     */
    public String getLogPage(String page){
        List timeList = new ArrayList<String>();
        List logList = new ArrayList<String>();
        
        int i = ( Integer.parseInt(page) - 1 ) * 100;
        if (i != 0)
            i--;
        
        try( Connection conn = DriverManager.getConnection(DBurl, DBuser, DBpassword) ){
            Statement stm = conn.createStatement();
            ResultSet result = stm.executeQuery("SELECT * FROM "+ db.log_Table.TABLE_NAME +" ORDER BY "+ db.log_Table.Time +" DESC LIMIT "+ i +", 100");

            while (result.next()) {
                timeList.add(result.getString(db.log_Table.Time));
                logList.add(result.getString(db.log_Table.Log));
            }
        }catch(SQLException ex){
            logger.log(Level.INFO, "In SQLException (getLogPage) : " + ex.toString());
            ex.printStackTrace();
            return getSearchPage("資料庫連線錯誤 ! ");
        }
        
        return getLogPage(timeList, logList, page, "log");
    }
    
    
    public String getProjectLogPage(String page){
        List timeList = new ArrayList<String>();
        List logList = new ArrayList<String>();
        
        int i = ( Integer.parseInt(page) - 1 ) * 100;
        if (i != 0)
            i--;
        
        try( Connection conn = DriverManager.getConnection(DB2url, DB2user, DB2password) ){
            Statement stm = conn.createStatement();
            ResultSet result = stm.executeQuery("SELECT * FROM "+ db2.log_Table.TABLE_NAME +" ORDER BY "+ db2.log_Table.Time +" DESC LIMIT "+ i +", 100");

            while (result.next()) {
                timeList.add(result.getString(db.log_Table.Time));
                logList.add(result.getString(db.log_Table.Log));
            }
        }catch(SQLException ex){
            logger.log(Level.INFO, "In SQLException (getProjectLogPage) : " + ex.toString());
            ex.printStackTrace();
            return getSearchPage("資料庫連線錯誤 ! ");
        }
        
        return getLogPage(timeList, logList, page, "Projectlog");
    }
    
    public String getNewProjectPage () {
        return "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "    <head>\n" +
                    "        <title>TODO supply a title</title>\n" +
                    "        <meta charset=\"UTF-8\">\n" +
                    "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <style>\n" +
                    "        #section {\n" +
                    //"            background-color:#F0F8FF;\n" +
                    "            position : absolute;\n" +
                    "            top : 0;\n" +
                    "            left : 0;\n" +
                    "            right : 0;\n" +
                    //"            bottom : 0;\n" +
                    "            text-align: left;\n" +
                    "            float:left;\n" +
                    "            padding:5px;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "    </head>\n" +
                    "    <body>\n" +
                    "        <div id = \"section\">\n" +
                    "            <h3>Create Project</h3>\n"+
                    "            <form method=\"POST\">\n" +
                    "                ProjectName : \n"+
                    "                <input type = \"text\" name = \"ProjectName\">\n" +
                    "                <input type = \"submit\" name = \"CreateProject\" value = \"Create Project\">\n" +
                    "            </form>\n" +
                    "        </div>\n" +
                    "    </body>\n" +
                    "</html>";
    }
    
    public String getCreateProjectPage (HttpServletRequest request) {
        return getCreateProjectPage(request.getParameter("ProjectName"));
    }
    
    
    private String resultToInputStr(ResultSet result, String name, String main, List<String> value, String tablename) throws SQLException{
        String str = "                    <tr>\n"
                            + "                        <td>"+ name +"</td><td><select id = \""+ tablename +"\" onchange = \" changeSelect(\'"+ tablename +"\') \">\n";
        
        for (int i = 0; i <= 10 ; i ++) {
            str = str + "                            <option value = \""+ i +"\">"+ i +"</option>\n";
        }
        str = str + "                        </select></td>\n"
                        + "                    </tr>\n";
        
        Iterator<String> iter = new ArrayList<String>().iterator();
        String select = "";
        if (value != null)
            iter = value.iterator();
        
        for (int i = 1; i <= 10 ; i++) {
             if (iter.hasNext()) {
                select = iter.next();
                str = str + "                    <tr id=\""+ tablename +"_"+ i +"\" style=\"display : table-row\">\n"
                                + "                        <td></td><td>"+ main +" : </td><td><select name = \""+ tablename +"\"><option value = \"\"></option>\n";
             }
             
             else {
                str = str + "                    <tr id=\""+ tablename +"_"+ i +"\" style=\"display : none\">\n"
                                + "                        <td></td><td>"+ main +" : </td><td><select name = \""+ tablename +"\"><option value = \"\"></option>\n";
             }
            while (result.next()) {
                if (select.equals(result.getString(main)))
                   str = str + "                            <option value = \"" + result.getString(main) + "\" SELECTED>" + result.getString(main) + "</option>\n";
                else
                    str = str + "                            <option value = \"" + result.getString(main) + "\">" + result.getString(main) + "</option>\n";
             }
            select = "";
             
             
             result.beforeFirst();
             
             str = str + "                        <select></td>\n"
                            + "                    </tr>\n";
        }
        
        
        return str;
    }
    
    private String desInput (List<String> desList, List<String> typeList) {
        String str = "                    <tr>\n"
                            + "                        <td>Description</td><td><select id = \"Description\" onchange = \" changeSelect(\'Description\') \">\n";
        
        for (int i = 0; i <= 10 ; i ++) {
            str = str + "                            <option value = \""+ i +"\">"+ i +"</option>\n";
        }
        str = str + "                        </select></td>\n"
                        + "                    </tr>\n";
        
        Iterator<String> desiter = desList.iterator();
        Iterator<String> typeiter = typeList.iterator();
        
        
        for (int i = 1; i <= 10 ; i++) {    
            if (typeiter.hasNext() && desiter.hasNext()) {
                str = str + "                    <tr id=\"Description_"+ i +"\" style=\"display : table-row\">\n"
                                + "                        <td></td><td>Type : </td><td><input type = \"text\" name = desType value = \""+ typeiter.next() +"\"></td>\n"
                                + "                        <td>Description : </td><td><input type = \"text\" name = Description value = \""+ desiter.next() +"\"></td>\n"
                                + "                    </tr>\n";
            }
            else {
                str = str + "                    <tr id=\"Description_"+ i +"\" style=\"display : none\">\n"
                                + "                        <td></td><td>Type : </td><td><input type = \"text\" name = desType value = \"\"></td>\n"
                                + "                        <td>Description : </td><td><input type = \"text\" name = Description value = \"\"></td>\n"
                                + "                    </tr>\n";
            }
        }
        
        return str;
    }
     
    
    private Map<String, List <String>> ProjectToMap (Statement stm, String pjname) throws SQLException {
        ResultSet result = null;
        Map<String, List <String>> value = new TreeMap<String, List <String>>();
        List<String> list;
        
        result = MySQLSyntax.getResultSet(stm, Antennas.ProjectTableName, db2.project_Table.ProjectName, pjname);
        while (result.next()) {
            if (value.containsKey(Antennas.TableName))
                value.get(Antennas.TableName).add(result.getString(Antennas.MainKey));
            else {
                list = new ArrayList<String>();
                list.add(result.getString(Antennas.MainKey));
                value.put(Antennas.TableName, list);
            }
        }
        
        result = MySQLSyntax.getResultSet(stm, Battery.ProjectTableName, db2.project_Table.ProjectName, pjname);
        while (result.next()) {
            if (value.containsKey(Battery.TableName))
                value.get(Battery.TableName).add(result.getString(Battery.MainKey));
            else {
                list = new ArrayList<String>();
                list.add(result.getString(Battery.MainKey));
                value.put(Battery.TableName, list);
            }
        }
        
        result = MySQLSyntax.getResultSet(stm, Button.ProjectTableName, db2.project_Table.ProjectName, pjname);
        while (result.next()) {
            if (value.containsKey(Button.TableName))
                value.get(Button.TableName).add(result.getString(Button.MainKey));
            else {
                list = new ArrayList<String>();
                list.add(result.getString(Button.MainKey));
                value.put(Button.TableName, list);
            }
        }
        
        result = MySQLSyntax.getResultSet(stm, Camera.ProjectTableName, db2.project_Table.ProjectName, pjname);
        while (result.next()) {
            if (value.containsKey(Camera.TableName))
                value.get(Camera.TableName).add(result.getString(Camera.MainKey));
            else {
                list = new ArrayList<String>();
                list.add(result.getString(Camera.MainKey));
                value.put(Camera.TableName, list);
            }
        }
        
        result = MySQLSyntax.getResultSet(stm, CardReader.ProjectTableName, db2.project_Table.ProjectName, pjname);
        while (result.next()) {
            if (value.containsKey(CardReader.TableName))
                value.get(CardReader.TableName).add(result.getString(CardReader.MainKey));
            else {
                list = new ArrayList<String>();
                list.add(result.getString(CardReader.MainKey));
                value.put(CardReader.TableName, list);
            }
        }
        
        result = MySQLSyntax.getResultSet(stm, Charger.ProjectTableName, db2.project_Table.ProjectName, pjname);
        while (result.next()) {
            if (value.containsKey(Charger.TableName))
                value.get(Charger.TableName).add(result.getString(Charger.MainKey));
            else {
                list = new ArrayList<String>();
                list.add(result.getString(Charger.MainKey));
                value.put(Charger.TableName, list);
            }
        }
        
        result = MySQLSyntax.getResultSet(stm, ClickPad.ProjectTableName, db2.project_Table.ProjectName, pjname);
        while (result.next()) {
            if (value.containsKey(ClickPad.TableName))
                value.get(ClickPad.TableName).add(result.getString(ClickPad.MainKey));
            else {
                list = new ArrayList<String>();
                list.add(result.getString(ClickPad.MainKey));
                value.put(ClickPad.TableName, list);
            }
        }
        
        result = MySQLSyntax.getResultSet(stm, CPU.ProjectTableName, db2.project_Table.ProjectName, pjname);
        while (result.next()) {
            if (value.containsKey(CPU.TableName))
                value.get(CPU.TableName).add(result.getString(CPU.MainKey));
            else {
                list = new ArrayList<String>();
                list.add(result.getString(CPU.MainKey));
                value.put(CPU.TableName, list);
            }
        }
        
        result = MySQLSyntax.getResultSet(stm, Ethernet.ProjectTableName, db2.project_Table.ProjectName, pjname);
        while (result.next()) {
            if (value.containsKey(Ethernet.TableName))
                value.get(Ethernet.TableName).add(result.getString(Ethernet.MainKey));
            else {
                list = new ArrayList<String>();
                list.add(result.getString(Ethernet.MainKey));
                value.put(Ethernet.TableName, list);
            }
        }
        
        result = MySQLSyntax.getResultSet(stm, KBCEBC.ProjectTableName, db2.project_Table.ProjectName, pjname);
        while (result.next()) {
            if (value.containsKey(KBCEBC.TableName))
                value.get(KBCEBC.TableName).add(result.getString(KBCEBC.MainKey));
            else {
                list = new ArrayList<String>();
                list.add(result.getString(KBCEBC.MainKey));
                value.put(KBCEBC.TableName, list);
            }
        }
        
        result = MySQLSyntax.getResultSet(stm, Keyboard.ProjectTableName, db2.project_Table.ProjectName, pjname);
        while (result.next()) {
            if (value.containsKey(Keyboard.TableName))
                value.get(Keyboard.TableName).add(result.getString(Keyboard.MainKey));
            else {
                list = new ArrayList<String>();
                list.add(result.getString(Keyboard.MainKey));
                value.put(Keyboard.TableName, list);
            }
        }
        
        result = MySQLSyntax.getResultSet(stm, LcdPanel.ProjectTableName, db2.project_Table.ProjectName, pjname);
        while (result.next()) {
            if (value.containsKey(LcdPanel.TableName))
                value.get(LcdPanel.TableName).add(result.getString(LcdPanel.MainKey));
            else {
                list = new ArrayList<String>();
                list.add(result.getString(LcdPanel.MainKey));
                value.put(LcdPanel.TableName, list);
            }
        }
        
        result = MySQLSyntax.getResultSet(stm, Measurement.ProjectTableName, db2.project_Table.ProjectName, pjname);
        while (result.next()) {
            if (value.containsKey(Measurement.TableName))
                value.get(Measurement.TableName).add(result.getString(Measurement.MainKey));
            else {
                list = new ArrayList<String>();
                list.add(result.getString(Measurement.MainKey));
                value.put(Measurement.TableName, list);
            }
        }
        
        result = MySQLSyntax.getResultSet(stm, Memory.ProjectTableName, db2.project_Table.ProjectName, pjname);
        while (result.next()) {
            if (value.containsKey(Memory.TableName))
                value.get(Memory.TableName).add(result.getString(Memory.MainKey));
            else {
                list = new ArrayList<String>();
                list.add(result.getString(Memory.MainKey));
                value.put(Memory.TableName, list);
            }
        }
        
        result = MySQLSyntax.getResultSet(stm, Sensor.ProjectTableName, db2.project_Table.ProjectName, pjname);
        while (result.next()) {
            if (value.containsKey(Sensor.TableName))
                value.get(Sensor.TableName).add(result.getString(Sensor.MainKey));
            else {
                list = new ArrayList<String>();
                list.add(result.getString(Sensor.MainKey));
                value.put(Sensor.TableName, list);
            }
        }
        
        result = MySQLSyntax.getResultSet(stm, Storage.ProjectTableName, db2.project_Table.ProjectName, pjname);
        while (result.next()) {
            if (value.containsKey(Storage.TableName))
                value.get(Storage.TableName).add(result.getString(Storage.MainKey));
            else {
                list = new ArrayList<String>();
                list.add(result.getString(Storage.MainKey));
                value.put(Storage.TableName, list);
            }
        }
        
        result = MySQLSyntax.getResultSet(stm, TouchPanel.ProjectTableName, db2.project_Table.ProjectName, pjname);
        while (result.next()) {
            if (value.containsKey(TouchPanel.TableName))
                value.get(TouchPanel.TableName).add(result.getString(TouchPanel.MainKey));
            else {
                list = new ArrayList<String>();
                list.add(result.getString(TouchPanel.MainKey));
                value.put(TouchPanel.TableName, list);
            }
        }
        
        result = MySQLSyntax.getResultSet(stm, WLAN.ProjectTableName, db2.project_Table.ProjectName, pjname);
        while (result.next()) {
            if (value.containsKey(WLAN.TableName))
                value.get(WLAN.TableName).add(result.getString(WLAN.MainKey));
            else {
                list = new ArrayList<String>();
                list.add(result.getString(WLAN.MainKey));
                value.put(WLAN.TableName, list);
            }
        }
        
        result = MySQLSyntax.getResultSet(stm, WWAN.ProjectTableName, db2.project_Table.ProjectName, pjname);
        while (result.next()) {
            if (value.containsKey(WWAN.TableName))
                value.get(WWAN.TableName).add(result.getString(WWAN.MainKey));
            else {
                list = new ArrayList<String>();
                list.add(result.getString(WWAN.MainKey));
                value.put(WWAN.TableName, list);
            }
        }
        
        result = MySQLSyntax.getResultSet(stm, Graphic.ProjectTableName, db2.project_Table.ProjectName, pjname);
        while (result.next()) {
            if (value.containsKey(Graphic.TableName))
                value.get(Graphic.TableName).add(result.getString(Graphic.MainKey));
            else {
                list = new ArrayList<String>();
                list.add(result.getString(Graphic.MainKey));
                value.put(Graphic.TableName, list);
            }
        }
        
        result = MySQLSyntax.getResultSet(stm, AudioCodec.ProjectTableName, db2.project_Table.ProjectName, pjname);
        while (result.next()) {
            if (value.containsKey(AudioCodec.TableName))
                value.get(AudioCodec.TableName).add(result.getString(AudioCodec.MainKey));
            else {
                list = new ArrayList<String>();
                list.add(result.getString(AudioCodec.MainKey));
                value.put(AudioCodec.TableName, list);
            }
        }
        
        result = MySQLSyntax.getResultSet(stm, PanelInterfaceBridge.ProjectTableName, db2.project_Table.ProjectName, pjname);
        while (result.next()) {
            if (value.containsKey(PanelInterfaceBridge.TableName))
                value.get(PanelInterfaceBridge.TableName).add(result.getString(PanelInterfaceBridge.MainKey));
            else {
                list = new ArrayList<String>();
                list.add(result.getString(PanelInterfaceBridge.MainKey));
                value.put(PanelInterfaceBridge.TableName, list);
            }
        }
        
        result = MySQLSyntax.getResultSet(stm, ExternalStorageCard.ProjectTableName, db2.project_Table.ProjectName, pjname);
        while (result.next()) {
            if (value.containsKey(ExternalStorageCard.TableName))
                value.get(ExternalStorageCard.TableName).add(result.getString(ExternalStorageCard.MainKey));
            else {
                list = new ArrayList<String>();
                list.add(result.getString(ExternalStorageCard.MainKey));
                value.put(ExternalStorageCard.TableName, list);
            }
        }
        
        result = MySQLSyntax.getResultSet(stm, ODD.ProjectTableName, db2.project_Table.ProjectName, pjname);
        while (result.next()) {
            if (value.containsKey(ODD.TableName))
                value.get(ODD.TableName).add(result.getString(ODD.MainKey));
            else {
                list = new ArrayList<String>();
                list.add(result.getString(ODD.MainKey));
                value.put(ODD.TableName, list);
            }
        }
        
        result = MySQLSyntax.getResultSet(stm, Speaker.ProjectTableName, db2.project_Table.ProjectName, pjname);
        while (result.next()) {
            if (value.containsKey(Speaker.TableName))
                value.get(Speaker.TableName).add(result.getString(Speaker.MainKey));
            else {
                list = new ArrayList<String>();
                list.add(result.getString(Speaker.MainKey));
                value.put(Speaker.TableName, list);
            }
        }
        
        result = MySQLSyntax.getResultSet(stm, Mic.ProjectTableName, db2.project_Table.ProjectName, pjname);
        while (result.next()) {
            if (value.containsKey(Mic.TableName))
                value.get(Mic.TableName).add(result.getString(Mic.MainKey));
            else {
                list = new ArrayList<String>();
                list.add(result.getString(Mic.MainKey));
                value.put(Mic.TableName, list);
            }
        }
        
        result = MySQLSyntax.getResultSet(stm, IoPort.ProjectTableName, db2.project_Table.ProjectName, pjname);
        while (result.next()) {
            if (value.containsKey(IoPort.TableName))
                value.get(IoPort.TableName).add(result.getString(IoPort.MainKey));
            else {
                list = new ArrayList<String>();
                list.add(result.getString(IoPort.MainKey));
                value.put(IoPort.TableName, list);
            }
        }
        
        result = MySQLSyntax.getResultSet(stm, OS.ProjectTableName, db2.project_Table.ProjectName, pjname);
        while (result.next()) {
            if (value.containsKey(OS.TableName))
                value.get(OS.TableName).add(result.getString(OS.MainKey));
            else {
                list = new ArrayList<String>();
                list.add(result.getString(OS.MainKey));
                value.put(OS.TableName, list);
            }
        }
        
        
        return value;
    }
    
    
    /**
     * 
     * @param coms 料件名稱及料件內容
     * @param value 所包含的料件及值
     * @return 建立Project網頁內容
     */
    private String getCreateProjectPage (String pjname) {
        
        String input = "";
        ResultSet result = null;
        
        Map<String, List <String>> value = new TreeMap<String, List <String>>();
        String category = "";
        List<String> cateopt = new ArrayList<String>();
        List<String> typelist = new ArrayList<String>();
        List<String> deslist = new ArrayList<String>();
        
        
        try( Connection conn = DriverManager.getConnection(DB2url, DB2user, DB2password) ){
            Statement stm = conn.createStatement();
           
            result = MySQLSyntax.getResultSet(stm, db2.category_Table.TABLE_NAME);
            while (result.next()) {
                cateopt.add(result.getString(db2.category_Table.Category));
            }
            
            result = MySQLSyntax.getResultSet(stm, db2.project_Table.TABLE_NAME, db2.project_Table.ProjectName, pjname);
            
            if (result.next()) {
                category = result.getString(db2.project_Table.Category);
                
                result = MySQLSyntax.getResultSet(stm, db2.description_Table.TABLE_NAME, db2.description_Table.ProjectName, pjname);
                while (result.next()) {
                    typelist.add(result.getString(db2.description_Table.Type));
                    deslist.add(result.getString(db2.description_Table.Description));
                }
                
               value = ProjectToMap(stm, pjname);
                
            }
        }catch(SQLException ex){
            logger.log(Level.INFO, "In SQLException (getCreateProjectPage) : " + ex.toString());
            ex.printStackTrace();
            return getSearchPage("資料庫連線錯誤 ! ");
        }
         
        String str = "";
        for (String s : cateopt) {
            if (category.equals(s))
                str = str + "                    <option value = \""+ s +"\" SELECTED>"+ s +"</option>\n";
            else
                str = str + "                    <option value = \""+ s +"\">"+ s +"</option>\n";
        }
        String catestr = "                <tr><td>Category : </td>\n"
                                + "                    <td><select name = \"category\">"
                                + str 
                                + "                     </select></td><tr>\n";
        
        
        
        
        try( Connection conn = DriverManager.getConnection(DBurl, DBuser, DBpassword) ){
            Statement stm = conn.createStatement();

            result= MySQLSyntax.getResultSet(stm, CPU.TableName);
            input = input + resultToInputStr(result, CPU.ComponentName, CPU.MainKey, value.get(CPU.TableName), CPU.TableName);
            
            result= MySQLSyntax.getResultSet(stm, Antennas.TableName);
            input = input + resultToInputStr(result, Antennas.ComponentName, Antennas.MainKey, value.get(Antennas.TableName), Antennas.TableName);
            
            result= MySQLSyntax.getResultSet(stm, Battery.TableName);
            input = input + resultToInputStr(result, Battery.ComponentName, Battery.MainKey, value.get(Battery.TableName), Battery.TableName);
            
            result= MySQLSyntax.getResultSet(stm, Button.TableName);
            input = input + resultToInputStr(result, Button.ComponentName, Button.MainKey, value.get(Button.TableName), Button.TableName);
            
            result= MySQLSyntax.getResultSet(stm, Camera.TableName);
            input = input + resultToInputStr(result, Camera.ComponentName, Camera.MainKey, value.get(Camera.TableName), Camera.TableName);
            
            result= MySQLSyntax.getResultSet(stm, CardReader.TableName);
            input = input + resultToInputStr(result, CardReader.ComponentName, CardReader.MainKey, value.get(CardReader.TableName), CardReader.TableName);
            
            result= MySQLSyntax.getResultSet(stm, Charger.TableName);
            input = input + resultToInputStr(result, Charger.ComponentName, Charger.MainKey, value.get(Charger.TableName), Charger.TableName);
            
            result= MySQLSyntax.getResultSet(stm, ClickPad.TableName);
            input = input + resultToInputStr(result, ClickPad.ComponentName, ClickPad.MainKey, value.get(ClickPad.TableName), ClickPad.TableName);
            
            result= MySQLSyntax.getResultSet(stm, Ethernet.TableName);
            input = input + resultToInputStr(result, Ethernet.ComponentName, Ethernet.MainKey, value.get(Ethernet.TableName), Ethernet.TableName);
            
            result= MySQLSyntax.getResultSet(stm, KBCEBC.TableName);
            input = input + resultToInputStr(result, KBCEBC.ComponentName, KBCEBC.MainKey, value.get(KBCEBC.TableName), KBCEBC.TableName);
            
            result= MySQLSyntax.getResultSet(stm, Keyboard.TableName);
            input = input + resultToInputStr(result, Keyboard.ComponentName, Keyboard.MainKey, value.get(Keyboard.TableName), Keyboard.TableName);
            
            result= MySQLSyntax.getResultSet(stm, LcdPanel.TableName);
            input = input + resultToInputStr(result, LcdPanel.ComponentName, LcdPanel.MainKey, value.get(LcdPanel.TableName), LcdPanel.TableName);
            
            result= MySQLSyntax.getResultSet(stm, Measurement.TableName);
            input = input + resultToInputStr(result, Measurement.ComponentName, Measurement.MainKey, value.get(Measurement.TableName), Measurement.TableName);
            
            result= MySQLSyntax.getResultSet(stm, Memory.TableName);
            input = input + resultToInputStr(result, Memory.ComponentName, Memory.MainKey, value.get(Memory.TableName), Memory.TableName);
            
            result= MySQLSyntax.getResultSet(stm, Sensor.TableName);
            input = input + resultToInputStr(result, Sensor.ComponentName, Sensor.MainKey, value.get(Sensor.TableName), Sensor.TableName);
            
            result= MySQLSyntax.getResultSet(stm, Storage.TableName);
            input = input + resultToInputStr(result, Storage.ComponentName, Storage.MainKey, value.get(Storage.TableName), Storage.TableName);
            
            result= MySQLSyntax.getResultSet(stm, TouchPanel.TableName);
            input = input + resultToInputStr(result, TouchPanel.ComponentName, TouchPanel.MainKey, value.get(TouchPanel.TableName), TouchPanel.TableName);
            
            result= MySQLSyntax.getResultSet(stm, WLAN.TableName);
            input = input + resultToInputStr(result, WLAN.ComponentName, WLAN.MainKey, value.get(WLAN.TableName), WLAN.TableName);
            
            result= MySQLSyntax.getResultSet(stm, WWAN.TableName);
            input = input + resultToInputStr(result, WWAN.ComponentName, WWAN.MainKey, value.get(WWAN.TableName), WWAN.TableName);
            
            result= MySQLSyntax.getResultSet(stm, Graphic.TableName);
            input = input + resultToInputStr(result, Graphic.ComponentName, Graphic.MainKey, value.get(Graphic.TableName), Graphic.TableName);
            
            result= MySQLSyntax.getResultSet(stm, AudioCodec.TableName);
            input = input + resultToInputStr(result, AudioCodec.ComponentName, AudioCodec.MainKey, value.get(AudioCodec.TableName), AudioCodec.TableName);
            
            result= MySQLSyntax.getResultSet(stm, PanelInterfaceBridge.TableName);
            input = input + resultToInputStr(result, PanelInterfaceBridge.ComponentName, PanelInterfaceBridge.MainKey, value.get(PanelInterfaceBridge.TableName), PanelInterfaceBridge.TableName);
            
            result= MySQLSyntax.getResultSet(stm, ExternalStorageCard.TableName);
            input = input + resultToInputStr(result, ExternalStorageCard.ComponentName, ExternalStorageCard.MainKey, value.get(ExternalStorageCard.TableName), ExternalStorageCard.TableName);
            
            result= MySQLSyntax.getResultSet(stm, ODD.TableName);
            input = input + resultToInputStr(result, ODD.ComponentName, ODD.MainKey, value.get(ODD.TableName), ODD.TableName);
            
            result= MySQLSyntax.getResultSet(stm, Speaker.TableName);
            input = input + resultToInputStr(result, Speaker.ComponentName, Speaker.MainKey, value.get(Speaker.TableName), Speaker.TableName);
            
            result= MySQLSyntax.getResultSet(stm, Mic.TableName);
            input = input + resultToInputStr(result, Mic.ComponentName, Mic.MainKey, value.get(Mic.TableName), Mic.TableName);
            
            result= MySQLSyntax.getResultSet(stm, IoPort.TableName);
            input = input + resultToInputStr(result, IoPort.ComponentName, IoPort.MainKey, value.get(IoPort.TableName), IoPort.TableName);
            
            result= MySQLSyntax.getResultSet(stm, OS.TableName);
            input = input + resultToInputStr(result, OS.ComponentName, OS.MainKey, value.get(OS.TableName), OS.TableName);
            
        }catch(SQLException ex){
            logger.log(Level.INFO, "In SQLException (getCreateProjectPage) : " + ex.toString());
            ex.printStackTrace();
            return getSearchPage("資料庫連線錯誤 ! ");
        }

        return "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "    <head>\n" +
                    "        <title>TODO supply a title</title>\n" +
                    "        <meta charset=\"UTF-8\">\n" +
                    "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <style>\n" +
                    "        #section {\n" +
                    //"            background-color:#F0F8FF;\n" +
                    "            position : absolute;\n" +
                    "            top : 0;\n" +
                    "            left : 0;\n" +
                    "            right : 0;\n" +
                    //"            bottom : 0;\n" +
                    "            text-align: left;\n" +
                    "            float:left;\n" +
                    "            padding:5px;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "    <script src=\"WEB-DATA/createProject-js.js\" type=\"text/javascript\"></script>\n" +
                    "    </head>\n" +
                    "    <body>\n" +
                    "        <div id = \"section\">\n" +
                    "            <h3>Project Name : "+ pjname +"</h3>\n"+
                    "            <form method=\"POST\">\n" +
                    "                <input type = \"hidden\" name = \"ProjectName\" value = \""+ pjname +"\">\n" +
                    "                <table>\n" +
                    catestr +
                    desInput(deslist, typelist) +
                    input +
                    "                </table>\n" +
                    "                <input type = \"submit\" name = \"checkModifyProject\" value = \"submit\">\n" +
                    "            </form>\n" +
                    "        </div>\n" +
                    "    </body>\n" +
                    "</html>";
    }
    
    
    private String inputTohidden(HttpServletRequest request, String tablename, String name){
        String s = "";
    
        for (String str : request.getParameterValues(tablename)) {
            if (!str.equals("")){
                s = s + "                <input type = \"hidden\" name = \""+ tablename +"\" value = \""+ str +"\">\n";
                s = s + "                <tr><td>"+ name +" : </td><td>"+ str +"</td></tr>\n";
            }
        }
        
        return s;
    }
    
    
    public String getCheckProjectPage (String ID, HttpServletRequest request) {
        
        String pjname = request.getParameter("ProjectName");
        
        String input = "                <input type = \"hidden\" name = \"ProjectName\" value = \""+ pjname +"\">\n"
                                + "                <input type = \"hidden\" name = \"Category\" value = \""+ request.getParameter("category") +"\">\n"
                                + "                <tr><td>Category : </td><td>"+ request.getParameter("category") +"</td></tr>\n";
        
        String[] typ = request.getParameterValues("desType");
        String[] des = request.getParameterValues("Description");
        
        for (int i = 0; i < des.length; i++) {
            if (des[i].equals("") || typ[i].equals(""))
                continue;
            
            input = input + "                <input type = \"hidden\" name = \"desType\" value = \""+ typ[i] +"\">\n"
                                        + "                <input type = \"hidden\" name = \"Description\" value = \""+ des[i] +"\">\n"
                                        + "                <tr><td>Description : </td><td>"+ typ[i] +"</td><td>"+ des[i] +"</td></tr>\n";
        }
        
        
        input = input + inputTohidden(request, CPU.TableName, CPU.ComponentName);
        input = input + inputTohidden(request, Antennas.TableName, Antennas.ComponentName);
        input = input + inputTohidden(request, Battery.TableName, Battery.ComponentName);
        input = input + inputTohidden(request, Button.TableName, Button.ComponentName);
        input = input + inputTohidden(request, Camera.TableName, Camera.ComponentName);
        input = input + inputTohidden(request, CardReader.TableName, CardReader.ComponentName);
        input = input + inputTohidden(request, Charger.TableName, Charger.ComponentName);
        input = input + inputTohidden(request, ClickPad.TableName, ClickPad.ComponentName);
        input = input + inputTohidden(request, Ethernet.TableName, Ethernet.ComponentName);
        input = input + inputTohidden(request, KBCEBC.TableName, KBCEBC.ComponentName);
        input = input + inputTohidden(request, Keyboard.TableName, Keyboard.ComponentName);
        input = input + inputTohidden(request, LcdPanel.TableName, LcdPanel.ComponentName);
        input = input + inputTohidden(request, Measurement.TableName, Measurement.ComponentName);
        input = input + inputTohidden(request, Memory.TableName, Memory.ComponentName);
        input = input + inputTohidden(request, Sensor.TableName, Sensor.ComponentName);
        input = input + inputTohidden(request, Storage.TableName, Storage.ComponentName);
        input = input + inputTohidden(request, TouchPanel.TableName, TouchPanel.ComponentName);
        input = input + inputTohidden(request, WLAN.TableName, WLAN.ComponentName);
        input = input + inputTohidden(request, WWAN.TableName, WWAN.ComponentName);
        input = input + inputTohidden(request, Graphic.TableName, Graphic.ComponentName);
        input = input + inputTohidden(request, AudioCodec.TableName, AudioCodec.ComponentName);
        input = input + inputTohidden(request, PanelInterfaceBridge.TableName, PanelInterfaceBridge.ComponentName);
        input = input + inputTohidden(request, ExternalStorageCard.TableName, ExternalStorageCard.ComponentName);
        input = input + inputTohidden(request, ODD.TableName, ODD.ComponentName);
        input = input + inputTohidden(request, Speaker.TableName, Speaker.ComponentName);
        input = input + inputTohidden(request, Mic.TableName, Mic.ComponentName);
        input = input + inputTohidden(request, IoPort.TableName, IoPort.ComponentName);
        input = input + inputTohidden(request, OS.TableName, OS.ComponentName);
        

        return "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "    <head>\n" +
                    "        <title>TODO supply a title</title>\n" +
                    "        <meta charset=\"UTF-8\">\n" +
                    "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <style>\n" +
                    "        #section {\n" +
                    //"            background-color:#F0F8FF;\n" +
                    "            position : absolute;\n" +
                    "            top : 0;\n" +
                    "            left : 0;\n" +
                    "            right : 0;\n" +
                    //"            bottom : 0;\n" +
                    "            text-align: left;\n" +
                    "            float:left;\n" +
                    "            padding:5px;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "    </head>\n" +
                    "    <body>\n" +
                    "        <div id = \"section\">\n" +
                    "            <h3>是否建立 "+ pjname +"</h3>\n"+
                    "            <form method=\"POST\">\n" +
                    "                <table>\n" +
                    input +
                    "                </table>\n" +
                    "                <input type = \"submit\" name = \"ModifyProject\" value = \"submit\">\n" +
                    "            </form>\n" +
                    "        </div>\n" +
                    "    </body>\n" +
                    "</html>";
    }
    
    public void DeleteProjectDB(Statement stm, String name, String ID) throws SQLException{
       
        MySQLSyntax.deleteDB(stm, db2.project_antennas_Table.TABLE_NAME, db2.project_antennas_Table.ProjectName, name);
        MySQLSyntax.deleteDB(stm, db2.project_battery_Table.TABLE_NAME, db2.project_battery_Table.ProjectName, name);
        MySQLSyntax.deleteDB(stm, db2.project_button_Table.TABLE_NAME, db2.project_button_Table.ProjectName, name);
        MySQLSyntax.deleteDB(stm, db2.project_camera_Table.TABLE_NAME, db2.project_camera_Table.ProjectName, name);
        MySQLSyntax.deleteDB(stm, db2.project_card_reader_Table.TABLE_NAME, db2.project_card_reader_Table.ProjectName, name);
        MySQLSyntax.deleteDB(stm, db2.project_charger_Table.TABLE_NAME, db2.project_charger_Table.ProjectName, name);
        MySQLSyntax.deleteDB(stm, db2.project_click_pad_Table.TABLE_NAME, db2.project_click_pad_Table.ProjectName, name);
        MySQLSyntax.deleteDB(stm, db2.project_cpu_Table.TABLE_NAME, db2.project_cpu_Table.ProjectName, name);
        MySQLSyntax.deleteDB(stm, db2.project_ethernet_Table.TABLE_NAME, db2.project_ethernet_Table.ProjectName, name);
        MySQLSyntax.deleteDB(stm, db2.project_kbc_ebc_Table.TABLE_NAME, db2.project_kbc_ebc_Table.ProjectName, name);
        MySQLSyntax.deleteDB(stm, db2.project_keyboard_Table.TABLE_NAME, db2.project_keyboard_Table.ProjectName, name);
        MySQLSyntax.deleteDB(stm, db2.project_lcd_panel_Table.TABLE_NAME, db2.project_lcd_panel_Table.ProjectName, name);
        MySQLSyntax.deleteDB(stm, db2.project_measurement_Table.TABLE_NAME, db2.project_measurement_Table.ProjectName, name);
        MySQLSyntax.deleteDB(stm, db2.project_memory_Table.TABLE_NAME, db2.project_memory_Table.ProjectName, name);
        MySQLSyntax.deleteDB(stm, db2.project_sensor_Table.TABLE_NAME, db2.project_sensor_Table.ProjectName, name);
        MySQLSyntax.deleteDB(stm, db2.project_storage_Table.TABLE_NAME, db2.project_storage_Table.ProjectName, name);
        MySQLSyntax.deleteDB(stm, db2.project_touch_panel_Table.TABLE_NAME, db2.project_touch_panel_Table.ProjectName, name);
        MySQLSyntax.deleteDB(stm, db2.project_wlan_Table.TABLE_NAME, db2.project_wlan_Table.ProjectName, name);
        MySQLSyntax.deleteDB(stm, db2.project_wwan_Table.TABLE_NAME, db2.project_wwan_Table.ProjectName, name);
        MySQLSyntax.deleteDB(stm, db2.project_graphic_Table.TABLE_NAME, db2.project_graphic_Table.ProjectName, name);
        MySQLSyntax.deleteDB(stm, db2.project_audio_codec_Table.TABLE_NAME, db2.project_audio_codec_Table.ProjectName, name);
        MySQLSyntax.deleteDB(stm, db2.project_panel_interface_bridge_Table.TABLE_NAME, db2.project_panel_interface_bridge_Table.ProjectName, name);
        MySQLSyntax.deleteDB(stm, db2.project_external_storage_card_Table.TABLE_NAME, db2.project_external_storage_card_Table.ProjectName, name);
        MySQLSyntax.deleteDB(stm, db2.project_odd_Table.TABLE_NAME, db2.project_odd_Table.ProjectName, name);
        MySQLSyntax.deleteDB(stm, db2.project_speaker_Table.TABLE_NAME, db2.project_speaker_Table.ProjectName, name);
        MySQLSyntax.deleteDB(stm, db2.project_mic_Table.TABLE_NAME, db2.project_mic_Table.ProjectName, name);
        MySQLSyntax.deleteDB(stm, db2.project_io_port_Table.TABLE_NAME, db2.project_io_port_Table.ProjectName, name);
        MySQLSyntax.deleteDB(stm, db2.project_os_Table.TABLE_NAME, db2.project_os_Table.ProjectName, name);
        
        MySQLSyntax.deleteDB(stm, db2.description_Table.TABLE_NAME, db2.description_Table.ProjectName, name);
        
        MySQLSyntax.deleteDB(stm, db2.project_Table.TABLE_NAME, db2.project_Table.ProjectName, name);
        
        String log = ID + " delete project " + name;
        MySQLSyntax.inserLog(stm, log);
        
    }
    
    private void InsertProjectDB (Statement stm, String ID, String table, String pjname, String[] values) throws SQLException {
        if (values == null)
            return;
        
        List<String> list;
        String log;
        
        for (String str : values) {
            list = new ArrayList<String>();
            list.add(pjname);
            list.add(str);
            
            MySQLSyntax.insertDB(stm, table, list);
            
            log = ID + " add " + str + " in project " + pjname;
            MySQLSyntax.inserLog(stm, log);
        }
        
    }
    
    private void CreateProjectDB (String ID, HttpServletRequest request, Statement stm) throws SQLException {
        String pjname = request.getParameter("ProjectName");
        String type = request.getParameter("Category");
        
        
        List<String> list = new ArrayList<String>();
        list.add(type);
        list.add(pjname);
        MySQLSyntax.insertDB(stm, db2.project_Table.TABLE_NAME, list);
        
        String log = ID + " add project " + pjname + " ,category is " + type;
        MySQLSyntax.inserLog(stm, log);
        
        String[] typ = request.getParameterValues("desType");
        String[] des = request.getParameterValues("Description");
        
        for (int i = 0; i < des.length; i++) {
            if (des[i].equals("") || typ[i].equals(""))
                continue;
            
            list = new ArrayList<String>();
            list.add(pjname);
            list.add(typ[i]);
            list.add(des[i]);
            
            MySQLSyntax.insertDB(stm, db2.description_Table.TABLE_NAME, list);
            log = ID + " add description in " + pjname + " ,type is " + typ[i] + ", description is " + des[i];
            MySQLSyntax.inserLog(stm, log);
        }
        
        
        
        InsertProjectDB(stm, ID, CPU.ProjectTableName, pjname, request.getParameterValues(CPU.TableName));
        InsertProjectDB(stm, ID, Antennas.ProjectTableName, pjname, request.getParameterValues(Antennas.TableName));
        InsertProjectDB(stm, ID, Battery.ProjectTableName, pjname, request.getParameterValues(Battery.TableName));
        InsertProjectDB(stm, ID, Button.ProjectTableName, pjname, request.getParameterValues(Button.TableName));
        InsertProjectDB(stm, ID, Camera.ProjectTableName, pjname, request.getParameterValues(Camera.TableName));
        InsertProjectDB(stm, ID, CardReader.ProjectTableName, pjname, request.getParameterValues(CardReader.TableName));
        InsertProjectDB(stm, ID, Charger.ProjectTableName, pjname, request.getParameterValues(Charger.TableName));
        InsertProjectDB(stm, ID, ClickPad.ProjectTableName, pjname, request.getParameterValues(ClickPad.TableName));
        InsertProjectDB(stm, ID, Ethernet.ProjectTableName, pjname, request.getParameterValues(Ethernet.TableName));
        InsertProjectDB(stm, ID, KBCEBC.ProjectTableName, pjname, request.getParameterValues(KBCEBC.TableName));
        InsertProjectDB(stm, ID, Keyboard.ProjectTableName, pjname, request.getParameterValues(Keyboard.TableName));
        InsertProjectDB(stm, ID, LcdPanel.ProjectTableName, pjname, request.getParameterValues(LcdPanel.TableName));
        InsertProjectDB(stm, ID, Measurement.ProjectTableName, pjname, request.getParameterValues(Measurement.TableName));
        InsertProjectDB(stm, ID, Memory.ProjectTableName, pjname, request.getParameterValues(Memory.TableName));
        InsertProjectDB(stm, ID, Sensor.ProjectTableName, pjname, request.getParameterValues(Sensor.TableName));
        InsertProjectDB(stm, ID, Storage.ProjectTableName, pjname, request.getParameterValues(Storage.TableName));
        InsertProjectDB(stm, ID, TouchPanel.ProjectTableName, pjname, request.getParameterValues(TouchPanel.TableName));
        InsertProjectDB(stm, ID, WWAN.ProjectTableName, pjname, request.getParameterValues(WWAN.TableName));
        InsertProjectDB(stm, ID, WLAN.ProjectTableName, pjname, request.getParameterValues(WLAN.TableName));
        InsertProjectDB(stm, ID, Graphic.ProjectTableName, pjname, request.getParameterValues(Graphic.TableName));
        InsertProjectDB(stm, ID, AudioCodec.ProjectTableName, pjname, request.getParameterValues(AudioCodec.TableName));
        InsertProjectDB(stm, ID, PanelInterfaceBridge.ProjectTableName, pjname, request.getParameterValues(PanelInterfaceBridge.TableName));
        InsertProjectDB(stm, ID, ExternalStorageCard.ProjectTableName, pjname, request.getParameterValues(ExternalStorageCard.TableName));
        InsertProjectDB(stm, ID, ODD.ProjectTableName, pjname, request.getParameterValues(ODD.TableName));
        InsertProjectDB(stm, ID, Speaker.ProjectTableName, pjname, request.getParameterValues(Speaker.TableName));
        InsertProjectDB(stm, ID, Mic.ProjectTableName, pjname, request.getParameterValues(Mic.TableName));
        InsertProjectDB(stm, ID, IoPort.ProjectTableName, pjname, request.getParameterValues(IoPort.TableName));
        InsertProjectDB(stm, ID, OS.ProjectTableName, pjname, request.getParameterValues(OS.TableName));
        
    }
    
    public String CreatProject (String ID, HttpServletRequest request) {
        String pjname = request.getParameter("ProjectName");
        
        try( Connection conn = DriverManager.getConnection(DB2url, DB2user, DB2password) ){
            Statement stm = conn.createStatement();
            ResultSet result = MySQLSyntax.getResultSet(stm, db2.project_Table.TABLE_NAME, db2.project_Table.ProjectName, pjname);
            
            if (result.next()) {
                DeleteProjectDB(stm, pjname, ID);
            }
            
            CreateProjectDB(ID, request, stm);
            
        }catch(SQLException ex){
            logger.log(Level.INFO, "In SQLException (CreatProject) : " + ex.toString());
            ex.printStackTrace();
            return getSearchPage("資料庫連線錯誤 ! ");
        }
        
        
        return getSearchPage("成功建立Project ! ");
    }
    
    public String getSearchProjectPage () {
        
        List<String> cateopt = new ArrayList<String>();
        Map<String, String> pjopt = new TreeMap<String, String>();
        
        try( Connection conn = DriverManager.getConnection(DB2url, DB2user, DB2password) ){
            Statement stm = conn.createStatement();
           
            ResultSet result = MySQLSyntax.getResultSet(stm, db2.category_Table.TABLE_NAME);
            while (result.next()) {
                cateopt.add(result.getString(db2.category_Table.Category));
            }
            
            result = MySQLSyntax.getResultSet(stm, db2.project_Table.TABLE_NAME);
            while (result.next()) {
                pjopt.put(result.getString(db2.project_Table.ProjectName), result.getString(db2.project_Table.Category));
            }
            
        }catch(SQLException ex){
            logger.log(Level.INFO, "In SQLException (getSearchProjectPage) : " + ex.toString());
            ex.printStackTrace();
            return getSearchPage("資料庫連線錯誤 ! ");
        }
        
        String str = "";
        for (String s : cateopt) {
                str = str + "                    <option value = \""+ s.replace(" ", "_") +"\">"+ s +"</option>\n";
        }
        String catestr = "                Category : <select id = \"sel\" onchange = \"changesel(\'sel\')\" name = \"category\">\n"
                                + "                    <option value = \"all\">ALL</option>\n"
                                + str 
                                + "                     </select>\n";
        
        str ="";
        for (Entry<String, String> e : pjopt.entrySet()) {
                str = str + "                    <option class = \"opt "+ e.getValue().replace(" ", "_") +"\" value = \""+ e.getKey() +"\">"+ e.getKey() +"</option>\n";
        }
        String pjstr = "                Project Name : <select name = \"ProjectName\">\n"
                                + str 
                                + "                     </select>\n";
        
        
        
        
        return "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "    <head>\n" +
                    "        <title>TODO supply a title</title>\n" +
                    "        <meta charset=\"UTF-8\">\n" +
                    "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <style>\n" +
                    "        #section {\n" +
                    //"            background-color:#F0F8FF;\n" +
                    "            position : absolute;\n" +
                    "            top : 0;\n" +
                    "            left : 0;\n" +
                    "            right : 0;\n" +
                    //"            bottom : 0;\n" +
                    "            text-align: left;\n" +
                    "            float:left;\n" +
                    "            padding:5px;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "    <script src=\"WEB-DATA/jquery-2.2.3.min.js\" type=\"text/javascript\"></script>\n" +
                    "    <script src=\"WEB-DATA/pjsearch-js.js\" type=\"text/javascript\"></script>\n" +
                    "    </head>\n" +
                    "    <body>\n" +
                    "        <div id = \"section\">\n" +
                    "            <form method=\"POST\">\n" +
                    catestr + pjstr +
                    "                <input type = \"submit\" name = \"SearchProject\" value = \"submit\">\n" +
                    "            </form>\n" +
                    "        </div>\n" +
                    "    </body>\n" +
                    "</html>";
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
    
    public String getSearchProjectResultPage (String ID, HttpServletRequest request) {
        String pjname = request.getParameter("ProjectName");
        String cate = null;
        
        Map<String, List<String>> map = new TreeMap<String, List<String>>();
        List<String> typ = new ArrayList<String>();
        List<String> des = new ArrayList<String>();
        
        
        try( Connection conn = DriverManager.getConnection(DB2url, DB2user, DB2password) ){
            Statement stm = conn.createStatement();
           
            ResultSet result = MySQLSyntax.getResultSet(stm, db2.project_Table.TABLE_NAME, db2.project_Table.ProjectName, pjname);
            if ( result.next() ) {
                cate = result.getString(db2.project_Table.Category);
            }
            
            result = MySQLSyntax.getResultSet(stm, db2.description_Table.TABLE_NAME, db2.description_Table.ProjectName, pjname);
            while (result.next()) {
                typ.add(result.getString(db2.description_Table.Type));
                des.add(result.getString(db2.description_Table.Description));
            }
            
            
            ProjectResultMap(stm, map, pjname, Antennas.ComponentName, db2.project_antennas_Table.TABLE_NAME, Antennas.MainKey);
            ProjectResultMap(stm, map, pjname, Battery.ComponentName, db2.project_battery_Table.TABLE_NAME, Battery.MainKey);
            ProjectResultMap(stm, map, pjname, Button.ComponentName, db2.project_button_Table.TABLE_NAME, Button.MainKey);
            ProjectResultMap(stm, map, pjname, Camera.ComponentName, db2.project_camera_Table.TABLE_NAME, Camera.MainKey);
            ProjectResultMap(stm, map, pjname, CardReader.ComponentName, db2.project_card_reader_Table.TABLE_NAME, CardReader.MainKey);
            ProjectResultMap(stm, map, pjname, Charger.ComponentName, db2.project_charger_Table.TABLE_NAME, Charger.MainKey);
            ProjectResultMap(stm, map, pjname, ClickPad.ComponentName, db2.project_click_pad_Table.TABLE_NAME, ClickPad.MainKey);
            ProjectResultMap(stm, map, pjname, CPU.ComponentName, db2.project_cpu_Table.TABLE_NAME, CPU.MainKey);
            ProjectResultMap(stm, map, pjname, Ethernet.ComponentName, db2.project_ethernet_Table.TABLE_NAME, Ethernet.MainKey);
            ProjectResultMap(stm, map, pjname, KBCEBC.ComponentName, db2.project_kbc_ebc_Table.TABLE_NAME, KBCEBC.MainKey);
            ProjectResultMap(stm, map, pjname, Keyboard.ComponentName, db2.project_keyboard_Table.TABLE_NAME, Keyboard.MainKey);
            ProjectResultMap(stm, map, pjname, LcdPanel.ComponentName, db2.project_lcd_panel_Table.TABLE_NAME, LcdPanel.MainKey);
            ProjectResultMap(stm, map, pjname, Measurement.ComponentName, db2.project_measurement_Table.TABLE_NAME, Measurement.MainKey);
            ProjectResultMap(stm, map, pjname, Memory.ComponentName, db2.project_memory_Table.TABLE_NAME, Memory.MainKey);
            ProjectResultMap(stm, map, pjname, Sensor.ComponentName, db2.project_sensor_Table.TABLE_NAME, Sensor.MainKey);
            ProjectResultMap(stm, map, pjname, Storage.ComponentName, db2.project_storage_Table.TABLE_NAME, Storage.MainKey);
            ProjectResultMap(stm, map, pjname, TouchPanel.ComponentName, db2.project_touch_panel_Table.TABLE_NAME, TouchPanel.MainKey);
            ProjectResultMap(stm, map, pjname, WLAN.ComponentName, db2.project_wlan_Table.TABLE_NAME, WLAN.MainKey);
            ProjectResultMap(stm, map, pjname, WWAN.ComponentName, db2.project_wwan_Table.TABLE_NAME, WWAN.MainKey);
            ProjectResultMap(stm, map, pjname, Graphic.ComponentName, db2.project_graphic_Table.TABLE_NAME, Graphic.MainKey);
            ProjectResultMap(stm, map, pjname, AudioCodec.ComponentName, db2.project_audio_codec_Table.TABLE_NAME, AudioCodec.MainKey);
            ProjectResultMap(stm, map, pjname, PanelInterfaceBridge.ComponentName, db2.project_panel_interface_bridge_Table.TABLE_NAME, PanelInterfaceBridge.MainKey);
            ProjectResultMap(stm, map, pjname, ExternalStorageCard.ComponentName, db2.project_external_storage_card_Table.TABLE_NAME, ExternalStorageCard.MainKey);
            ProjectResultMap(stm, map, pjname, ODD.ComponentName, db2.project_odd_Table.TABLE_NAME, ODD.MainKey);
            ProjectResultMap(stm, map, pjname, Speaker.ComponentName, db2.project_speaker_Table.TABLE_NAME, Speaker.MainKey);
            ProjectResultMap(stm, map, pjname, Mic.ComponentName, db2.project_mic_Table.TABLE_NAME, Mic.MainKey);
            ProjectResultMap(stm, map, pjname, IoPort.ComponentName, db2.project_io_port_Table.TABLE_NAME, IoPort.MainKey);
            ProjectResultMap(stm, map, pjname, OS.ComponentName, db2.project_os_Table.TABLE_NAME, OS.MainKey);
            
        }catch(SQLException ex){
            logger.log(Level.INFO, "In SQLException (getSearchProjectResultPage) : " + ex.toString());
            ex.printStackTrace();
            return getSearchPage("資料庫連線錯誤 ! ");
        }
        
        String str = "            <table border=\"1\">\n"
                        + "                <caption>--- Description ---</caption>\n"
                        + "                <tr><td>Type</td><td>Description</td></tr>\n";
        for (int i = 0; i < typ.size() && i < des.size(); i++) {
            str = str + "                <tr><td>"+ typ.get(i) +"</td><td>"+ des.get(i) +"</td></tr>\n";
        }
        str = str + "            </table>\n";
        
        for (Entry<String, List<String>> entry : map.entrySet()) {
            str = str + "            <br>\n" 
                        + "            <table border=\"1\">\n"
                        + "                <tr><td ROWSPAN = "+ entry.getValue().size()+1 +">"+ entry.getKey() +"</td></tr>\n";
            for (String s : entry.getValue()) {
                str = str + "                <tr><td>"+ s +"</td></tr>\n";
            }
            str = str + "            </table>\n";
        }
        
        return "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "    <head>\n" +
                    "        <title>TODO supply a title</title>\n" +
                    "        <meta charset=\"UTF-8\">\n" +
                    "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <style>\n" +
                    "        #section {\n" +
                    //"            background-color:#F0F8FF;\n" +
                    "            position : absolute;\n" +
                    "            top : 0;\n" +
                    "            left : 0;\n" +
                    "            right : 0;\n" +
                    //"            bottom : 0;\n" +
                    "            text-align: left;\n" +
                    "            float:left;\n" +
                    "            padding:5px;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "    </head>\n" +
                    "    <body>\n" +
                    "        <div id = \"section\">\n" +
                    "            <h3>Project Name : "+ pjname +"</h3>\n" +
                    "            <h3>Category : "+ cate +"</h3>\n" +
                    str +
                    "            <form method=\"POST\">\n" +
                    "                <input type = \"hidden\" name = \"ProjectName\" value = \""+ pjname +"\">\n" +
                    "                <input type = \"submit\" name = \"CreateProject\" value = \"修改\">\n" +
                    "            </form>\n" +
                    "            <form method=\"POST\">\n" +
                    "                <input type = \"hidden\" name = \"ProjectName\" value = \""+ pjname +"\">\n" +
                    "                <input type = \"submit\" name = \"CheckDeleteProject\" value = \"刪除\">\n" +
                    "            </form>\n" +
                    "            <form method=\"POST\" action = \"download\">\n" +
                    "                <input type = \"hidden\" name = \"ProjectName\" value = \""+ pjname +"\">\n" +
                    "                <input type = \"submit\" name = \"getProject\" value = \"下載Excel\">\n" +
                    "            </form>\n" +
                    "        </div>\n" +
                    "    </body>\n" +
                    "</html>";
    }
    
    public String getCheckDeleteProject (String ID, HttpServletRequest request) {
        String pjname = request.getParameter("ProjectName");
        
        
        return "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "    <head>\n" +
                    "        <title>TODO supply a title</title>\n" +
                    "        <meta charset=\"UTF-8\">\n" +
                    "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <style>\n" +
                    "        #section {\n" +
                    //"            background-color:#F0F8FF;\n" +
                    "            position : absolute;\n" +
                    "            top : 0;\n" +
                    "            left : 0;\n" +
                    "            right : 0;\n" +
                    //"            bottom : 0;\n" +
                    "            text-align: left;\n" +
                    "            float:left;\n" +
                    "            padding:5px;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "    </head>\n" +
                    "    <body>\n" +
                    "        <div id = \"section\">\n" +
                    "            <h3>是否刪除 : "+ pjname +"</h3>\n" +
                    "            <form method=\"POST\">\n" +
                    "                <input type = \"hidden\" name = \"ProjectName\" value = \""+ pjname +"\">\n" +
                    "                <input type = \"submit\" name = \"DeleteProject\" value = \"刪除\">\n" +
                    "            </form>\n" +
                    "        </div>\n" +
                    "    </body>\n" +
                    "</html>";
    }
    
    public String DeleteProject (String ID, HttpServletRequest request) {
        String pjname = request.getParameter("ProjectName");
        
        try( Connection conn = DriverManager.getConnection(DB2url, DB2user, DB2password) ){
            Statement stm = conn.createStatement();
            
            DeleteProjectDB(stm, pjname, ID);
        }catch(SQLException ex){
            logger.log(Level.INFO, "In SQLException (DeleteProject) : " + ex.toString());
            ex.printStackTrace();
            return getSearchPage("資料庫連線錯誤 ! ");
        }
        
        return getSearchPage("刪除成功 ! ");
    }
    
        
    
}
