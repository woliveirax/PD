package Server;

import BD.ConnectedUser;
import BD.DBConnection;
import Exceptions.UserException;
import comm.AuthPackets.LoginAccepted;
import comm.CloudData;
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
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler extends Thread {

    private final ServerObservable serverObs;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private final Socket s;
    private DBConnection DB;
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
                }

            } catch (SocketException e) {
            } catch (IOException e) {
            }
    }
    
    private void sendPrivateMsg(String msg, String dest_name) {
        msg = "PM -" + username + ": " + msg;
        Boolean found = false;

        for (int i = 0; i < serverObs.getLoggedClients().size(); i++) {
            if (serverObs.getLoggedClients().get(i).compareTo(dest_name) == 0) {
                try {
                    serverObs.getLoggedUserThreads().get(i).writeObject(msg);
                } catch (IOException e) {
                    try {
                        serverObs.getLoggedUserThreads().get(i).writeObject(
                                new IOException("Error while sending chat msg"));
                    } catch (IOException ex) {
                    }
                }
                found = true;
                break;
            }
        }

        if (!found) {
            ConnectedUser receiver = null;
            try {
                receiver = DB.getSingleAuthenticatedUser(dest_name);
            } catch (SQLException | UserException e) {
            }
            
            createDatagramAndSendIt(receiver,msg);
        }
    }

    private void sendGlobalMsg(String msg) {
        //à msg colocar no ini: nome do user:
        ArrayList<ConnectedUser> outsideUsers = null;
        msg = username + ": " + msg;

        try {
            outsideUsers = DB.getAuthenticatedUsers();
        } catch (SQLException e) {
        }

        if (outsideUsers != null) { 
            //gets the authenticated users from other servers
            for (Iterator<ConnectedUser> it = outsideUsers.iterator(); it.hasNext(); it.next()) {
                ConnectedUser next = it.next();
                for (String connectedUser : serverObs.getLoggedClients()) {
                    if (next.getUsername().compareTo(connectedUser) == 0) {
                        it.remove();
                    }
                }
            }
            //sends the Msg via UDP to the users from other servers
            for(ConnectedUser receiver : outsideUsers){ //Sends UDP msg
                 createDatagramAndSendIt(receiver,msg);
            }
            //sends the Msg via TCP to the logged users to this server
            for (int i = 0; i < serverObs.getLoggedClients().size(); i++) {
                if (serverObs.getLoggedClients().get(i).compareTo(username) != 0) {
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
                    if (DB.userExists(splitted[1])) {
                        if (msg.startsWith("@", 0)) {
                            sendPrivateMsg(msg, splitted[1]);
                        } else {
                            sendGlobalMsg(msg);
                        }
                    }

                } else if (received instanceof LoginInfo) {//TODO: compare to all object types
                    //TODO: check in DATABASE IF USER HAS VALID CREDENCIALS
                    writeObject(new LoginAccepted());
                    System.out.println("Login accepted");
                    username = ((LoginInfo) received).getUsername();
                    serverObs.addLoggedClient(username);
                    serverObs.addLoggedThread(this);

                } else if (received instanceof InitialFilePackage) {
                    //updateClientsFilesInfo throught UDP and TCP
                } else if (received instanceof KeepAlive) {
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
