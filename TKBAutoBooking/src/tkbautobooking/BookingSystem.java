/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tkbautobooking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.System.out;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Logger;
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Jasper
 */
public class BookingSystem {

    private static final Logger logger = Logger.getLogger(BookingSystem.class.getName());
    
    private String userId;
    private String userPassword;
    
    private String LoginPageHTML;
    private String BookingPageHTML;
    private String ClassroomJSON;
    private String DateJSON;
    private String TimeJSON;
    private String BookingResultJSON;
    private String login_hidden_token;
    private String booking_hidden_token;
    private Map<String, String> cookieMap;
    private Map<String, String> classMap;
    private Map<String, String> dateMap;
    private Map<String, String> classroomMap;
    private Map<String, Map<String, String>> timeMap;
    
    
    public BookingSystem(String id, String pwd) throws Exception {
        userId = id;
        userPassword = pwd;
        
        getLoginPage();
        praseLoginPage();
        postLoginInfo();
        getBookingPage();
        praseBookingPage();
        praseBookingToken();
    }

    public Map<String, String> getClassMap() {
        return classMap;
    }

    public Map<String, String> getClassroomMap() {
        return classroomMap;
    }

    public Map<String, String> getDateMap() {
        return dateMap;
    }

    public Map<String, Map<String, String>> getTimeMap() {
        return timeMap;
    }
   
    
    
    public void chooseClass(String classValue) throws IOException {
        
        postClassData(classValue);
        praseClassroomJSON();
        praseDateJSON();
        
    }
    
    
    public void chooseClassroom(String classroomValue) throws IOException {
     
        timeMap = new TreeMap<>();
        for (Entry<String, String> entry : dateMap.entrySet()) {
            postClassroomData(entry.getKey(), classroomValue);
            Map<String, String> map = praseTimeJSON();
            timeMap.put(entry.getKey(), map);
        }
        
    }
    
    
    public List<String> checkBookingSeat (String classroomValue, String dateValue) throws IOException {
        
        postClassroomData(dateValue, classroomValue);
        List<String> haveSeatList = praseBookingSeat();
        
        return haveSeatList;
    }
    
    public boolean goBooking (String classValue, String classroomValue, String dateValue, String timeValue) throws Exception {
    
        getBookingPage();
        praseBookingToken();
        postBookingData(classValue, classroomValue, dateValue, timeValue);
        boolean isBookingSuccess = praseBookingResult();
        
        return isBookingSuccess;
    }
    
    public void reLogin () throws Exception {
        
        getLoginPage();
        praseLoginPage();
        postLoginInfo();
        
    }
    
    private void getLoginPage () throws IOException {
        
        try ( CloseableHttpClient httpClient = HttpClients.createDefault() ) {
            HttpGet httpGet = new HttpGet(BookingParam.WEB_LOGIN_URI);
            httpGet.setConfig(BookingParam.REQUEST_PARAMS);
            
            try ( CloseableHttpResponse httpResponse = httpClient.execute(httpGet) ) {  
                //  HttpStatusCode != 200
                if ( httpResponse.getStatusLine().getStatusCode() !=  HttpStatus.SC_OK) {
                    String ErrorCode = "Response Status : " + httpResponse.getStatusLine().getStatusCode();
                    throw new IOException(ErrorCode);
                }
                
                try ( InputStreamReader isr = new InputStreamReader(httpResponse.getEntity().getContent(), StandardCharsets.UTF_8);
                        BufferedReader reader = new BufferedReader(isr) ) {
                    
                    cookieMap = new TreeMap<>();
                    for (Header header : httpResponse.getHeaders("Set-Cookie")) {
                        //out.println(header);
                        String hstr = header.getValue().split(";")[0];
                        cookieMap.put(hstr.split("=", 2)[0], hstr.split("=", 2)[1]);
                    }
                    
                    String line;
                    StringBuilder builder = new StringBuilder();
                    
                    while ( (line = reader.readLine()) != null ) {
                        builder.append(line);
                        builder.append("\n");
                    }
                    LoginPageHTML = builder.toString();
                }
            }
        }
    }
    
    private void praseLoginPage () throws Exception {
        
        Document doc = Jsoup.parse(LoginPageHTML);
        Elements token_elm = doc.getElementsByAttributeValue("name", "access_token");
        
        if (token_elm.isEmpty() || token_elm.size() > 1 || !token_elm.first().hasAttr("value"))
            throw new Exception("Prase Login Page fail !");
        
        login_hidden_token = token_elm.first().attr("value");
        
    }
    
