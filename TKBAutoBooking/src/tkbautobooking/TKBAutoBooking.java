/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tkbautobooking;

import static java.lang.System.out;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;


/**
 *
 * @author Jasper
 */
public class TKBAutoBooking {

    private static final Logger logger = Logger.getLogger(TKBAutoBooking.class.getName());
    private static final String SYSTEM_ENCODE = System.getProperty("file.encoding");
    private static final Scanner console = new Scanner(System.in);
    
    private static final String RSA_PUBLIC_KEY = "30819f300d06092a864886f70d010101050003818d003081890281810099b2e144cc224facbb786d3783867da85c9959fc85c440e2d4798632a4397a60a9c1eab38c1644650a7176254d1edd419faadec0f2af4635e86249f656a086787e0ff1a666176f31a7c4ddcf40b1b68556c78dba32e8e1985fb1041f2d6c638ac5cbd5823394c8406cc6d6b984997703605da53df2338a5be23ea604eac9bac30203010001";
    //private static String RSA_DATA;
    
    private BookingSystem bks;
    private int queryInterval = 5;
    
    private String USER_ID = "";
    private String USER_PASSWORD = "";
    private String SELECT_CLASS = "";
    private String SELECT_CLASSROOM = "";
    private Map<String, List<String>> SELECT_TIME;
    
    
    public TKBAutoBooking() throws InterruptedException {
        
        try {
            out.print("\n身分證 : ");
            USER_ID = console.next();
            out.print("密碼 : ");
            USER_PASSWORD = console.next();
            
            //if (!checkPermission(USER_ID, RSA_DATA)) {
            //    out.println("Permission denied !");
            //    return;
            //}
            
            bks = new BookingSystem(USER_ID, USER_PASSWORD);
            
            Map<String, String> classMap = bks.getClassMap();
            SELECT_CLASS = indexScanner(classMap);
            
            bks.chooseClass(SELECT_CLASS);
            
            Map<String, String> classroomMap = bks.getClassroomMap();
            Map<String, String> dateMap = bks.getDateMap();
            SELECT_CLASSROOM = indexScanner(classroomMap);
            
            bks.chooseClassroom(SELECT_CLASSROOM);
            Map<String, Map<String, String>> timeMap;
            timeMap = bks.getTimeMap();
            
            SELECT_TIME = timeScanner(dateMap, timeMap);
            
            out.print("\n設定查詢間格秒數 (+0~5) : ");
            queryInterval = console.nextInt();
            
            out.println("\n課程 : " + classMap.get(SELECT_CLASS));
            out.println("區域 : " + classroomMap.get(SELECT_CLASSROOM));
            out.print("時段 : ");
            SELECT_TIME.forEach( (k, v) -> { out.print(k + "_" + v + ", "); });
            
        } catch (Exception ex) {
            //ex.printStackTrace();
            logger.log(Level.WARNING, ex.toString());
        }
        
        
        if (SELECT_TIME != null)
            startAutoBooking();
    }
    
    private boolean checkPermission (String userId, String rsaData) {
        try {
        
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PublicKey publicKey = kf.generatePublic(new X509EncodedKeySpec(Hex.decodeHex(RSA_PUBLIC_KEY.toCharArray())));

            Cipher cipher = Cipher.getInstance("RSA");

            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            byte[] decodeBytes = cipher.doFinal(Hex.decodeHex(rsaData.toCharArray()));
            String decodeString = new String(decodeBytes);
        
            return userId.equals(decodeString);
        }catch (NoSuchAlgorithmException | DecoderException | InvalidKeySpecException | 
                NoSuchPaddingException | InvalidKeyException | 
                IllegalBlockSizeException | BadPaddingException ex) {
            return false;
        }
    }
    
