package Server;

import BD.DBConnection;
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
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;
import org.omg.CORBA.TIMEOUT;


public class ClientHandler extends Thread{
    private final ServerObservable serverObs;
    private ObjectInputStream in; 
    private ObjectOutputStream out; 
    private final Socket s; 
    private DBConnection DB;
    private String username;
    private boolean CONTINUE;
    
    private int TIMEOUT = 5000;
    public ClientHandler(Socket s, ServerObservable serverObs)
    { 
        this.s = s;
        this.serverObs = serverObs;
        
        CONTINUE = true;
    } 
    
    public void exit(){
        CONTINUE = false;
        try{
            s.close();
        }catch(IOException e){
            System.out.println("could not close the socket!");
        }
    }
      
    private void sendPrivateMsg(String msg, String dest_name){
        //get userSocketData from DB
        msg = "PM -" + username + ": " + msg;
        Boolean found = false;

        for(int i = 0; i < serverObs.getLoggedClients().size(); i++)
            if(serverObs.getLoggedClients().get(i).compareTo(username) == 0){
                try{
                    serverObs.getLoggedUserThreads().get(i).out.writeObject(msg);
                    }catch(IOException e){
                        try{
                            serverObs.getLoggedUserThreads().get(i).out.writeObject(
                                    new IOException("Error while sending chat msg"));
                        }catch(IOException ex){}
                    }
                found = true;
                break;
            }
        
        if(!found){
            //CloudData receiver = DB.getReceiverData(dest_name);
            try{
                DatagramSocket dSckt = new DatagramSocket();
                dSckt.setSoTimeout(TIMEOUT);
                //DatagramPacket dPkt = new DatagramPacket(msg.getBytes(),msg.length(),receiver.getPorto(),InetAddress().getByName(receiver.getIP));
                //dSckt.send(dPkt);
            }catch(SocketException e){ }
            
        }
        //s não: mandar msg UDP
    }
    
    private void sendGlobalMsg(String msg){
        //à msg colocar no ini: nome do user:
        msg = username + ": " + msg;
        Boolean found = false;
        //ArrayList <CloudData> users = DB.getUsers();
        
        //1 get AuthUers
        //2 remover logged users
        //3 mandar msg TCP p self logged Users
        //4 mandar msg UDP p other logged Users
    }
    
    @Override
    public void run()  
    { 
        Object received; 
        Object toreturn; 
        
        try{//initialize stream reader and writer
            out = new ObjectOutputStream(s.getOutputStream());
            in = new ObjectInputStream(s.getInputStream());
        }catch(IOException e){
            e.printStackTrace();
            try{
                s.close();
            }catch(IOException ex){
                ex.printStackTrace();
            }
            return;
        }
           
        while (CONTINUE)  
        { 
            try {
                // receive the answer from client 
                received = in.readObject();
                
                if(received instanceof String){//means a message is to be sent to other people
                    String msg = (String)received;
                    String [] splitted = msg.split("\\s+|\\n+|\\@");//separates when it encounters a space(\\s), a paragraph(\\n) or an arroba(\\@)
                    if(splitted[0].compareTo("@")==0 && DB.isUser(splitted[1]))
                        //sendPrivateMsg(msg,splitted[1])
                        return;//TODO: remove
                    else
                        //sendGlobslMsg(msg)//sends msg to everyone
                        return;//TODO: remove
                }else if(received instanceof LoginInfo){//TODO: compare to all object types
                    //TODO: check in DATABASE IF USER HAS VALID CREDENCIALS
                    out.writeObject(new LoginAccepted());
                    out.flush();
                    System.out.println("Login accepted");
                    username = ((LoginInfo) received).getUsername();
                    serverObs.addLoggedClient(username);
                    serverObs.addLoggedThread(this);
                    
                }else if(received instanceof InitialFilePackage){
                    //updateClientsFilesInfo throught UDP and TCP
                }else if(received instanceof KeepAlive){
                    //update DB
                }else if(received instanceof RefreshData){
                    //updateClientsFilesInfo throught UDP and TCP
                }
            }catch(SQLException e){
                
            }catch (IOException e) { 
                System.out.println("IO"+e);
                break;
            }catch (ClassNotFoundException e){
                System.out.println("CNF"+e);
                break;
            }
        }
    }
}
