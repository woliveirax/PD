package Server;

import BD.ConnectedUser;
import Exceptions.FileException;
import Exceptions.UserException;
import comm.AuthPackets.LoginAccepted;
import comm.FileData;
import comm.Packets.InitialFilePackage;
import comm.Packets.KeepAlive;
import comm.LoginInfo;
import comm.Packets.AddFileRequest;
import comm.Packets.RemoveFileRequest;
import comm.Packets.UpdateFileRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;

public class ClientHandler extends Thread {

    private final ServerObservable serverObs;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private final Socket s;
    private Socket notification;
    private String username;
    private boolean CONTINUE;

    private int TIMEOUT = 5000;

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
    }
    
    public void startNotificationSocket(LoginInfo info) {
        try {
            notification = new Socket(s.getInetAddress(),info.getNotificationPort());
        } catch (IOException ex) {
            try {
                serverObs.getDB().userLogout(username);
            } catch (UserException | SQLException ex1) {
                //Do nothing
            }
            this.exit();
        }
    }

    private void createDatagramAndSendIt(ConnectedUser receiver,String msg){
        
        DatagramSocket dSckt = null;
        DatagramPacket dPkt = null;
        
        try {
            
            ByteArrayOutputStream bStream = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bStream);
            
            out.writeObject(msg);
            out.flush();
            out.close();
            
            dSckt = new DatagramSocket(0);
            dPkt = new DatagramPacket(bStream.toByteArray(), bStream.size(), InetAddress.getByName(receiver.getIp()), receiver.getKeepAlivePort());
            dSckt.send(dPkt);
            
            //TODO: recebe o pacote com timeout!
            
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
            ConnectedUser receiver = null;
            try {
                receiver = serverObs.getDB().getSingleAuthenticatedUser(dest_name);
            } catch (SQLException | UserException e) {
                System.out.println(e);
            }
            
            createDatagramAndSendIt(receiver,msg);
        }
    }

    private void sendGlobalMsg(String msg) {
        //à msg colocar no ini: nome do user:
        ArrayList<ConnectedUser> outsideUsers = null;
        msg = username + ": " + msg;

        try {
            outsideUsers =  serverObs.getDB().getAuthenticatedUsers();
        } catch (SQLException e) {
        }

        if (outsideUsers != null) { 
            //gets the authenticated users from other servers
            for (Iterator<ConnectedUser> it = outsideUsers.iterator(); it.hasNext();) {
                ConnectedUser next = it.next();
                for (ClientHandler connectedUser : serverObs.getLoggedUserThreads()) {
                    if (next.getUsername().compareTo(connectedUser.username) == 0) {
                        it.remove();
                    }
                }
            }
            //sends the Msg via UDP to the users from other servers
            for(ConnectedUser receiver : outsideUsers){ //Sends UDP msg
                System.out.println("Envia UDP para: " + receiver);
                 createDatagramAndSendIt(receiver,msg);
            }
            
            //sends the Msg via TCP to the logged users to this server
            for (int i = 0; i < serverObs.getLoggedUserThreads().size(); i++) {
                if (serverObs.getLoggedUserThreads().get(i).username.compareTo(username) != 0) {
                    try {
                        serverObs.getLoggedUserThreads().get(i).writeObject(msg);
                    } catch (IOException e) {
                        try {
                            serverObs.getLoggedUserThreads().get(i).writeObject(
                                    new IOException("Error while sending chat msg"));
                        } catch (IOException ex) {
                        }
                    }
                }
            }
        }
    }

    @Override
    public void run() {
        Object received;

        try {//initialize stream reader and writer
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
                    if (msg.startsWith("@", 0) && serverObs.getDB().userExists(splitted[1])) {
                        sendPrivateMsg(msg, splitted[1]);
                    } else {
                        sendGlobalMsg(msg);
                    }

                } else if (received instanceof LoginInfo) {//TODO: compare to all object types
                    try{
                        serverObs.getDB().userLogin((LoginInfo)received,s.getInetAddress().getHostAddress());
                        //startNotificationSocket((LoginInfo) received);
                    }catch(UserException | SQLException e){
                        writeObject(e);
                    }
                    writeObject(new LoginAccepted());
                    System.out.println("Login accepted"); 
                    username = ((LoginInfo) received).getUsername();
                    serverObs.addLoggedThread(this);

                } else if (received instanceof InitialFilePackage) {
                    System.out.println("Initial file");
                    InitialFilePackage filePackages = (InitialFilePackage)received;
                    for(FileData file : filePackages.getFiles()){
                        try {
                            serverObs.getDB().addFile(this.username, file.getName(),file.getSize());
                        } catch (UserException | SQLException | FileException ex) {
                            System.err.println(ex);
                        }
                    }
                    //TODO: missing notify for updateClientsFilesInfo throught UDP and TCP
                } else if (received instanceof AddFileRequest) {
                    try{
                        System.out.println("Adding file");
                        FileData file = ((AddFileRequest)received).getFile();
                        serverObs.getDB().addFile(username, file.getName(),file.getSize());
                        //serverObs.getDB().setStrikes(this.username,0);
                    }catch(SQLException | UserException | FileException e){
                        System.out.println(e);
                        writeObject(e);
                    }
                    
                } else if (received instanceof RemoveFileRequest) {
                    try{
                        System.out.println("Removing");
                        serverObs.getDB().removeFile(username, ((RemoveFileRequest)received).getFilename());
                        //serverObs.getDB().setStrikes(this.username,0);
                    }catch(SQLException | UserException | FileException e){
                        System.out.println(e);
                        writeObject(e);
                    }
                } else if (received instanceof UpdateFileRequest) {
                    try{
                        System.out.println("updating");
                        FileData file = ((UpdateFileRequest)received).getFile();
                        serverObs.getDB().updateFile(username,file);
                        //serverObs.getDB().setStrikes(this.username,0);
                    }catch(SQLException | UserException | FileException e){
                        System.out.println(e);
                        writeObject(e);
                    }
                }
                //}
            }catch (IOException e) {
                System.out.println("IO" + e);
                break;
            }catch (ClassNotFoundException e) {
                System.out.println("CNF" + e);
                break;
            }
        }
    }

    private void writeObject(Object obj) throws IOException{
        out.writeObject(obj);
        out.flush();
    }
}
