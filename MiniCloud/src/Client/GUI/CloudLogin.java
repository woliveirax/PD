package Client.GUI;

import Client.DataObservable;
import Exceptions.InvalidDirectoryException;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;


public class CloudLogin extends javax.swing.JFrame {
    
    private final DataObservable obs;
    private JFileChooser chooser;
    
    public CloudLogin(DataObservable obs) {
            initComponents();
            this.obs = obs;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnLogin = new javax.swing.JButton();
        loginPanel = new javax.swing.JPanel();
        labelUsername = new javax.swing.JLabel();
        labelPassword = new javax.swing.JLabel();
        fieldUsername = new javax.swing.JTextField();
        btnLeave = new javax.swing.JButton();
        titlePanel = new javax.swing.JPanel();
        labelTitle = new javax.swing.JLabel();
        fieldPassword = new javax.swing.JPasswordField();
        btnRegister = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setFocusable(false);

        btnLogin.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnLogin.setText("Login");
        btnLogin.setInheritsPopupMenu(true);
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });

        loginPanel.setBackground(new java.awt.Color(200, 210, 255));
        loginPanel.setToolTipText("");
        loginPanel.setFocusable(false);
        loginPanel.setName(""); // NOI18N

        labelUsername.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        labelUsername.setForeground(new java.awt.Color(110, 110, 110));
        labelUsername.setText("Username");

        labelPassword.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        labelPassword.setForeground(new java.awt.Color(110, 110, 110));
        labelPassword.setText("Password");

        fieldUsername.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        fieldUsername.setText("joana");

        btnLeave.setBackground(new java.awt.Color(255, 255, 255));
        btnLeave.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnLeave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Client/GUI/system-log-out.png"))); // NOI18N
        btnLeave.setText("Leave");
        btnLeave.setToolTipText("");
        btnLeave.setInheritsPopupMenu(true);
        btnLeave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLeaveActionPerformed(evt);
            }
        });

        titlePanel.setBackground(new java.awt.Color(205, 200, 205));

        labelTitle.setFont(new java.awt.Font("Verdana", 1, 24)); // NOI18N
        labelTitle.setText("Login");

        javax.swing.GroupLayout titlePanelLayout = new javax.swing.GroupLayout(titlePanel);
        titlePanel.setLayout(titlePanelLayout);
        titlePanelLayout.setHorizontalGroup(
            titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(titlePanelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(labelTitle)
                .addContainerGap(270, Short.MAX_VALUE))
        );
        titlePanelLayout.setVerticalGroup(
            titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(titlePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelTitle)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        fieldPassword.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        fieldPassword.setText("1234");
        fieldPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldPasswordActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout loginPanelLayout = new javax.swing.GroupLayout(loginPanel);
        loginPanel.setLayout(loginPanelLayout);
        loginPanelLayout.setHorizontalGroup(
            loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loginPanelLayout.createSequentialGroup()
                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(loginPanelLayout.createSequentialGroup()
                        .addComponent(titlePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnLeave))
                    .addGroup(loginPanelLayout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, loginPanelLayout.createSequentialGroup()
                                .addComponent(labelPassword)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(fieldPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(loginPanelLayout.createSequentialGroup()
                                .addComponent(labelUsername)
                                .addGap(26, 26, 26)
                                .addComponent(fieldUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        loginPanelLayout.setVerticalGroup(
            loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loginPanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnLeave, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(titlePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(30, 30, 30)
                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelUsername)
                    .addComponent(fieldUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelPassword)
                    .addComponent(fieldPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        btnRegister.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnRegister.setInheritsPopupMenu(true);
        btnRegister.setLabel("Register");
        btnRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegisterActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(loginPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnRegister)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(loginPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLogin)
                    .addComponent(btnRegister))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        loginPanel.getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        File fUpload = null;
        File fDownload = null;
        try {
            // TODO add your handling code here:
                obs.login(fieldUsername.getText(),fieldPassword.getSelectedText());
                while(fUpload == null || fDownload == null){
                    try{
                        if(obs.getUploadPath() == null){
                        fUpload = getDirectory("UPLOAD folder");
                        obs.setUploadPath(fUpload);
                        }
                        if(obs.getDownloadPath() == null){
                            fDownload = getDirectory("DOWNLOAD folder");
                            obs.setDownloadPath(fDownload);
                        }
                    }catch(InvalidDirectoryException ex){ } 
                }
                
                CloudMainScreen mainScreen = new  CloudMainScreen(obs);
                this.setVisible(false);
                mainScreen.setVisible(true);
            }catch (Exception ex) {
                String str = "Provide a valid authentication";
            JOptionPane.showMessageDialog(this, 
                              str, 
                              "Not valid", 
                              JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnLoginActionPerformed

    private void btnLeaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLeaveActionPerformed
        obs.shutdownClient();//TODO check if this the right method
        System.exit(0);
    }//GEN-LAST:event_btnLeaveActionPerformed

    private void fieldPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldPasswordActionPerformed
        // TODO add your handling code here:
        //CloudRegister registerScreen = new CloudRegister();
        this.setVisible(false);
        //registerScreen.setVisible(true);
    }//GEN-LAST:event_fieldPasswordActionPerformed

    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterActionPerformed
        // TODO add your handling code here:
        CloudRegister register = new CloudRegister(obs,this);
        this.setVisible(false);
        register.setVisible(true);

    }//GEN-LAST:event_btnRegisterActionPerformed


    private void fieldUsername1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldUsername1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldUsername1ActionPerformed

    private File getDirectory(String msg)
            throws InvalidDirectoryException
    {
        chooser = new JFileChooser(System.getProperty("user.home") + "/Desktop");
        
        chooser.setDialogTitle(msg);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            File f = chooser.getSelectedFile();
            System.out.println(f.toString());
            return f;
        }
        else
        {
            throw new InvalidDirectoryException("Directory is not valid");
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLeave;
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnRegister;
    private javax.swing.JPasswordField fieldPassword;
    private javax.swing.JTextField fieldUsername;
    private javax.swing.JLabel labelPassword;
    private javax.swing.JLabel labelTitle;
    private javax.swing.JLabel labelUsername;
    private javax.swing.JPanel loginPanel;
    private javax.swing.JPanel titlePanel;
    // End of variables declaration//GEN-END:variables
}
