
package railwaybookinghelper;

import static java.lang.System.out;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;


public class TicketByNo {
    String id, from, to, date, train, order;
    boolean ispum,useGoogle;
    int getImgSec,interval,connNum;
    LocalDateTime startTime,getImgTime,canTicketTime;
    TickingThread go;
    
    TicketByNo(){
        out.println("---------------------------------------------------------------------------"
                + "開始預約訂票---------------------------------------------------------------------------");
        try{
            Scanner console = new Scanner(System.in);
            GetTicketInfo getinfo = new GetTicketInfo();
            getinfo.getData();
            

            ScanData(getinfo);

            
            out.println("\nUse default setting (0/1) : ");
            
            if (console.nextLine().equals("0"))
                ScanSetting();
            else{
                canTicketTime = LocalDate.now().plusDays(1).atStartOfDay();
                out.println( "開始訂票日期 : " + canTicketTime );

                startTime = canTicketTime.minusNanos(300*1000000);
                out.println( "訂票時間 : " + startTime );

                getImgTime = canTicketTime.minusSeconds(300);
                out.println( "取得驗證碼時間 : " + getImgTime );

                interval = 150;
                out.println("訂票間隔 (ms): " + interval);
            
                connNum = 5;
                out.println("驗證碼數量 : " + connNum);
                
                useGoogle = false;
                out.println("Google_Speech分析驗證碼  : " + useGoogle);
            }
            
            go = new TickingThread(startTime, getImgTime, interval, connNum, useGoogle);
            go.SettingInfo(id, from, to, date, train, order, ispum);
            
            go.start();
                
                
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
    
    void ScanSetting(){
        Scanner console = new Scanner(System.in);
        out.println("設定可開始訂票日期 (yyyy-mm-dd)  : ");
        LocalDateTime canTicketTime = LocalDateTime.of (
                LocalDate.parse( console.nextLine() , DateTimeFormatter.ISO_LOCAL_DATE), 
                LocalTime.MIDNIGHT);
        out.println( canTicketTime );

        out.println("設定訂票提早秒數 (ms) : ");
            startTime = canTicketTime.minusNanos( console.nextLong()*1000000 );
        out.println( startTime );

        out.println("設定取得驗證碼提早秒數 <600(s) : ");
            getImgSec = console.nextInt();
            if( getImgSec>600 )
                getImgTime = canTicketTime.minusSeconds(600);
            else
                getImgTime = canTicketTime.minusSeconds(getImgSec);
        out.println( getImgTime );

        out.println("設定訂票間隔 (ms): ");
            interval = console.nextInt();

        out.println("設定驗證碼數量 : ");
            connNum = console.nextInt();

        out.println("是否使用Google_Speech分析驗證碼 (0/1)");
            if ( console.nextInt()==1 )
                useGoogle = true;
            else
                useGoogle = false;
    }
    
    void  ScanData(GetTicketInfo getinfo){
        Scanner console = new Scanner(System.in);
        int i = 0;
        
        while(i == 0) {

            out.print("\n輸入身分證字號 : ");
            id = console.next();

            out.println("\n選擇訂票日期 : ");
            i = 1;
            for (Object data : getinfo.getDateList().keySet().toArray())
                out.print("(" + i++ + ") "+ data +"  ");
            out.println();
            int dateNo = console.nextInt();
            i = 1;
            for (Object data : getinfo.getDateList().values().toArray()){
                if(i++ == dateNo)
                    date = (String)data;
            }

            out.println("\n選擇起站代碼 : ");
            getinfo.getFsatationList().keySet().forEach(
                    key -> out.printf("%s  ",key)
            );
            out.println();
            from = console.next();

            out.println("\n選擇到站代碼 : ");
            getinfo.getTsatationList().keySet().forEach(
                    key -> out.printf("%s  ",key)
            );
            out.println();
            to = console.next();

            out.print("\n輸入車次代碼 : ");
            train = console.next();

            out.print("\n訂票張數 : ");
            order = console.next();

            out.print("\n是否為普悠瑪(0/1) : ");
            i = console.nextInt();
            if (i==1)
                ispum = true;
            else
                ispum = false;

            out.println("\n確認訂票資訊(0/1) : 身分證字號 : "+id+", 起站 : "+from+", 到站 : "+to+", 乘車日期 : "+date+
                                ", 車次代碼 : "+train+", 訂票張數 : "+order+", 是否為普悠瑪 : "+ispum);
            i = console.nextInt();
        }
        
    }
    
    
        
    
}
