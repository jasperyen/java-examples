/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cudaServer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Jasper
 */
public class initServer {
    //private static final String DOWNLOAD_PATH = "C:\\workspace\\Mining Space\\mining-config\\";
    //private static final String CONFIG_PATH = DOWNLOAD_PATH + "server_config.json";
    
    private static final String DOWNLOAD_PATH = "/home/admin/mining-config/";
    private static final String CONFIG_PATH = DOWNLOAD_PATH + "server_config.json";
    
    
    private static final List<String> ipList = new ArrayList<>();
    private static final List<String> depList = new ArrayList<>();
    private static final Map<Integer, String> stateMap = new TreeMap<>();
    private static final Map<String, String> fileMap = new TreeMap<>();
    
    static {
        JSONObject jsonFile = reloadConfig();
        
        JSONArray jlist = jsonFile.getJSONArray("ipList");
        
        for (int i = 0; i < jlist.length(); i++) {
            JSONObject index = jlist.getJSONObject(i);
            
            String subnet = index.getString("subnet");
            int start = index.getInt("start");
            int end = index.getInt("end");
            
            for (int j = start; j <= end; j++)
                ipList.add(subnet + "." + j);
        }
        
        jlist = jsonFile.getJSONArray("DeployerList");
        for (int i = 0; i < jlist.length(); i++) {
            depList.add(jlist.getString(i));
        }
    }
    
    private static JSONObject reloadConfig() {
        
        StringBuilder builder;
        try ( InputStreamReader isr = new InputStreamReader( new FileInputStream(CONFIG_PATH) ) ;
                BufferedReader reader = new BufferedReader(isr);) {
            String line;
            builder = new StringBuilder();

            while ( (line = reader.readLine()) != null ) {
                builder.append(line);
                builder.append("\n");
            }
        } catch (IOException ex) {
            System.out.println("In IOException : " + ex);
            return null;
        }

        return new JSONObject(builder.toString());
    }
    
    private static boolean reloadFileMap() {
        JSONObject jsonFile = reloadConfig();
        
        if (jsonFile == null)
            return false;
        
        JSONArray jlist = jsonFile.getJSONArray("FileList");
        fileMap.clear();
        
        for (int i = 0 ; i < jlist.length(); i++) {
            JSONObject index = jlist.getJSONObject(i);
            fileMap.put(index.getString("param"), index.getString("name"));
        }
        return true;
    }
    
    private static boolean reloadStateMap() {
        
        JSONObject jsonFile = reloadConfig();
        
        if (jsonFile == null)
            return false;
        
        JSONArray jlist = jsonFile.getJSONArray("StateList");
        stateMap.clear();
        
        for (int i = 0 ; i < jlist.length(); i++) {
            JSONObject index = jlist.getJSONObject(i);
            stateMap.put(index.getInt("code"), index.getString("detail"));
        }
        
        return true;
    }
    
    public static Map<Integer, String> getStateMap() {
        reloadStateMap();
        return stateMap;
    }
    
    public static Map<String, String> getFileMap() {
        reloadFileMap();
        return fileMap;
    }

    public static String getDOWNLOAD_PATH() {
        return DOWNLOAD_PATH;
    }

    public static List<String> getIpList() {
        return ipList;
    }

    public static List<String> getDepList() {
        return depList;
    }
    
    
}
