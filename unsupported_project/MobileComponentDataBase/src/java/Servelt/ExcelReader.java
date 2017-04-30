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
import static DataBaseInfo.DataBaseConn.DBpassword;
import static DataBaseInfo.DataBaseConn.DBurl;
import static DataBaseInfo.DataBaseConn.DBuser;
import DataBaseInfo.DataBaseName.db;
import DataBaseInfo.DataBaseConn;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
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
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Jasper-Yen
 */
public class ExcelReader implements DataBaseConn {
    static final String cpuSheetName = "CPU";
    static final int cpuSheetColumn = 7;
    Logger logger = Logger.getLogger(ExcelReader.class.getName());
    
    List sheetList = new ArrayList<ComponentSet>();

    private void readSheet (XSSFSheet sheet, int Max_Col, ComponentSet comSet) throws Exception{
        String data = null;
        Component com = null;
        
        for (int i = 1; i <= sheet.getLastRowNum(); i++){
            com = comSet.newComponent();
            for (int j = 0; j < Max_Col; j++) {
                Cell cell = sheet.getRow(i).getCell(j);
                if (cell != null){
                    data = cellToString(cell);
                }
                else{
                    data = "";
                }
                System.out.printf("| %2d-%2d%30s |", i, j, data);
                com.setComponentByColumn(j, data);
            }
            System.out.println();
            comSet.addComponent(com);
        }
        return ;
    }
    
    public String ComponentsToHTML(){
        registerJDBC();
        String str = "";
        for (Object obj : sheetList) {
            ComponentSet comSet = (ComponentSet)obj;
            str = str + comSet.getComponentSetTable();
        }

        return "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "    <head>\n" +
                    "        <title>TODO supply a title</title>\n" +
                    "        <meta charset=\"UTF-8\">\n" +
                    "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <style>\n" +
                    "        #section {\n" +
                    "            background-color:#F0F8FF;\n" +
                    "            position : absolute;\n" +
                    "            top : 0;\n" +
                    "            left : 0;\n" +
                    "            right : 0;\n" +
                    "            bottom : 0;\n" +
                    "            text-align: left;\n" +
                    "            float:left;\n" +
                    "            padding:5px;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "    </head>\n" +
                    "    <body>\n" +
                    "        <div id = \"section\">\n" +
                    "        <form action = \"main\" method = \"POST\">\n" +
                    str +
                    "            <input type=\"submit\" name=\"uploadData\" value=\"submit\">\n" +
                    "        </form>\n" +
                    "        </div>\n" +
                    "    </body>\n" +
                    "</html>";
    }
    
