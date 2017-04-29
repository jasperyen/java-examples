/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mp3randomplayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javazoom.jl.player.advanced.AdvancedPlayer;

/**
 *
 * @author Jasper
 */
public class JPlayer {
    static int playerCount = 0;
    private AdvancedPlayer jplayer;
    private InputStream mp3Stream = null;
    private File file;
    
    //撥放狀態
    boolean playing;
    //檔案讀取狀態
    boolean loadFile;
    
    //初始化撥放狀態
    JPlayer (){
        playing = false;                            
        loadFile = false;
        System.out.println("------------------Start JPlayer_"+playerCount+"----------------");
        playerCount++;
    }
    
    
    //由檔案路徑讀取檔案
    public boolean loadAudio(String filePath){
        try {
            loadAudio(new File(filePath));
            loadFile = true;
            System.out.println("loadFile = " + loadFile);
            return true;
        } catch (Exception e) {
            System.out.println("loadFile = " + loadFile);
            System.out.println("Runing StackTrace : ");
            e.printStackTrace();
            loadFile = false;
            return false;
        }
    }
    
    //進行檔案串流
    protected void loadAudio(File file) throws Exception{
         mp3Stream = new FileInputStream(file);
         this.file = file;
         finishLoadingAudio();
    }
    
    //將串流傳入撥放器
    protected void finishLoadingAudio() throws Exception {
        jplayer = new AdvancedPlayer(mp3Stream);
    }
    
    //建立播放方法
    public boolean play(){
        try {
            playing = true;
            System.out.println("playing = " + playing);
            jplayer.play();
            playing = false;
            System.out.println("playing = " + playing);
            System.out.println("----------------Playback Compelet----------------");
            return true;
        } catch (Exception ex) {
            playing = false;
            System.out.println("playing = " + playing);
            System.out.println("Runing StackTrace : ");
            ex.printStackTrace();
            return false;
        }
    }
    
    //建立停止方法
    public void stop (){
        System.out.println("Stop by user");
        jplayer.close();
    }
    
    
    //取得檔案路徑
    public String getFilePath (){
        if (mp3Stream == null)
            return "No file input yet!";
        else
            return file.getPath();
    }
    
    protected void finalize() { 
        System.out.println("JPlayer_Reclaim_by_GC"); 
    } 
}
