package ffmpeg.wrapper;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 *
 * @author NaufalPC
 */
public class FileChooserUtil {

    /**
     *
     * @param parent
     * @return
     */
    public static File openFile(Component parent) {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Media Files", "mp4", "avi", "mov", "mkv", "mp3", "wav", "flac", "m4a"));
        
        int result = fc.showOpenDialog(parent);
        return result == JFileChooser.APPROVE_OPTION
                ? fc.getSelectedFile()
                : null;
    }

    /**
     *
     * @param parent
     * @return
     */
    public static File chooseFolder(Component parent) {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setDialogTitle("Pilih Folder Tujuan");
        
        int result = fc.showSaveDialog(parent);
        return result == JFileChooser.APPROVE_OPTION
                ? fc.getSelectedFile()
                : null;
    }

    /**
     *
     * @param parent
     * @return
     */
    public static File saveFile(Component parent) {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        int result = fc.showSaveDialog(parent);
        return result == JFileChooser.APPROVE_OPTION
                ? fc.getSelectedFile()
                : null;
    }
}