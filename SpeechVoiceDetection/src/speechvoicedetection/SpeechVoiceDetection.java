/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package speechvoicedetection;

import java.io.File;
import java.io.IOException;
import static java.lang.System.out;

public class SpeechVoiceDetection {

    public static void main(String[] args) throws IOException {
        
        /*    目前僅支援單稱道    */
        int channel = 1;
        //  採樣率
        int sampleRate = 8000;
        //  採樣位元數
        int sampleSizeInBits = 16;
        
        
        String fileName = "test.wav";
        
        File wavFile = new File(fileName);
        File flacFile = new File(fileName.substring(0, fileName.lastIndexOf(".")) + ".flac");
        
        
        ConvertToFlac con = new ConvertToFlac(wavFile, flacFile, channel, sampleRate, sampleSizeInBits);
        ResponesByGoogle res = new ResponesByGoogle(flacFile, sampleRate);
        
        res.getGoogleResponse().forEach((entry) -> {
            out.println("confidence : " + entry.getKey());
            out.println("transcript : " + entry.getValue());
        });
    }
    
}
