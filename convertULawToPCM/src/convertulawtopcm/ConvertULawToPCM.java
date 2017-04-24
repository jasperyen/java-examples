/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package convertulawtopcm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class ConvertULawToPCM {
    
    //  聲道數
    private static final int channel = 1;
    //  採樣率
    private static final int sampleRate = 8000;
    //  採樣位元數
    private static final int sampleSizeInBits = 8;

    private static File ULawWav;
    private static File PCMWav;
    
    public static void main(String[] args) throws IOException {
        
        String fileName = "test.wav";
        
        ULawWav = new File(fileName);
        PCMWav = new File(fileName .substring(0, fileName.lastIndexOf(".")) + "_PCM.wav");
        
        convert(ULawWav, PCMWav);
    }
    
    private static void convert(File ULawWav, File PCMWav) throws IOException {
        int frameSize = (sampleSizeInBits / 8) * channel;
        float frameRate = sampleRate * channel;

        long fileSize = ULawWav.length();
        long FramesNum = fileSize / frameSize;
        long bytesToSkip = 256;

        AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.ULAW, sampleRate, 
                                    sampleSizeInBits, channel, frameSize, frameRate, true);

        AudioFormat newFormat = new AudioFormat (AudioFormat.Encoding.PCM_SIGNED, audioFormat.getSampleRate(), 
                                    audioFormat.getSampleSizeInBits() * 2, audioFormat.getChannels(), 
                                    audioFormat.getFrameSize() * 2, audioFormat.getFrameRate(), true);

        AudioInputStream ULawInputStream = new AudioInputStream(new FileInputStream(ULawWav), audioFormat, FramesNum);

        AudioInputStream PCMInputStream =  AudioSystem.getAudioInputStream(newFormat, ULawInputStream);

        skip(PCMInputStream, bytesToSkip);

        AudioSystem.write(PCMInputStream, AudioFileFormat.Type.WAVE, PCMWav);
    }
    
    private static void skip(AudioInputStream audioInputStream, long bytesToSkip) throws IOException {
        long skipped = 0;
        int frameSize = audioInputStream.getFormat().getFrameSize();
        while (bytesToSkip - skipped > frameSize
                        && audioInputStream.available() > frameSize) {
                skipped += audioInputStream.skip(bytesToSkip - skipped);
        }
    }
    
}
