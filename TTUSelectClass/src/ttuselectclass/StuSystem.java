/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ttuselectclass;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import static java.lang.System.out;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.GZIPInputStream;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Jasper
 */
public class StuSystem {
    
    private static final String Host = "http://stucis.ttu.edu.tw/";
    
    private String ID = "";
    private String PWD = "";
    private String StuNo = "";
    private String StuName = "";
    
    private List<String> cookie;
    private StringBuilder CourseWebStr;
    private Map<String, String> classData;
    
    
    private Map<String, String> depMap;
    private Map<String, String> classMap;
    
    String CourseWeb = "C:\\workspace c\\webbb\\ListClassCourse.htm";
    String LimitWeb = "C:\\workspace c\\webbb\\seldeny.htm";
    
    private void GetLogin() throws Exception{
        try{
            out.println("\n-----取得登入頁面-----");
            
            String loginURL = Host + "login.php";
            //out.println(loginURL);
            
            HttpURLConnection conn = (HttpURLConnection)new URL(loginURL).openConnection();
            
            conn.setRequestMethod("GET"); 
            
            conn.setRequestProperty("Accept",
                                                            "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            conn.setRequestProperty("Upgrade-Insecure-Requests", "1");
            
            conn.setRequestProperty("Accept-Language",
                                                            "zh-TW,zh;q=0.8,en-US;q=0.6,en;q=0.4,zh-CN;q=0.2");
            conn.setRequestProperty("User-Agent",
                                                            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36");
            conn.setRequestProperty("Accept-Encoding",
                                                            "gzip, deflate, sdch");
            conn.setRequestProperty("Host", Host);
            
            conn.setRequestProperty("Connection",
                                                            "keep-alive");

            InputStream ips;
            String encode = conn.getContentEncoding();
            
            if (encode != null) {
                out.println("content-encoding :: " + encode);
                if (!encode.equals("gzip"))
                    throw new Exception("CanNotEncodeInputStream");
                ips = new GZIPInputStream(conn.getInputStream());
            }
            else {
                out.println("content-encoding :: null, encode in default !");
                ips = conn.getInputStream();
            }
            try ( //FileOutputStream checkWeb = new FileOutputStream(LogWeb) ;
                     BufferedReader buff = new BufferedReader( new InputStreamReader(ips, "big5") 
                     ) 
                    ){
                
                cookie = new ArrayList<String>();
                //out.println("Reponse Cookie : ");
                
                for (Object str : conn.getHeaderFields().get("Set-Cookie")){
                    cookie.add(str.toString().split(";", 2)[0]);
                    //out.println(str.toString().split(";", 2)[0]);
                }
                /*
                String line;
                while ( (line = buff.readLine()) != null ){
                    //out.println(line);
                    checkWeb.write(line.getBytes("big5"));
                }
                */
            }
            finally{
                ips.close();
            }

            
            out.println("-----取得登入頁面成功-----\n");
            
        }
        catch(Exception ex){
            ex.printStackTrace();
            throw new Exception("GetLoginFailed");
        }
    }
    
    
    private void PostStu() throws Exception {
        try{
            out.println("-----開始登入學生資訊系統-----");

            String postWeb = Host + "login.php";                              
            String postData;                                                                                //請求資料
            
            postData = "ID=" + ID + "&PWD=" + PWD + "&Submit=%B5n%A4J%A8t%B2%CE";
            String requestCookie = cookie.get(0).toString();
            for (int i = 1; i < cookie.size(); i++){
                requestCookie = requestCookie + "; " + cookie.get(i);
            }
            //ID=410306224&PWD=jasper826&Submit=%B5n%A4J%A8t%B2%CE
            
            //out.println(postWeb);
            //out.println("postData : " + postData);
            //out.println("Request Cookie : " + requestCookie);
            
            /*      設定連線參數         */
            
            HttpURLConnection conn = (HttpURLConnection) new URL(postWeb).openConnection();
            
            conn.setDoOutput(true); 
            conn.setDoInput(true); 
            conn.setRequestMethod("POST"); 
            conn.setUseCaches(false); 
            conn.setAllowUserInteraction(true); 
            conn.setFollowRedirects(true); 
            conn.setInstanceFollowRedirects(true); 
            
            conn.setRequestProperty("Host", Host);
            
            conn.setRequestProperty("Connection",
                                                            "Keep-Alive");
            conn.setRequestProperty("Content-Length",
                                                            String.valueOf(postData.getBytes().length));
            conn.setRequestProperty("Cache-Control",
                                                            "max-age=0");
            conn.setRequestProperty("Accept",
                                                            "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            conn.setRequestProperty("Origin", Host);
            
            conn.setRequestProperty("Upgrade-Insecure-Requests", "1");
            
            conn.setRequestProperty("User-Agent",
                                                            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36");
            conn.setRequestProperty("Content-Type",
                                                            "application/x-www-form-urlencoded");
            conn.setRequestProperty("Referer", 
                                                            Host + "login.php");
            conn.setRequestProperty("Accept-Encoding",
                                                            "gzip, deflate");
            conn.setRequestProperty("Accept-Language",
                                                            "zh-TW,zh;q=0.8,en-US;q=0.6,en;q=0.4,zh-CN;q=0.2");

            conn.setRequestProperty("Cookie", requestCookie);
            
            /*      設定連線參數結束         */
            
            
            
            /*           送出請求資料           */
            
            try ( DataOutputStream postStream = new DataOutputStream(conn.getOutputStream()) ){
                postStream.writeBytes(postData);
            }
            catch (Exception ex){
                out.println("-----POST_Failed-----");
                ex.printStackTrace();
            }
            
             /*           送出請求資料結束           */
            
            InputStream ips;
            String encode = conn.getContentEncoding();
            
            if (encode != null) {
                out.println("content-encoding :: " + encode);
                if (!encode.equals("gzip"))
                    throw new Exception("CanNotEncodeInputStream");
                ips = new GZIPInputStream(conn.getInputStream());
            }
            else {
                out.println("content-encoding :: null, encode in default !");
                ips = conn.getInputStream();
            }
            try ( //FileOutputStream checkWeb = new FileOutputStream("") ;
                     BufferedReader buff = new BufferedReader( new InputStreamReader(ips, "big5") ) 
                    ){
                /*
                String line;
                while ( (line = buff.readLine()) != null ){
                    //out.println(line);
                    checkWeb.write(line.getBytes("big5"));
                }
                   */     
            }
            finally{
                ips.close();
            }

            out.println("-----登入成功-----\n");
                     
        }
        catch(Exception ex){
            ex.printStackTrace();
            throw new Exception("PostLoginFailed");
        }
       

    }
    
    public void GetCourseList() throws Exception{
        try{
            out.println("-----取得選課頁面-----");
            
            String loginURL = Host + "selcourse/ListClassCourse.php";
            //out.println(loginURL);
            
            String requestCookie = cookie.get(0).toString();
            for (int i = 1; i < cookie.size(); i++){
                requestCookie = requestCookie + "; " + cookie.get(i);
            }
            
            HttpURLConnection conn = (HttpURLConnection)new URL(loginURL).openConnection();
            
            conn.setRequestMethod("GET"); 
            
            conn.setRequestProperty("Accept",
                                                            "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            conn.setRequestProperty("Upgrade-Insecure-Requests", "1");
            
            conn.setRequestProperty("Accept-Language",
                                                            "zh-TW,zh;q=0.8,en-US;q=0.6,en;q=0.4,zh-CN;q=0.2");
            conn.setRequestProperty("User-Agent",
                                                            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36");
            conn.setRequestProperty("Accept-Encoding",
                                                            "gzip, deflate, sdch");
            conn.setRequestProperty("Referer",
                                                            Host + "menu/selmenu.htm");
            conn.setRequestProperty("Host", Host);
            
            conn.setRequestProperty("Connection",
                                                            "keep-alive");
            conn.setRequestProperty("Cookie", requestCookie);
            

            InputStream ips;
            String encode = conn.getContentEncoding();
            
            if (encode != null) {
                out.println("content-encoding :: " + encode);
                if (!encode.equals("gzip"))
                    throw new Exception("CanNotEncodeInputStream");
                ips = new GZIPInputStream(conn.getInputStream());
            }
            else {
                out.println("content-encoding :: null, encode in default !");
                ips = conn.getInputStream();
            }
            try ( //FileOutputStream Web = new FileOutputStream(CourseWeb) ;
                     BufferedReader buff = new BufferedReader( new InputStreamReader(ips, "big5") ) 
                    ){
                
                String line;
                CourseWebStr = new StringBuilder();
                while ( (line = buff.readLine()) != null ){
                    CourseWebStr.append(line);
                    //out.println(line);
                    //Web.write(line.getBytes("big5"));
                }
                
            }
            finally{
                ips.close();
            }

            
            out.println("-----取得選課頁面成功-----\n");
            
            praseSelectClass();
        }
        catch(Exception ex){
            ex.printStackTrace();
            throw new Exception("GetCourseListFailed");
        }
    }
    
    public void GetCourseMain() throws Exception {
            try{
            out.println("-----取得選課主頁面-----");
            
            String loginURL = Host + "menu/seltop.php";
            //out.println(loginURL);
            
            String requestCookie = cookie.get(0).toString();
            for (int i = 1; i < cookie.size(); i++){
                requestCookie = requestCookie + "; " + cookie.get(i);
            }
            
            HttpURLConnection conn = (HttpURLConnection)new URL(loginURL).openConnection();
            
            conn.setRequestMethod("GET"); 
            
            conn.setRequestProperty("Accept",
                                                            "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            conn.setRequestProperty("Upgrade-Insecure-Requests", "1");
            
            conn.setRequestProperty("Accept-Language",
                                                            "zh-TW,zh;q=0.8,en-US;q=0.6,en;q=0.4,zh-CN;q=0.2");
            conn.setRequestProperty("User-Agent",
                                                            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36");
            conn.setRequestProperty("Accept-Encoding",
                                                            "gzip, deflate, sdch");
            conn.setRequestProperty("Referer",
                                                            Host + "menu/selmenu.htm");
            conn.setRequestProperty("Host", Host);
            
            conn.setRequestProperty("Connection",
                                                            "keep-alive");
            conn.setRequestProperty("Cookie", requestCookie);
            

            InputStream ips;
            String encode = conn.getContentEncoding();
            
            if (encode != null) {
                out.println("content-encoding :: " + encode);
                if (!encode.equals("gzip"))
                    throw new Exception("CanNotEncodeInputStream");
                ips = new GZIPInputStream(conn.getInputStream());
            }
            else {
                out.println("content-encoding :: null, encode in default !");
                ips = conn.getInputStream();
            }
            try ( //FileOutputStream Web = new FileOutputStream(CourseWeb) ;
                     BufferedReader buff = new BufferedReader( new InputStreamReader(ips, "big5") ) 
                    ){
                /*
                String line;
                while ( (line = buff.readLine()) != null ){
                    //out.println(line);
                    //Web.write(line.getBytes("big5"));
                }
                */
            }
            finally{
                ips.close();
            }

            
            out.println("-----取得選課主頁面成功-----\n");
        }
        catch(Exception ex){
            ex.printStackTrace();
            throw new Exception("GetCourseMainFailed");
        }

    }
    
    public void PostCourseList(String dep, String cla) throws Exception{
        try{
            out.println("-----取得選課頁面-----");
            
            String loginURL = Host + "selcourse/ListClassCourse.php";
            //out.println(loginURL);
            
            String postData = "SelDepNo=" + dep + "&SelClassNo=" + cla;
            //out.println("postData : " + postData);
            
            String requestCookie = cookie.get(0).toString();
            for (int i = 1; i < cookie.size(); i++){
                requestCookie = requestCookie + "; " + cookie.get(i);
            }
            
            HttpURLConnection conn = (HttpURLConnection)new URL(loginURL).openConnection();
            
            conn.setDoOutput(true); 
            conn.setDoInput(true); 
            conn.setRequestMethod("POST"); 
            conn.setUseCaches(false); 
            conn.setAllowUserInteraction(true); 
            conn.setFollowRedirects(true); 
            conn.setInstanceFollowRedirects(true); 
            
            
            conn.setRequestProperty("Content-Length",
                                                            String.valueOf(postData.getBytes().length));
            
            conn.setRequestProperty("Accept",
                                                            "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            conn.setRequestProperty("Upgrade-Insecure-Requests", "1");
            
            conn.setRequestProperty("Origin", Host);
            
            conn.setRequestProperty("Cache-Control",
                                                            "max-age=0");
            conn.setRequestProperty("Accept-Language",
                                                            "zh-TW,zh;q=0.8,en-US;q=0.6,en;q=0.4,zh-CN;q=0.2");
            conn.setRequestProperty("User-Agent",
                                                            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36");
            conn.setRequestProperty("Accept-Encoding",
                                                            "gzip, deflate");
            conn.setRequestProperty("Referer",
                                                            Host + "selcourse/ListClassCourse.php");
            conn.setRequestProperty("Host", Host);
            
            conn.setRequestProperty("Connection",
                                                            "keep-alive");
            conn.setRequestProperty("Cookie", requestCookie);
            
            conn.setRequestProperty("Content-Type",
                                                            "application/x-www-form-urlencoded");
            
            
            try ( DataOutputStream postStream = new DataOutputStream(conn.getOutputStream()) ){
                postStream.writeBytes(postData);
            }
            catch (Exception ex){
                out.println("-----POST_Failed-----");
                ex.printStackTrace();
            }
            
            InputStream ips;
            String encode = conn.getContentEncoding();
            
            if (encode != null) {
                out.println("content-encoding :: " + encode);
                if (!encode.equals("gzip"))
                    throw new Exception("CanNotEncodeInputStream");
                ips = new GZIPInputStream(conn.getInputStream());
            }
            else {
                out.println("content-encoding :: null, encode in default !");
                ips = conn.getInputStream();
            }
            try (  //FileOutputStream Web = new FileOutputStream(CourseWeb) ;
                     BufferedReader buff = new BufferedReader( new InputStreamReader(ips, "big5") ) 
                    ){
                
                String line;
                CourseWebStr = new StringBuilder();
                while ( (line = buff.readLine()) != null ){
                    CourseWebStr.append(line);
                    //out.println(line);
                    //Web.write(line.getBytes("big5"));
                }
                
            }
            finally{
                ips.close();
            }

            
            out.println("-----取得選課頁面成功-----");
            
            praseSelectClass();
        }
        catch(Exception ex){
            ex.printStackTrace();
            throw new Exception("GetCourseListFailed");
        }
    }
    
    
    public void GetLimit() throws Exception{
        try{
            out.println("\n-----檢查選課頁面-----");
            
            String loginURL = Host + "selcourse/seldeny.php";
            //out.println(loginURL);
            
            String requestCookie = cookie.get(0).toString();
            for (int i = 1; i < cookie.size(); i++){
                requestCookie = requestCookie + "; " + cookie.get(i);
            }
            
            HttpURLConnection conn = (HttpURLConnection)new URL(loginURL).openConnection();
            
            conn.setRequestMethod("GET"); 
            
            conn.setRequestProperty("Accept",
                                                            "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            conn.setRequestProperty("Upgrade-Insecure-Requests", "1");
            
            conn.setRequestProperty("Accept-Language",
                                                            "zh-TW,zh;q=0.8,en-US;q=0.6,en;q=0.4,zh-CN;q=0.2");
            conn.setRequestProperty("User-Agent",
                                                            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36");
            conn.setRequestProperty("Accept-Encoding",
                                                            "gzip, deflate, sdch");
            conn.setRequestProperty("Cache-Control",
                                                            "max-age=0");
            conn.setRequestProperty("Host", Host);
            
            conn.setRequestProperty("Connection",
                                                            "keep-alive");
            conn.setRequestProperty("Cookie", requestCookie);
            

            try ( FileOutputStream Web = new FileOutputStream(LimitWeb) ;
                     BufferedReader buff = new BufferedReader( new InputStreamReader(conn.getInputStream(), "big5") ) 
                    ){
                
                
                String line;
                while ( (line = buff.readLine()) != null ){
                    //out.println(line);
                    Web.write(line.getBytes("big5"));
                }
                
            }

            
            out.println("-----取得檢查選課頁面成功-----\n");
            
        }
        catch(Exception ex){
            ex.printStackTrace();
            throw new Exception("GetLimit");
        }
    }
    

    public StuSystem(String ID, String PWD) throws Exception {
        this.ID = ID;
        this.PWD = PWD;
        
        GetLogin();
        PostStu();
        
    }
    
    private void praseSelectClass(){
        
        //out.println("-----praseSelectClass-----");
            
        Document doc = Jsoup.parse(CourseWebStr.toString());
            
        Elements elms = doc.getElementById("logo").getElementsByTag("select");
        
        depMap = new TreeMap<String, String>();
        for (Element elm : elms.get(0).getElementsByTag("option")) {
            //System.out.println(elm.attr("value"));
            //System.out.println(elm.text());
            depMap.put(elm.attr("value"), elm.text());
        }
        
        classMap = new TreeMap<String, String>();
        for (Element elm : elms.get(1).getElementsByTag("option")) {
            //System.out.println(elm.attr("value"));
            //System.out.println(elm.text());
            classMap.put(elm.attr("value"), elm.text());
        }
        
        //out.println("-----praseSelectClassSuccess-----");
    }
    
    public void praseTable() {
        
        out.println("-----praseTable-----");
        
        classData = new TreeMap<String, String>();
        Document doc = Jsoup.parse(CourseWebStr.toString());
        
        Element table = doc.getElementsByTag("table").first();
        
        String str;
        
        for (Element tr : table.getElementsByTag("tr")) {
            Elements td = tr.getElementsByTag("td");
            
            if (td == null || td.size() < 8)
                continue;
                
            str = "代碼 : " + td.get(1).text() + ", 名稱 : " + td.get(2).text() + ", 教師 : " + td.get(3).text() 
                    + ", 選別 : " + td.get(4).text() + ", 學分 : " + td.get(5).text() + ", 人數 : " + td.get(6).text();
            
            classData.put(td.get(1).text(), str);
        }
        
        
        out.println("-----praseTableSuccess-----");
    }
    
    public Map<String, String> checkClass(List<String> course) {
        out.println("\n-----檢查是否還能選課-----");
        out.println("現在時間 : " + LocalDateTime.now().toString());
        
        Document doc = Jsoup.parse(CourseWebStr.toString());
        
        Map<String, String> href = new TreeMap<String, String>();
        
        for (Element tr : doc.getElementsByTag("table").first().getElementsByTag("tr")) {
            Elements td = tr.getElementsByTag("td");
            
            if (td == null || td.size() < 8)
                continue;
            
            if ( course.contains( td.get(1).text() ) && !td.get(0).getElementsByTag("a").isEmpty() ){
                if (td.get(0).getElementsByTag("a").first().getElementsByTag("img").first().attr("src").equals("../images/Add.jpg")){
                    href.put(td.get(1).text(), td.get(0).getElementsByTag("a").first().attr("href"));
                    System.out.println(td.get(1).text() + "有空位, 加選連結 : " + td.get(0).getElementsByTag("a").first().attr("href"));
                }
            }

        }
        
        out.println("-----檢查完畢-----\n");
        return href;
    }
    
    public boolean checkAddClass(String course) {
        out.println("\n-----檢查是否選到課-----");
        
        Document doc = Jsoup.parse(CourseWebStr.toString());
        
        for (Element tr : doc.getElementsByTag("table").first().getElementsByTag("tr")) {
            Elements td = tr.getElementsByTag("td");
            
            if (td == null || td.size() < 8)
                continue;
            
            if ( course.equals(td.get(1).text() ) && !td.get(0).getElementsByTag("a").isEmpty() ){
                if (td.get(0).getElementsByTag("a").first().getElementsByTag("img").first().attr("src").equals("../images/Remove.jpg")){
                    out.println("-----已成功選到"+ course +"-----");
                    return true;
                }
            }

        }
        
        out.println("-----未選到"+ course +"-----");
        return false;
    }
    
    public void GetAddCourse(String href) throws Exception {
        try{
            out.println("\n-----進行選課-----");
            
            String loginURL = Host + "selcourse/" + href;
            out.println(loginURL);
            
            String requestCookie = cookie.get(0).toString();
            for (int i = 1; i < cookie.size(); i++){
                requestCookie = requestCookie + "; " + cookie.get(i);
            }
            
            HttpURLConnection conn = (HttpURLConnection)new URL(loginURL).openConnection();
            
            conn.setRequestMethod("GET"); 
            
            conn.setRequestProperty("Accept",
                                                            "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            conn.setRequestProperty("Upgrade-Insecure-Requests", "1");
            
            conn.setRequestProperty("Accept-Language",
                                                            "zh-TW,zh;q=0.8,en-US;q=0.6,en;q=0.4,zh-CN;q=0.2");
            conn.setRequestProperty("User-Agent",
                                                            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36");
            conn.setRequestProperty("Accept-Encoding",
                                                            "gzip, deflate, sdch");
            conn.setRequestProperty("Referer",
                                                            Host + "selcourse/ListClassCourse.php");
            conn.setRequestProperty("Host", Host);
            
            conn.setRequestProperty("Connection",
                                                            "keep-alive");
            conn.setRequestProperty("Cookie", requestCookie);
            

            InputStream ips;
            String encode = conn.getContentEncoding();
            
            if (encode != null) {
                out.println("content-encoding :: " + encode);
                if (!encode.equals("gzip"))
                    throw new Exception("CanNotEncodeInputStream");
                ips = new GZIPInputStream(conn.getInputStream());
            }
            else {
                out.println("content-encoding :: null, encode in default !");
                ips = conn.getInputStream();
            }
            try ( //FileOutputStream Web = new FileOutputStream(CourseWeb) ;
                     BufferedReader buff = new BufferedReader( new InputStreamReader(ips, "big5") ) 
                    ){
                /*
                String line;
                while ( (line = buff.readLine()) != null ){
                    //out.println(line);
                    //Web.write(line.getBytes("big5"));
                }
                */
            }finally{
                ips.close();
            }

            
            out.println("-----選課請求送出-----\n");
        }
        catch(Exception ex){
            ex.printStackTrace();
            throw new Exception("GetAddCourse");
        }
    }

    public Map<String, String> getDepMap() {
        return depMap;
    }

    public Map<String, String> getClassMap() {
        return classMap;
    }

    public Map<String, String> getClassData() {
        return classData;
    }

    
    
    
}
