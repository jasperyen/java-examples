/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deployworking;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.URL;
import java.net.UnknownHostException;
import javax.net.ssl.HttpsURLConnection;


/**
 *
 * @author Jasper
 */
public class LoggerHandler {
    private static final String SERVER_URL = "https://www.jasper-tw.com/cudaComputeServer/dataStreamInput";
    private static final String PROXY_HOST = "proxy.jasper-tw.com";
    private static final String PROXY_PORT = "443";
    private static final String LOCAL_IP;

    static {
        System.setProperty("https.proxyHost", PROXY_HOST);
        System.setProperty("https.proxyPort", PROXY_PORT);
        
        String ip;
        try {
            ip = Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            System.out.println(ex);
            ip = "0.0.0.0";
        }
        LOCAL_IP = ip;
        //LOCAL_IP = "140.137.9.223";
    }

    
    public boolean sendLog(double cpuUsage, boolean isMining) {
        
        String usage = (int)cpuUsage + "";
        
        if (usage.length() == 1)
            usage = "0" + usage;
        else if (usage.length() == 3)
            usage = "99";
        
        if (isMining)
            return sendLog("8" + usage);
        else
            return sendLog("7" + usage);
    }
    
    public boolean sendLog(String state) {
        
        boolean toKill = false;
        String url = SERVER_URL + "?i=" + LOCAL_IP + "&s=" + state;
        
        try {
            
            URL httpsUrl = new URL(url);
            HttpsURLConnection httpsCon = (HttpsURLConnection)httpsUrl.openConnection();
            httpsCon.setConnectTimeout(1000);
            
            int code = httpsCon.getResponseCode();
            
            toKill = (code == 201 || code == 202);
            
        } catch (IOException ex) {
            //System.out.println(ex);
        }
        
        return toKill;
    }
    
    
}
