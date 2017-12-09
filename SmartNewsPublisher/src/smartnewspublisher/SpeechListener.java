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
            Arrays.asList("是", "好", "要", "可", "對", "行");
    
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
        final int LOOP_THRESHOLD = 50;
        final int MIN_RECORD_SEC = 5;
        final int SOUND_THRESHOLD = 60;
        
        int volume;
        
        try {
            recFile.delete();
                
            mic.open();
            
            for (int i = 0; i < LOOP_THRESHOLD; i++) {
                volume = mic.getAudioVolume();
                
                if (volume < SOUND_THRESHOLD){
                    logger.log(Level.INFO, "Nobody speeking... Current volume : {0}", volume);
                    Thread.sleep(200);
                }
                else{
                    logger.log(Level.INFO, "Start record, Current volume : {0}", volume);
                    
                    mic.captureAudioToFile(recFile);
                    Thread.sleep(MIN_RECORD_SEC * 1000);
                    
                    do{
                        Thread.sleep(1000);
                        volume = mic.getAudioVolume();
                        
                        logger.log(Level.INFO, "Recording... Current volume : {0}", volume);
                    }while(volume > SOUND_THRESHOLD);

                    logger.log(Level.INFO, "Recording Complete !");
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
            logger.log(Level.INFO, "No response");
            return false;
	}
        
        logger.log(Level.INFO, "Google response : {0}", response.getAllPossibleResponses().toString());
        
        
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
