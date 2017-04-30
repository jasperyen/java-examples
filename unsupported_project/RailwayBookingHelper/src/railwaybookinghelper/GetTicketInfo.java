
package railwaybookinghelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import static java.lang.System.out;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GetTicketInfo {
    
    private String Host;                                                                //台鐵訂票主機位置
    private String ticketWebHtm = "ctno1.htm";          //網頁儲存位置
    private Map dateList = null;                                        //可訂票日期
    private Map fsatationList = null;                               //出發車站
    private Map tsatationList = null;                           //到達車站

    public Map getDateList() {
        return dateList;
    }

    public Map getFsatationList() {
        return fsatationList;
    }

    public Map getTsatationList() {
        return tsatationList;
    }
    
    GetTicketInfo () throws Exception{
        this("http://railway1.hinet.net/");
    }
    
    GetTicketInfo (String host) throws Exception{
        try {
            this.Host = host;
            String ticketWeb = Host + "ctno1.htm";                  //設定訂票網頁
            out.println("-----開始下載訂票網頁-----");
            out.println(ticketWeb);
            
            /*      設定連線參數         */
            
            HttpURLConnection conn = (HttpURLConnection) new URL(ticketWeb).openConnection();
            
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
            conn.setRequestProperty("Cache-Control",
                                                            "max-age=0");
            conn.setRequestProperty("Accept",
                                                            "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            conn.setRequestProperty("User-Agent",
                                                            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36");
            conn.setRequestProperty("Referer",
                                                             Host + "index_left.htm");
            conn.setRequestProperty("Accept-Encoding",
                                                            "gzip, deflate, sdch");
            conn.setRequestProperty("Accept-Language",
                                                            "zh-TW,zh;q=0.8,en-US;q=0.6,en;q=0.4,zh-CN;q=0.2");
            
            /*      設定連線參數結束         */
            
            /*           開始下載網頁    以big5解碼, big5編碼        */
            
            try (FileOutputStream tickethtm = new FileOutputStream(ticketWebHtm)
                    ; BufferedReader buff = new BufferedReader( new InputStreamReader(conn.getInputStream(), "big5") ) 
                    ){
                String line = "";
                while ( (line = buff.readLine()) != null ){
                    //out.println(line);
                    tickethtm.write(line.getBytes("big5"));
                } 
            }
            
            out.println("-----網頁下載成功-----\n");
            
            
             /*           網頁下載結束            */
        }
        catch(Exception ex){
            out.println("-----網頁下載失敗-----");
            ex.printStackTrace();
            throw (Exception) ex.fillInStackTrace();
        }
    }
    
    public void getData() throws Exception{                                                  //分析網頁資訊
        out.println("-----開始分析網頁資訊-----");
        dateList = new TreeMap<String, String>();
        fsatationList = new TreeMap<String, String>();
        tsatationList = new TreeMap<String, String>();
        try {
            out.println(ticketWebHtm);
            
            /*                      以Jsoup分析網頁                   */
            Document doc = Jsoup.parse(new File(ticketWebHtm), "big5");
            Element date = doc.getElementById("getin_date");
            Element fsatation = doc.getElementById("from_station");
            Element tsatation = doc.getElementById("to_station");
            /*                      以Jsoup分析網頁                   */
            
            if (date == null || fsatation == null || tsatation == null)
                throw new Exception("缺少某些網頁資訊");
            
            //out.println("乘車日期選項 : ");
            for (Element optiondata : date.getElementsByTag("option")){
                //out.print(optiondata.text() +"_"+ optiondata.attr("value")+", ");
                dateList.put(optiondata.text(), optiondata.attr("value"));
            }
            
            //out.println("\n起站代碼選項 : ");
            for (Element optiondata : fsatation.getElementsByTag("option")){
                //out.print(optiondata.text() +"_"+ optiondata.attr("value")+", ");
                fsatationList.put(optiondata.text(), optiondata.attr("value"));
            }
            
            //out.println("\n到站代碼選項 : ");
            for (Element optiondata : tsatation.getElementsByTag("option")){
                //out.print(optiondata.text() +"_"+ optiondata.attr("value")+", ");
                tsatationList.put(optiondata.text(), optiondata.attr("value"));
            }
            out.println();
            
            out.println("-----分析網頁資訊成功-----");
            
        } catch (Exception ex) {
            out.println("-----分析網頁資訊失敗-----");
            ex.printStackTrace();
            throw (Exception) ex.fillInStackTrace();
        }
    }
    
    
}
