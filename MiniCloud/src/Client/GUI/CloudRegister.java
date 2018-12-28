package Client.GUI;

import Client.DataObservable;
import Exceptions.InvalidDirectoryException;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class CloudRegister extends javax.swing.JFrame {

    private final CloudLogin previous;
    private final DataObservable obs;
    
    public CloudRegister(DataObservable obs, CloudLogin previous) {
        initComponents();
        this.obs = obs;
        this.previous = previous;
        
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                obs.shutdownClient();
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnCancel = new javax.swing.JButton();
        registerPanel = new javax.swing.JPanel();
        labelUsername = new javax.swing.JLabel();
        labelPassword = new javax.swing.JLabel();
        fieldUsername = new javax.swing.JTextField();
        btnLeave = new javax.swing.JButton();
        titlePanel = new javax.swing.JPanel();
        labelTitle = new javax.swing.JLabel();
        fieldPassword = new javax.swing.JPasswordField();
        btnSave = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        btnCancel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnCancel.setText("Cancel");
        btnCancel.setInheritsPopupMenu(true);
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        registerPanel.setBackground(new java.awt.Color(200, 210, 255));
        registerPanel.setToolTipText("");
        registerPanel.setFocusable(false);

        labelUsername.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        labelUsername.setForeground(new java.awt.Color(110, 110, 110));
        labelUsername.setText("Username");

        labelPassword.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        labelPassword.setForeground(new java.awt.Color(110, 110, 110));
        labelPassword.setText("Password");

        fieldUsername.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        fieldUsername.setText("Pegasus");

        btnLeave.setBackground(new java.awt.Color(255, 255, 255));
        btnLeave.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnLeave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Client/GUI/system-log-out.png"))); // NOI18N
        btnLeave.setText("Leave");
        btnLeave.setInheritsPopupMenu(true);
        btnLeave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLeaveActionPerformed(evt);
            }
        });

        titlePanel.setBackground(new java.awt.Color(205, 200, 205));

        labelTitle.setFont(new java.awt.Font("Verdana", 1, 24)); // NOI18N
        labelTitle.setText("Register");

        javax.swing.GroupLayout titlePanelLayout = new javax.swing.GroupLayout(titlePanel);
        titlePanel.setLayout(titlePanelLayout);
        titlePanelLayout.setHorizontalGroup(
            titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(titlePanelLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(labelTitle)
                .addContainerGap(301, Short.MAX_VALUE))
        );
        titlePanelLayout.setVerticalGroup(
            titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(titlePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelTitle)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        fieldPassword.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        fieldPassword.setText("jPasswordField1");

        javax.swing.GroupLayout registerPanelLayout = new javax.swing.GroupLayout(registerPanel);
        registerPanel.setLayout(registerPanelLayout);
        registerPanelLayout.setHorizontalGroup(
            registerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(registerPanelLayout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(registerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelUsername)
                    .addComponent(labelPassword))
                .addGap(47, 47, 47)
                .addGroup(registerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(fieldUsername)
                    .addComponent(fieldPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(registerPanelLayout.createSequentialGroup()
                .addComponent(titlePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnLeave)
                .addGap(0, 10, Short.MAX_VALUE))
        );
        registerPanelLayout.setVerticalGroup(
            registerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(registerPanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(registerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnLeave, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(titlePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(29, 29, 29)
                .addGroup(registerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelUsername)
                    .addComponent(fieldUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(registerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelPassword)
                    .addComponent(fieldPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31))
        );

        btnSave.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnSave.setText("Save");
        btnSave.setInheritsPopupMenu(true);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(410, Short.MAX_VALUE)
                .addComponent(btnCancel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(registerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(registerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave)
                    .addComponent(btnCancel))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
        previous.setVisible(true);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnLeaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLeaveActionPerformed
        obs.shutdownClient();//TODO check if this the right method
        System.exit(0);
    }//GEN-LAST:event_btnLeaveActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        File fUpload = null;
        File fDownload = null;
       
        String user = fieldUsername.getText();
        String pass = new String(fieldPassword.getPassword());
        
//        if(sizeIsValid(user, 4, 10) && sizeIsValid(pass, 4, 10) && spellingIsValid(user)){
            try {
                obs.registerUser(user, pass);//TODO: uncomment
                obs.login(user,pass);
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
                    }catch(InvalidDirectoryException ex){
                        System.out.println(ex);
                    } 
                }
                
                CloudMainScreen mainScreen = new  CloudMainScreen(obs);
                this.setVisible(false);
                mainScreen.setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(),"Error in Aunthentication", JOptionPane.WARNING_MESSAGE);
            }
//        }else{
//            JOptionPane.showMessageDialog(this, "Username and password must have between 3 to 10 characters. Username cannot contain special characters, nor a number in the first letter", 
//                    "Invalid Aunthentication Data", JOptionPane.WARNING_MESSAGE);
//        }
    }//GEN-LAST:event_btnSaveActionPerformed

        private File getDirectory(String msg)
            throws InvalidDirectoryException
        {
            JFileChooser chooser = new JFileChooser(System.getProperty("user.home") + "/Desktop");

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
        
    private static boolean spellingIsValid(String s) {
        if(Character.isLetter(s.codePointAt(0))){
            return s.matches("[a-zA-Z0-9]+");
        }
        return false;
    }
    
    private static boolean sizeIsValid(String name, int minSize, int maxSize) {
        return name.length() >= minSize && name.length() <= maxSize;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnLeave;
    private javax.swing.JButton btnSave;
    private javax.swing.JPasswordField fieldPassword;
    private javax.swing.JTextField fieldUsername;
    private javax.swing.JLabel labelPassword;
    private javax.swing.JLabel labelTitle;
    private javax.swing.JLabel labelUsername;
    private javax.swing.JPanel registerPanel;
    private javax.swing.JPanel titlePanel;
    // End of variables declaration//GEN-END:variables
}
