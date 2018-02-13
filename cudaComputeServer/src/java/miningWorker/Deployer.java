/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miningWorker;

import cudaServer.initServer;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jasper
 */
public class Deployer {
    
    private static final List<Deployer> DeployerSet;
    
    private final String ip;
    private final List<String> logList = new ArrayList<>();
    
    static {
        DeployerSet = new ArrayList<>();
        initServer.getDepList().forEach( ip -> {
            DeployerSet.add(new Deployer(ip));
        });
    }
    
    
    public static void insertLogByIp(String ip, String content) {
        Deployer d = getDeployerByIp(ip);
        
        if (d != null)
            d.insertLog(content);
    }
    
    public static Deployer getDeployerByIp (String ip) { 
        for (Deployer d : DeployerSet) {
            if (d.getIp().equals(ip))
                return d;
        }
        return null;
    }
    
    public Deployer (String ip) {
        this.ip = ip;
    }
    
    private void insertLog (String content) {
        
        while(logList.size() > 20)
            logList.remove(0);
        
        logList.add(content);
    }

    public List<String> getHTLMLogList() {
        List<String> htmlList = new ArrayList<>();
        
        logList.forEach( log -> {
            htmlList.add(log.replace("\n", "<br>"));
        });
        
        return htmlList;
    }

    
    
    public String getIp() {
        return ip;
    }
}
