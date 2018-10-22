/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upnpwatchdog;

import com.dosse.upnp.UPnP;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author User
 */
public class UpnpWatchDog {
    private static final long checkInterval = 5 * 30;   //  5min 
    private static final Logger logger = Logger.getLogger(UpnpWatchDog.class.getName());
    
    public static void main(String[] args) throws InterruptedException, IOException {
        String logPath = System.getProperty("user.dir") + File.separator
                        + "UPnPLog_" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".log";
        FileHandler fh = new FileHandler(logPath);
        fh.setFormatter(new SimpleFormatter());
        
        logger.setLevel(Level.ALL);
        logger.addHandler(fh);
        
        List<Integer> targetPort = new ArrayList<>();
        for (String arg : args)
            targetPort.add(Integer.parseInt(arg));
        
        logger.log(Level.INFO, "Attempting UPnP port forwarding...");
        
        if (!UPnP.isUPnPAvailable()) {
            logger.log(Level.WARNING, "UPnP is not available");
            return;
        }
            
        while (true) {
            for (Integer p : targetPort)
                if (UPnP.isMappedTCP(p))
                    logger.log(Level.FINE, "UPnP port {0} is already mapped", p);
                else if (UPnP.openPortTCP(p))
                    logger.log(Level.INFO, "UPnP port {0} forwarding enabled", p);
                else
                    logger.log(Level.WARNING, "UPnP port {0} forwarding failed", p);
            
            Thread.sleep(checkInterval * 1000);
        }
    }
}
