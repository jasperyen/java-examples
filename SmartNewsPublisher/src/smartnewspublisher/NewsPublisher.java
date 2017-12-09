/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smartnewspublisher;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Jasper
 */
public class NewsPublisher {
    private static final Logger logger = Logger.getLogger(NewsPublisher.class.getName());
    
    private static final String API_KEY = "c28cf352f8b04661991f6aa3db8309e4";
    
    private static final String HOST = "https://api.cognitive.microsoft.com/bing/v7.0/news/search";
    
    
    //  <英文名字, 中文名字>
    private static final Map<String, String> namesMap;
    //  < 英文名字, [新聞嗜好 . . .] >
    private static final Map<String, List<String>> focusMap;
    
    
    static {
        namesMap = new TreeMap<>();
        focusMap = new TreeMap<>();
        
        loadJSONData();
    }
    
    private static void loadJSONData () {
        namesMap.clear();
        focusMap.clear();
        
        StringBuilder builder;
        try ( BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(SmartNewsPublisher.TEMP_PATH + "user_news.json") ))) {
            
            String line;
            builder = new StringBuilder();
            while ( (line = reader.readLine()) != null ) {
                builder.append(line);
            }
        } catch (IOException ex) {
            logger.log(Level.WARNING, "Read user data in IOException : {0}", ex.toString());
            return;
        }
        
        JSONObject json = new JSONObject(builder.toString());
        JSONArray array = json.getJSONArray("user");

        for (int i = 0; i < array.length(); i++) {
            JSONObject user = array.getJSONObject(i);

            namesMap.put(user.getString("enName"), user.getString("chName"));

            List<String> list = new ArrayList<>();
            for (int j = 0; j < user.getJSONArray("focus").length(); j++)
                list.add(user.getJSONArray("focus").getString(j));

            focusMap.put(user.getString("enName"), list);
        }
        logger.log(Level.INFO, "Read user data success !");
    }
    
    public NewsPublisher() {
    }
    
    public String getChName(String enName){ 
        loadJSONData();
        return namesMap.get(enName);
    }
    
    public boolean getNewsByName(String name, Map<String, String> newsMap){
        
        if (!namesMap.containsKey(name))
            return false;
        
        List<String> focusList = focusMap.get(name);
        
        int i = new Random().nextInt(focusList.size());
        String search = focusList.get(i);
        
        logger.log(Level.INFO, "Ready to search {1} for {0}", new String[]{name, search});
        
        JSONObject searchJson = searchFromBing(search);
        
        if (searchJson == null)
            return false;
        
        praseJSONResult(searchJson, newsMap);
        
        return true;
    }
    
    private JSONObject searchFromBing (String query) {
        
        try {
            URL url = new URL(HOST + "?mkt=zh-tw&q=" +  URLEncoder.encode(query, "UTF-8"));
            
            HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
            connection.setRequestProperty("Ocp-Apim-Subscription-Key", API_KEY);
            
            try (InputStream ips = connection.getInputStream()){
                String response = new Scanner(ips).useDelimiter("\\A").next();
                //System.out.println(response);
                return new JSONObject(response);
            }
        } catch (IOException ex) {
            logger.log(Level.WARNING, "Search news data in IOException : {0}", ex.toString());
        }
        
        return null;
    }
    
    private void praseJSONResult (JSONObject response, Map<String, String> newsMap) {
        
        JSONArray array = response.getJSONArray("value");
        
        for (int i = 0; i < array.length(); i++) {
            JSONObject news = array.getJSONObject(i);
            newsMap.put(news.getString("name"), news.getString("url"));
        }
    }
}