    private void postLoginInfo () throws IOException {
    
        try ( CloseableHttpClient httpClient = HttpClients.createDefault() ) {
            HttpPost httpPost = new HttpPost(BookingParam.WEB_LOGIN_POST_URI);
            httpPost.setConfig(BookingParam.REQUEST_PARAMS);
            httpPost.addHeader("Referer", BookingParam.WEB_LOGIN_URI);
            
            setCookieData(httpPost);
            
            //  設定封包參數
            List<NameValuePair> nvps = new ArrayList<>();
            nvps.add(new BasicNameValuePair("id", userId));
            nvps.add(new BasicNameValuePair("pwd", userPassword));
            nvps.add(new BasicNameValuePair("access_token", login_hidden_token));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));

            try ( CloseableHttpResponse httpResponse = httpClient.execute(httpPost) ) {
                //  HttpStatusCode != 302
                if ( httpResponse.getStatusLine().getStatusCode() !=  HttpStatus.SC_MOVED_TEMPORARILY) {
                    String ErrorCode = "Response Status : " + httpResponse.getStatusLine().getStatusCode() + "\n";
                    ErrorCode += "登入失敗"; 
                    throw new IOException(ErrorCode);
                }
                /*
                try ( InputStreamReader isr = new InputStreamReader(httpResponse.getEntity().getContent(), StandardCharsets.UTF_8);
                        BufferedReader reader = new BufferedReader(isr) ) {
                    String line;
                    StringBuilder builder = new StringBuilder();
                    
                    while ( (line = reader.readLine()) != null ) {
                        builder.append(line);
                        builder.append("\n");
                    }
                }*/
            }
        }
        
    }
    
    private void getBookingPage () throws IOException {
        
        try ( CloseableHttpClient httpClient = HttpClients.createDefault() ) {
            HttpGet httpGet = new HttpGet(BookingParam.WEB_BOOKING_URI);
            httpGet.setConfig(BookingParam.REQUEST_PARAMS);
            httpGet.addHeader("Referer", BookingParam.WEB_LOGIN_POST_URI);
            
            setCookieData(httpGet);
            
            try ( CloseableHttpResponse httpResponse = httpClient.execute(httpGet) ) {  
                //  HttpStatusCode != 200
                if ( httpResponse.getStatusLine().getStatusCode() !=  HttpStatus.SC_OK) {
                    String ErrorCode = "Response Status : " + httpResponse.getStatusLine().getStatusCode();
                    throw new IOException(ErrorCode);
                }
                
                try ( InputStreamReader isr = new InputStreamReader(httpResponse.getEntity().getContent(), StandardCharsets.UTF_8);
                        BufferedReader reader = new BufferedReader(isr) ) {
                    
                    String line;
                    StringBuilder builder = new StringBuilder();
                    
                    while ( (line = reader.readLine()) != null ) {
                        builder.append(line);
                        builder.append("\n");
                    }
                    BookingPageHTML = builder.toString();
                }
            }
        }
    }
    
    
    private void praseBookingPage () throws Exception {
        
        Document doc = Jsoup.parse(BookingPageHTML);
        Element class_selector = doc.getElementById("class_selector");
        
        if (class_selector == null)
            throw new Exception("Prase Booking Page fail !");
        
        classMap = new TreeMap<>();
        for (Element option : class_selector.getElementsByTag("option")) {
            if (option.attr("value").equals(""))
                continue;
            
            classMap.put(option.attr("value"), option.text().replace(" ", " "));
        }
    }
    
    
    private void praseBookingToken () throws Exception {
        
        Document doc = Jsoup.parse(BookingPageHTML);
        Element script = doc.getElementsByTag("script").last();
        
        String str = script.toString().substring(script.toString().indexOf("access_token"));
        str = str.substring(str.indexOf("\"") + 1);
        str = str.substring(0, str.indexOf("\""));
        
        booking_hidden_token = str;
    }
    
    
    private void postClassData (String classValue) throws IOException {
        
        try ( CloseableHttpClient httpClient = HttpClients.createDefault() ) {
            HttpPost httpPost = new HttpPost(BookingParam.WEB_BOOKING_CLASSROOM_URI);
            httpPost.setConfig(BookingParam.REQUEST_PARAMS);
            httpPost.addHeader("Referer", BookingParam.WEB_BOOKING_URI);
            
            setCookieData(httpPost);

            List<NameValuePair> nvps = new ArrayList<>();
            nvps.add(new BasicNameValuePair("class_data", classValue));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));

            try ( CloseableHttpResponse httpResponse = httpClient.execute(httpPost) ) {
                //  HttpStatusCode != 200
                if ( httpResponse.getStatusLine().getStatusCode() !=  HttpStatus.SC_OK) {
                    String ErrorCode = "Response Status : " + httpResponse.getStatusLine().getStatusCode();
                    throw new IOException(ErrorCode);
                }
                
                try ( InputStreamReader isr = new InputStreamReader(httpResponse.getEntity().getContent(), StandardCharsets.UTF_8);
                        BufferedReader reader = new BufferedReader(isr) ) {
                    String line;
                    StringBuilder builder = new StringBuilder();
                    
                    while ( (line = reader.readLine()) != null ) {
                        builder.append(line);
                        builder.append("\n");
                    }
                    ClassroomJSON = builder.toString();
                }
            }
        }
        
        JSONObject json = new JSONArray(ClassroomJSON).getJSONObject(0);
        
        try ( CloseableHttpClient httpClient = HttpClients.createDefault() ) {
            HttpPost httpPost = new HttpPost(BookingParam.WEB_BOOKING_DATE_URI);
            httpPost.setConfig(BookingParam.REQUEST_PARAMS);
            httpPost.addHeader("Referer", BookingParam.WEB_BOOKING_URI);
            
            setCookieData(httpPost);
      
            List<NameValuePair> nvps = new ArrayList<>();
            nvps.add(new BasicNameValuePair("effectiveDate", json.getString("EFFECTIVE_DATE")));
            nvps.add(new BasicNameValuePair("class_data", classValue));
            nvps.add(new BasicNameValuePair("class_status", json.getString("CLASS_STATUS")));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));

            try ( CloseableHttpResponse httpResponse = httpClient.execute(httpPost) ) {
                //  HttpStatusCode != 200
                if ( httpResponse.getStatusLine().getStatusCode() !=  HttpStatus.SC_OK) {
                    String ErrorCode = "Response Status : " + httpResponse.getStatusLine().getStatusCode();
                    throw new IOException(ErrorCode);
                }
                
                try ( InputStreamReader isr = new InputStreamReader(httpResponse.getEntity().getContent(), StandardCharsets.UTF_8);
                        BufferedReader reader = new BufferedReader(isr) ) {
                    String line;
                    StringBuilder builder = new StringBuilder();
                    
                    while ( (line = reader.readLine()) != null ) {
                        builder.append(line);
                        builder.append("\n");
                    }
                    DateJSON = builder.toString();
                }
            }
        }
    }
    
    private void praseClassroomJSON () {
        JSONArray jsonArray = new JSONArray(ClassroomJSON);
        classroomMap = new TreeMap<>();
        
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            classroomMap.put(json.getString("LOCATION"), json.getString("BRANCH_NAME"));
        }
    }
    
    private void praseDateJSON () {
        JSONArray jsonArray = new JSONArray(DateJSON);
        dateMap = new TreeMap<>();
        
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            dateMap.put(json.getString("DATE_VALUE"), json.getString("DATE_STRING"));
        }
    }
    
    
    private void postClassroomData (String dateValue, String classroomValue) throws IOException {
    
        try ( CloseableHttpClient httpClient = HttpClients.createDefault() ) {
            HttpPost httpPost = new HttpPost(BookingParam.WEB_BOOKING_TIME_URI);
            httpPost.setConfig(BookingParam.REQUEST_PARAMS);
            httpPost.addHeader("Referer", BookingParam.WEB_BOOKING_URI);
            
            setCookieData(httpPost);

            List<NameValuePair> nvps = new ArrayList<>();
            nvps.add(new BasicNameValuePair("date", dateValue));
            nvps.add(new BasicNameValuePair("branch_no", classroomValue));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));

            try ( CloseableHttpResponse httpResponse = httpClient.execute(httpPost) ) {
                //  HttpStatusCode != 200
                if ( httpResponse.getStatusLine().getStatusCode() !=  HttpStatus.SC_OK) {
                    String ErrorCode = "Response Status : " + httpResponse.getStatusLine().getStatusCode();
                    throw new IOException(ErrorCode);
                }
                
                try ( InputStreamReader isr = new InputStreamReader(httpResponse.getEntity().getContent(), StandardCharsets.UTF_8);
                        BufferedReader reader = new BufferedReader(isr) ) {
                    String line;
                    StringBuilder builder = new StringBuilder();
                    
                    while ( (line = reader.readLine()) != null ) {
                        builder.append(line);
                        builder.append("\n");
                    }
                    TimeJSON = builder.toString();
                }
            }
        }
    }
    
    private Map<String, String> praseTimeJSON () {
        JSONArray jsonArray = new JSONArray(TimeJSON);
        Map<String, String> map = new TreeMap<>();
        
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            
            String timeData = json.getString("INIT_TIME") + " ~ " + json.getString("END_TIME");
            
            if (!json.getString("HASCLASS").equals("0"))
                timeData += " 該場次已預約";
            else if (json.getString("SEATNUM").equals("0"))
                timeData += " 此時段已滿場";
            else if (!json.getString("OFFCLASS").equals("0"))
                timeData += " 該場次己停課";
            else
                timeData += " 該時段座位數 : " + json.getString("SEATNUM");
            
            
            map.put(json.getString("SEGMENT"), timeData);
        }
        
        return map;
    }
    
    
    private List<String> praseBookingSeat () {
        JSONArray jsonArray = new JSONArray(TimeJSON);
        List<String> haveSeatList = new ArrayList<>();
        
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            
            if ( json.getString("HASCLASS").equals("0") && json.getString("OFFCLASS").equals("0")) {
                int seatNum = Integer.parseInt(json.getString("SEATNUM"));
                if (seatNum > 0)
                    haveSeatList.add(json.getString("SEGMENT"));
            }
        }
        
        return haveSeatList;
    }
    
    
    private void postBookingData (String classValue, String classroomValue, String dateValue, String timeValue) throws IOException {
    
        try ( CloseableHttpClient httpClient = HttpClients.createDefault() ) {
            HttpPost httpPost = new HttpPost(BookingParam.WEB_POST_BOOKING_URI);
            httpPost.setConfig(BookingParam.REQUEST_PARAMS);
            httpPost.addHeader("Referer", BookingParam.WEB_BOOKING_URI);
            
            setCookieData(httpPost);

            List<NameValuePair> nvps = new ArrayList<>();
            nvps.add(new BasicNameValuePair("class_data", classValue));
            nvps.add(new BasicNameValuePair("date", dateValue));
            nvps.add(new BasicNameValuePair("branch_no", classroomValue));
            nvps.add(new BasicNameValuePair("session_time[]", timeValue));
            nvps.add(new BasicNameValuePair("access_token", booking_hidden_token));
            
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));

            try ( CloseableHttpResponse httpResponse = httpClient.execute(httpPost) ) {
                //  HttpStatusCode != 200
                if ( httpResponse.getStatusLine().getStatusCode() !=  HttpStatus.SC_OK) {
                    String ErrorCode = "Response Status : " + httpResponse.getStatusLine().getStatusCode();
                    throw new IOException(ErrorCode);
                }
                
                try ( InputStreamReader isr = new InputStreamReader(httpResponse.getEntity().getContent(), StandardCharsets.UTF_8);
                        BufferedReader reader = new BufferedReader(isr) ) {
                    String line;
                    StringBuilder builder = new StringBuilder();
                    
                    while ( (line = reader.readLine()) != null ) {
                        builder.append(line);
                        builder.append("\n");
                    }
                    BookingResultJSON = builder.toString();
                }
            }
        }
    }
    
    private boolean praseBookingResult () {
        JSONObject json = new JSONObject(BookingResultJSON);
        
        return json.getString("MESSAGE").contains("預約成功");
    }
    
    private void setCookieData (AbstractHttpMessage httpMessage) {
        StringBuilder CookieBuilder = new StringBuilder();
        
        cookieMap.forEach((key, value) -> {
            CookieBuilder.append("; ").append(key).append("=").append(value);
        });
        
        if (CookieBuilder.length() > 2)
            httpMessage.setHeader("Cookie", CookieBuilder.substring(2));
    }
    
}
