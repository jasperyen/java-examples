/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smartnewspublisher;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jasper
 */
public class FaceReceiver {

    private static final Logger logger = Logger.getLogger(FaceReceiver.class.getName());
    
    InetSocketAddress addr;
    private boolean keepClient, isConnect;
    
    Thread socketHandler, listenHandler, SendHandler;
    
    
    public FaceReceiver(String host, int port) {
        
        addr = new InetSocketAddress(host, port);
        
        keepClient = true;
        
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
                }
            }
        }
        catch (IOException ex) {
            logger.log(Level.INFO, "Socket InputStream in IOException : {0}", ex.toString());
            
            //  喚醒 socket
            if (isConnect)
                Thread.interrupted();
        }
        
    }
    
    private void handleOutputStream (Socket socket) {
    
        try (BufferedOutputStream buff = new BufferedOutputStream(socket.getOutputStream())) {
            
        }
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
    
    public List<String> getNameList () {
        
        List<String> nameList = new ArrayList<>();
        
        for (int i = 0; i < 10; i++)
            nameList.add("Jasper");
        
        return nameList;
    }
    
    public boolean isReceiveData () {
        return true;
    }
    
}
