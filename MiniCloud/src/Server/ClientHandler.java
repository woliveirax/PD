package Server;

import BD.ConnectedUser;
import Exceptions.FileException;
import Exceptions.UserException;
import comm.AuthPackets.LoginAccepted;
import comm.AuthPackets.Logout;
import comm.FileData;
import comm.Packets.InitialFilePackage;
import comm.LoginInfo;
import comm.Packets.AddFileRequest;
import comm.Packets.DataMass;
import comm.Packets.RemoveFileRequest;
import comm.Packets.TransferHistoryPackage;
import comm.Packets.UpdateFileRequest;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler extends Thread implements Observer {

    private final ServerObservable serverObs;
    
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private final Socket s;
    
    private ObjectOutputStream notificationOut;
    private Socket notification;
    
    private String username;
    private boolean CONTINUE;

    public ClientHandler(Socket s, ServerObservable serverObs) {
        this.s = s;
        this.serverObs = serverObs;
        CONTINUE = true;
    }

    public void exit() {
        CONTINUE = false;
        try {
            s.close();
        } catch (IOException e) {
            System.out.println("could not close the socket!");
        }
        
        try {
            if(notification != null)
                notification.close();
        } catch (IOException e) {
            System.out.println("could not close the socket!");
        }
    }
    
    public String getUsername(){
        return username;
    }
    
    public void startNotificationSocket(LoginInfo info) {
        try { 
            System.out.println("IP: " + s.getInetAddress().getHostAddress() + " Port: " + info.getNotificationPort());
            notification = new Socket(s.getInetAddress().getHostAddress(),info.getNotificationPort());
            System.out.println("notificationPort: " + info.getNotificationPort());
            
            notificationOut = new ObjectOutputStream(notification.getOutputStream());
            
        } catch (IOException ex) {
            try {
                serverObs.userLogout(username);
            } catch (UserException | SQLException ex1) {
            }
            this.exit();
        }
    }
    
    private void sendPrivateMsg(String msg, String dest_name) {
        msg = "PM -" + username + ": " + msg;
        Boolean found = false;

        for (int i = 0; i < serverObs.getLoggedUserThreads().size(); i++) {
            if (serverObs.getLoggedUserThreads().get(i).username.compareTo(dest_name) == 0) {
                try {
                    serverObs.getLoggedUserThreads().get(i).writeObject(msg);
                } catch (IOException e) {
                    try {
                        serverObs.getLoggedUserThreads().get(i).writeObject(
                                new IOException("Error while sending chat msg"));
                    } catch (IOException ex) {
                        e.printStackTrace();
                    }
                }
                found = true;
                break;
            }
        }
        
        if (!found) {
            ConnectedUser receiver;
            try {
                receiver = serverObs.getSingleAuthenticatedUser(dest_name);
                serverObs.sendUdpPacketToClient(receiver, msg);
            } catch (SQLException | UserException e) {
                System.out.println(e);
            }
        }
    }

    private void sendGlobalMsg(String msg) {
        //à msg colocar no ini: nome do user:
        ArrayList<ConnectedUser> outsideUsers;
        msg = username + ": " + msg;

        try {
            outsideUsers =  serverObs.getAuthenticatedUsers();
                    
            for (ClientHandler user : serverObs.getLoggedUserThreads()){
                try {
                    user.writeObject(msg);
                } catch (IOException ex) {
                    System.out.println("Error sending chat message");
                }
            }
            
            serverObs.broadcastUdpPacketToClients(msg);
            
        } catch(SQLException e){
              System.out.println(e);
        }
    }

    @Override
    public void run() {
        Object received;

        try {
            out = new ObjectOutputStream(s.getOutputStream());
            in = new ObjectInputStream(s.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            try {
                s.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return;
        }

        while (CONTINUE) {
            try {
                // receive the answer from client 
                received = in.readObject();
                
                if (received instanceof String) {//means a message is to be sent to other people                    
                    String msg = (String) received;
                    String[] splitted = msg.split("\\s+|\\n+|\\@");//separates when it encounters a space(\\s), a paragraph(\\n) or an arroba(\\@)
                    if (msg.startsWith("@", 0) && serverObs.userExists(splitted[1])) {
                        sendPrivateMsg(msg, splitted[1]);
                    } else {
                        sendGlobalMsg(msg);
                    }

                } else if (received instanceof AddFileRequest) {
                    try{
                        System.out.println("Adding file \'" + ((AddFileRequest)received).getFile().getName() + "\' to client: " + username);
                        
                        FileData file = ((AddFileRequest)received).getFile();
                        serverObs.addFile(username, file.getName(),file.getSize());
                        
                    }catch(SQLException | UserException | FileException e){
                        System.out.println(e);
                        //writeObject(e);
                    }
                    
                } else if (received instanceof RemoveFileRequest) {
                    try{
                        System.out.println("Removing File \'" + ((RemoveFileRequest)received).getFilename() + "\' to client: " + username );
                        serverObs.removeFile(username, ((RemoveFileRequest)received).getFilename());
                        
                    }catch(SQLException | UserException | FileException e){
                        System.out.println(e);
                        //writeObject(e);
                    }
                } else if (received instanceof UpdateFileRequest) {
                    try{
                        System.out.println("Updating file \'" + ((UpdateFileRequest)received).getFile().getName() + "\' to client: " + username);
                        FileData file = ((UpdateFileRequest)received).getFile();
                        serverObs.updateFile(username,file);
                        
                    }catch(SQLException | UserException | FileException e){
                        System.out.println(e);
                    }
                    
                } else if (received instanceof DataMass) {
                    try{
                        writeObject(serverObs.getAllUsersData());
                        
                    }catch(IOException | SQLException | UserException e){
                        writeObject(e);
                    }
                    
                } else if (received instanceof LoginInfo) {
                    try{
                        serverObs.userLogin((LoginInfo)received,s.getInetAddress().getHostAddress());
                        startNotificationSocket((LoginInfo)received);
                        serverObs.addObserver(this);
                    }catch(UserException | SQLException e){
                        writeObject(e);
                        System.out.println("Login accepted for client: {" + s.getInetAddress());
                    }
                    
                    writeObject(new LoginAccepted());
                    System.out.println("Login accepted for client: {" + s.getInetAddress() + "} as "+ ((LoginInfo) received).getUsername()); 
                    username = ((LoginInfo) received).getUsername();
                    serverObs.addLoggedThread(this);

                } else if (received instanceof InitialFilePackage) {
                    System.out.println("Initial files package from client: " + username);
                    InitialFilePackage filePackages = (InitialFilePackage)received;
                    serverObs.addFilesBunk(username, filePackages);
                } else if (received instanceof Logout) {
                    System.out.println("User: " + username + " Logged out");
                    serverObs.disconnectUser(username);
                    
                } else if (received instanceof TransferHistoryPackage) {
                    System.out.println("client : " + username + "requested history");
                    try {
                        writeObject(serverObs.getTransferHistory(username));
                    } catch (SQLException ex) {
                    }
                }
                
            }catch (IOException e) {
                serverObs.deleteObserver(this);
                this.exit();
            }catch (ClassNotFoundException e) {
                System.out.println("CNF" + e);
            }
        }
    }
    
    private void writeObject(Object obj) throws IOException{
        out.writeObject(obj);
        out.flush();
    }

    @Override
    public void update(Observable o, Object arg) {
        try{
            notificationOut.writeObject(arg);
            notificationOut.flush();
            
        }catch(IOException e){
        }
    }
}
