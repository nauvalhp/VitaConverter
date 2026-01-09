package ffmpeg.wrapper;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

/**
 * Simple utility to check JDK version and show installation instructions
 * No automatic installation - user must install manually
 */
public class JdkUtil {

    /**
     * Get JDK version as integer (e.g., 8, 11, 21)
     * Returns 0 if Java not found
     * @return 
     */
    public static int getJdkVersion() {
        try {
            ProcessBuilder pb = new ProcessBuilder("java", "-version");
            pb.redirectErrorStream(true);
            Process p = pb.start();
            
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            
            while ((line = br.readLine()) != null) {
                if (line.contains("version")) {
                    // Extract version number
                    if (line.contains("\"")) {
                        String versionString = line.split("\"")[1];
                        if (versionString.startsWith("1.")) {
                            // Java 8 and earlier: "1.8.0_301"
                            return Integer.parseInt(versionString.split("\\.")[1]);
                        } else {
                            // Java 9+: "21.0.1"
                            return Integer.parseInt(versionString.split("\\.")[0]);
                        }
                    }
                }
            }
            p.waitFor();
        } catch (IOException | InterruptedException | NumberFormatException e) {
            // Java not found or error
        }
        return 0;
    }
    
    /**
     * Get full Java version string for display
     * @return 
     */
    public static String getJavaVersionString() {
        try {
            ProcessBuilder pb = new ProcessBuilder("java", "-version");
            pb.redirectErrorStream(true);
            Process p = pb.start();
            
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = br.readLine();
            p.waitFor();
            
            return (line != null && !line.trim().isEmpty()) ? line.trim() : "Java tidak terdeteksi";
        } catch (IOException | InterruptedException e) {
            return "Java tidak terdeteksi";
        }
    }
    
    /**
     * 
     * @return 
     */
    public static boolean isJdk21Installed() {
        return getJdkVersion() >= 21;
    }
    
