package Server;

import BD.ConnectedUser;
import Exceptions.UserException;
import comm.AuthPackets.LoginAccepted;
import comm.InitialFilePackage;
import comm.KeepAlive;
import comm.LoginInfo;
import comm.RefreshData;
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

public class ClientHandler extends Thread {

    private final ServerObservable serverObs;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private final Socket s;
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

    private void createDatagramAndSendIt(ConnectedUser receiver,String msg){
        
            DatagramSocket dSckt = null;
            DatagramPacket dPkt = null;

            try {
                dSckt = new DatagramSocket();
                dPkt = new DatagramPacket(msg.getBytes(), msg.length(), InetAddress.getByName(receiver.getIp()), receiver.getPort());
                dSckt.send(dPkt);

            } catch (UnknownHostException e) {
                try {
                    dPkt = new DatagramPacket(e.toString().getBytes(), e.toString().length(), InetAddress.getByName(receiver.getIp()), receiver.getPort());
                } catch (UnknownHostException ex) {
                }
                try {
                    if (dSckt != null) {
                        dSckt.send(dPkt);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

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
        Object toreturn;

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
                        System.out.println("mmmmm");
                    } else {
                        sendGlobalMsg(msg);
                    }

                } else if (received instanceof LoginInfo) {//TODO: compare to all object types
                    try{
                        serverObs.getDB().userLogin((LoginInfo)received,s.getInetAddress().getHostAddress());
                    }catch(UserException | SQLException e){
                        writeObject(e);
                    }
                    writeObject(new LoginAccepted());
                    System.out.println("Login accepted"); 
                    username = ((LoginInfo) received).getUsername();
                    serverObs.addLoggedThread(this);

                } else if (received instanceof InitialFilePackage) {
                    InitialFilePackage filePackages = (InitialFilePackage)received;
                    
                    //addFile(String username, String filename, long filesize)
                } else if (received instanceof KeepAlive) {
                    try{
                        serverObs.getDB().setStrikes(this.username,0);
                    }catch(SQLException | UserException e){
                        System.err.println(e);
                    }
                    //update DB
                } else if (received instanceof RefreshData) {
                    //updateClientsFilesInfo throught UDP and TCP
                }
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
