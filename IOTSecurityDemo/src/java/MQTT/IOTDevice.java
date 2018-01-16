
package MQTT;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Jasper
 */
public class IOTDevice {

    private static final Logger logger = Logger.getLogger(IOTDevice.class.getName());
    
    private static final Map<String, IOTDevice> DeviceMap = new TreeMap<>();
    
    
    private final List<Stamp> DeviceLog = new ArrayList<>();
    
    public final String deviceUid;
    public String AESKey = "000102030405060708090A0B0C0D0E0F101112131415161718191A1B1C1D1E1F";
    public Boolean state = false;
    public Double maxTemperature = 45.0;
    public Double minTemperature = 15.0;
    
    public static class Stamp {
        public final LocalDateTime time;
        public final Boolean state;
        public final Double temperature;
        public final Double maxTemperature;
        public final Double minTemperature;

        public Stamp(LocalDateTime time, Boolean state, Double temperature, Double maxTemperature, Double minTemperature) {
            this.time = time;
            this.state = state;
            this.temperature = temperature;
            this.maxTemperature = maxTemperature;
            this.minTemperature = minTemperature;
        }
    }

    static {
        addIOTDevice("device-noid", "no-uid");
        addIOTDevice("device-0D22", "011A2888028AD1190D22");
        addIOTDevice("device-E923", "011A2888028AD091E923");
        addIOTDevice("device-1123", "011A2888028AD0691123");
    }
    
    public static void addIOTDevice (String deviceName, String deviceUid) {
         DeviceMap.put(deviceName, new IOTDevice(deviceUid));
         MQTTHandler.addSubscribe(deviceName);
    }
    
    public IOTDevice(String deviceUid) {
        this.deviceUid = deviceUid;
    }

    
    public JSONArray getTemperatureHistory () {
        JSONArray arrays = new JSONArray();
        
        LocalDateTime time = LocalDateTime.now().minusMinutes(20);
        
        for (int i = 0; i < 20; i++) {
            int count = 0;
            double sum = 0.0;
            
            for (Stamp stamp : DeviceLog)
                if ( stamp.time.isAfter(time) && stamp.time.isBefore(time.plusMinutes(1)) ) {
                    count++;
                    sum += stamp.temperature;
                }
            
            if (count == 0)
                arrays.put(JSONObject.NULL);
            else
                arrays.put( (int)(sum / count * 100) / 100.0 );
            
            time = time.plusMinutes(1);
        }
        
        return arrays;
    }
    
    public void addStamp (boolean state, double temperature) {
        this.state = state;
        
        DeviceLog.add( new Stamp (LocalDateTime.now(), state, temperature, maxTemperature, minTemperature));
    }

    public List<Stamp> getDeviceLog() {
        return DeviceLog;
    }

    public String getAESKey() {
        keyUpdater();
        return AESKey;
    }
    
    public static IOTDevice getDeviceByName (String DeviceName) {
        return DeviceMap.get(DeviceName);
    }
    
    public static List<String> getDeviceNameList () {
        List<String> list = new ArrayList<>();
        
        DeviceMap.forEach( (name, device) -> {
            list.add(name);
        });
        
        return list;
    }
    
    private static void keyUpdater () {
        try {
        
            URL loginURL = new URL("http://60.250.111.124/vendorTrial/api/iBadgeService.php");
            HttpURLConnection con = (HttpURLConnection) loginURL.openConnection();
        
            String loginParam = "endpoint=UserLogin&mail=test%40example.com&passphrase=testpw123&submit=Login";
            
            con.setRequestMethod("POST"); 
            con.setDoOutput(true);
            
            try ( DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.writeBytes(loginParam);
            }
            
            int responseCode = con.getResponseCode();
            
            if (responseCode != 200)
                throw new Exception("ResponseCode : " + responseCode);
            
            List<String> cookieList = new ArrayList<>();
            StringBuilder response = new StringBuilder();
            try (BufferedReader buff = new BufferedReader( new InputStreamReader(con.getInputStream()))) {
                
                con.getHeaderFields().get("Set-Cookie").forEach(str -> {
                    cookieList.add(str.split(";", 2)[0]);
                });
                
                String line;
                while ((line = buff.readLine()) != null) {
                    response.append(line);
                }
            }
            
            
            int errorCode = new JSONObject(response.toString()).getInt("errorCode");
            
            if (errorCode != 0)
                throw new Exception("Login error with errorCode : " + errorCode);
            
            if (cookieList.isEmpty())
                throw new Exception("No response cookie");
            
            
            URL queryURL = new URL("http://60.250.111.124/vendorTrial/api/iBadgeService.php");
            con = (HttpURLConnection) queryURL.openConnection();
            
            String queryParam = "endpoint=ListDevices&statusFilter=all&submit=Submit";
            StringBuilder requestCookie = new StringBuilder();
            
            cookieList.forEach( c -> {
                requestCookie.append(";").append(c);
            });
            
            con.setRequestMethod("POST"); 
            con.setDoOutput(true);
            con.setRequestProperty("Cookie", requestCookie.substring(1));
            
            try ( DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.writeBytes(queryParam);
            }
            
            responseCode = con.getResponseCode();
            
            if (responseCode != 200)
                throw new Exception("ResponseCode : " + responseCode);
            
            response = new StringBuilder();
            try (BufferedReader buff = new BufferedReader( new InputStreamReader(con.getInputStream()))) {
                String line;
                while ((line = buff.readLine()) != null) {
                    response.append(line);
                }
            }
            
            //System.out.println(response);
            
            errorCode = new JSONObject(response.toString()).getInt("errorCode");
            
            if (errorCode != 0)
                throw new Exception("Query error with errorCode : " + errorCode);
            
            JSONArray deviceList = new JSONObject(response.toString()).getJSONArray("deviceList");
            
            for (int i = 0; i < deviceList.length(); i++)
                for (Entry<String, IOTDevice> entry : DeviceMap.entrySet())
                    if (entry.getValue().deviceUid.equals(
                            deviceList.getJSONObject(i).getString("uid"))) {
                        
                        JSONArray attr = deviceList.getJSONObject(i).getJSONArray("deviceAttributes");
                        
                        for (int j = 0; j < attr.length(); j++)
                            if (attr.getJSONObject(j).getInt("attId") == 1) {
                                entry.getValue().AESKey = attr.getJSONObject(j).getString("attValue");
                                logger.log(Level.INFO, "Update key {0} - {1}", new String[]{entry.getKey(), entry.getValue().AESKey});
                            }
                    }
            
        } catch (Exception ex) {
            logger.log(Level.WARNING, "keyUpdater failed : {0}", ex.toString());
        }
    }
}
