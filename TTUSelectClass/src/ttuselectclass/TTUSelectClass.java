/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ttuselectclass;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Jasper
 */
public class TTUSelectClass {


    public static void main(String[] args) throws Exception {
        // TODO code application logic here
    
        
        initSelect();
    }
    
    
    public static void initSelect() throws Exception {
    
        Scanner console = new Scanner(System.in);
        
        String ID, PWD;
        
        System.out.print("學號 : ");
        ID = console.next();
        
        if (!ID.startsWith("4103062") || ID.equals("")) {
            return;
        }
            
            
        System.out.print("密碼 : ");
        PWD = console.next();
        
        StuSystem stu = new StuSystem(ID, PWD);
        stu.GetCourseMain();
        stu.GetCourseList();
        
        String dep, cla;
        List<String> cou;
        
        List<String> list = new ArrayList<>();
        int i = 0;
        
        for (Map.Entry<String, String> entry : stu.getDepMap().entrySet()) {
            System.out.println(i++ + " - " + entry.getValue());
            list.add(entry.getKey());
        }
        System.out.print("選擇系所 : ");
        dep = list.get(console.nextInt());
        stu.PostCourseList(dep, stu.getClassMap().entrySet().iterator().next().getKey());
        
        
        list = new ArrayList<>();
        i = 0;
        for (Map.Entry<String, String> entry : stu.getClassMap().entrySet()) {
            System.out.println(i++ + " - " + entry.getValue());
            list.add(entry.getKey());
        }
        System.out.print("選擇班級 : ");
        cla = list.get(console.nextInt());
        
        stu.PostCourseList(dep, cla);
        //stu.GetLimit();
        //stu.GetCourseList();
        stu.praseTable();
        
        list = new ArrayList<>();
        i = 0;
        for (Map.Entry<String, String> entry : stu.getClassData().entrySet()) {
            System.out.println(i++ + " - " + entry.getValue());
            list.add(entry.getKey());
        }
        
        while (true) {
            console.nextLine();
            System.out.println("選擇要選科目(空格分開數字) : ");
            cou = new ArrayList<String>();
            for (String snum : console.nextLine().split(" ")){
                if ( Integer.valueOf(snum) < list.size() && Integer.valueOf(snum) >= 0 )
                    cou.add(list.get(Integer.valueOf(snum)));
            }
            System.out.println("確認是否要選下列科目 : ");
            for (String s : cou) {
                System.out.println(s);
            }
            System.out.print("(0/1) : ");
            if (console.next().equals("1"))
                break;
        }
        
        
        
        int time;
        System.out.println("設定選課秒數間格(s) : ");
        time = console.nextInt();
        
        goSelect(stu, cou, time);
        
    }
    
    
    public static void goSelect(StuSystem stu, List<String> cou, int time) throws Exception {
        
        Thread thread = new Thread( () ->{
            try {
                Map<String, String> map;
                Map.Entry<String, String> entry;
                Iterator<Map.Entry<String, String>> iter;
                while (!cou.isEmpty()) {
                    map = stu.checkClass(cou);
                    iter = map.entrySet().iterator();
                    
                    while(iter.hasNext()) {
                        entry = iter.next();
                        
                        stu.GetAddCourse(entry.getValue());
                        stu.GetCourseList();
                        if ( stu.checkAddClass(entry.getKey()) )
                            cou.remove(entry.getKey());
                    }
                    
                    if (!cou.isEmpty()) {
                        Thread.sleep(time * 1000);
                        stu.GetCourseList();
                    }
                }
            } catch(Exception ex){
                ex.printStackTrace();
            }
        } );
        
        thread.setDaemon(false);
        thread.start();
        
    }
    
}
