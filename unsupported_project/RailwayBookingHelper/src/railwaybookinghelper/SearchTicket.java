
package railwaybookinghelper;

import java.io.BufferedInputStream;
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
import java.util.Map;
import java.util.TreeMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class SearchTicket {
    
    private String Host;
    private String checkWebHtm = "checkWeb.htm";
    private String checkSound = "PronounceRandonNumber.wav";
    private String checkImg = "checkImg.jpg";
    private String searchTicketHtm = "searchTicket.htm";
    private String haveTicketHtm = "haveSearchTicket.htm";
    private Map result;
    private String checkingURL;
    List cookie;
    String comNo;
    
    public String getSoundName(){
        return checkSound;
    }
    
    public Map getSearchResult(){
        return result;
    }
    
    SearchTicket(String id, String from, String to, String date, String type, String start, String end, String order){
        this("http://railway1.hinet.net/", id, from, to, date, type, start, end, order);
    }
    
    SearchTicket(String host, String id, String from, String to, String date, String type, String start, String end, String order){

        try {
        
                Host = host;
                String postWeb = Host + "check_csearch.jsp";                              //訂票網頁
                String postData;                                                                                //請求資料

                out.println("\n查詢剩餘車票 : \n身分證字號 : "+id+" ,起站 : "+from+" ,到站 : "+to+" ,乘車日期 : "+date+
                                    " ,車種 : "+type+" ,起始時間 : "+start+" ,結束時間 : "+end+" ,訂票張數 : "+order);            
                out.println("\n-----開始取得訂票驗證網頁-----\n"+postWeb);

                postData = "person_id="+id+"&from_station="+from+"&to_station="+to+"&getin_date="+date.replace("/", "%2F")
                                    +"&train_type="+type+"&getin_start_dtime="+start.replace(":", "%3A")+"&order_qty_str=1&getin_end_dtime="+end.replace(":", "%3A")
                                    +"&returnTicket="+order;

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
                conn.setRequestProperty("Origin", Host);
                
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
                                                            Host + "check_csearch.jsp");
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
                                                            Host + "check_csearch.jsp");
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
            if ( new File(checkSound).length() < 51200)
                throw new Exception("sound size < 50KB may be download false");
            
            
            /*                  取得認證音檔                   */
            
            out.println("-----取得驗證音檔成功-----");
        }
        
        catch (Exception ex){
            out.println("-----取得驗證音檔失敗-----");
            ex.printStackTrace();
        }
    }
    
      
    void checkingImg (String checknum){
        
        
        try{
            out.println("\n-----開始以驗證碼 : "+checknum+" 查詢剩餘車票-----");
            
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
            
            
            String searchURL = Host + form.attr("action") + "?randInput=" + checknum + inputURL.replace("/", "%2F");
            out.println(searchURL);
            checkingURL = searchURL;
             
            /*                  設定連線參數                  */
            
            HttpURLConnection conn = (HttpURLConnection)new URL(searchURL).openConnection();
            
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
                                                            Host + "check_csearch.jsp");
            conn.setRequestProperty("Accept-Encoding",
                                                            "gzip, deflate, sdch");
            conn.setRequestProperty("Accept-Language",
                                                            "zh-TW,zh;q=0.8,en-US;q=0.6,en;q=0.4,zh-CN;q=0.2");
            
             /*                  設定連線參數結束                  */
            
            
            /*                  取得訂票結果網頁                  */
            
            String line;
            try (FileOutputStream haveticket = new FileOutputStream(searchTicketHtm)
                    ; BufferedReader buff = new BufferedReader( new InputStreamReader(conn.getInputStream(), "big5") ) 
                    ){
                while ( (line = buff.readLine()) != null ){
                    //out.println(line);
                    haveticket.write(line.getBytes("big5"));
                } 
            }
            /*                  取得訂票結果網頁                  */
            
            out.println("-----搜尋要求成功送出-----");
        }
        catch(Exception ex){
            out.println("-----搜尋要求送出失敗-----");
            ex.printStackTrace();
        }
    }
    
     boolean searchResult(){
        try{
            out.println("\n-----開始檢查是否有剩餘車票-----");
            
            /*                  Jsoup分析網頁               */
            Document doc = Jsoup.parse(new File(searchTicketHtm), "big5");
            result = new TreeMap<String, String>();
            for (Element item : doc.getElementsByAttributeValue("onclick", "return orderRemains();")){
                result.put(item.text(), item.attr("href"));
                out.println("車次 : " + item.text());
            }
            /*                  Jsoup分析網頁               */
            
            if(result.isEmpty()){
                out.println("-----無剩餘車票-----");
                return false;
            }
            out.println("-----有剩餘車票-----");
            return true;
        }
        catch(Exception ex){
            out.println("-----檢查是否有剩餘車票失敗-----");
            ex.printStackTrace();
            return false;
        }
    }
    
    void getTicket(String trainNo){
        try{
            out.println("\n-----開始取得剩餘車票-----");
            
            if( !result.containsKey(trainNo) ){
                out.println("-----此班次無剩餘車票-----");
                return;
            }
            
            String getTicket = Host + result.get(trainNo).toString();
            out.println(getTicket);
            
            String requestCookie = cookie.get(0) + "; " + cookie.get(1);
            out.println("Request Cookie : " + requestCookie);
            /*                  設定連線參數                  */
            
            HttpURLConnection conn = (HttpURLConnection)new URL(getTicket).openConnection();
            
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
            conn.setRequestProperty("Referer", checkingURL);
            
            conn.setRequestProperty("Accept-Encoding",
                                                            "gzip, deflate, sdch");
            conn.setRequestProperty("Accept-Language",
                                                            "zh-TW,zh;q=0.8,en-US;q=0.6,en;q=0.4,zh-CN;q=0.2");
            conn.setRequestProperty("Cookie", requestCookie);
            
             /*                  設定連線參數結束                  */

            
            String line;
            try (FileOutputStream haveticket = new FileOutputStream(haveTicketHtm)
                    ; BufferedReader buff = new BufferedReader( new InputStreamReader(conn.getInputStream(), "big5") ) 
                    ){
                while ( (line = buff.readLine()) != null ){
                    //out.println(line);
                    haveticket.write(line.getBytes("big5"));
                } 
            }
            
            out.println("-----訂票要求成功送出-----");
            
        }
        catch(Exception ex){
            out.println("-----訂票要求送出失敗-----");
            ex.printStackTrace();
        }
    
    }
    
    boolean recheckTicket(){
        try{
            out.println("-----開始確認訂票是否成功-----");
            
            /*                  Jsoup分析網頁               */
            Document doc = Jsoup.parse(new File(haveTicketHtm), "big5");
            comNo = doc.getElementById("spanOrderCode").text();
            /*                  Jsoup分析網頁               */
        
            if (comNo == null)
                throw new Exception("無法取得電腦代碼");
            
            out.println("-----確認訂票成功, 電腦代碼 : "+comNo+"-----");
            return true;
        }
        catch(Exception ex){
            out.println("-----確認訂票失敗-----");
            ex.printStackTrace();
            return false;
        }
    }
    
    
}
