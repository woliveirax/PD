package Client.GUI;

import Client.DataObservable;
import Client.TransferNotification;
import Client.UpdateType;
import Client.WatchDog.WatchDogException;
import Exceptions.DirectoryException;
import comm.CloudData;
import comm.FileData;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class CloudMainScreen extends javax.swing.JFrame implements Observer, UpdateType{
    JFileChooser chooser;
    DataObservable observable;
    
    public CloudMainScreen(DataObservable o) {
        initComponents();
        observable = o;
        downloadSelection();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog();
        jTextField1 = new javax.swing.JTextField();
        mainPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        fieldMessage = new javax.swing.JTextArea();
        btnLogout = new javax.swing.JButton();
        titlePanel = new javax.swing.JPanel();
        labelTitle = new javax.swing.JLabel();
        btnSend = new javax.swing.JButton();
        btnTransfers = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtAreaChat = new javax.swing.JTextArea();
        fieldNotification = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableFiles = new javax.swing.JTable();

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        jTextField1.setEditable(false);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mainPanel.setBackground(new java.awt.Color(200, 210, 255));
        mainPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        mainPanel.setForeground(new java.awt.Color(225, 225, 225));
        mainPanel.setToolTipText("mainPanel");
        mainPanel.setFocusable(false);
        mainPanel.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        mainPanel.setPreferredSize(new java.awt.Dimension(800, 500));

        fieldMessage.setColumns(20);
        fieldMessage.setRows(5);
        jScrollPane3.setViewportView(fieldMessage);

        btnLogout.setBackground(new java.awt.Color(255, 255, 255));
        btnLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Client/GUI/system-log-out.png"))); // NOI18N
        btnLogout.setText("Logout");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        titlePanel.setBackground(new java.awt.Color(106, 90, 205));
        titlePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));

        labelTitle.setFont(new java.awt.Font("Verdana", 1, 24)); // NOI18N
        labelTitle.setForeground(new java.awt.Color(255, 255, 255));
        labelTitle.setText("Client Cloud");
        labelTitle.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        javax.swing.GroupLayout titlePanelLayout = new javax.swing.GroupLayout(titlePanel);
        titlePanel.setLayout(titlePanelLayout);
        titlePanelLayout.setHorizontalGroup(
            titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(titlePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelTitle)
                .addContainerGap(474, Short.MAX_VALUE))
        );
        titlePanelLayout.setVerticalGroup(
            titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(titlePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelTitle)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnSend.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnSend.setForeground(new java.awt.Color(0, 100, 255));
        btnSend.setText("SEND");
        btnSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendActionPerformed(evt);
            }
        });

        btnTransfers.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        btnTransfers.setText("Transfers");
        btnTransfers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTransfersActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel2.setText("Chat Room");

        txtAreaChat.setEditable(false);
        txtAreaChat.setColumns(20);
        txtAreaChat.setRows(5);
        jScrollPane4.setViewportView(txtAreaChat);

        fieldNotification.setEditable(false);

        tableFiles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "User", "FileName", "Size"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tableFiles);
        if (tableFiles.getColumnModel().getColumnCount() > 0) {
            tableFiles.getColumnModel().getColumn(0).setMinWidth(80);
            tableFiles.getColumnModel().getColumn(0).setPreferredWidth(80);
            tableFiles.getColumnModel().getColumn(0).setMaxWidth(80);
            tableFiles.getColumnModel().getColumn(1).setPreferredWidth(14);
            tableFiles.getColumnModel().getColumn(2).setMinWidth(40);
            tableFiles.getColumnModel().getColumn(2).setPreferredWidth(40);
            tableFiles.getColumnModel().getColumn(2).setMaxWidth(40);
        }

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE)
                            .addComponent(fieldNotification))
                        .addGap(18, 18, 18)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(mainPanelLayout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnSend, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnTransfers, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(jScrollPane4)))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(titlePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnLogout, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)))
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(titlePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(fieldNotification, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addComponent(btnTransfers, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnSend, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE)
        );

        mainPanel.getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        try {
            observable.logout();
            DataObservable obs = new DataObservable();
            CloudLogin login = new CloudLogin(obs);
            obs.startServerConnection(observable.getServerIPAddr(),observable.getServerPort());
            observable = obs;
            
            this.setVisible(false);
            login.setVisible(true);
        } catch (WatchDogException | IOException | DirectoryException ex) {
            JOptionPane.showMessageDialog(this, 
                              ex.getMessage() + "Forced shutdown activated", 
                              "Error trying to loggout", 
                              JOptionPane.WARNING_MESSAGE);
            System.exit(1);
        }
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed
        try { 
            observable.sendChatMessage(fieldMessage.getText());
            fieldMessage.setText("");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, 
                              ex.getMessage(), 
                              "Error trying to send msg", 
                              JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnSendActionPerformed

    private void btnTransfersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransfersActionPerformed
        FilesTransferHistory transfers = new FilesTransferHistory(observable, this);
        this.setVisible(false);
        transfers.setVisible(true);
    }//GEN-LAST:event_btnTransfersActionPerformed

    private void downloadSelection(){
        tableFiles.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent event) {
                try {
                    String value;
                    Object o = tableFiles.getValueAt(tableFiles.getSelectedRow(),1);
                    System.out.println(o.toString());
                    if( o != null){
                        value = o.toString();
                        File f = new File(value);
                        observable.addFileRequest(f);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(CloudMainScreen.this,ex.getMessage(), "Error in File Selection", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnSend;
    private javax.swing.JButton btnTransfers;
    private javax.swing.JTextArea fieldMessage;
    private javax.swing.JTextField fieldNotification;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel labelTitle;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JTable tableFiles;
    private javax.swing.JPanel titlePanel;
    private javax.swing.JTextArea txtAreaChat;
    // End of variables declaration//GEN-END:variables

    
    //TODO: this needs to do something
    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof Integer){//Means it is a FileUpdate
                
            System.out.println("Ola amigo");
                ArrayList<CloudData> users = observable.getUsers();
                DefaultTableModel model = new DefaultTableModel();
                model.addColumn("User");
                model.addColumn("File Name");
                model.addColumn("Size");
                
                for(CloudData user : users){
                    ArrayList<FileData> files = user.getFiles();
                    for(FileData file : files){
                        //tableFiles.addRow(new Object[]{user.getUser(),file.getName(),file.getSize()});
                        model.addRow(new Object[]{user.getUser(),file.getName(), file.getSize()});
                    }
                }
                tableFiles.setModel(model);
        }else if(arg instanceof String)//Means it's a chat msg
            txtAreaChat.append((String)arg);
        else if(arg instanceof TransferNotification)//Means it's a transfernotification
            fieldNotification.setText(((TransferNotification)arg).getDetails());
        else //Means it's a dta mass package
            System.out.println("Data Mass xD"); //TODO: replace
           
    }
}
