package ffmpeg.wrapper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;

/**
 *
 * @author NaufalPC
 */
public class FfmpegUtil {

    public static boolean isFfmpegInstalled() {
        try {
            String ffmpegPath = LogicController.getFfmpegPath();
            ProcessBuilder pb;
            
            if (ffmpegPath.equals("ffmpeg")) {
                pb = new ProcessBuilder("ffmpeg", "-version");
            } else {
                File ffmpegExe = new File(ffmpegPath, "ffmpeg");
                if (!ffmpegExe.exists()) {
                    ffmpegExe = new File(ffmpegPath, "ffmpeg.exe");
                }
                
                if (!ffmpegExe.exists()) {
                    ffmpegExe = new File(ffmpegPath);
                    if (!ffmpegExe.exists()) {
                        return false;
                    }
                }
                
                pb = new ProcessBuilder(ffmpegExe.getAbsolutePath(), "-version");
            }
            
            Process p = pb.start();
            return p.waitFor() == 0;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }

    public static void run(String command, Runnable onFinish) {
        new Thread(() -> {
            try {
                String ffmpegPath = LogicController.getFfmpegPath();
                String fullCommand;
                
                if (ffmpegPath.equals("ffmpeg")) {
                    fullCommand = "cmd /c " + command;
                } else {
                    File ffmpegExe = new File(ffmpegPath, "ffmpeg");
                    if (!ffmpegExe.exists()) {
                        ffmpegExe = new File(ffmpegPath, "ffmpeg.exe");
                    }
                    
                    if (!ffmpegExe.exists()) {
                        ffmpegExe = new File(ffmpegPath);
                        if (!ffmpegExe.exists()) {
                            throw new IOException("FFmpeg executable not found at: " + ffmpegPath);
                        }
                    }
                    
                    String escapedPath = "\"" + ffmpegExe.getAbsolutePath() + "\"";
                    fullCommand = "cmd /c " + command.replaceFirst("ffmpeg", escapedPath);
                }
                
                ProcessBuilder pb = new ProcessBuilder("cmd", "/c", fullCommand);
                pb.redirectErrorStream(true);
                Process p = pb.start();

                BufferedReader br = new BufferedReader(
                        new InputStreamReader(p.getInputStream(), "UTF-8")
                );

                List<String> outputLines = new ArrayList<>();
                String line;
                while ((line = br.readLine()) != null) {
                    outputLines.add(line);
                }

                SwingUtilities.invokeLater(() -> {
                    for (String outputLine : outputLines) {
                        LogicController.log(outputLine);
                    }
                });

                int exitCode = p.waitFor();

                SwingUtilities.invokeLater(() -> {
                    if (exitCode == 0) {
                        LogicController.log("✓ Konversi selesai dengan sukses!");
                        onFinish.run();
                    } else {
                        LogicController.log("✗ Konversi gagal! Exit code: " + exitCode);
                        LogicController.unlockUI();
                        JOptionPane.showMessageDialog(null,
                            "Konversi gagal dengan exit code: " + exitCode + "\n" +
                            "Periksa log untuk detail.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    }
                });

            } catch (IOException | InterruptedException e) {
                SwingUtilities.invokeLater(() -> {
                    LogicController.log("ERROR: " + e.getMessage());
                    LogicController.unlockUI();
                    JOptionPane.showMessageDialog(null,
                        "Error: " + e.getMessage() + "\n" +
                        "Pastikan FFmpeg terinstal dengan benar.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }
}