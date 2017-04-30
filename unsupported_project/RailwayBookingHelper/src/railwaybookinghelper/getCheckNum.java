
package railwaybookinghelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import javaFlacEncoder.FLAC_FileEncoder;
import static java.lang.System.out;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class getCheckNum {
    static final String CHINESE_TRANDITIONAL = "zh-TW";
    static final String AUTO_DETECT = "auto";
    private static final String GOOGLE_RECOGNIZER_URL = "http://www.google.com/speech-api/v2/recognize?output=json";

    
    String checkSound;
    String  flacSound = "flacSound.flac";
    String apiKey = "AIzaSyATbrkRAHDuVHFYWd-k5vkKJfw6fYUSnTE";
    String sampleRate = "22050";
    String maxResult = "2";
    GoogleResponse googleResponse;
    String answer = "";
    boolean success = false;
    
    getCheckNum(String sound){
        checkSound = sound;
        
        out.println("\n\n-----開始以Google_Speech分析音檔-----");

        /*      轉換wav為flac      */
        try{
            out.println("轉換wav為flac : ");
            FLAC_FileEncoder flacEncoder = new FLAC_FileEncoder();
            String encodeResult = flacEncoder.encode(new File(checkSound), new File(flacSound) ).name();

            if ( !encodeResult.equals("OK") && !encodeResult.equals("FULL_ENCODE") )
                throw new Exception("Encode Error : " + encodeResult);
           
            out.println("Encode_Result : " + encodeResult);   
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        
        /*      向GoogleSpeech送出辨識請求     */
        String [] rawResponse = getRecognized(flacSound, apiKey, getCheckNum.CHINESE_TRANDITIONAL, maxResult, sampleRate);
        if (rawResponse == null) {
            out.println("There are no response !");
            return;
        }

        googleResponse = new GoogleResponse();
        parseResponse(rawResponse, googleResponse);
        
        analyzeResponse();
        
        out.println("-----音檔分析完成-----");
    }
    
    void analyzeResponse(){             //處理辨識結果成為驗證碼
        int  j = 0;
        String[] stmp = new String[2];
        
        for (String str : googleResponse.getAllPossibleResponses()){
            if (j == 2)
                break;
            if (str == null)
                continue;
            out.print("change " + str + " to : ");
            stmp[j] = str;
            stmp[j] = stmp[j].replaceAll(" ", "");
            stmp[j] = stmp[j].replaceAll("奇", "7");
            stmp[j] = stmp[j].replaceAll("雞", "7");
            stmp[j] = stmp[j].replaceAll("是", "4");
            stmp[j] = stmp[j].replaceAll("酒", "9");
            stmp[j] = stmp[j].replaceAll("無5", "5");
            stmp[j] = stmp[j].replaceAll("無", "5");
            stmp[j] = stmp[j].replaceAll("六", "6");
            stmp[j] = stmp[j].replaceAll("林", "0");
            stmp[j] = stmp[j].replaceAll("三", "3");
            stmp[j] = stmp[j].replaceAll("11", "1");
            stmp[j] = stmp[j].replaceAll("5梧", "5");
            stmp[j] = stmp[j].replaceAll("棲", "7");
            out.println(stmp[j++]);
        }
        if ( stmp[0].length() == 6 || stmp[0].length() == 5 ){
            answer = stmp[0];
            out.println("answer = " + answer);
            return;
        }
        else if ( stmp[1].length() == 6 || stmp[1].length() == 5 ){
            answer = stmp[1];
            out.println("answer = " + answer);
            return;
        }
        else if (stmp[0].length() < 5 && stmp[1].length() < 5) {
            answer = stmp[0] + "0";
            out.println("answer = " + answer);
            return;
        }
        
        while ( j++ < 10) {
            for (int i = 0; i < 2; i++) {
                out.print("change " + stmp[i] + " to : ");
                stmp[i] = stmp[i].replaceFirst("1", "");
                out.println(stmp[i]);
                answer = stmp[i];
                if (stmp[i].length() == 5 || stmp[0].length() == 6)
                    break;
            }
             if ( stmp[0].length() == 5 || stmp[0].length() == 6){
                answer = stmp[0];
                out.println("answer = " + answer);
                return;
            }
            else if (stmp[1].length() == 5 || stmp[0].length() == 6){
                answer = stmp[1];
                out.println("answer = " + answer);
                return;
            }
        }
    }
    
    String[] getRecognized (String inputFile, String apiKey, String Lang, String maxResult, String sampleRate){
        try {
            out.println("開始上傳至Google : ");
            
            StringBuilder url = new StringBuilder(getCheckNum.GOOGLE_RECOGNIZER_URL);
            url.append("&lang=" + Lang + "&key=" + apiKey + "&maxresults=" + maxResult);
            
            System.out.println("RequestURL = " + url);

            // Open New URL connection channel.
            URLConnection urlConn = new URL (url.toString()).openConnection();

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

            List<String> completeResponse = new ArrayList<String>();
            String response = br.readLine();
            while(response != null) {
                completeResponse.add(response);
                response = br.readLine();
            }

            br.close();
            
            out.println("上傳至 Google 成功");
            
            System.out.println("Complete_Response = " + completeResponse);
            
            return completeResponse.toArray(new String[completeResponse.size()]);
        }
        catch(Exception ex){
                ex.printStackTrace();
                return null;
        }
    }
    
    private void parseResponse(String[] rawResponse, GoogleResponse googleResponse) {

        for(String s : rawResponse) {
            JSONObject jsonResponse = new JSONObject(s);
            JSONArray jsonResultArray = jsonResponse.getJSONArray("result");
            for(int i = 0; i < jsonResultArray.length(); i++) {
                JSONObject jsonAlternativeObject = jsonResultArray.getJSONObject(i);
                JSONArray jsonAlternativeArray = jsonAlternativeObject.getJSONArray("alternative");
                double prevConfidence = 0;
                for(int j = 0; j < jsonAlternativeArray.length(); j++) {
                    JSONObject jsonTranscriptObject = jsonAlternativeArray.getJSONObject(j);
                    String transcript = jsonTranscriptObject.optString("transcript", "");
                    double confidence = jsonTranscriptObject.optDouble("confidence", 0.0);
                    if(confidence > prevConfidence) {
                        googleResponse.setResponse(transcript);
                        googleResponse.setConfidence(String.valueOf(confidence));
                        prevConfidence = confidence;
                    } else
                        googleResponse.getOtherPossibleResponses().add(transcript);
                }
            }
        }
    }
    
}
