/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mp3randomplayer;

import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import static java.lang.System.out;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 *
 * @author Jasper
 */
public class Mp3RandomPlayer extends JFrame {

    //  視窗名稱
    String name = "Player Demo 1.5";
    JPlayer jplayer = null;
    
    // 輸入檔案目錄
    String FilePath= null;
    JFileChooser chooser = new JFileChooser("C:\\"); 
    
    //讀取MP3類型檔案
    String Filename = "*.mp3";
    String nowFile = "";
    
    //將檔名存入List
    List <String> AllFile = new ArrayList<>();
    
    //顯示面板
    JLabel event;
    //播放執行緒
    Thread playingThread;
    //讀檔執行緒
    Thread loadingThread;
    
    JMenuItem[] numitem = new JMenuItem[10];  
    JButton playButton;
    JButton stopButton;
    JButton[] numButton = new JButton[10]; 

    
    public Mp3RandomPlayer() {
        Components();   
        EventListeners();              
    }
    
    //初始化視窗
    private void Components (){
        
        setTitle(name);
        MenuBar();
        initContainer();

        setSize(360, 480);

        //初始化按鈕
        DisableclickPlay();
        stopButton.setEnabled(false);
        clickCaption.actionPerformed(null);
        clickFile.actionPerformed(null);
    }
    
    //初始化視窗元件
    private void initContainer(){
        
        //運用JPanel ,GridLayout配置整體面板
        JPanel jpAll = new JPanel();
        jpAll.setLayout(new GridLayout (2, 1));
            JPanel jpUP = new JPanel();      
            
            jpAll.add(jpUP);
                //設定顯示面板資訊
                event = new JLabel ("",JLabel.CENTER);
                event.setFont(new Font(Font.MONOSPACED, Font.BOLD , 18));
                jpUP.add(event);
                JPanel jpPlayStop = new JPanel();
                jpUP.add(jpPlayStop);
            
            JPanel jpNum = new JPanel();
            jpAll.add(jpNum);

        //Play Stop鍵面板配置
        jpUP.setLayout(new GridLayout (2, 1));        
            jpPlayStop.setLayout(new GridLayout(1, 2, 5, 5));
                playButton = new JButton ("Play");
                jpPlayStop.add(playButton);
                playButton.addActionListener(clickPlay);
                
                stopButton = new JButton ("Stop");
                jpPlayStop.add(stopButton);
                stopButton.addActionListener(clickStop);      


        //數字鍵盤配置
        jpNum.setLayout(new GridLayout (4 , 3));
            for (int i=0; i<10; i++){
                numButton[i] = new JButton ("  "+i+" ");
                numButton[i].addActionListener(clickPlay);
            }
            for (int i=1; i<10; i++)
                 jpNum.add(numButton[i]);
            jpNum.add(new JButton (" * "));
            jpNum.add(numButton[0]);
            jpNum.add(new JButton (" # "));
        
            
        //取得可放置元件之面板
        Container cp = getContentPane();
        cp.add(jpAll);
        
        //初始化設定chooser
        chooser.setDialogTitle("開啟音檔所在資料夾");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        
    }
    
    private void MenuBar() {
        //新增選單列
        JMenuBar menubar = new JMenuBar();
            JMenu filemenu = new JMenu("檔案");
            JMenu aboutmenu = new JMenu("說明");
            JMenu keymenu = new JMenu("快捷鍵");
        
        setJMenuBar (menubar);
        
        JMenuItem file = new JMenuItem("開啟");
            filemenu.add(file);
            file.addActionListener(clickFile);
            menubar.add(filemenu);
        
        JMenuItem aboutitem = new JMenuItem("說明");
            aboutmenu.add(aboutitem);
            aboutitem.addActionListener(clickAbout);
        JMenuItem captionitem = new JMenuItem("警告");
            aboutmenu.add(captionitem);
            captionitem.addActionListener(clickCaption);
        menubar.add(aboutmenu);
        
        //設定快捷鍵欄
        for (int i=0; i<10; i++){
            numitem[i] = new JMenuItem ("  "+i+" ");
            numitem[i].addActionListener(clickPlay);
            keymenu.add(numitem[i]);
        }
        numitem[0].setAccelerator(KeyStroke.getKeyStroke('0'));
        numitem[1].setAccelerator(KeyStroke.getKeyStroke('1'));
        numitem[2].setAccelerator(KeyStroke.getKeyStroke('2'));
        numitem[3].setAccelerator(KeyStroke.getKeyStroke('3'));
        numitem[4].setAccelerator(KeyStroke.getKeyStroke('4'));
        numitem[5].setAccelerator(KeyStroke.getKeyStroke('5'));
        numitem[6].setAccelerator(KeyStroke.getKeyStroke('6'));
        numitem[7].setAccelerator(KeyStroke.getKeyStroke('7'));
        numitem[8].setAccelerator(KeyStroke.getKeyStroke('8'));
        numitem[9].setAccelerator(KeyStroke.getKeyStroke('9'));
        
        menubar.add(keymenu);
    }
    

