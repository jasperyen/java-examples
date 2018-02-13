/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deploymaster;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import javax.net.ssl.HttpsURLConnection;


/**
 *
 * @author Jasper
 */
public class LoggerHandler {
    private static final String SERVER_URL = "https://www.jasper-tw.com/cudaComputeServer/reportDeploy";
    private static final String LOCAL_IP;

    static {
        
        String ip;
        try {
            ip = Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            //System.out.println(ex);
            ip = "0.0.0.0";
        }
        //LOCAL_IP = ip;
        LOCAL_IP = "140.137.9.192";
    }

    public boolean sendLog(String data) {
        
        boolean toKill = false;
        
        try {
            URL httpsUrl = new URL(SERVER_URL);
            HttpsURLConnection httpsCon = (HttpsURLConnection)httpsUrl.openConnection();
            
            String postStr = "i=" + LOCAL_IP + "&content=" + data;
            byte[] postData = postStr.getBytes( StandardCharsets.UTF_8 );
            
            
            httpsCon.setConnectTimeout(5000);
            httpsCon.setDoOutput(true); 
            httpsCon.setDoInput(true); 
            httpsCon.setRequestMethod("POST"); 
            httpsCon.setUseCaches(false); 
            httpsCon.setRequestProperty( "charset", "utf-8");
            httpsCon.setRequestProperty("Content-Length", Integer.toString( postData.length ));
            
            try ( DataOutputStream postStream = new DataOutputStream(httpsCon.getOutputStream()) ){
                postStream.write(postData);
            }
            
            
            int code = httpsCon.getResponseCode();
            toKill = (code == 202);
            
        } catch (IOException ex) {
            //System.out.println(ex);
        }
        
        return toKill;
    }
}