    public ExcelReader(File excelFile) {
        
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(excelFile);

            ComponentSet comSet = null;
            
            if (workbook.getSheet(CPU.ComponentName) != null){
                comSet = new CpuSet();
                readSheet(workbook.getSheet(CPU.ComponentName), CPU.AllKeyList.size(), comSet);
                sheetList.add(comSet);
            }
            
            if (workbook.getSheet(Antennas.ComponentName) != null){
                comSet = new AntennasSet();
                readSheet(workbook.getSheet(Antennas.ComponentName), Antennas.AllKeyList.size(), comSet);
                sheetList.add(comSet);
            }
            
            if (workbook.getSheet(Button.ComponentName) != null){
                comSet = new ButtonSet();
                readSheet(workbook.getSheet(Button.ComponentName), Button.AllKeyList.size(), comSet);
                sheetList.add(comSet);
            }
            
            if (workbook.getSheet(Camera.ComponentName) != null){
                comSet = new CameraSet();
                readSheet(workbook.getSheet(Camera.ComponentName), Camera.AllKeyList.size(), comSet);
                sheetList.add(comSet);
            }
            
            if (workbook.getSheet(CardReader.ComponentName) != null){
                comSet = new CardReaderSet();
                readSheet(workbook.getSheet(CardReader.ComponentName), CardReader.AllKeyList.size(), comSet);
                sheetList.add(comSet);
            }
            
            if (workbook.getSheet(ClickPad.ComponentName) != null){
                comSet = new ClickPadSet();
                readSheet(workbook.getSheet(ClickPad.ComponentName), ClickPad.AllKeyList.size(), comSet);
                sheetList.add(comSet);
            }
            
            if (workbook.getSheet(Ethernet.ComponentName) != null){
                comSet = new EthernetSet();
                readSheet(workbook.getSheet(Ethernet.ComponentName), Ethernet.AllKeyList.size(), comSet);
                sheetList.add(comSet);
            }
            
            if (workbook.getSheet(KBCEBC.ComponentName) != null){
                comSet = new KbcEbcSet();
                readSheet(workbook.getSheet(KBCEBC.ComponentName), KBCEBC.AllKeyList.size(), comSet);
                sheetList.add(comSet);
            }
            
            if (workbook.getSheet(LcdPanel.ComponentName) != null){
                comSet = new LcdPanelSet();
                readSheet(workbook.getSheet(LcdPanel.ComponentName), LcdPanel.AllKeyList.size(), comSet);
                sheetList.add(comSet);
            }
            
            if (workbook.getSheet(Memory.ComponentName) != null){
                comSet = new MemorySet();
                readSheet(workbook.getSheet(Memory.ComponentName), Memory.AllKeyList.size(), comSet);
                sheetList.add(comSet);
            }
            
            if (workbook.getSheet(Sensor.ComponentName) != null){
                comSet = new SensorSet();
                readSheet(workbook.getSheet(Sensor.ComponentName), Sensor.AllKeyList.size(), comSet);
                sheetList.add(comSet);
            }
            
            if (workbook.getSheet(Storage.ComponentName) != null){
                comSet = new StorageSet();
                readSheet(workbook.getSheet(Storage.ComponentName), Storage.AllKeyList.size(), comSet);
                sheetList.add(comSet);
            }
            
            if (workbook.getSheet(TouchPanel.ComponentName) != null){
                comSet = new TouchPanelSet();
                readSheet(workbook.getSheet(TouchPanel.ComponentName), TouchPanel.AllKeyList.size(), comSet);
                sheetList.add(comSet);
            }
            
            if (workbook.getSheet(WLAN.ComponentName) != null){
                comSet = new WlanSet();
                readSheet(workbook.getSheet(WLAN.ComponentName), WLAN.AllKeyList.size(), comSet);
                sheetList.add(comSet);
            }
            
            if (workbook.getSheet(WWAN.ComponentName) != null){
                comSet = new WwanSet();
                readSheet(workbook.getSheet(WWAN.ComponentName), WWAN.AllKeyList.size(), comSet);
                sheetList.add(comSet);
            }
            
            if (workbook.getSheet(Keyboard.ComponentName) != null){
                comSet = new KeyboardSet();
                readSheet(workbook.getSheet(Keyboard.ComponentName), Keyboard.AllKeyList.size(), comSet);
                sheetList.add(comSet);
            }
            
            if (workbook.getSheet(Battery.ComponentName) != null){
                comSet = new BatterySet();
                readSheet(workbook.getSheet(Battery.ComponentName), Battery.AllKeyList.size(), comSet);
                sheetList.add(comSet);
            }
            
            if (workbook.getSheet(Charger.ComponentName) != null){
                comSet = new ChargerSet();
                readSheet(workbook.getSheet(Charger.ComponentName), Charger.AllKeyList.size(), comSet);
                sheetList.add(comSet);
            }
            
            if (workbook.getSheet(Measurement.ComponentName) != null){
                comSet = new MeasurementSet();
                readSheet(workbook.getSheet(Measurement.ComponentName), Measurement.AllKeyList.size(), comSet);
                sheetList.add(comSet);
            }
            
            if (workbook.getSheet(Graphic.ComponentName) != null){
                comSet = new GraphicSet();
                readSheet(workbook.getSheet(Graphic.ComponentName), Graphic.AllKeyList.size(), comSet);
                sheetList.add(comSet);
            }
            
            if (workbook.getSheet(AudioCodec.ComponentName) != null){
                comSet = new AudioCodecSet();
                readSheet(workbook.getSheet(AudioCodec.ComponentName), AudioCodec.AllKeyList.size(), comSet);
                sheetList.add(comSet);
            }
            
            if (workbook.getSheet(PanelInterfaceBridge.ComponentName) != null){
                comSet = new PanelInterfaceBridgeSet();
                readSheet(workbook.getSheet(PanelInterfaceBridge.ComponentName), PanelInterfaceBridge.AllKeyList.size(), comSet);
                sheetList.add(comSet);
            }
            
            if (workbook.getSheet(ExternalStorageCard.ComponentName) != null){
                comSet = new ExternalStorageCardSet();
                readSheet(workbook.getSheet(ExternalStorageCard.ComponentName), ExternalStorageCard.AllKeyList.size(), comSet);
                sheetList.add(comSet);
            }
            
            if (workbook.getSheet(ODD.ComponentName) != null){
                comSet = new ODDSet();
                readSheet(workbook.getSheet(ODD.ComponentName), ODD.AllKeyList.size(), comSet);
                sheetList.add(comSet);
            }
            
            if (workbook.getSheet(Speaker.ComponentName) != null){
                comSet = new SpeakerSet();
                readSheet(workbook.getSheet(Speaker.ComponentName), Speaker.AllKeyList.size(), comSet);
                sheetList.add(comSet);
            }
            
            if (workbook.getSheet(Mic.ComponentName) != null){
                comSet = new MicSet();
                readSheet(workbook.getSheet(Mic.ComponentName), Mic.AllKeyList.size(), comSet);
                sheetList.add(comSet);
            }
            
            if (workbook.getSheet(IoPort.ComponentName) != null){
                comSet = new IoPortSet();
                readSheet(workbook.getSheet(IoPort.ComponentName), IoPort.AllKeyList.size(), comSet);
                sheetList.add(comSet);
            }
            
            if (workbook.getSheet(OS.ComponentName) != null){
                comSet = new OSSet();
                readSheet(workbook.getSheet(OS.ComponentName), OS.AllKeyList.size(), comSet);
                sheetList.add(comSet);
            }
            
            for (Object set : sheetList) {
                for (Object obj : ((ComponentSet)set).getComList()){
                    logger.log(Level.INFO, "\n" + ((Component)obj).ComponentToString());
                }
            }
            
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    
    private String cellToString (Cell cell) throws Exception{
        String data = null;
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                    data = cell.getRichStringCellValue().getString();
                    break;
            case Cell.CELL_TYPE_NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        data = cell.getDateCellValue().toString();
                    } else {
                        data = String.valueOf(cell.getNumericCellValue());
                        while (data.endsWith("0") && data.contains(".")){
                            data = data.substring(0, data.length()-1);
                        }
                        if (data.endsWith("."))
                            data = data.substring(0, data.length()-1);
                    }
                    break;
            case Cell.CELL_TYPE_BOOLEAN:
                    data = String.valueOf(cell.getBooleanCellValue());
                    break;
            case Cell.CELL_TYPE_FORMULA:
                    data = cell.getCellFormula();
                    break;
            case Cell.CELL_TYPE_BLANK:
                    data = "";
                    break;
            case Cell.CELL_TYPE_ERROR:
                throw new Exception("CELL_TYPE_ERROR");
            }
        return data;
    }

}
