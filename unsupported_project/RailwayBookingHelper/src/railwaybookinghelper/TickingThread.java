
package railwaybookinghelper;

import static java.lang.System.out;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class TickingThread {
    
    boolean useGoogle, ispum;
    int interval,connNum;
    LocalDateTime startTime,getImgTime;
    Thread startTicking;
    String id, from, to, date, train, order, filePath;
    LocalDateTime now;
    List manyTicket, tickingTime;
    
    
    
    TickingThread( LocalDateTime startTime, LocalDateTime getImgTime, int interval, int connNum, boolean useGoogle ){

        out.println("\n---------------執行續初始化---------------");
        this.startTime = startTime;
        this.getImgTime = getImgTime;
        this.interval = interval;
        this.connNum = connNum;
        this.useGoogle = useGoogle;
        now = LocalDateTime.now();
        manyTicket = new ArrayList<goTicket>();
        tickingTime = new ArrayList<LocalDateTime>();
        LocalDateTime time = startTime;
        
        for (int i = 0; i < connNum; i++){

            tickingTime.add(time);
            
            time = time.plusNanos( interval*1000000 );
        }
    }
    
    void SettingInfo(String id, String from, String to, String date, String train, String order, boolean ispum){
        out.println("\n---------------訂票資訊初始化---------------");
        this.id = id;
        this.from = from;
        this.to = to;
        this.date = date;
        this.train = train;
        this.order = order;
        this.ispum = ispum;
    }
    
    void start() {
        Scanner console = new Scanner (System.in);
        out.println("\n取得驗證碼時間為 : " + getImgTime.toString());
        now = LocalDateTime.now();
        out.println("現在時間為 : " + now.toString());

        startTicking = new Thread( ()->{
             out.println("\n---------------執行續開始---------------");
             
             while ( getImgTime.isAfter(LocalDateTime.now()) );
             
             now = LocalDateTime.now();
            out.println("現在時間為 : " + now.toString());
            out.println("---------------準備取得驗證碼---------------");
            
            filePath = LocalDate.now().toString() + "_" +train + "_";
            
            for (int i = 0; i < connNum; i++){
                out.println("\n----------取得驗證碼 ( " + i + " )----------");
                manyTicket.add( new goTicket( (filePath + i + "\\"), "http://railway1.hinet.net/", id, from, to, date, train, order, ispum ) );
            }
            
            int k = 0;
            for ( Object ticket : manyTicket){
                if (useGoogle){
                    out.println("\n----------使用GOOGLE分析驗證碼 : ( " + k + " )----------");
                    getCheckNum getchecknum = new getCheckNum( ((goTicket)ticket).getSoundName() );
                    getchecknum.analyzeResponse();
                    ((goTicket)ticket).setCheckNum(getchecknum.answer);
                }
                
                else{
                    out.println("\n----------手動輸入驗證碼----------");
                    out.println("請輸入第 "+k+" 個驗證碼 : ");
                    ((goTicket)ticket).setCheckNum( console.nextLine() );
                }
                k++;
            }

            for (int i = 0; i < connNum; i++){
                out.println("\n-----開始訂票時間 : "+tickingTime.get(i).toString()+"-----");
                
                while(  ((LocalDateTime)tickingTime.get(i)).isAfter(LocalDateTime.now())  );
                out.println("-----開始訂票("+i+") : "+LocalDateTime.now().toString()+"-----");
                try {
                    getRespone( (goTicket)manyTicket.get(i) );
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                out.println("-----完成訂票("+i+")  : " + LocalDateTime.now().toString() + "-----");
            }

        });
            startTicking.setDaemon(false);
            startTicking.start();
            try {
                startTicking.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

    }
    
    void getRespone ( goTicket go )throws Exception{
        Thread goThread = new Thread ( ()->{
            go.checkingImg();
        } );
        goThread.start();
    }
}
