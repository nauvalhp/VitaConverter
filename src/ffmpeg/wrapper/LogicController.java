package ffmpeg.wrapper;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 *
 * @author NaufalPC
 */
public class LogicController {

    private static MainFrame ui;

    /**
     *
     * @param frame
     */
public static void init(MainFrame frame) {
    ui = frame;
    

    if (!checkJDKRequirements()) {
        return; 
    }
    
    lockUI();
    
    initComboBox();
    
    checkAndInstallFfmpeg();
}

    private static boolean checkJDKRequirements() {
        int jdkVersion = JdkUtil.getJdkVersion();

        if (jdkVersion == 0) {
            boolean continueAnyway = JdkUtil.showJdkRequirementsDialog(ui);
            if (!continueAnyway) {
                System.exit(0);
                return false;
            }
        } else if (jdkVersion < 21) {
            int choice = JOptionPane.showConfirmDialog(ui,
                "Versi Java " + jdkVersion + " terdeteksi (21+ disarankan).\n" +
                "Apakah Anda ingin melanjutkan?",
                "Versi Java Lama",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

            if (choice != JOptionPane.YES_OPTION) {
                System.exit(0);
                return false;
            }
        }
        return true;
    }


    public static void handleInstallDeclined() {
        SwingUtilities.invokeLater(() -> {
            log("Instalasi FFmpeg ditolak oleh pengguna. Menutup aplikasi.");

            JOptionPane.showMessageDialog(ui,
                "<html><b>FFmpeg tidak akan diinstal.</b><br><br>" +
                "Tanpa FFmpeg, aplikasi tidak dapat berfungsi.<br><br>" +
                "Aplikasi akan ditutup...</html>",
                "FFmpeg Diperlukan",
                JOptionPane.WARNING_MESSAGE);

            javax.swing.Timer timer = new javax.swing.Timer(500, ev -> {
                System.exit(0);
            });
            timer.setRepeats(false);
            timer.start();
        });
    }

    public static String getFfmpegPath() {
        try {
            String appDataDir = System.getenv("APPDATA");
            if (appDataDir == null) {
                appDataDir = System.getProperty("user.home") + File.separator + "AppData" + File.separator + "Roaming";
            }

            File configFile = new File(appDataDir, "VitaConverter/ffmpeg_path.txt");

            if (configFile.exists()) {
                java.nio.file.Path path = configFile.toPath();
                return java.nio.file.Files.readAllLines(path).get(0);
            }
        } catch (IOException e) {
        }
        return "ffmpeg";
    }

    public static void lockUI() {
        SwingUtilities.invokeLater(() -> {
            ui.getConvertButton().setEnabled(false);
            ui.getSourceTextField().setEnabled(false);
            ui.getDestinationTextField().setEnabled(false);
            ui.getVideoCodecComboBox().setEnabled(false);
            ui.getAudioCodecComboBox().setEnabled(false);
            ui.getMediaFormatComboBox().setEnabled(false);
            ui.getVideoQualityChooseComboBox().setEnabled(false);
            ui.getSourceChooseButton().setEnabled(false);
            ui.getDestinationChooseButton().setEnabled(false);
        });
    }

    public static void unlockUI() {
        SwingUtilities.invokeLater(() -> {
            ui.getConvertButton().setEnabled(true);
            ui.getSourceTextField().setEnabled(true);
            ui.getDestinationTextField().setEnabled(true);
            ui.getVideoCodecComboBox().setEnabled(true);
            ui.getAudioCodecComboBox().setEnabled(true);
            ui.getMediaFormatComboBox().setEnabled(true);
            ui.getVideoQualityChooseComboBox().setEnabled(true);
            ui.getSourceChooseButton().setEnabled(true);
            ui.getDestinationChooseButton().setEnabled(true);
            ui.getConsoleLogTextArea().setEnabled(true);
        });
    }

    public static void log(String text) {
        if (ui == null) {
            System.out.println("[LOG] " + text);
            return;
        }

        SwingUtilities.invokeLater(() -> {
            try {
                JTextArea ta = ui.getConsoleLogTextArea();
                if (ta != null && ta.isEnabled()) {
                    ta.append(text + "\n");
                    ta.setCaretPosition(ta.getDocument().getLength());
                }
            } catch (Exception e) {
                System.err.println("Error logging to UI: " + e.getMessage());
                System.out.println("[LOG] " + text);
            }
        });
    }

    private static void initComboBox() {
        JComboBox<String> videoCb = ui.getVideoCodecComboBox();
        videoCb.removeAllItems();
        videoCb.addItem("Copy");
        videoCb.addItem("H.264 (libx264)");
        videoCb.addItem("H.265 (libx265)");

        JComboBox<String> audioCb = ui.getAudioCodecComboBox();
        audioCb.removeAllItems();
        audioCb.addItem("Copy");
        audioCb.addItem("AAC (aac)");
        audioCb.addItem("MP3 (libmp3lame)");

        JComboBox<String> formatCb = ui.getMediaFormatComboBox();
        formatCb.removeAllItems();
        formatCb.addItem("MP4");
        formatCb.addItem("MKV");

        JComboBox<String> qualityCb = ui.getVideoQualityChooseComboBox();
        qualityCb.removeAllItems();
        qualityCb.addItem("Original");
        qualityCb.addItem("4K (3840x2160)");
        qualityCb.addItem("2K (2560x1440)");
        qualityCb.addItem("1080p (1920x1080)");
        qualityCb.addItem("720p (1280x720)");
        qualityCb.addItem("480p (854x480)");
        qualityCb.addItem("360p (640x360)");
        qualityCb.addItem("240p (426x240)");
        qualityCb.addItem("144p (256x144)");
        
        qualityCb.setToolTipText("Pilih resolusi output video. 'Original' akan mempertahankan resolusi asli.");
        qualityCb.setSelectedIndex(0);
    }

    private static void checkAndInstallFfmpeg() {
        log("Memeriksa FFmpeg...");

        if (!FfmpegUtil.isFfmpegInstalled()) {
            JDialog dialog = ui.getUpdateFfmpegJDialog();
            dialog.setLocationRelativeTo(ui);
            dialog.setModal(true);

            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    LogicController.handleInstallDeclined();
                }
            });

