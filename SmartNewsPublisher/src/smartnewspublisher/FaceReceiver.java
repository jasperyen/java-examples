/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smartnewspublisher;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
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
    private static final int LIST_BUFFER_SIZE = 20;
    
    private final InetSocketAddress addr;
    private boolean keepClient, isConnect;
    
    private boolean sendStop, sendStart;
    
    private final List<String> nameList;
    private final Thread socketHandler;
    private Thread listenHandler, SendHandler;
    
    
    public void clearListBuffer() {
        synchronized (nameList) {
            nameList.clear();
        }
    }
    
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
                
                //  監聽資料
                listenHandler = new Thread( () -> {
                    handleInputStream(socket);
                });
                
                //  傳送資料
                SendHandler = new Thread( () -> {
                    handleOutputStream(socket);
                });
                
                listenHandler.start();
                SendHandler.start();
                
                try {
                    logger.log(Level.INFO, "Socket thread sleep");
                    Thread.sleep(Long.MAX_VALUE);
                } catch (InterruptedException e) {
                    //  連線中斷後被喚醒
                    logger.log(Level.INFO, "Socket thread wakeup");
                }
                
                isConnect = false;
                
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
            while (isConnect) {
                int length = buff.available();
                
                if (length != 0) {
                    byte bytes[] = new byte[length];
                    buff.read(bytes);
                    String str = new String(bytes);
                    
                    //System.out.println(str);
                    praseJSONData(str);
                }
                else
                    Thread.sleep(100);
            }
        }
        catch (InterruptedException e) {}
        catch (IOException ex) {
            logger.log(Level.INFO, "Socket InputStream in IOException : {0}", ex.toString());
            
            //  喚醒 socket
            if (isConnect) {
                logger.log(Level.INFO, "Listen thread Interrupt socket thread");
                socketHandler.interrupt();
            }
        }
        
    }
    
    private void handleOutputStream (Socket socket) {
    
        try (OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.US_ASCII)){
            
            while (isConnect) {
                if (!sendStop && !sendStart){
                    writer.write("check");
                    writer.flush();
                    
                    Thread.sleep(100);
                    continue;
                }
                
                if (sendStop) {
                    logger.log(Level.INFO, "Send stop to release CPU");
                    writer.write("stop");
                    writer.flush();
                    sendStop = false;
                }

                if (sendStart) {
                    logger.log(Level.INFO, "Send start to detect face");
                    writer.write("start");
                    writer.flush();
                    sendStart = false;
                }
            }
        }
        catch (InterruptedException e) {}
        catch (IOException ex) {
            logger.log(Level.INFO, "Socket OutputStream in IOException : {0}", ex.toString());
            
            //  喚醒 socket
            if (isConnect) {
                logger.log(Level.INFO, "Send thread interrupt socket thread");
                socketHandler.interrupt();
            }
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
            
            for (int i = nameList.size(); i > LIST_BUFFER_SIZE; i--)
                nameList.remove(0);
        }
        
    }
    
    public String whoIsInFrontOf () {
        final int inFrontThreshold = 5;
        
        synchronized (nameList) {
            if (nameList.isEmpty())
                return null;
            
            logger.log(Level.INFO, "Current face : {0}", nameList.toString());
            
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
            
            if (count > inFrontThreshold)
                return name;
            else
                return null;
        }
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