    private void EventListeners(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    
    public void EnableclickPlay(){
        playButton.setEnabled(true);
        for (int i=0; i<10; i++){
            numitem[i].setEnabled(true);
            numButton[i].setEnabled(true);
        }
    }
    
    public void DisableclickPlay() {
        playButton.setEnabled(false);
        for (int i=0; i<10; i++){
            numitem[i].setEnabled(false);
            numButton[i].setEnabled(false);
        }
    }
    
    //按下播放時的動作
    ActionListener clickPlay = new ActionListener () {
        public void actionPerformed(ActionEvent e) {
            
            //檢查是否為第一次執行
            if (jplayer!=null) {
                
                //如果檔案還在撥放中,先停止撥放
                if (jplayer.playing == true){
                    jplayer.stop();
                    try {
                        //等待停止撥放程序結束
                        playingThread.join();
                    } catch (InterruptedException ex) {
                        out.println(ex.toString());
                    }
                }
                
                //將播放程序指向null,等待JAVA釋放RAM
                jplayer = null;
                playingThread = null;
                loadingThread = null;
            }
            DisableclickPlay();
            
            //釋放RAM
            System.gc();
            //初始化撥放介面
            jplayer = new JPlayer();
            
           if (AllFile.isEmpty()) {
               out.println("No MP3 files in this folder");
               event.setText("資料夾內沒有MP3檔");
               return;
           }
           else if (AllFile.size()==1) {
               nowFile = AllFile.get(0);
           }
           else {
               while (true){
                   //隨機抓取List中的檔案名稱
                   int rand = (int) (Math.random()*AllFile.size() ); 
                   if (!nowFile.equals( AllFile.get(rand) ) ){
                       nowFile = AllFile.get(rand);
                       break;
                   }
               }
           }
           

           

           out.println("Preapaering load file : "+FilePath+"\\"+nowFile);
           event.setText(nowFile+" 讀取中");
            
           loadingThread  = new Thread ( ()->{
                //新增執行緒 進行檔案讀取
                jplayer.loadAudio(FilePath+"\\"+nowFile);
             });
             loadingThread.start();
            try {
                loadingThread.join();
            } catch (InterruptedException ex) {
                out.println(ex.toString());
            }
             
            
            if (jplayer.loadFile == false) {
                event.setText(nowFile + " 讀取失敗");
                EnableclickPlay();
            }
            else { 
                //若讀取成功->進行播放程序
                event.setText(nowFile + " 播放中");
                
                //建構播放執行緒
                playingThread = new Thread( ()->{
                        stopButton.setEnabled(true);
                        if (jplayer.play()) 
                            event.setText(nowFile + " 播放完畢");
                        else
                            event.setText(nowFile + " 播放失敗");
                        stopButton.setEnabled(false);
                });
                playingThread.start();
                try {
                    //插入播放執行緒, 延遲主執行緒100ms
                    playingThread.join(100);
                } catch (InterruptedException ex) {
                    out.println(ex.toString());
                }
                EnableclickPlay();
            }
        }
    };
    
    //按下停止時的動作
    ActionListener clickStop = new ActionListener () {
        public void actionPerformed(ActionEvent e) {
           if (jplayer.playing == true) {
                jplayer.stop();
                event.setText(Filename + " 停止播放");
            }
        }
    };
    
        ActionListener clickAbout = new ActionListener () {
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null, "程式會自動匯入指定目錄之MP3檔\n並隨機撥放MP3檔\n"
                    + "  1.出現讀取失敗請確認音訊檔後存在後重新匯入資料夾\n"
                    + "  2.出現播放失敗或播放完畢後沒聲音可能為檔案有損毀或格式不支援", " 關於"+name, 
                    JOptionPane.DEFAULT_OPTION);
        }
      };
        ActionListener clickCaption = new ActionListener () {
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null,"程式僅支援MP3檔\n請指定檔案目錄", " 注意", 
                    JOptionPane.WARNING_MESSAGE);
        }
       };
        
        ActionListener clickFile = new ActionListener () {
        public void actionPerformed(ActionEvent e) {
            String all = "";
            AllFile.clear();
            
            //檢查是否為第一次執行 
            if (jplayer!=null) {
                //如果檔案還在撥放中,先停止撥放
                if (jplayer.playing == true){
                    jplayer.stop();
                    try {
                        //等待停止撥放程序結束
                        playingThread.join();
                    } catch (InterruptedException ex) {
                        out.println(ex.toString());
                    }
                }
            }
            
            //跳出選取資料夾之視窗
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                System.out.println("getSelectedFile() : "+ chooser.getSelectedFile());
                FilePath = chooser.getSelectedFile().toString();
                
                try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(chooser.getSelectedFile().toPath(), Filename)) {
                    directoryStream.forEach(
                            path -> AllFile.add(path.getFileName().toString())
                    );
                    //將資料夾內檔案匯入AllFile
                    for (int i=0;i<AllFile.size();i++){
                        all=all+(i+1)+":"+AllFile.get(i)+",  ";
                        if ((i+1)%5==0)
                            all = all+"\n";
                        out.println(AllFile.get(i));
                    }
                    JOptionPane.showMessageDialog(null,all, " 已讀到的音訊檔", JOptionPane.DEFAULT_OPTION);
                    
                    EnableclickPlay();
                    
                } catch (IOException ex) {
                    out.println(ex.toString());
                    JOptionPane.showMessageDialog(null,ex.toString()+"\n請確認資料夾"+FilePath+"存在", "錯誤", JOptionPane.ERROR_MESSAGE);
                    DisableclickPlay();
                    return;
                }   
            }
            else {
                System.out.println("No Selection ");
                JOptionPane.showMessageDialog(null,"請選擇資料夾後開始", "注意", JOptionPane.WARNING_MESSAGE);
                DisableclickPlay();
            }
            
            
        }
       };
    
    
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater( () ->{
                new Mp3RandomPlayer().setVisible(true);
            }
        );
    }
    
}