            dialog.setVisible(true);
        } else {
            log("✓ FFmpeg ditemukan di sistem.");
            unlockUI();
        }
    }

    public static void installFfmpeg() {
        boolean success = FfmpegInstaller.installFfmpegInteractive(ui);
        if (success) {
            if (FfmpegUtil.isFfmpegInstalled()) {
                log("✓ FFmpeg berhasil diinstal!");
                unlockUI();
            } else {
                log("✗ FFmpeg diinstal tetapi tidak terdeteksi di PATH. Silakan restart aplikasi.");
            }
        } else {
            log("✗ Instalasi FFmpeg gagal. Aplikasi akan ditutup.");
            JOptionPane.showMessageDialog(ui,
                "Instalasi FFmpeg gagal.\nAplikasi akan ditutup.",
                "Instalasi Gagal",
                JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    public static void chooseSource() {
        File file = FileChooserUtil.openFile(ui);
        if (file != null) {
            ui.getSourceTextField().setText(file.getAbsolutePath());

            File parentFolder = file.getParentFile();
            if (parentFolder != null) {
                ui.getDestinationTextField().setText(parentFolder.getAbsolutePath());
            }
        }
    }

    public static void chooseDestination() {
        File folder = FileChooserUtil.chooseFolder(ui);
        if (folder != null) {
            int option = JOptionPane.showConfirmDialog(ui,
                    "Ingin menentukan nama file output spesifik?\nKlik No untuk generate otomatis.",
                    "Nama File Output",
                    JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                String fileName = JOptionPane.showInputDialog(ui,
                    "Masukkan nama file (tanpa ekstensi):",
                    "converted_video");

                if (fileName != null && !fileName.trim().isEmpty()) {
                    ui.getDestinationTextField().setText(
                        folder.getAbsolutePath() + File.separator + fileName.trim()
                    );
                } else {
                    ui.getDestinationTextField().setText(folder.getAbsolutePath());
                }
            } else {
                ui.getDestinationTextField().setText(folder.getAbsolutePath());
            }
        }
    }

    public static void convert() {
        lockUI();

        String input = ui.getSourceTextField().getText();
        String outputFolder = ui.getDestinationTextField().getText();

        if (input.isEmpty() || outputFolder.isEmpty()) {
            JOptionPane.showMessageDialog(ui,
                "Harap pilih file sumber dan folder tujuan!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            unlockUI();
            return;
        }

        File inputFile = new File(input);
        File outputDir = new File(outputFolder);

        if (!inputFile.exists()) {
            JOptionPane.showMessageDialog(ui,
                "File sumber tidak ditemukan!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            unlockUI();
            return;
        }

        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        String format = ui.getMediaFormatComboBox()
                          .getSelectedItem()
                          .toString()
                          .toLowerCase();

        String inputName = inputFile.getName();
        int dotIndex = inputName.lastIndexOf('.');
        if (dotIndex > 0) {
            inputName = inputName.substring(0, dotIndex);
        }

        String selectedQuality = (String) ui.getVideoQualityChooseComboBox().getSelectedItem();
        String resolution = mapVideoQuality(selectedQuality);

        String qualitySuffix = "";
        if (resolution != null && selectedQuality.contains("(")) {
            String qualityText = selectedQuality.substring(0, selectedQuality.indexOf("(")).trim();
            qualitySuffix = "_" + qualityText.replace("p", "").replace("K", "k");
        }

        String outputFileName = inputName + "_converted" + qualitySuffix + "." + format;
        File outputFile = new File(outputDir, outputFileName);

        int counter = 1;
        while (outputFile.exists()) {
            outputFileName = inputName + "_converted" + qualitySuffix + "_" + counter + "." + format;
            outputFile = new File(outputDir, outputFileName);
            counter++;
        }

        String output = outputFile.getAbsolutePath();

        String vCodec = mapVideoCodec((String) ui.getVideoCodecComboBox().getSelectedItem());
        String aCodec = mapAudioCodec((String) ui.getAudioCodecComboBox().getSelectedItem());

        StringBuilder commandBuilder = new StringBuilder();
        commandBuilder.append("ffmpeg -y -i \"").append(input).append("\"");

        if (resolution != null) {
            String aspectRatio = getAspectRatio(resolution);
            if (aspectRatio != null) {
                commandBuilder.append(" -vf \"scale=").append(resolution)
                             .append(":force_original_aspect_ratio=decrease")
                             .append(",pad=").append(resolution)
                             .append(":(ow-iw)/2:(oh-ih)/2\"");
            } else {
                commandBuilder.append(" -vf scale=").append(resolution);
            }
        }

        commandBuilder.append(" -c:v ").append(vCodec);

        if (!vCodec.equals("copy")) {
            String crf = getCrfForQuality(selectedQuality);
            commandBuilder.append(" -preset medium -crf ").append(crf);
        }

        commandBuilder.append(" -c:a ").append(aCodec);

        if (!aCodec.equals("copy")) {
            if (aCodec.equals("aac")) {
                commandBuilder.append(" -b:a 128k");
            } else if (aCodec.equals("libmp3lame")) {
                commandBuilder.append(" -q:a 2");
            }
        }

        commandBuilder.append(" \"").append(output).append("\"");

        String command = commandBuilder.toString();

        log("Menjalankan perintah FFmpeg:");
        log(command);
        log("Output akan disimpan ke: " + output);
        if (resolution != null) {
            log("Resolusi output: " + selectedQuality);
        }

        final String finalOutputPath = output;

        FfmpegUtil.run(command, () -> {
            showConvertFinishedDialog(finalOutputPath, selectedQuality);
        });
    }

    private static String mapVideoCodec(String s) {
        return switch (s) {
            case "H.264 (libx264)" -> "libx264";
            case "H.265 (libx265)" -> "libx265";
            default -> "copy";
        };
    }

    private static String mapAudioCodec(String s) {
        return switch (s) {
            case "AAC (aac)" -> "aac";
            case "MP3 (libmp3lame)" -> "libmp3lame";
            default -> "copy";
        };
    }

    private static String mapVideoQuality(String quality) {
        return switch (quality) {
            case "4K (3840x2160)" -> "3840:2160";
            case "2K (2560x1440)" -> "2560:1440";
            case "1080p (1920x1080)" -> "1920:1080";
            case "720p (1280x720)" -> "1280:720";
            case "480p (854x480)" -> "854:480";
            case "360p (640x360)" -> "640:360";
            case "240p (426x240)" -> "426:240";
            case "144p (256x144)" -> "256:144";
            default -> null;
        };
    }

    private static String getAspectRatio(String resolution) {
        if (resolution == null) return null;

        return switch (resolution) {
            case "3840:2160", "1920:1080", "1280:720" -> "16:9";
            case "2560:1440" -> "16:9";
            case "854:480" -> "16:9";
            case "640:360" -> "16:9";
            case "426:240" -> "16:9";
            case "256:144" -> "16:9";
            default -> null;
        };
    }

    private static String getCrfForQuality(String quality) {
        return switch (quality) {
            case "4K (3840x2160)", "2K (2560x1440)" -> "21";
            case "1080p (1920x1080)" -> "23";
            case "720p (1280x720)" -> "24";
            case "480p (854x480)" -> "26";
            case "360p (640x360)", "240p (426x240)", "144p (256x144)" -> "28";
            default -> "23";
        };
    }

    private static void showConvertFinishedDialog(String outputPath, String quality) {
        JDialog doneDialog = ui.getConvertFinishedJDialog();
        doneDialog.getContentPane().removeAll();

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String qualityInfo = "";
        if (!quality.equals("Original")) {
            qualityInfo = "<br>Kualitas: <font color='green'>" + quality + "</font>";
        }

        JLabel messageLabel = new JLabel(
            "<html><div style='text-align: center;'>" +
            "<font size='4'><b>✓ KONVERSI SELESAI</b></font><br><br>" +
            "File berhasil dikonversi:<br>" +
            "<font color='blue'>" + new File(outputPath).getName() + "</font>" +
            qualityInfo + "<br><br>" +
            "Lokasi:<br>" +
            "<font size='2'>" + outputPath + "</font>" +
            "</div></html>"
        );
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Arial", Font.BOLD, 14));
        okButton.setPreferredSize(new Dimension(100, 40));
        okButton.addActionListener(e -> {
            doneDialog.setVisible(false);
            unlockUI();
        });

        JButton openFolderButton = new JButton("Buka Folder");
        openFolderButton.addActionListener(e -> {
            try {
                File outputFile = new File(outputPath);
                java.awt.Desktop.getDesktop().open(outputFile.getParentFile());
            } catch (IOException ex) {
                log("Tidak bisa membuka folder: " + ex.getMessage());
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.add(okButton);
        buttonPanel.add(openFolderButton);

        panel.add(messageLabel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        doneDialog.setContentPane(panel);
        doneDialog.pack();
        doneDialog.setLocationRelativeTo(ui);
        doneDialog.setModal(true);
        doneDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        doneDialog.setVisible(true);
    }
}