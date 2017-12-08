/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smartnewspublisher;

import com.darkprograms.speech.microphone.MicrophoneAnalyzer;
import com.darkprograms.speech.recognizer.GoogleResponse;
import com.darkprograms.speech.recognizer.Recognizer;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javaFlacEncoder.FLACFileWriter;
import javax.sound.sampled.LineUnavailableException;


/**
 *
 * @author Jasper
 */
public class SpeechListener {

    private static final Logger logger = Logger.getLogger(SpeechListener.class.getName());
    
    private static final List<String> yesList = 
            Arrays.asList("是", "好", "要");
    
    private static final List<String> noList = 
            Arrays.asList("不");
    
    private final MicrophoneAnalyzer mic;
    private final File recFile;
    private final Recognizer rec;
    
    public SpeechListener() {
        recFile = new File(SmartNewsPublisher.TEMP_PATH + "speech.flac");
        
        mic = new MicrophoneAnalyzer(FLACFileWriter.FLAC);
        
        rec = new Recognizer(Recognizer.Languages.CHINESE_TRANDITIONAL, 
                                SmartNewsPublisher.API_KEY);
    }
    
    
    public boolean askForYes () {
        if (!ambientListeningLoop())
            return false;
        
        return analyzeResponse();
    }
    
    
    public boolean ambientListeningLoop() {
        final int LOOP_THRESHOLD = 20;
        final int SOUND_THRESHOLD = 8;
        
        int volume;
        
        try {
            mic.open();
            
            for (int i = 0; i < LOOP_THRESHOLD; i++) {
                volume = mic.getAudioVolume();
                System.out.println("Current volume : " + volume);

                if (volume < SOUND_THRESHOLD){
                    System.out.println("Nobody speek...");
                    Thread.sleep(500);
                }
                else{
                    System.out.println("Start recording...");
                    mic.captureAudioToFile(recFile);

                    do{
                        Thread.sleep(1000);
                        volume = mic.getAudioVolume();
                        
                        System.out.println("Current volume : " + volume);
                    }while(volume > SOUND_THRESHOLD);

                    System.out.println("Recording Complete!");
                    return true;
                }
            }
        } catch (LineUnavailableException | InterruptedException ex) {
            logger.log(Level.WARNING, "Recording failed in Exception : {0}", ex.toString());
        } finally{
            mic.close();
        }
        return false;
    }
    
    private boolean analyzeResponse () {
        GoogleResponse response;
        
        try {
            response = rec.getRecognizedDataForFlac(recFile, 3);
        } catch (IOException ex) {
            logger.log(Level.WARNING, "Analyze failed in Exception : {0}", ex.toString());
            return false;
        }
        
        if(response.getResponse() == null){
            System.out.println("No response");
            return false;
	}
        
        response.getAllPossibleResponses().forEach( s -> {
            System.out.println("\t" + s);
        });
        
        
        //  排除否定
        for (String str : response.getAllPossibleResponses())
            for (String no : noList)
                if (str.contains(no))
                    return false;
        
        //  檢查肯定
        for (String str : response.getAllPossibleResponses())
            for (String yes : yesList)
                if (str.contains(yes))
                    return true;
        
        return false;
    }
}
