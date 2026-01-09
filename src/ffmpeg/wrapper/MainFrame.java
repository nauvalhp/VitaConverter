/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package ffmpeg.wrapper;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.Dimension;

/**
 *
 * @author NaufalPC
 */
public class MainFrame extends javax.swing.JFrame {

    
    private void bindEvents() {
        InstallYes.addActionListener(e -> {
            
            new Thread(() -> {
                LogicController.installFfmpeg();
                
                SwingUtilities.invokeLater(() -> {
                    UpdateFfmpegJDialog.setVisible(false);
                });
            }).start();
        });

        InstallNo.addActionListener(e -> {
            UpdateFfmpegJDialog.setVisible(false);
            LogicController.handleInstallDeclined();
        });

        SourceChooseButton.addActionListener(e -> LogicController.chooseSource());
        DestinationChooseButton.addActionListener(e -> LogicController.chooseDestination());
        ConvertButton.addActionListener(e -> LogicController.convert());
        OkeConvertSelesaiButton.addActionListener(e -> {
            ConvertFinishedJDialog.setVisible(false);
            LogicController.unlockUI();
        });
    }


    public MainFrame() {

        initComponents();
        
        
        fixComponentSizes();
        
        bindEvents();

        
        fixDialogSizes();
        
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));
        
        
        setTitle("VitaConverter - Media Converter");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
        setMinimumSize(new java.awt.Dimension(900, 500));
        setMaximumSize(new java.awt.Dimension(900, 500));
        setPreferredSize(new java.awt.Dimension(900, 500));

        pack();
        setLocationRelativeTo(null);
    }
    
    
    private void fixComponentSizes() {
        
        int textFieldWidth = 250;
        int comboBoxWidth = 150;
        int consoleWidth = 400;
        int consoleHeight = 300;


        SourceTextField.setPreferredSize(new Dimension(textFieldWidth, SourceTextField.getPreferredSize().height));
        SourceTextField.setMaximumSize(new Dimension(textFieldWidth, SourceTextField.getPreferredSize().height));

        DestinationTextField.setPreferredSize(new Dimension(textFieldWidth, DestinationTextField.getPreferredSize().height));
        DestinationTextField.setMaximumSize(new Dimension(textFieldWidth, DestinationTextField.getPreferredSize().height));

        
        VideoCodecComboBox.setPreferredSize(new Dimension(comboBoxWidth, VideoCodecComboBox.getPreferredSize().height));
        VideoCodecComboBox.setMaximumSize(new Dimension(comboBoxWidth, VideoCodecComboBox.getPreferredSize().height));

        AudioCodecComboBox.setPreferredSize(new Dimension(comboBoxWidth, AudioCodecComboBox.getPreferredSize().height));
        AudioCodecComboBox.setMaximumSize(new Dimension(comboBoxWidth, AudioCodecComboBox.getPreferredSize().height));

        MediaFormatComboBox.setPreferredSize(new Dimension(comboBoxWidth, MediaFormatComboBox.getPreferredSize().height));
        MediaFormatComboBox.setMaximumSize(new Dimension(comboBoxWidth, MediaFormatComboBox.getPreferredSize().height));

        VideoQualityChooseComboBox.setPreferredSize(new Dimension(comboBoxWidth, VideoQualityChooseComboBox.getPreferredSize().height));
        VideoQualityChooseComboBox.setMaximumSize(new Dimension(comboBoxWidth, VideoQualityChooseComboBox.getPreferredSize().height));


        jScrollPane1.setPreferredSize(new Dimension(consoleWidth, consoleHeight));
        jScrollPane1.setMaximumSize(new Dimension(consoleWidth, consoleHeight));

       
        ConsoleLogTextArea.setPreferredSize(new Dimension(consoleWidth - 20, consoleHeight - 20));
    }

    private void fixDialogSizes() {
   
        UpdateFfmpegJDialog.setPreferredSize(new java.awt.Dimension(500, 300));
        UpdateFfmpegJDialog.pack();

     
        UpdateFfmpegJDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        UpdateFfmpegJDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                UpdateFfmpegJDialog.setVisible(false);
                LogicController.handleInstallDeclined();
            }
        });


        ConvertFinishedJDialog.pack();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        UpdateFfmpegJDialog = new javax.swing.JDialog();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel7 = new javax.swing.JLabel();
        InstallYes = new javax.swing.JButton();
        InstallNo = new javax.swing.JButton();
        ConvertFinishedJDialog = new javax.swing.JDialog();
        jLabel6 = new javax.swing.JLabel();
        OkeConvertSelesaiButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        SourceTextField = new javax.swing.JTextField();
        DestinationTextField = new javax.swing.JTextField();
        VideoCodecComboBox = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        AudioCodecComboBox = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        MediaFormatComboBox = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        ConsoleLogTextArea = new javax.swing.JTextArea();
        SourceChooseButton = new javax.swing.JButton();
        DestinationChooseButton = new javax.swing.JButton();
        ConvertButton = new javax.swing.JButton();
        VideoQualityChooseComboBox = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();

        UpdateFfmpegJDialog.setTitle("FFMPEG tidak terinstal");
        UpdateFfmpegJDialog.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setText("Program ini menggunakan FFmpeg untuk memproses dan mengonversi berkas\nvideo dan audio.\n\nFFmpeg adalah alat baris perintah yang melakukan konversi media secara aktual.\nTanpa FFmpeg yang terinstal, aplikasi ini tidak dapat berfungsi.\n\nSilakan instal FFmpeg dan pastikan telah ditambahkan ke jalur sistem PATH Anda,\nkemudian restart program ini.");
        jScrollPane2.setViewportView(jTextArea1);

        jLabel7.setText("Install FFMPEG dan tambahkan ke PATH?");

        InstallYes.setText("Ya");
        InstallYes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InstallYesActionPerformed(evt);
            }
        });

        InstallNo.setText("Tidak");
        InstallNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InstallNoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout UpdateFfmpegJDialogLayout = new javax.swing.GroupLayout(UpdateFfmpegJDialog.getContentPane());
        UpdateFfmpegJDialog.getContentPane().setLayout(UpdateFfmpegJDialogLayout);
        UpdateFfmpegJDialogLayout.setHorizontalGroup(
            UpdateFfmpegJDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UpdateFfmpegJDialogLayout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(InstallYes, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 195, Short.MAX_VALUE)
                .addComponent(InstallNo, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52))
            .addGroup(UpdateFfmpegJDialogLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(UpdateFfmpegJDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 449, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        UpdateFfmpegJDialogLayout.setVerticalGroup(
            UpdateFfmpegJDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UpdateFfmpegJDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(UpdateFfmpegJDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(InstallYes, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(InstallNo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(40, Short.MAX_VALUE))
        );

        jLabel6.setText("<!> Konversi Selesai <!>️");

        OkeConvertSelesaiButton.setText("Oke");
        OkeConvertSelesaiButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OkeConvertSelesaiButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ConvertFinishedJDialogLayout = new javax.swing.GroupLayout(ConvertFinishedJDialog.getContentPane());
        ConvertFinishedJDialog.getContentPane().setLayout(ConvertFinishedJDialogLayout);
        ConvertFinishedJDialogLayout.setHorizontalGroup(
            ConvertFinishedJDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ConvertFinishedJDialogLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(ConvertFinishedJDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(OkeConvertSelesaiButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        ConvertFinishedJDialogLayout.setVerticalGroup(
            ConvertFinishedJDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ConvertFinishedJDialogLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(OkeConvertSelesaiButton)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        setTitle("VitaConverter");

        jLabel1.setText("Sumber");

        jLabel2.setText("Tujuan");

        SourceTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SourceTextFieldActionPerformed(evt);
            }
        });

        DestinationTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DestinationTextFieldActionPerformed(evt);
            }
        });

        VideoCodecComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        VideoCodecComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VideoCodecComboBoxActionPerformed(evt);
            }
        });

        jLabel3.setText("Video Codec");

        AudioCodecComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        AudioCodecComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AudioCodecComboBoxActionPerformed(evt);
            }
        });

        jLabel4.setText("Audio Codec");

        jLabel5.setText("Format");

        MediaFormatComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        MediaFormatComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MediaFormatComboBoxActionPerformed(evt);
            }
        });

        ConsoleLogTextArea.setEditable(false);
        ConsoleLogTextArea.setColumns(20);
        ConsoleLogTextArea.setLineWrap(true);
        ConsoleLogTextArea.setRows(5);
        ConsoleLogTextArea.setDebugGraphicsOptions(javax.swing.DebugGraphics.LOG_OPTION);
        ConsoleLogTextArea.setEnabled(false);
        ConsoleLogTextArea.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                ConsoleLogTextAreaPropertyChange(evt);
            }
        });
        jScrollPane1.setViewportView(ConsoleLogTextArea);

        SourceChooseButton.setText("Pilih");
        SourceChooseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SourceChooseButtonActionPerformed(evt);
            }
        });

        DestinationChooseButton.setText("Pilih");
        DestinationChooseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DestinationChooseButtonActionPerformed(evt);
            }
        });

        ConvertButton.setText("Ubah(Konversi)");
        ConvertButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConvertButtonActionPerformed(evt);
            }
        });

        VideoQualityChooseComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        VideoQualityChooseComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VideoQualityChooseComboBoxActionPerformed(evt);
            }
        });

        jLabel8.setText("Kualitas(Video)");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(DestinationTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
                            .addComponent(SourceTextField))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(SourceChooseButton)
                            .addComponent(DestinationChooseButton)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(MediaFormatComboBox, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addGap(19, 19, 19)
                                        .addComponent(jLabel3))
                                    .addComponent(VideoCodecComboBox, javax.swing.GroupLayout.Alignment.LEADING, 0, 108, Short.MAX_VALUE))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(47, 47, 47)
                                        .addComponent(jLabel4))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(28, 28, 28)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(AudioCodecComboBox, 0, 108, Short.MAX_VALUE)
                                            .addComponent(VideoQualityChooseComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addGap(18, 18, 18))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel8)
                                .addGap(33, 33, 33)))
                        .addComponent(ConvertButton, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(SourceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(SourceChooseButton)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(DestinationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(DestinationChooseButton)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(VideoCodecComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(AudioCodecComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel8))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(MediaFormatComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(VideoQualityChooseComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(52, 52, 52)
                                .addComponent(ConvertButton, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void SourceTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SourceTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SourceTextFieldActionPerformed

    private void VideoCodecComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_VideoCodecComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_VideoCodecComboBoxActionPerformed

    private void ConsoleLogTextAreaPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_ConsoleLogTextAreaPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_ConsoleLogTextAreaPropertyChange

    private void InstallYesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InstallYesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_InstallYesActionPerformed

    private void InstallNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InstallNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_InstallNoActionPerformed

    private void DestinationTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DestinationTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_DestinationTextFieldActionPerformed

    private void SourceChooseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SourceChooseButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SourceChooseButtonActionPerformed

    private void DestinationChooseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DestinationChooseButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_DestinationChooseButtonActionPerformed

    private void AudioCodecComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AudioCodecComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AudioCodecComboBoxActionPerformed

    private void MediaFormatComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MediaFormatComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_MediaFormatComboBoxActionPerformed

    private void ConvertButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConvertButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ConvertButtonActionPerformed

    private void OkeConvertSelesaiButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OkeConvertSelesaiButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_OkeConvertSelesaiButtonActionPerformed

    private void VideoQualityChooseComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_VideoQualityChooseComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_VideoQualityChooseComboBoxActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> AudioCodecComboBox;
    private javax.swing.JTextArea ConsoleLogTextArea;
    private javax.swing.JButton ConvertButton;
    private javax.swing.JDialog ConvertFinishedJDialog;
    private javax.swing.JButton DestinationChooseButton;
    private javax.swing.JTextField DestinationTextField;
    private javax.swing.JButton InstallNo;
    private javax.swing.JButton InstallYes;
    private javax.swing.JComboBox<String> MediaFormatComboBox;
    private javax.swing.JButton OkeConvertSelesaiButton;
    private javax.swing.JButton SourceChooseButton;
    private javax.swing.JTextField SourceTextField;
    private javax.swing.JDialog UpdateFfmpegJDialog;
    private javax.swing.JComboBox<String> VideoCodecComboBox;
    private javax.swing.JComboBox<String> VideoQualityChooseComboBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables

    /**
     *
     * @return
     */
    public javax.swing.JButton getConvertButton() {
        return ConvertButton;
    }

    /**
     *
     * @return
     */
    public javax.swing.JTextArea getConsoleLogTextArea() {
        return ConsoleLogTextArea;
    }

    /**
     *
     * @return
     */
    public javax.swing.JComboBox<String> getVideoCodecComboBox() {
        return VideoCodecComboBox;
    }

    /**
     *
     * @return
     */
    public javax.swing.JComboBox<String> getAudioCodecComboBox() {
        return AudioCodecComboBox;
    }

    /**
     *
     * @return
     */
    public javax.swing.JComboBox<String> getMediaFormatComboBox() {
        return MediaFormatComboBox;
    }

    /**
     *
     * @return
     */
    public javax.swing.JTextField getSourceTextField() {
        return SourceTextField;
    }

    /**
     *
     * @return
     */
    public javax.swing.JTextField getDestinationTextField() {
        return DestinationTextField;
    }

    /**
     *
     * @return
     */
    public javax.swing.JDialog getUpdateFfmpegJDialog() {
        return UpdateFfmpegJDialog;
    }

    /**
     *
     * @return
     */
    public javax.swing.JDialog getConvertFinishedJDialog() {
        return ConvertFinishedJDialog;
    }

    /**
     *
     * @return
     */
    public JButton getSourceChooseButton() {
        return SourceChooseButton;
    }

    /**
     *
     * @return
     */
    public JButton getDestinationChooseButton() {
        return DestinationChooseButton;
    }

    /**
     *
     * @return
     */
    public javax.swing.JComboBox<String> getVideoQualityChooseComboBox() {
        return VideoQualityChooseComboBox;
    }
}