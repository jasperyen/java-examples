
package railwaybookinghelper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import static java.lang.System.out;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


public class getImg {

    static int threadcount = 0;    
    
    public class go extends Thread{

        String checkImg;
                
        go(int i){
            checkImg = "manyImg\\" + i + ".jpg";
        }
        
        @Override
        public void run(){
            threadcount++;
            try{

                out.println(threadcount + " - "+checkImg);

                String imgURL = "http://railway.hinet.net/ImageOut.jsp;pageRandom=0.00000000000000000";
                //out.println(imgURL);

                /*                  設定連線參數                  */

                HttpURLConnection conn = (HttpURLConnection)new URL(imgURL).openConnection();

                conn.setRequestMethod("GET"); 
                conn.setRequestProperty("Host", 
                                                                "http://railway.hinet.net/");
                conn.setRequestProperty("Connection",
                                                                "keep-alive");
                conn.setRequestProperty("Accept",
                                                                "image/webp,image/*,*/*;q=0.8");
                conn.setRequestProperty("User-Agent",
                                                                "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36");
                conn.setRequestProperty("Referer",
                                                                "http://railway.hinet.net/check_ctno1.jsp");
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

                //out.println("-----取得驗證圖片成功-----\n");
                out.println("finish" + checkImg);
                threadcount--;
            }
            catch(Exception ex){
                //out.println("-----取得驗證圖片失敗-----");
                out.println(ex.toString());
                threadcount--;
                run();
            }
            
        }
    }
    
    public getImg() throws InterruptedException {
        List threadList = new ArrayList<go>();
        
        for (int i = 0; i < 100000; i++){
            threadList.add(new go(i));
        }
      
        for(Object obj : threadList){

            if (threadcount > 10)
                while(threadcount > 10){
                    Thread.sleep(50);
                }

            ((go)obj).start();

        }

    }

}