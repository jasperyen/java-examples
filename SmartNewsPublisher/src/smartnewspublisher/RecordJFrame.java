/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smartnewspublisher;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Jasper
 */
public class RecordJFrame {
    
    private static final String WAVE_GIF_NAME = "sound-wave.gif";
    
    private final JFrame recordFrame;

    
    public RecordJFrame(String frameTitle) {
        
        recordFrame = new JFrame(frameTitle);
        recordFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        
        Icon icon = new ImageIcon(SmartNewsPublisher.TEMP_PATH + WAVE_GIF_NAME);
        JLabel label = new JLabel(icon);
        
        
        recordFrame.getContentPane().add(label);
        recordFrame.pack();
        recordFrame.setVisible(false);
        
    }
    
    public void showFrame () {
        recordFrame.setVisible(true);
    }
    
    public void hideFrame() {
        recordFrame.setVisible(false);
    }
    
    public void closeFrame() {
        recordFrame.dispose();
    }
    
}
