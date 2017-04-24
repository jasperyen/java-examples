/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package speechvoicedetection;

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
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Jasper
 */
public class ResponesByGoogle {
    private static final Logger logger = Logger.getLogger(ResponesByGoogle.class.getName());
    
    private static final String MAX_RESULT = "1";
    private static final String CHINESE_TRANDITIONAL = "zh-TW";
    private static final String APIKEY = "AIzaSyBsIounZbhWreS9StJyp0wygE5cxsDhDOo";
    private static final String GOOGLE_RECOGNIZER_URL = "http://www.google.com/speech-api/v2/recognize?output=json";
    
    private final String sampleRate;
    private final File soundFile;
    private final List<String> rawResponse;
    private final List<Map.Entry<Double, String>> googleResponse;        

    public List<Map.Entry<Double, String>> getGoogleResponse() {
        return googleResponse;
    }
    
    public ResponesByGoogle(File soundFile, int sampleRate) throws IOException, JSONException {
        this.soundFile = soundFile;
        rawResponse = new ArrayList<>();
        googleResponse = new ArrayList<>();
        
        this.sampleRate = String.valueOf(sampleRate);

        getRecognized();
        parseResponse();
    }
    
    private void getRecognized () throws IOException {
        out.println("Start upload to Google : ");

        String url = GOOGLE_RECOGNIZER_URL + "&lang=" + CHINESE_TRANDITIONAL + "&key=" + APIKEY + "&maxresults=" + MAX_RESULT;
        out.println("RequestURL = " + url);

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
        try (OutputStream outputStream = urlConn.getOutputStream();
                FileInputStream fileInputStream = new FileInputStream(soundFile)) {

            byte[] buffer = new byte[256];
            while ((fileInputStream.read(buffer, 0, 256)) != -1) {
                outputStream.write(buffer, 0, 256);
            }
        }

        // Get response data.
        try (InputStreamReader insr = new InputStreamReader(urlConn.getInputStream(), Charset.forName("UTF-8"));
                BufferedReader br = new BufferedReader(insr)) {
            String response;
            while(( response = br.readLine() ) != null) {
                rawResponse.add(response);
            }
        }

        out.println("Upload to Google success !");
        
        /*
        System.out.println("Complete_Response = ");
        rawResponse.forEach( (value) -> {
            out.println(value);
        });
        */
    }

    
    private void parseResponse() throws JSONException {
        for (String s : rawResponse) {
            JSONObject response = new JSONObject(s);
            JSONArray resultArray = response.getJSONArray("result");
            for(int i = 0; i < resultArray.length(); i++) {
                JSONArray alternativeArray = resultArray.getJSONObject(i).getJSONArray("alternative");
                for(int j = 0; j < alternativeArray.length(); j++) {
                    JSONObject transcript = alternativeArray.getJSONObject(j);
                    googleResponse.add(new AbstractMap.SimpleEntry<>(transcript.optDouble("confidence", 0.0), transcript.optString("transcript", "")));
                }
            }
        }
        googleResponse.sort((s1, s2) -> -s1.getKey().compareTo(s2.getKey()));
        
        /*
        googleResponse.forEach((entry) -> {
            out.println("confidence : " + entry.getKey());
            out.println("transcript : " + entry.getValue());
        });
        */
    }

}
