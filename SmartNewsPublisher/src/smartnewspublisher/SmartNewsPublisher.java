
package smartnewspublisher;

import com.darkprograms.speech.synthesiser.Synthesiser;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;



/**
 *
 * @author Jasper
 */
public class SmartNewsPublisher {

    public static final String TEMP_PATH = "C:\\workspace\\temp\\";
    //public static final String TEMP_PATH = "/home/pi/workspace/SmartNewsPublisher/temp/";
    
    private static RecordJFrame recordFrame;
    private static FaceReceiver recv;
    
    static {
        Locale.setDefault(new Locale("en", "EN"));
        
        System.setProperty("java.util.logging.SimpleFormatter.format", 
            "%4$s %1$tm-%1$td %1$tH:%1$tM:%1$tS %5$s%6$s%n");
    }
    
    private static void publishController() {
        
        recordFrame = new RecordJFrame("Recoding . . .");
        
        recordFrame.showFrame();
        
        recv = new FaceReceiver("127.0.0.1", 8000);
        
        talkEn("Hello");
        
        while (true) {
            String name = whoIsInFrontOf();
        }
    }

    public static void talkZh(String text){
        talk (text, "zh");
    }
    
    public static void talkEn(String text){
        talk (text, "en-us");
    }
    
    public static void talk(String text, String language){
        
	Synthesiser synth = new Synthesiser(language);
	
        try (InputStream is = synth.getMP3Data(text)) {
            int length = is.available();
            byte[] mp3Bytes = new byte[length];
            is.read(mp3Bytes);

            try (FileOutputStream fos = new FileOutputStream(TEMP_PATH + "talk.mp3")) {
                fos.write(mp3Bytes);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
   }
    
    private static String whoIsInFrontOf () {
        int inFrontThreshold = 5;
        List<String> nameList = new ArrayList<>();
        List<String> recvList;
        String name;
        
        while (true){
            if (!recv.isReceiveData()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {}
                
                continue;
            }
                
            
            recvList = recv.getNameList();
            nameList.addAll(recvList);
            
            name = "";
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
                break;
        }
        
        return name;
    }
    

    public static void main(String[] args) {
        publishController();
    }
    
}
