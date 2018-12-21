package Server;

import comm.LoginInfo;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerComm {
    private final ServerObservable serverObs;

    private static final int MAX_SIZE = 4000;
    private static final int TIMEOUT = 5000; //miliSegundos
    private ServerSocket server = null;
    private final Socket socket = null;
    Socket nextClient = null;
    
    private static String DB_IP;
    private static int DB_Port;
    
    public ServerComm(String DB_IP, int DB_Port){
        ServerComm.DB_IP = DB_IP;
        ServerComm.DB_Port = DB_Port;
        
        serverObs = new ServerObservable();
    }
    
    public void startComm() {
        ObjectInputStream in= null;
        ObjectOutputStream out = null;
        LoginInfo loginInfo = null;
        
        try {
            // BIND TO THE SERVICE PORT
            server = new ServerSocket(6001);
            System.out.println ("Server started, port: "+server.getLocalPort());
            
            // LOOP INDEFINITELY, ACCEPTING CLIENTS
            for (;;){
                // GET THE NEXT TCP CLIENT
                nextClient = server.accept();
                System.out.println ("Received request from " + nextClient.getInetAddress() + ":" + nextClient.getPort() );
               
                // create a new thread object 
                Thread t = new ClientHandler(nextClient); 
                //TODO: change so that only logged clients are added
                serverObs.addLoggedClient(t);
                // Invoking the start() method 
                t.start(); 
                
            }
        }catch(IOException e){
            try{
                nextClient.close();
            }catch(IOException ex){
                System.out.println("error: "+ ex);  //TODO: Send o exception object as message to client 
            }
            System.out.println("Error alert!!s"); //TODO: Send o exception object as message to client 
        }
    }
}
