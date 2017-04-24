/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package speechvoicedetection;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import net.sourceforge.javaflacencoder.EncodingConfiguration;
import net.sourceforge.javaflacencoder.FLAC_FileEncoder;
import net.sourceforge.javaflacencoder.StreamConfiguration;

/**
 *
 * @author Jasper
 */
public class ConvertToFlac {
    private static final Logger logger = Logger.getLogger(ConvertToFlac.class.getName());
    
    private final int channel;
    private final int sampleRate;
    private final int sampleSizeInBits;
    
    private final File PCMWav;
    private final File FlacFile;
    
    public ConvertToFlac(File PCMWav, File FlacFile, int channel, int sampleRate, int sampleSizeInBits) throws IOException {
        this.channel = channel;
        this.PCMWav = PCMWav;
        this.FlacFile = FlacFile;
        
        this.sampleRate = sampleRate;
        this.sampleSizeInBits = sampleSizeInBits;
        
        convert();
    }
    

    private void convert() throws IOException {
        FLAC_FileEncoder flacEncoder = new FLAC_FileEncoder();

        StreamConfiguration streamconfig = new StreamConfiguration(channel, StreamConfiguration.DEFAULT_MIN_BLOCK_SIZE, 
                                        StreamConfiguration.DEFAULT_MAX_BLOCK_SIZE, sampleRate, sampleSizeInBits);

        EncodingConfiguration enconfig = new EncodingConfiguration();
        enconfig.setSubframeType(EncodingConfiguration.SubframeType.LPC);

        flacEncoder.setStreamConfig(streamconfig);
        flacEncoder.setEncodingConfig(enconfig);

        flacEncoder.encode(PCMWav, FlacFile);
    }
}
