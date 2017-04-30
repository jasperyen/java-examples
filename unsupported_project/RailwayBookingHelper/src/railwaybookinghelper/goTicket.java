
package railwaybookinghelper;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.System.out;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class goTicket {
    private String Host;
    private String checkWebHtm = "checkWeb.htm";
    private String checkSound = "PronounceRandonNumber.wav";
    private String checkImg = "checkImg.jpg";
    private String haveTicketHtm = "haveticket.htm";
    private String checkNum;
    private String filePath;
    
    List cookie;
    
    public void setCheckNum( String checkNum ){
        this.checkNum = checkNum;
    }
    
    public String getSoundName(){
        return checkSound;
    }
    
    goTicket(String host, String id, String from, String to, String date, String train, String order, boolean ispum){
        this("", host, id, from, to, date, train, order, ispum);
    }
    
    
    goTicket(String id, String from, String to, String date, String train, String order, boolean ispum){
        this("http://railway1.hinet.net/", id, from, to, date, train, order, ispum);
    }
    
    goTicket(String filePath, String host, String id, String from, String to, String date, String train, String order, boolean ispum){
        try{
            Host = host;
            String postWeb = Host + "check_ctno1.jsp";                              //訂票網頁
            String postData;                                                                                //請求資料
            checkWebHtm = filePath + checkWebHtm;
            checkSound = filePath + checkSound;
            checkImg = filePath + checkImg;
            this.filePath = filePath;
            

            out.println("\n訂票資訊 : \n身分證字號 : "+id+" ,起站 : "+from+" ,到站 : "+to+" ,乘車日期 : "+date+
                                " ,車次代碼 : "+train+" ,訂票張數 : "+order+" ,是否為普悠瑪 : "+ispum);            
            out.println("\n-----開始取得訂票驗證網頁-----\n"+postWeb);

            /*      設定postData      */
            if (ispum){
                postData = "person_id="+id+"&from_station="+from+"&to_station="+to+"&getin_date="+date.replace("/", "%2F")
                                               +"&train_no="+train+"&order_qty_str="+order+"&t_order_qty_str=0&n_order_qty_str="+order+"&d_order_qty_str=0"
                                               +"&b_order_qty_str=0&z_order_qty_str=0&returnTicket=0";
            }
            else{
                postData = "person_id="+id+"&from_station="+from+"&to_station="+to+"&getin_date="+date.replace("/", "%2F")
                                               +"&train_no="+train+"&order_qty_str="+order+"&t_order_qty_str=0&n_order_qty_str=0&d_order_qty_str=0"
                                               +"&b_order_qty_str=0&z_order_qty_str=0&returnTicket=0";
            }
            out.println("postData : \n" + postData);
            
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
                                                            "keep-alive");
            conn.setRequestProperty("Cache-Control",
                                                            "max-age=0");
            conn.setRequestProperty("Accept",
                                                            "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            conn.setRequestProperty("User-Agent",
                                                            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36");
            conn.setRequestProperty("Content-Type",
                                                            "application/x-www-form-urlencoded");
            conn.setRequestProperty("Referer",
                                                            Host + "ctno1.htm");
            conn.setRequestProperty("Origin",Host);
            
            conn.setRequestProperty("Accept-Encoding",
                                                            "gzip, deflate");
            conn.setRequestProperty("Accept-Language",
                                                            "zh-TW,zh;q=0.8,en-US;q=0.6,en;q=0.4,zh-CN;q=0.2");
            conn.setRequestProperty("Content-Length",
                                                            String.valueOf(postData.getBytes().length));
            
            /*      設定連線參數結束         */
            
            /*           送出請求資料           */
            
            try ( DataOutputStream postStream = new DataOutputStream(conn.getOutputStream()) ){
                postStream.writeBytes(postData);
            }
            catch (Exception ex){
                out.println("-----POST失敗-----");
                ex.printStackTrace();
            }
            
             /*           送出請求資料結束           */
            
            
            File myFilePath = new File(filePath);
                if (!myFilePath.exists()) {
                    myFilePath.mkdir();
                }
            
            
            /*              取得訂票驗證網頁, Cookie        big5編碼, big5解碼            */
            
            try ( FileOutputStream checkWeb = new FileOutputStream(checkWebHtm)
                   ; BufferedReader buff = new BufferedReader( new InputStreamReader(conn.getInputStream(), "big5") ) 
                    ){
                
                cookie = new ArrayList<String>();
                out.println("Reponse Cookie : ");
                for (Object str : conn.getHeaderFields().get("Set-Cookie")){
                    cookie.add(str.toString().split(";", 2)[0]);
                    out.println(str.toString().split(";", 2)[0]);
                }

                String line;
                while ( (line = buff.readLine()) != null ){
                    //out.println(line);
                    checkWeb.write(line.getBytes("big5"));
                }
            }
            
            /*              取得訂票驗證網頁結束            */

            
            out.println("-----取得驗證網頁成功-----\n");
                     
        }
        catch(Exception ex){
            out.println("-----取得驗證網頁失敗-----");
            ex.printStackTrace();
        }
        
                    
        getImg();
        getsound();

    }
    
    void getImg(){                                                          //取得驗證圖片
        try{
            out.println("-----開始取得驗證圖片-----");
            
            /*              Jsoup分析網頁           */
            Document doc = Jsoup.parse(new File(checkWebHtm), "big5");
            Element img =doc.getElementById("idRandomPic");
            if (img == null)
                throw new Exception("無法取得驗證圖片連結");
            /*              Jsoup分析網頁           */
            
            
            String imgURL = Host + img.attr("src");
            out.println(imgURL);
            
            
            /*                  設定連線參數                  */
            
            HttpURLConnection conn = (HttpURLConnection)new URL(imgURL).openConnection();
            
            conn.setRequestMethod("GET"); 
            conn.setRequestProperty("Host", Host);
            
            conn.setRequestProperty("Connection",
                                                            "keep-alive");
            conn.setRequestProperty("Accept",
                                                            "image/webp,image/*,*/*;q=0.8");
            conn.setRequestProperty("User-Agent",
                                                            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36");
            conn.setRequestProperty("Referer",
                                                            Host + "check_ctno1.jsp");
            conn.setRequestProperty("Accept-Encoding",
                                                            "gzip, deflate, sdch");
            conn.setRequestProperty("Accept-Language",
                                                            "zh-TW,zh;q=0.8,en-US;q=0.6,en;q=0.4,zh-CN;q=0.2");
            
            /*                  設定連線參數結束                  */
            
            
            /*                  開始下載圖片                */
            
            try( FileOutputStream imgfile = new FileOutputStream(checkImg);
                    BufferedInputStream buff = new BufferedInputStream( conn.getInputStream() ) 
                    ){
                byte[] data = new byte[1024];
                int length;
                while ( (length = buff.read(data)) != -1 ){
                    imgfile.write(data, 0, length);
                }
            }
            //checkImg = img.attr("src");
            
            /*                  下載圖片結束                */
            
            out.println("-----取得驗證圖片成功-----\n");
            
        }
        catch(Exception ex){
            out.println("-----取得驗證圖片失敗-----");
            ex.printStackTrace();
        }
    
    }
    
    
    void getsound(){
       try{
            out.println("-----開始取得驗證音檔-----");
            
            /*              Jsoup分析網頁           */
            Document doc = Jsoup.parse(new File(checkWebHtm), "big5");
            Element sound =doc.getElementsByTag("form").first().getElementsByTag("noscript").last().getElementsByTag("bgsound").first();
            if (sound == null)
                throw new Exception("無法取得驗證圖片連結");
            /*              Jsoup分析網頁           */
            
            
            String requestCookie = cookie.get(0) + "; " + cookie.get(1);
            String soundURL = Host + sound.attr("src");
            
            out.println(soundURL);
            out.println("Request Cookie : " + requestCookie);
            
            
            /*                  設定連線參數                  */
            
            HttpURLConnection conn = (HttpURLConnection)new URL(soundURL).openConnection();
            
            conn.setRequestMethod("GET"); 
            conn.setRequestProperty("Host", Host);
            
            conn.setRequestProperty("Connection",
                                                            "Keep-Alive");
            conn.setRequestProperty("Accept",
                                                            "*/*");
            conn.setRequestProperty("User-Agent",
                                                            "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
            conn.setRequestProperty("Accept-Encoding",
                                                            "gzip, deflate");
            conn.setRequestProperty("Accept-Language",
                                                            "zh-TW");
            conn.setRequestProperty("DNT",
                                                            "1");
            conn.setRequestProperty("Referer",
                                                            Host + "check_ctno1.jsp");
            conn.setRequestProperty("Cookie", requestCookie);
            
             /*                  設定連線參數結束                  */
            
            
            /*                  取得認證音檔                   */
            
            try( FileOutputStream soundfile = new FileOutputStream(checkSound);
                    BufferedInputStream buff = new BufferedInputStream( conn.getInputStream() ) 
                    ){
                byte[] data = new byte[1024];
                int length;
                while ( (length = buff.read(data)) != -1 ){
                    soundfile.write(data, 0, length);
                }
            }
            if ( new File(checkSound).length() < 30720)
                throw new Exception("sound size < 30KB may be download false");
            
            
            /*                  取得認證音檔                   */
            
            out.println("-----取得驗證音檔成功-----");
        }
        
        catch (Exception ex){
            out.println("-----取得驗證音檔失敗-----");
            ex.printStackTrace();
        }
    }

    void checkingImg(){
        checkingImg(null);
    }
    
    void checkingImg (String checknum){
        haveTicketHtm = filePath + haveTicketHtm;
        
        if ( checknum==null )
            checknum = this.checkNum;
        
        try{
            out.println("\n-----開始以驗證碼 : "+checknum+" 訂票-----");
            
            /*                  Jsoup分析網頁               */
            Document doc = Jsoup.parse(new File(checkWebHtm), "big5");
            Element form =doc.getElementsByTag("form").first();
            Elements formInput = doc.getElementsByTag("input");
            String inputURL = "";
            for (Element inputelm : formInput){
                if ( inputelm.attr("type").equals("hidden") )
                    inputURL +="&"+inputelm.attr("name")+"="+inputelm.attr("value");
            }
            /*                  Jsoup分析網頁               */
            
            
            String ticketingURL = Host + form.attr("action") + "?randInput=" + checknum + inputURL.replace("/", "%2F");
            //out.println(ticketingURL);
            
             
            /*                  設定連線參數                  */
            
            HttpURLConnection conn = (HttpURLConnection)new URL(ticketingURL).openConnection();
            
            conn.setDoOutput(true); 
            conn.setDoInput(true); 
            conn.setRequestMethod("GET"); 
            conn.setUseCaches(false); 
            conn.setAllowUserInteraction(true); 
            conn.setFollowRedirects(true); 
            conn.setInstanceFollowRedirects(true); 
            
            conn.setRequestProperty("Host", Host);
            conn.setRequestProperty("Connection",
                                                            "keep-alive");
            conn.setRequestProperty("Accept",
                                                            "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            conn.setRequestProperty("User-Agent",
                                                            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36");
            conn.setRequestProperty("Referer",
                                                            Host + "check_ctno1.jsp");
            conn.setRequestProperty("Accept-Encoding",
                                                            "gzip, deflate, sdch");
            conn.setRequestProperty("Accept-Language",
                                                            "zh-TW,zh;q=0.8,en-US;q=0.6,en;q=0.4,zh-CN;q=0.2");
            
             /*                  設定連線參數結束                  */
            
            
            
            /*                  取得訂票結果網頁                  */
            
            String line;
            try (FileOutputStream haveticket = new FileOutputStream(haveTicketHtm)
                    ; BufferedReader buff = new BufferedReader( new InputStreamReader(conn.getInputStream(), "big5") ) 
                    ){
                while ( (line = buff.readLine()) != null ){
                    //out.println(line);
                    haveticket.write(line.getBytes("big5"));
                } 
            }
            /*                  取得訂票結果網頁                  */
            
            out.println("-----訂票要求成功送出-----");
        }
        catch(Exception ex){
            out.println("-----訂票要求送出失敗-----");
            ex.printStackTrace();
        }
    }
    
}
