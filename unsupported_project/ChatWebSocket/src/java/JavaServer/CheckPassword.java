/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaServer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import static java.lang.System.out;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author Jasper-Yen
 */
public class CheckPassword {
    

    String Host = "http://stucis.ttu.edu.tw/";
    
    /*
    String StuWeb = "C:\\webbb\\stumain.htm";
    String LogWeb = "C:\\webbb\\login.htm";
    String SelWeb = "C:\\webbb\\seltop.htm";
    String MeuWeb = "C:\\webbb\\selmenu.htm";
    */
    
    private StringBuilder SelWebStr;
    private List cookie;
    
    private String ID, PWD, StuNo, StuName;
    
    private void GetSel() throws Exception{
        try{
            out.println("-----StartGetSel()-----");
            
            String SelURL = Host + "menu/seltop.php";
            
            String requestCookie = cookie.get(0).toString();
            for (int i = 1; i < cookie.size(); i++){
                requestCookie = requestCookie + "; " + cookie.get(i);
            }
            
            out.println(SelURL);
            out.println("Request Cookie : " + requestCookie);
            
            HttpURLConnection conn = (HttpURLConnection)new URL(SelURL).openConnection();
            
            conn.setRequestMethod("GET"); 
            conn.setRequestProperty("Host", Host);
            
            conn.setRequestProperty("Connection",
                                                            "keep-alive");
            conn.setRequestProperty("Accept",
                                                            "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            conn.setRequestProperty("Upgrade-Insecure-Requests", "1");
            
            conn.setRequestProperty("User-Agent",
                                                            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36");
            conn.setRequestProperty("Referer",
                                                            Host + "menu/selmenu.htm");
            conn.setRequestProperty("Accept-Encoding",
                                                            "gzip, deflate, sdch");
            conn.setRequestProperty("Accept-Language",
                                                            "zh-TW,zh;q=0.8,en-US;q=0.6,en;q=0.4,zh-CN;q=0.2");
            conn.setRequestProperty("Cookie", requestCookie);

            
            

            try ( //FileOutputStream checkWeb = new FileOutputStream(SelWeb)
                     BufferedReader buff = new BufferedReader( new InputStreamReader(conn.getInputStream(), "big5") ) 
                    ){
                String line;
                SelWebStr = new StringBuilder();
                while ( (line = buff.readLine()) != null ){
                    SelWebStr.append(line);
                    //out.println(line);
                    //checkWeb.write(line.getBytes("big5"));
                }
            }

            
            out.println("-----FinishGetSel()-----\n");
            
        }
        catch(Exception ex){
            ex.printStackTrace();
            throw new Exception("GetSelFailed");
        }
    
    }
    
    private void GetLogin() throws Exception{
        try{
            out.println("-----StartGetLogin()-----");
            
            String loginURL = Host + "login.php";
            out.println(loginURL);
            

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

            try ( //FileOutputStream checkWeb = new FileOutputStream(LogWeb) ;
                     BufferedReader buff = new BufferedReader( new InputStreamReader(conn.getInputStream(), "big5") ) 
                    ){
                
                cookie = new ArrayList<String>();
                out.println("Reponse Cookie : ");
                
                for (Object str : conn.getHeaderFields().get("Set-Cookie")){
                    cookie.add(str.toString().split(";", 2)[0]);
                    out.println(str.toString().split(";", 2)[0]);
                }
                /*
                String line;
                while ( (line = buff.readLine()) != null ){
                    //out.println(line);
                    checkWeb.write(line.getBytes("big5"));
                }
                */
            }

            
            out.println("-----FinishGetLogin()-----\n");
            
        }
        catch(Exception ex){
            ex.printStackTrace();
            throw new Exception("GetLoginFailed");
        }
    }
    
    
    private void GetMeu() throws Exception{
        
        try{
            out.println("-----StartGetMeu()-----");
            
            String MeuURL = Host + "menu/selmenu.htm";
            String requestCookie = cookie.get(0).toString();
            for (int i = 1; i < cookie.size(); i++){
                requestCookie = requestCookie + "; " + cookie.get(i);
            }
            
            out.println(MeuURL);
            out.println("Request Cookie : " + requestCookie);
            
            HttpURLConnection conn = (HttpURLConnection)new URL(MeuURL).openConnection();
            
            conn.setRequestMethod("GET"); 
            conn.setRequestProperty("Host", Host);
            
            conn.setRequestProperty("Connection",
                                                            "keep-alive");
            conn.setRequestProperty("Accept",
                                                            "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            conn.setRequestProperty("Upgrade-Insecure-Requests", "1");
            
            conn.setRequestProperty("User-Agent",
                                                            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36");
            conn.setRequestProperty("Referer",
                                                            Host + "menu/topmenu.htm");
            conn.setRequestProperty("Accept-Encoding",
                                                            "gzip, deflate, sdch");
            conn.setRequestProperty("Accept-Language",
                                                            "zh-TW,zh;q=0.8,en-US;q=0.6,en;q=0.4,zh-CN;q=0.2");
            conn.setRequestProperty("Cookie", requestCookie);

            
            

            try ( //FileOutputStream checkWeb = new FileOutputStream(MeuWeb) ;
                     BufferedReader buff = new BufferedReader( new InputStreamReader(conn.getInputStream(), "big5") ) 
                    ){
                /*
                String line;
                while ( (line = buff.readLine()) != null ){
                    //out.println(line);
                    checkWeb.write(line.getBytes("big5"));
                }
                */
            }

            
            out.println("-----FinishGetMeu()-----\n");
            
        }
        catch(Exception ex){
            ex.printStackTrace();
            throw new Exception("GetMenuFailed");
        }
    
    }

    public CheckPassword(String ID, String PWD) {
        this.ID = ID;
        this.PWD = PWD;
    }
    
    public boolean check(){
        try{
            GetLogin();
            PostStu();
            GetSel();
            ParseData();
            
            return true;
        }
        catch (Exception ex){
            out.println(ex.toString());
            return false;
        }
    }
    
    
    private void PostStu() throws Exception {
        try{
            
            out.println("-----StartPostStu()-----");

            
            String postWeb = Host + "login.php";                              
            String postData;                                                                                //請求資料
            
            postData = "ID=" + ID + "&PWD=" + PWD + "&Submit=%B5n%A4J%A8t%B2%CE";
            String requestCookie = cookie.get(0).toString();
            for (int i = 1; i < cookie.size(); i++){
                requestCookie = requestCookie + "; " + cookie.get(i);
            }
            //ID=410306224&PWD=jasper826&Submit=%B5n%A4J%A8t%B2%CE
            
            out.println(postWeb);
            out.println("postData : " + postData);
            out.println("Request Cookie : " + requestCookie);
            
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
            
            /*              取得驗證網頁, Cookie        big5編碼, big5解碼            */
            
            try ( //FileOutputStream checkWeb = new FileOutputStream(StuWeb) ;
                     BufferedReader buff = new BufferedReader( new InputStreamReader(conn.getInputStream(), "big5") ) 
                    ){
                /*
                String line;
                while ( (line = buff.readLine()) != null ){
                    //out.println(line);
                    checkWeb.write(line.getBytes("big5"));
                }
                        */
            }

            out.println("-----FinishPostStu()-----\n");
                     
        }
        catch(Exception ex){
            ex.printStackTrace();
            throw new Exception("PostLoginFailed");
        }

    }
    
    private void ParseData() throws Exception{
        try {
            out.println("-----StartParseData()-----");
            
            Document doc = Jsoup.parse(SelWebStr.toString());
            
            Element tr =doc.getElementsByTag("tr").get(2);
            out.println(tr.text());
            
            
            String[] str = tr.text().split(" ");
            StuNo = str[1];
            str = str[2].split(" ");
            StuName = str[0];
                
            
            out.println("-----FinishParseData()-----\n\n");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("ParseDataFailed");
        }
        
    }

    public String getStuNo() {
        return StuNo;
    }

    public String getStuName() {
        return StuName;
    }
    
    
    
}
