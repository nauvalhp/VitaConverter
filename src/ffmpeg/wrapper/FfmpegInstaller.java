package ffmpeg.wrapper;

import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author NaufalPC
 */
public class FfmpegInstaller {
    
    private static JTextArea logArea;
    private static JProgressBar progressBar;
    private static File vitaConverterDir;
    private static File extractDir;
    
    public static boolean installFfmpegInteractive(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Menginstal FFmpeg", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(parent);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel(
            "<html><h2>Instalasi FFmpeg</h2>" +
            "<p>Menginstal FFmpeg untuk VitaConverter...</p></html>"
        );
        
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        JScrollPane scrollPane = new JScrollPane(logArea);
        
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setString("Mempersiapkan...");
        
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(progressBar, BorderLayout.SOUTH);
        
        dialog.add(mainPanel);
        
        SwingWorker<Boolean, String> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return installFfmpeg();
            }
            
            @Override
            protected void done() {
                try {
                    boolean success = get();
                    if (success) {
                        JOptionPane.showMessageDialog(parent, """
                                                              \u2713 FFmpeg berhasil diinstal!
                                                              Anda sekarang dapat menggunakan VitaConverter.
                                                              
                                                              Catatan: Anda mungkin perlu restart aplikasi
                                                              agar perubahan PATH berlaku sepenuhnya.""",
                            "Instalasi Selesai",
                            JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        // Clean up on failure
                        cleanupFailedInstallation();
                    }
                    dialog.dispose();
                } catch (HeadlessException | InterruptedException | ExecutionException e) {
                    log("ERROR: " + e.getMessage());
                    cleanupFailedInstallation();
                    dialog.dispose();
                }
            }
        };
        
        worker.execute();
        dialog.setVisible(true);
        
        try {
            return worker.get();
        } catch (InterruptedException | ExecutionException e) {
            cleanupFailedInstallation();
            return false;
        }
    }
    
    private static boolean installFfmpeg() {

        vitaConverterDir = null;
        extractDir = null;
        
        if (!checkInternetConnection()) {
            log("✗ Tidak ada koneksi internet");
            log("FFmpeg tidak dapat diunduh tanpa internet.");
            log("Silakan sambungkan ke internet dan coba lagi.");
            
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(null,
                    "<html><b>Tidak Ada Koneksi Internet</b><br><br>" +
                    "FFmpeg tidak dapat diunduh tanpa internet.<br>" +
                    "Silakan:<br>" +
                    "1. Sambungkan ke internet<br>" +
                    "2. Restart aplikasi<br>" +
                    "3. Coba instal FFmpeg lagi</html>",
                    "Koneksi Internet Diperlukan",
                    JOptionPane.ERROR_MESSAGE);
            });
            return false;
        }
        
        try {
            log("Memulai instalasi FFmpeg...");
            updateProgress(10, "Memeriksa OS...");
            

            String appDataDir = System.getenv("APPDATA");
            if (appDataDir == null) {
                appDataDir = System.getProperty("user.home") + File.separator + "AppData" + File.separator + "Roaming";
            }
            
            vitaConverterDir = new File(appDataDir, "VitaConverter");
            if (!vitaConverterDir.exists()) {
                vitaConverterDir.mkdirs();
            }
            

            String os = System.getProperty("os.name").toLowerCase();
            String ffmpegUrl;
            String ffmpegFilename;
            
            if (os.contains("win")) {
                ffmpegUrl = "https://github.com/BtbN/FFmpeg-Builds/releases/download/latest/ffmpeg-master-latest-win64-gpl.zip";
                ffmpegFilename = "ffmpeg-windows.zip";
            } else if (os.contains("mac")) {
                ffmpegUrl = "https://evermeet.cx/ffmpeg/ffmpeg-7.0.zip";
                ffmpegFilename = "ffmpeg-mac.zip";
            } else {
                ffmpegUrl = "https://johnvansickle.com/ffmpeg/releases/ffmpeg-release-amd64-static.tar.xz";
                ffmpegFilename = "ffmpeg-linux.tar.xz";
            }
            
            log("Mengunduh FFmpeg dari: " + ffmpegUrl);
            updateProgress(30, "Mengunduh FFmpeg...");
            
            File ffmpegFile = new File(vitaConverterDir, ffmpegFilename);
            downloadFileWithProgress(ffmpegUrl, ffmpegFile, 30, 50);
            
            log("Mengekstrak FFmpeg...");
            updateProgress(60, "Mengekstrak file...");
            
            extractDir = new File(vitaConverterDir, "ffmpeg");
            if (extractDir.exists()) {
                deleteDirectory(extractDir);
            }
            extractDir.mkdir();
            
            extractArchive(ffmpegFile, extractDir);
            
            File binDir = findBinDirectory(extractDir);
            if (binDir == null) {
                log("ERROR: Tidak dapat menemukan biner FFmpeg");
                return false;
            }
            
            log("Biner FFmpeg ditemukan di: " + binDir.getAbsolutePath());
            updateProgress(80, "Menyiapkan lingkungan...");
            
            // Try to add to system PATH with fallback
            log("Menambahkan FFmpeg ke PATH sistem...");
            boolean pathModified = addFfmpegToPathWithFallback(binDir);
            
            if (!pathModified) {
                log("⚠️  PERINGATAN: Gagal menambahkan FFmpeg ke PATH sistem.");
                log("   Aplikasi akan menggunakan path lokal dari konfigurasi.");
                log("   Anda mungkin perlu restart aplikasi untuk perubahan berlaku.");
            } else {
                log("✓ FFmpeg berhasil ditambahkan ke PATH sistem.");
            }
            
            storeFfmpegPath(binDir);
            

            updateProgress(90, "Memverifikasi instalasi...");
            boolean verified = verifyFfmpegInstallation(binDir);
            
            if (verified) {
                log("✓ Instalasi FFmpeg berhasil!");
                updateProgress(100, "Instalasi Selesai");
                
                if (ffmpegFile.exists()) {
                    ffmpegFile.delete();
                }
                
                return true;
            } else {
                log("✗ Verifikasi FFmpeg gagal");
                return false;
            }
            
        } catch (Exception e) {
            log("ERROR: " + e.getMessage());
            return false;
        }
    }
    
    private static boolean addFfmpegToPathWithFallback(File binDir) {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            
            log("Mencoba menambahkan FFmpeg ke PATH sistem...");
            boolean pathModified = false;
            
            if (os.contains("win")) {
                pathModified = addToWindowsPath(binDir);
            } else if (os.contains("mac") || os.contains("nix") || os.contains("nux")) {
                pathModified = addToUnixPath(binDir);
            }
            
            if (!pathModified) {
                log("Mencoba metode alternatif: PATH pengguna...");
                pathModified = addToUserPath(binDir);
            }
            
            if (!pathModified) {
                log("Mengatur variabel lingkungan untuk proses ini saja...");
                String binPath = binDir.getAbsolutePath();
                String currentPath = System.getenv("PATH");
                
                if (currentPath != null && !currentPath.contains(binPath)) {
                    System.setProperty("java.library.path", 
                        System.getProperty("java.library.path") + 
                        File.pathSeparator + binPath);
                    
                    java.lang.reflect.Field field = ClassLoader.class.getDeclaredField("sys_paths");
                    field.setAccessible(true);
                    field.set(null, null);
                    
                    log("✓ FFmpeg path ditambahkan ke lingkungan proses Java.");
                    return true;
                }
            }
            
            return pathModified;
            
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException e) {
            log("ERROR: Semua metode PATH gagal: " + e.getMessage());
            return false;
        }
    }
    
    private static boolean addToWindowsPath(File binDir) {
        try {
            String binPath = binDir.getAbsolutePath();
            
            try {
                ProcessBuilder pb = new ProcessBuilder("reg", "query", 
                    "HKLM\\SYSTEM\\CurrentControlSet\\Control\\Session Manager\\Environment", "/v", "Path");
                Process p = pb.start();
                
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                StringBuilder pathBuilder = new StringBuilder();
                String line;
                boolean foundPath = false;
                
                while ((line = reader.readLine()) != null) {
                    if (line.contains("REG_SZ") || line.contains("REG_EXPAND_SZ")) {
                        String[] parts = line.split("\\s{4,}");
                        if (parts.length > 2) {
                            pathBuilder.append(parts[2]);
                            foundPath = true;
                        }
                    }
                }
                p.waitFor();
                
                if (foundPath) {
                    String currentPath = pathBuilder.toString();
                    
                    if (!currentPath.toLowerCase().contains(binPath.toLowerCase())) {
                        String newPath = currentPath + ";" + binPath;
                        
                        ProcessBuilder setPb = new ProcessBuilder("reg", "add", 
                            "HKLM\\SYSTEM\\CurrentControlSet\\Control\\Session Manager\\Environment", 
                            "/v", "Path", "/t", "REG_EXPAND_SZ", 
                            "/d", newPath, "/f");
                        Process setP = setPb.start();
                        int exitCode = setP.waitFor();
                        
                        if (exitCode == 0) {
                            log("✓ FFmpeg ditambahkan ke PATH sistem (semua pengguna).");
                            log("⚠️  Restart komputer diperlukan untuk perubahan penuh.");
                            return true;
                        }
                    } else {
                        log("✓ FFmpeg sudah ada di PATH sistem.");
                        return true;
                    }
                }
            } catch (IOException | InterruptedException e) {
                log("Metode registry gagal: " + e.getMessage());
            }
            
            try {
                String currentUserPath = System.getenv("PATH");
                if (currentUserPath != null && !currentUserPath.contains(binPath)) {
                    String newPath = currentUserPath + ";" + binPath;
                    
                    ProcessBuilder pb = new ProcessBuilder("cmd", "/c", 
                        "setx", "PATH", "\"" + newPath + "\"");
                    Process p = pb.start();
                    int exitCode = p.waitFor();
                    
                    if (exitCode == 0) {
                        log("✓ FFmpeg ditambahkan ke PATH pengguna.");
                        log("⚠️  Restart CMD/PowerShell diperlukan.");
                        return true;
                    }
                } else if (currentUserPath != null && currentUserPath.contains(binPath)) {
                    log("✓ FFmpeg sudah ada di PATH pengguna.");
                    return true;
                }
            } catch (IOException | InterruptedException e) {
                log("Metode setx gagal: " + e.getMessage());
            }
            
            return false;
            
        } catch (Exception e) {
            log("ERROR: Semua metode Windows PATH gagal: " + e.getMessage());
            return false;
        }
    }
    
    private static boolean addToUserPath(File binDir) {
        try {
            String binPath = binDir.getAbsolutePath();
            File batchFile = new File(vitaConverterDir, "add_to_path.bat");
            
            String batchContent = """
                                  @echo off
                                  echo Menambahkan FFmpeg ke PATH...
                                  setx PATH "%%PATH%%;""" + binPath + "\"\n" +
                "echo Selesai! Restart CMD/PowerShell Anda.\n" +
                "pause\n";
            
            try (FileWriter writer = new FileWriter(batchFile)) {
                writer.write(batchContent);
            }
            
            log("⚠️  Dibuat file batch untuk menambahkan PATH: " + batchFile.getAbsolutePath());
            log("   Jalankan file ini sebagai Administrator jika perlu.");
            
            return false;
            
        } catch (IOException e) {
            log("ERROR: Gagal membuat file batch: " + e.getMessage());
            return false;
        }
    }
    
    private static boolean addToUnixPath(File binDir) {
        try {
            String homeDir = System.getProperty("user.home");
            File bashProfile = new File(homeDir, ".bash_profile");
            File bashrc = new File(homeDir, ".bashrc");
            File zshrc = new File(homeDir, ".zshrc");
            File profile = new File(homeDir, ".profile");
            
            File profileFile = null;
            if (bashProfile.exists()) profileFile = bashProfile;
            else if (bashrc.exists()) profileFile = bashrc;
            else if (zshrc.exists()) profileFile = zshrc;
            else if (profile.exists()) profileFile = profile;
            else profileFile = bashProfile;
            
            String binPath = binDir.getAbsolutePath();
            String exportLine = "\n# Added by VitaConverter for FFmpeg\nexport PATH=\"$PATH:" + binPath + "\"\n";
            
            if (profileFile.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(profileFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.contains(binPath)) {
                            log("FFmpeg sudah ada di PATH.");
                            return true;
                        }
                    }
                }
            }
            
            try (FileWriter writer = new FileWriter(profileFile, true)) {
                writer.write(exportLine);
                log("✓ FFmpeg ditambahkan ke " + profileFile.getName());
                log("Jalankan 'source " + profileFile.getName() + "' atau restart terminal.");
                return true;
            }
            
        } catch (IOException e) {
            log("ERROR: Gagal menambahkan FFmpeg ke PATH Unix: " + e.getMessage());
            return false;
        }
    }
    
    private static void cleanupFailedInstallation() {
        log("Membersihkan instalasi yang gagal...");
        
        try {
            if (extractDir != null && extractDir.exists()) {
                deleteDirectory(extractDir);
                log("✓ Folder ekstrak dibersihkan.");
            }
            
            if (vitaConverterDir != null && vitaConverterDir.exists()) {
                File[] files = vitaConverterDir.listFiles((dir, name) -> 
                    name.toLowerCase().endsWith(".zip") || 
                    name.toLowerCase().endsWith(".tar.xz") ||
                    name.toLowerCase().endsWith(".tar.gz"));
                
                if (files != null) {
                    for (File file : files) {
                        if (file.delete()) {
                            log("✓ File arsip dihapus: " + file.getName());
                        }
                    }
                }
            }
            
            if (vitaConverterDir != null) {
                File batchFile = new File(vitaConverterDir, "add_to_path.bat");
                if (batchFile.exists() && batchFile.delete()) {
                    log("✓ File batch dibersihkan.");
                }
            }
            
            log("✓ Pembersihan selesai.");
            
        } catch (Exception e) {
            log("ERROR selama pembersihan: " + e.getMessage());
        }
    }
    
    private static boolean checkInternetConnection() {
        try {
            URL url = new URL("https://www.google.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();
            return connection.getResponseCode() == 200;
        } catch (IOException e) {
            return false;
        }
    }
    
    private static void storeFfmpegPath(File binDir) {
        try {
            String appDataDir = System.getenv("APPDATA");
            if (appDataDir == null) {
                appDataDir = System.getProperty("user.home") + File.separator + "AppData" + File.separator + "Roaming";
            }
            
            File configFile = new File(appDataDir, "VitaConverter/ffmpeg_path.txt");
            configFile.getParentFile().mkdirs();
            
            try (PrintWriter writer = new PrintWriter(configFile)) {
                writer.println(binDir.getAbsolutePath());
                log("✓ Path FFmpeg disimpan ke konfigurasi: " + configFile.getAbsolutePath());
            }
        } catch (IOException e) {
            log("ERROR: Tidak dapat menyimpan path FFmpeg: " + e.getMessage());
        }
    }
    
    private static void downloadFileWithProgress(String urlString, File outputFile, 
                                                 int startProgress, int endProgress) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("User-Agent", "VitaConverter/1.0");
        
        try (InputStream in = connection.getInputStream();
             FileOutputStream out = new FileOutputStream(outputFile)) {
            
            byte[] buffer = new byte[8192];
            int bytesRead;
            long totalRead = 0;
            long fileSize = connection.getContentLengthLong();
            
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                totalRead += bytesRead;
                
                if (fileSize > 0) {
                    int progress = startProgress + (int)((endProgress - startProgress) * totalRead / fileSize);
                    String sizeMB = String.format("%.1f", totalRead / (1024.0 * 1024.0));
                    updateProgress(progress, "Mengunduh: " + sizeMB + " MB");
                }
            }
        }
    }
    
    private static File findBinDirectory(File extractDir) {
        File binDir = new File(extractDir, "bin");
        if (binDir.exists()) return binDir;
        
        File[] files = extractDir.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    File subBin = new File(f, "bin");
                    if (subBin.exists()) return subBin;
                    
                    File found = findBinDirectory(f);
                    if (found != null) return found;
                }
            }
        }
        return null;
    }
        
    private static boolean verifyFfmpegInstallation(File binDir) {
        try {
            File ffmpegExe = new File(binDir, "ffmpeg");
            if (!ffmpegExe.exists()) {
                ffmpegExe = new File(binDir, "ffmpeg.exe");
            }
            
            if (ffmpegExe.exists()) {
                ProcessBuilder pb = new ProcessBuilder(ffmpegExe.getAbsolutePath(), "-version");
                Process p = pb.start();
                
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line = reader.readLine();
                p.waitFor();
                
                if (line != null && line.contains("ffmpeg")) {
                    log("Verifikasi: " + line.trim());
                    return true;
                }
            }
        } catch (IOException | InterruptedException e) {
            log("Verifikasi gagal: " + e.getMessage());
        }
        return false;
    }
    
    private static void extractArchive(File archive, File destDir) throws Exception {
        String filename = archive.getName().toLowerCase();
        
        if (filename.endsWith(".zip")) {
            try (java.util.zip.ZipFile zipFile = new java.util.zip.ZipFile(archive)) {
                java.util.Enumeration<? extends java.util.zip.ZipEntry> entries = zipFile.entries();
                
                while (entries.hasMoreElements()) {
                    java.util.zip.ZipEntry entry = entries.nextElement();
                    File entryFile = new File(destDir, entry.getName());
                    
                    if (entry.isDirectory()) {
                        entryFile.mkdirs();
                    } else {
                        entryFile.getParentFile().mkdirs();
                        try (InputStream in = zipFile.getInputStream(entry);
                             FileOutputStream out = new FileOutputStream(entryFile)) {
                            byte[] buffer = new byte[8192];
                            int bytesRead;
                            while ((bytesRead = in.read(buffer)) != -1) {
                                out.write(buffer, 0, bytesRead);
                            }
                        }
                    }
                }
            }
        } else if (filename.endsWith(".tar.xz") || filename.endsWith(".tar.gz")) {
            String[] cmd;
            if (filename.endsWith(".tar.xz")) {
                cmd = new String[]{"tar", "-xJf", archive.getAbsolutePath(), "-C", destDir.getAbsolutePath()};
            } else {
                cmd = new String[]{"tar", "-xzf", archive.getAbsolutePath(), "-C", destDir.getAbsolutePath()};
            }
            
            ProcessBuilder pb = new ProcessBuilder(cmd);
            Process p = pb.start();
            p.waitFor();
        }
        
        if (archive.exists()) {
            archive.delete();
        }
    }
    
    private static void deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            if (children != null) {
                for (File child : children) {
                    deleteDirectory(child);
                }
            }
        }
        if (dir.exists()) {
            dir.delete();
        }
    }
    
    private static void log(String message) {
        System.out.println("[FFmpeg Installer] " + message);
        if (logArea != null) {
            SwingUtilities.invokeLater(() -> {
                logArea.append(message + "\n");
                logArea.setCaretPosition(logArea.getDocument().getLength());
            });
        }
    }
    
    private static void updateProgress(int value, String message) {
        if (progressBar != null) {
            SwingUtilities.invokeLater(() -> {
                progressBar.setValue(value);
                progressBar.setString(message);
            });
        }
    }
}