
package smartnewspublisher;

import com.darkprograms.speech.synthesiser.Synthesiser;
import java.io.File;
import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;



/**
 *
 * @author Jasper
 */
public class SmartNewsPublisher {

    private static final Logger logger = Logger.getLogger(SmartNewsPublisher.class.getName());
    
    public static final String TEMP_PATH = "C:\\workspace\\temp\\";
    //public static final String TEMP_PATH = "/home/pi/workspace/SmartNewsPublisher/temp/";
    
    private static final String CHROME_CMD = "\"C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe\" --app=";
    //private static final String CHROME_CMD = "./run_chrome.sh ";
    
    public static final String API_KEY = "AIzaSyBsIounZbhWreS9StJyp0wygE5cxsDhDOo";
    
    private static final RecordJFrame recordFrame;
    private static final FaceReceiver receiver;
    private static final SpeechListener listener;
    private static final NewsPublisher publisher;
    
    static {
        Locale.setDefault(new Locale("en", "EN"));
        
        System.setProperty("java.util.logging.SimpleFormatter.format", 
            "%4$s %1$tm-%1$td %1$tH:%1$tM:%1$tS %5$s%6$s%n");
        
        recordFrame = new RecordJFrame("Recoding . . .");
        recordFrame.showFrame();
        
        receiver = new FaceReceiver("127.0.0.1", 8000);
        
        listener = new SpeechListener();
        publisher = new NewsPublisher();
    }
    
    private static void publishController() {
        
        //System.out.println(listener.askForYes());
        Map<String, String> m = new TreeMap<>();
        
        publisher.getNewsByName("Jasper", m);
        //System.out.println(m);
        Entry<String, String> e = m.entrySet().iterator().next();
        
        System.out.println(e);
        //talkZh(e.getKey());
        
        try {
            Process p = Runtime.getRuntime().exec(CHROME_CMD + e.getValue());
            try {
                p.waitFor();
            } catch (InterruptedException ex) {}
        } catch (IOException ex) {
            logger.log(Level.WARNING, "Chrome in IOException : {0}", ex.toString());
        }
        
        System.out.println("end");
        
        /*
        while (true) {
            
            String enName;
            do {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {}
                
                enName = receiver.whoIsInFrontOf();
            } while (enName == null);
            
            String chName = publisher.getChName(enName);
            
            if ( chName == null ){
                talkZh(chName + " 你還不在資料庫裡");
                continue;
            }
            
            receiver.sendStop();
            
            talkZh("你好 " + chName + " 要看新聞嗎");
            
            if (!listener.askForYes()) {
                talkZh("不要就算了");
                receiver.sendStart();
                continue;
            }
            
            Map<String, String> newsMap = new TreeMap<>();
            
            if (!publisher.getNewsByName("Jasper", newsMap)) {
                talkZh("抱歉, 找不到新聞");
                receiver.sendStart();
                continue;
            }
            
            Entry<String, String> news = m.entrySet().iterator().next();
            publisher.addHaveRead(enName, news.getKey());
            talkZh("找到新聞 " + news.getKey());
            
            try {
                Process p = Runtime.getRuntime().exec(CHROME_CMD + e.getValue());
                p.waitFor();
            } 
            catch (InterruptedException ex) {}
            catch (IOException ex) {
                logger.log(Level.WARNING, "Chrome in IOException : {0}", ex.toString());
            }
            talkZh("掰掰");
            receiver.sendStart();
        }
        */
    }

    private static void talkZh(String text){
        talk (text, "zh");
    }
    
    private static void talkEn(String text){
        talk (text, "en-us");
    }
    
    private static void talk(String text, String language){
        
	Thread talkThread = new Thread( () -> {
            Synthesiser synth = new Synthesiser(language);
            
            try (InputStream is = synth.getMP3Data(text)) {
                logger.log(Level.INFO, "Start talking : {0}", text); 
                AdvancedPlayer mp3Player = new AdvancedPlayer(is);
                mp3Player.play();
            }
            catch (IOException | JavaLayerException ex) {
                logger.log(Level.WARNING, "talkThread in IOException : {0}", ex.toString());
            }
        });
        
        talkThread.start();
   }
    
    public static void main(String[] args) {
        publishController();
    }
    
}
