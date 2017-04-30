/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package google_cloud_speech_api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import static java.lang.System.out;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import javaFlacEncoder.EncodingConfiguration;
import javaFlacEncoder.FLAC_FileEncoder;
import javaFlacEncoder.StreamConfiguration;
import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Jasper
 */
public class Google_Cloud_Speech_API {

    String flacSound = "C:\\workspace\\temp\\captchVoc_222.flac";
    static String  wavSound = "C:\\workspace\\temp\\captchVoc_222.wav";
    static String  wavSound_cov = "C:\\workspace\\temp\\fff.wav";
    String apiKey = "AIzaSyATbrkRAHDuVHFYWd-k5vkKJfw6fYUSnTE";
    String sampleRate = "8000";
    String maxResult = "1";
    String CHINESE_TRANDITIONAL = "zh-TW";
    String GOOGLE_RECOGNIZER_URL = "http://www.google.com/speech-api/v2/recognize?output=json";
    String AUTO_DETECT = "auto";
    
    public static void main(String[] args) {
        // TODO code application logic here
        
        convertULawFileToWav(wavSound);
        
        Google_Cloud_Speech_API goo = new Google_Cloud_Speech_API();
        
    }

    private String WavToFlac (String input) {
        
        String output = flacSound;
        
        FLAC_FileEncoder flacEncoder = new FLAC_FileEncoder();
        
        File inputFile = new File(input);
        File outputFile = new File(output);

        StreamConfiguration strcon = new StreamConfiguration(1, 
                    StreamConfiguration.DEFAULT_MIN_BLOCK_SIZE, StreamConfiguration.DEFAULT_MAX_BLOCK_SIZE,
                    8000, 16);
        
        
        
        EncodingConfiguration en = new EncodingConfiguration();
        en.setSubframeType(EncodingConfiguration.SubframeType.LPC);
        
        
        out.println(strcon.isStreamSubsetCompliant());
        out.println(strcon.isValid());
        out.println(strcon.isEncodingSubsetCompliant(en));
        
        flacEncoder.setStreamConfig( strcon );
        flacEncoder.setEncodingConfig(en);
        //flacEncoder.setEncodingConfig(null);
        
        flacEncoder.encode(inputFile, outputFile);
        
        return output;
    }
    
    public static void convertULawFileToWav(String filename) {
        
        File file = new File(filename);
        if (!file.exists())
            return;
        try {
            long fileSize = file.length();
            int frameSize = 1;
            long numFrames = fileSize / frameSize;
            out.println(fileSize);
            out.println(numFrames);
            
            AudioFormat audioFormat = new AudioFormat(Encoding.ULAW, 8000, 8, 1, frameSize, 8000, true);
            
            AudioFormat newFormat = new AudioFormat (AudioFormat.Encoding.PCM_SIGNED,
                    audioFormat.getSampleRate(), audioFormat.getSampleSizeInBits() * 2, audioFormat.getChannels(), 
                    audioFormat.getFrameSize() * 2, audioFormat.getFrameRate(), true);
            
            AudioInputStream audioInputStream =  AudioSystem.getAudioInputStream(newFormat, 
                                                    new AudioInputStream(new FileInputStream(file), audioFormat, numFrames));
            
            //AudioInputStream audioInputStream = new AudioInputStream(new FileInputStream(file), audioFormat, numFrames);
            
            AudioSystem.write(audioInputStream, Type.WAVE, new File(wavSound_cov));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
                
        /*
        AudioFormat format = new AudioFormat(Encoding.ULAW, 8000, 8, 1, 1, 8000, true);
        if (
            (format.getEncoding() ==
            AudioFormat.Encoding.ULAW)
            ||(format.getEncoding() ==
            AudioFormat.Encoding.ALAW)
        ) 
        {
          AudioFormat newFormat = new AudioFormat
          (AudioFormat.Encoding.PCM_SIGNED,
           format.getSampleRate(),
           format.getSampleSizeInBits() * 2,
           format.getChannels(),
           format.getFrameSize() * 2,
           format.getFrameRate(),
           true);

          stream =AudioSystem
           .getAudioInputStream(newFormat, stream);

          format = newFormat;
        }*/
}
    
    public Google_Cloud_Speech_API() {
        
        List<String> str = getRecognized(WavToFlac(wavSound_cov), apiKey, CHINESE_TRANDITIONAL, maxResult, sampleRate);
        
        parseResponse(str);
        
        
    }
    
    private List<String> getRecognized (String inputFile, String apiKey, String Lang, String maxResult, String sampleRate){
        
        try {
            out.println("開始上傳至Google : ");
            
            String url = GOOGLE_RECOGNIZER_URL + "&lang=" + Lang + "&key=" + apiKey + "&maxresults=" + maxResult;
            
            System.out.println("RequestURL = " + url);

            // Open New URL connection channel.
            URLConnection urlConn = new URL (url).openConnection();

            // we want to do output.
            urlConn.setDoOutput(true);

            // No caching
            urlConn.setUseCaches(false);

            // Specify the header content type.
            urlConn.setRequestProperty("Content-Type", "audio/x-flac; rate=" + sampleRate);
            urlConn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) "
                            + "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.52 Safari/537.36");

            // Send POST output.
            OutputStream outputStream = urlConn.getOutputStream();

            FileInputStream fileInputStream = new FileInputStream(inputFile);

            byte[] buffer = new byte[256];

            while ((fileInputStream.read(buffer, 0, 256)) != -1) {
                outputStream.write(buffer, 0, 256);
            }

            fileInputStream.close();
            outputStream.close();

            // Get response data.
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), Charset.forName("UTF-8")));

            List<String> completeResponse = new ArrayList<>();
            String response = br.readLine();
            while(response != null) {
                completeResponse.add(response);
                response = br.readLine();
            }

            br.close();
            
            out.println("上傳至 Google 成功");
            
            System.out.println("Complete_Response = ");
            
            completeResponse.forEach( (value) -> {
                out.println(value);
            });
            
            return completeResponse;
        }
        catch(Exception ex){
                ex.printStackTrace();
                return null;
        }
    }
    
    
    private void parseResponse(List<String> rawResponse) {
        List<Entry<Double, String>> resList = new ArrayList<>();
        
        for (String s : rawResponse) {
            JSONObject response = new JSONObject(s);
            JSONArray resultArray = response.getJSONArray("result");
            
            for(int i = 0; i < resultArray.length(); i++) {
                JSONArray alternativeArray = resultArray.getJSONObject(i).getJSONArray("alternative");
                for(int j = 0; j < alternativeArray.length(); j++) {
                    JSONObject transcript = alternativeArray.getJSONObject(j);
                    resList.add(new SimpleEntry<>(transcript.optDouble("confidence", 0.0), transcript.optString("transcript", "")));
                }
            }
        }
        
        resList.sort((s1, s2) -> -s1.getKey().compareTo(s2.getKey()));
        
        resList.forEach((entry) -> {
            out.println("confidence : " + entry.getKey());
            out.println("transcript : " + entry.getValue());
        });
        
    }
    
    
}
