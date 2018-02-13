/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miningWorker;


import cudaServer.initServer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Jasper
 */
public class Worker {
    
    public static boolean killWorker = false;
    public static boolean shutdownWorker = false;
    
    private static final List<Worker> workerSet = new ArrayList<>();
    private static final DateTimeFormatter dtformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
    
    private final String ip;
    private final List<Entry<LocalDateTime, Integer>> logs;
    
    
    static {
        
        initServer.getIpList().forEach( ip -> {
            workerSet.add(new Worker(ip));
        });
    }
    
    public Worker(String ip) {
        this.ip = ip;
        logs = new ArrayList<>();
        logs.add(new SimpleEntry<>(LocalDateTime.now(), 500));
    }
    
    public List<String> getWorkerLogs (String day) {
        LocalDateTime today = LocalDate.now().atStartOfDay();
        int forwardDay;
        
        try {
            forwardDay = Integer.valueOf(day);
        } catch (NumberFormatException ex) { forwardDay = 0; }
        
        
        //  以時間排序 log
        Collections.sort(logs, (t1, t2) -> {
            return t1.getKey().compareTo(t2.getKey());
        });
        
        //  刪除 5天前的 log
        while (true) {
            if (logs.size() < 2)
                break;
            
            if (logs.get(1).getKey().isAfter(today.minusDays(5)))
                break;
            else 
                logs.remove(1);
        }
        
        
        //  取得指定天數的 log 
        List<String> logList = new ArrayList<>();
        LocalDateTime searchDay = today.minusDays(forwardDay);
        logs.forEach( (e) -> {
            LocalDateTime logTime = e.getKey();
            if ( logTime.isAfter(searchDay) && logTime.isBefore(searchDay.plusDays(1)))
                logList.add(dtformatter.format(logTime) + " - " + state2String(e.getValue()));
        });
        
        return logList;
    }

    public static List<String> getDeathWorker() {
        List<String> workers = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        
        workerSet.forEach( (w) -> {
            LocalDateTime t = w.logs.get(w.logs.size() - 1).getKey();
            long hours = ChronoUnit.HOURS.between(t, now);
           
            t = t.plusHours(hours);
            long minutes = ChronoUnit.MINUTES.between(t, now);
        
            if (hours > 0 || minutes > 10)
                workers.add(w.ip);
        });
        
        return workers;
    }

    public static List<Entry<String, String>> getTotalWorkerLog () {
        List<Entry<String, String>> workers = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        
        workerSet.forEach( (w) -> {
            LocalDateTime t = w.logs.get(w.logs.size() - 1).getKey();
            long hours = ChronoUnit.HOURS.between(t, now);
           
            t = t.plusHours(hours);
            long minutes = ChronoUnit.MINUTES.between(t, now);
           
            String log = state2String(w.logs.get(w.logs.size() - 1).getValue()) + "&emsp;";
            
            if (hours > 0 || minutes > 5)
                log = log + "<span>" + hours + "h " + minutes + "m ago </span>";
            else
                log = log + "<span style=\"color:#DC143C\">" + hours + "h " + minutes + "m ago </span>";

            workers.add(new SimpleEntry<>( w.ip, log ));
        });
        return workers;
    }
    
    public static Worker getWorkerByIp (String ip) {
        Worker worker = null;
        
        for (Worker w : workerSet)
            if (w.ip.equals(ip))
                worker = w;
        
        return worker;
    }
    
    public static void insertWorkerLog (String ip, String state) {
        Worker worker = getWorkerByIp(ip);
        if (worker == null)
            return;
        
        try {
            int s = Integer.valueOf(state);

            worker.logs.add(new SimpleEntry<>(LocalDateTime.now(), s));
        } catch (NumberFormatException | DateTimeParseException ex) {
            //System.out.println(ex);
        }
    }
    
    public static String state2String (int state) {
        
        if (state < 100)
            return "running ...  cpu usage : " + state + " %";
        
        if (state >= 800 && state < 900)
            return "running xmr ...  cpu usage : " + (state % 800) + " %";
        
        if (state >= 700 && state < 800)
            return "standing by ...  cpu usage : " + (state % 700) + " %";
        
        
        Map<Integer, String> stateMap = initServer.getStateMap();
        
        if (!stateMap.containsKey(state))
            return "unknow state code";
        
        return stateMap.get(state);
    }
}
