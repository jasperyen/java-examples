/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smartnewspublisher;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Jasper
 */
public class FaceReceiver {

    private static final Logger logger = Logger.getLogger(FaceReceiver.class.getName());
    
    private final InetSocketAddress addr;
    private boolean keepClient, isConnect;
    
    private boolean sendStop, sendStart;
    
    private final List<String> nameList;
    private final Thread socketHandler;
    private Thread listenHandler, SendHandler;
    
    public FaceReceiver(String host, int port) {
        
        addr = new InetSocketAddress(host, port);
        nameList = new LinkedList<>();
        
        keepClient = true;
        sendStop = false;
        sendStart = false;
        
        socketHandler = new Thread( () -> {
            try {
                handleSocket();
            } catch (InterruptedException e) {}
        });
        
        socketHandler.start();
    }
    
    private void handleSocket () throws InterruptedException {
        
        while (keepClient) {
            
            try (Socket socket = new Socket()) {
                socket.connect(addr, 3000);
                
                isConnect = true;
                logger.log(Level.INFO, "Socket connect success !");
                
                synchronized (nameList) {
                    nameList.clear();
                }
                
                //  監聽資料
                listenHandler = new Thread( () -> {
                    handleInputStream(socket);
                });
                
                //  傳送資料
                SendHandler = new Thread( () -> {
                    handleOutputStream(socket);
                });
                
                
                //  等待
                logger.log(Level.INFO, "SocketHandler sleep");
                
                try {
                    Thread.sleep(Long.MAX_VALUE);
                } catch (InterruptedException e) {
                    //  連線中斷後被喚醒
                    logger.log(Level.INFO, "SocketHandler wakeup");
                }
                
                isConnect = false;
                
                //  關閉串流
                if (!socket.isInputShutdown())
                    socket.shutdownInput();
                if (!socket.isOutputShutdown())
                    socket.shutdownOutput();
                
                //  等待串流執行續關閉
                if (listenHandler.isAlive())
                    listenHandler.join();
                if (SendHandler.isAlive())
                    SendHandler.join();
            } 
            catch (IOException ex) {
                logger.log(Level.WARNING, "Socket in IOException : {0}", ex.toString());
                logger.log(Level.INFO, "Socket connect close !");
            }
            
            if (keepClient) {
                logger.log(Level.INFO, "Reconnect in 10 s ...");
                
                Thread.sleep(10 * 1000);
            }
            
        }
    }
    
    private void handleInputStream (Socket socket) {
        
        try (BufferedInputStream buff = new BufferedInputStream(socket.getInputStream())) {
            while (true) {
                int length = buff.available();
                
                if (length != 0) {
                    byte bytes[] = new byte[length];
                    buff.read(bytes);
                    String str = new String(bytes);
                    
                    System.out.println(str);
                    //praseJSONData(str);
                }
                else
                    Thread.sleep(100);
            }
        }
        catch (InterruptedException e) {}
        catch (IOException ex) {
            logger.log(Level.INFO, "Socket InputStream in IOException : {0}", ex.toString());
            
            //  喚醒 socket
            if (isConnect)
                Thread.interrupted();
        }
        
    }
    
    private void handleOutputStream (Socket socket) {
    
        try (PrintWriter printer = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()))){
            
            while (true) {
                if (!sendStop && !sendStart){
                    Thread.sleep(100);
                    continue;
                }
                
                if (sendStop) {
                    printer.write("stop");
                    printer.flush();
                    sendStop = false;
                }

                if (sendStart) {
                    printer.write("start");
                    printer.flush();
                    sendStart = false;
                }
            }
        }
        catch (InterruptedException e) {}
        catch (IOException ex) {
            logger.log(Level.INFO, "Socket OutputStream in IOException : {0}", ex.toString());
            
            //  喚醒 socket
            if (isConnect)
                Thread.interrupted();
        }
        
    }
    
    public void closeSocket() throws InterruptedException {
        keepClient = false;
        
        if (isConnect)
            socketHandler.interrupt();
        
        socketHandler.join();
    }
    
    private void praseJSONData (String rawData) {
        
        JSONObject json = new JSONObject(rawData);
        JSONArray names = json.getJSONArray("names");
        
        synchronized (nameList) {
            for (int i = 0; i < names.length(); i++)
                nameList.add(names.getString(i));
        }
        
    }
    
    public String whoIsInFrontOf () {
        final int inFrontThreshold = 5;
        
        synchronized (nameList) {
            if (nameList.isEmpty())
                return null;
            
            String name = "";
            int count = 0;
            for (int i = nameList.size() - 1 ; i >= 0; i--) {
                if (nameList.get(i).equals(name)) {
                    count++;
                    
                    if (count > inFrontThreshold)
                        break;
                }
                else {
                    name = nameList.get(i);
                    count = 1;
                }
            }
            
            if (count > inFrontThreshold) {
                nameList.clear();
                return name;
            }
            else
                return null;
        }
        
    }
    
    public List<String> getNameList () {
        List<String> returnList = new LinkedList<>();
        
        synchronized (nameList) {
            returnList.addAll(nameList);
            nameList.clear();
        }
        
        return returnList;
    }
    
    public boolean isReceiveData () {
        return true;
    }
    
    
    public void sendStop () {
        sendStop = true;
    }
    
    public void sendStart () {
        sendStart = true;
    }
    
}