    private void startAutoBooking () throws InterruptedException {
        long millis = System.currentTimeMillis();
        
        while (!SELECT_TIME.isEmpty()) {
            try {
                out.println("\n現在時間 : " + LocalDateTime.now().toString());
                
                //  每三分鐘重新登入
                if (System.currentTimeMillis() - millis > 3 * 60 * 1000) {
                    out.println("\n 開始重新登入 ");
                    bks.reLogin();
                    millis = System.currentTimeMillis();
                    out.println(" 重新登入完成 ");
                }
                
                //  逐天檢查剩餘座位
                for (Entry<String, List<String>> time : SELECT_TIME.entrySet()) {
                    //  想預約的時段
                    List<String> wannTime = time.getValue();
                    out.println("\n" + time.getKey() + " 想預約的時段 " + wannTime);
                    //  可預約的時段
                    List<String> haveSeatList = bks.checkBookingSeat(SELECT_CLASSROOM, time.getKey());
                    out.println(time.getKey() + " 可預約的時段 " + haveSeatList);
                    
                    for (String t : haveSeatList) {
                        //  檢查有座位的時段中, 是否有想預約的時段
                        if (wannTime.contains(t)) {
                            out.println(" 開始預約 " + time.getKey() + " 第 " + t + " 時段 ");
                            
                            //  預約成功
                            if (bks.goBooking(SELECT_CLASS, SELECT_CLASSROOM, time.getKey(), t)) {
                                out.println(" 預約成功 ");
                                wannTime.remove(t);
                            }
                            //  預約失敗
                            else{
                                out.println(" 預約失敗 ");
                            }
                        }
                    }

                    //  移除預約成功的日期
                    if (wannTime.isEmpty())
                        SELECT_TIME.remove(time.getKey());
                    
                    //  等待一段時間再查詢
                    int waitSec = queryInterval + (int)(Math.random()*5);
                    out.println("\n等待 " + waitSec + " s . . .");
                    Thread.sleep(waitSec * 1000);
                }
                
            } catch (Exception ex) {
                //ex.printStackTrace();
                logger.log(Level.WARNING, ex.toString());
                
                out.println("\nWaiting for 10s . . .");
                Thread.sleep(10 * 1000);
            }
        }
        
    }

    private Map<String, List<String>> timeScanner (Map<String, String> dateMap, Map<String, Map<String, String>> timeMap) {
        
        int index = 1;
        for (Entry<String, String> date : dateMap.entrySet()) {
            out.println("\n" + date.getValue());

            for (Entry<String, String> time : timeMap.get(date.getKey()).entrySet()) {
                out.println(index++ + " - " + time.getValue());
            }
            index = (index / 10 + 1) * 10 + 1;
        }
        
        console.nextLine();
        out.println("\n選擇要預約的時段代號(空格分開數字) : ");
        List<Integer> scanList = new ArrayList<>();
        Arrays.asList(console.nextLine().split(" ")).forEach( (v) -> {
            scanList.add(Integer.valueOf(v));
        });
            
        index = 1;
        Map<String, List<String>> chooseMap = new TreeMap<>();
        for (Entry<String, String> date : dateMap.entrySet()) {
            List<String> timeList = new ArrayList<>();
            for (Entry<String, String> time : timeMap.get(date.getKey()).entrySet()) {
                if (scanList.contains(index++)) {
                    //out.println(time.getValue());
                    timeList.add(time.getKey());
                }
            }
            if (!timeList.isEmpty())
                chooseMap.put(date.getKey(), timeList);
            
            index = (index / 10 + 1) * 10 + 1;
        }
        
        return chooseMap;
    }
    
    private String indexScanner (Map<String, String> map) {
        int index = 1;
        out.println();
        for (Entry<String, String> entry : map.entrySet())
            out.println(index++ + " - " + entry.getValue());

        out.print("輸入代號 : ");
        int chooseIndex = console.nextInt();
        String chooseValue = "";
        
        if (chooseIndex < 1 || chooseIndex > map.size())
            chooseIndex = 1;
        
        index = 1;
        for (Entry<String, String> entry : map.entrySet())
            if (index++ == chooseIndex)
                chooseValue = entry.getKey();
        
        return chooseValue;
    }

    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        
        if ( (!SYSTEM_ENCODE.equals("UTF-8") && !SYSTEM_ENCODE.equals("UTF8")) /*|| args.length < 1 */) {
            //out.println("\nPlease excude by following commond : java -Dfile.encoding=UTF8 -jar TKBAutoBooking.jar \"YOUR_RSA_KEY\"");
            out.println("\nPlease excude by following commond : java -Dfile.encoding=UTF8 -jar TKBAutoBooking.jar");
            return;
        }
        
        //RSA_DATA = args[0];
        TKBAutoBooking tkb = new TKBAutoBooking();
    }
    
}
