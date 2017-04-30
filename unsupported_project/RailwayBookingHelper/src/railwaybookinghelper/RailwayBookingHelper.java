/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package railwaybookinghelper;

import static java.lang.System.out;
import java.util.Scanner;

/**
 *
 * @author Jasper
 */
public class RailwayBookingHelper {

    

    public static void main(String[] args)  {
        //getManyImg();
        RailwayDemo();
    }
    
    static void RailwayDemo(){
        Scanner console = new Scanner(System.in);
        
        while(true) {
            out.println("Please choose(0 to End) : 1.預約定票     2.定時搜尋剩餘車票");
            switch (console.nextInt()){
                
                case 0 :
                    break;
                    
                case 1 :
                    TicketByNo ticketbyno = new TicketByNo();
                    break;
                
                case 2 :
                    out.println("Unfinished :(\n");
                    break;
                    
                default :
                    continue;
                    
            }
            break;
        }
        
    }
    
    static void getManyImg(){
        try {
            //大量抓取驗證碼
            getImg g = new getImg();
                        
        } catch (Exception ex) {
           ex.printStackTrace();
        }
    
    }
    
}