    /**
     * Show clear, user-friendly JDK installation instructions
     * @param parent Parent frame for dialog positioning (can be null)
     * @return true if user wants to continue anyway, false if wants to exit
     */
    public static boolean showJdkRequirementsDialog(Component parent) {
        int jdkVersion = getJdkVersion();
        String currentVersion = getJavaVersionString();
        
        String title;
        int messageType;
        String continueButtonText;
        
        if (jdkVersion == 0) {
            title = "Java Tidak Ditemukan";
            messageType = JOptionPane.ERROR_MESSAGE;
            continueButtonText = "Lanjutkan Saja";
        } else if (jdkVersion < 21) {
            title = "Versi Java Terlalu Lama";
            messageType = JOptionPane.WARNING_MESSAGE;
            continueButtonText = "Lanjutkan Saja (Tidak Disarankan)";
        } else {
            return true;
        }
        
        String message = createJdkRequirementsMessage(jdkVersion, currentVersion);
        
        Object[] options;
        if (jdkVersion == 0) {
            options = new Object[] {"Buka Halaman Unduhan", "Salin Instruksi", continueButtonText, "Keluar"};
        } else {
            options = new Object[] {"Buka Halaman Unduhan", "Salin Instruksi", continueButtonText, "Keluar"};
        }
        
        int choice = JOptionPane.showOptionDialog(parent,
            message,
            title,
            JOptionPane.DEFAULT_OPTION,
            messageType,
            null,
            options,
            options[0]);
        
        switch (choice) {
            case 0:
                openDownloadPage();
                return false;
            
            case 1:
                copyInstructionsToClipboard();
                JOptionPane.showMessageDialog(parent,
                    "Instruksi disalin ke clipboard!",
                    "Tersalin",
                    JOptionPane.INFORMATION_MESSAGE);
                return false;
                
            case 2:
                if (jdkVersion == 0) {
                    int confirm = JOptionPane.showConfirmDialog(parent,
                        "<html><b>⚠️ Java Runtime Tidak Ditemukan!</b><br><br>" +
                        "Tanpa Java, aplikasi tidak akan bekerja.<br>" +
                        "Apakah Anda yakin ingin melanjutkan?</html>",
                        "Konfirmasi",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                    return confirm == JOptionPane.YES_OPTION;
                } else {
                    return true;
                }
                
            case 3:
            default: 
                return false;
        }
    }
    
    private static String createJdkRequirementsMessage(int jdkVersion, String currentVersion) {
        String statusMessage;
        if (jdkVersion == 0) {
            statusMessage = "<font color='red'><b>✗ Java tidak ditemukan di sistem Anda</b></font>";
        } else if (jdkVersion < 21) {
            statusMessage = "<font color='orange'><b>⚠️ Versi Java terlalu lama: " + currentVersion + "</b></font>";
        } else {
            statusMessage = "<font color='green'><b>✓ Versi Java OK: " + currentVersion + "</b></font>";
        }
        
        return """
            <html>
            <div style='width: 500px; font-family: Arial, sans-serif;'>
            
            <h2>Java Development Kit (JDK) Diperlukan</h2>
            
            <p><b>Status Saat Ini:</b><br>
            %s</p>
            
            <p><b>VitaConverter membutuhkan:</b><br>
            <font size='+1'><b>JDK versi 21 atau lebih tinggi</b></font></p>
            
            <hr style='border: 1px solid #ccc;'>
            
            <h3>📥 Cara Menginstal:</h3>
            
            <p><b>1. Unduh JDK 21 (pilih salah satu):</b></p>
            <ul style='margin-top: 5px;'>
            <li>• <a href='https://adoptium.net/temurin/releases/?version=21'>Eclipse Temurin JDK 21</a> (Disarankan, gratis)</li>
            <li>• <a href='https://www.oracle.com/java/technologies/downloads/#jdk21-windows'>Oracle JDK 21</a> (Resmi)</li>
            <li>• <a href='https://jdk.java.net/21/'>OpenJDK 21</a> (Sumber terbuka)</li>
            </ul>
            
            <p><b>2. Jalankan installer:</b><br>
            • Windows: Unduh file <code>.msi</code> dan jalankan<br>
            • macOS: Unduh file <code>.pkg</code><br>
            • Linux: Gunakan package manager atau unduh arsip</p>
            
            <p><b>3. Setelah instalasi:</b><br>
            • <font color='red'><b>Restart komputer Anda</b></font><br>
            • Jalankan VitaConverter lagi</p>
            
            <hr style='border: 1px solid #ccc;'>
            
            <h3>🔍 Verifikasi Instalasi:</h3>
            <p>Buka Command Prompt/Terminal dan ketik:</p>
            <pre style='background: #f5f5f5; padding: 10px; border-radius: 5px;'>
java -version</pre>
            <p>Harus menampilkan: <code>java version "21.x.x"</code></p>
            
            <hr style='border: 1px solid #ccc;'>
            
            <p style='font-size: 11px; color: #666;'>
            <b>Catatan:</b> JDK sudah termasuk JRE. Jika Anda sudah punya Java, mungkin perlu update.<br>
            Aplikasi ini membutuhkan JDK lengkap untuk fitur pengembangan.
            </p>
            
            </div>
            </html>
            """.formatted(statusMessage);
    }
    

    private static void openDownloadPage() {
        try {
            Desktop desktop = Desktop.getDesktop();
            desktop.browse(new java.net.URI("https://adoptium.net/temurin/releases/?version=21"));
        } catch (IOException | URISyntaxException e) {
            JOptionPane.showMessageDialog(null, """
                                                Silakan kunjungi: https://adoptium.net/temurin/releases/?version=21
                                                Unduh Windows x64 MSI Installer.""",
                "URL Unduhan",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    

    private static void copyInstructionsToClipboard() {
        String instructions = """
            VitaConverter - Instruksi Instalasi JDK 21
            
            Diperlukan: Java Development Kit (JDK) versi 21 atau lebih tinggi
            
            Tautan Unduhan (pilih salah satu):
            1. Eclipse Temurin JDK 21 (Disarankan): https://adoptium.net/temurin/releases/?version=21
            2. Oracle JDK 21: https://www.oracle.com/java/technologies/downloads/#jdk21-windows
            3. OpenJDK 21: https://jdk.java.net/21/
            
            Langkah Instalasi:
            1. Unduh installer untuk sistem operasi Anda
            2. Windows: Jalankan file .msi sebagai Administrator
            3. Ikuti wizard instalasi
            4. PENTING: Restart komputer setelah instalasi
            5. Jalankan VitaConverter lagi
            
            Verifikasi Instalasi:
            1. Buka Command Prompt (Windows) atau Terminal (macOS/Linux)
            2. Ketik: java -version
            3. Harus menampilkan: java version "21.x.x"
            
            Catatan: JDK berbeda dengan JRE. Aplikasi ini membutuhkan JDK lengkap.
            """;
        
        java.awt.datatransfer.StringSelection selection = 
            new java.awt.datatransfer.StringSelection(instructions);
        java.awt.datatransfer.Clipboard clipboard = 
            java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }
}