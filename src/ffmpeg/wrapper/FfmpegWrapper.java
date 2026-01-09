package ffmpeg.wrapper;

import javax.swing.*;

public class FfmpegWrapper {
    public static void main(String[] args) {
        System.out.println("=== VitaConverter Starting ===");
        
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setLocationRelativeTo(null);
            mainFrame.setVisible(true);
            
            LogicController.init(mainFrame);
        });
    }
}