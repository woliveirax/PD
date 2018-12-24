package Server;

import BD.DBConnection;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServerComm extends Thread{
    private final ServerObservable serverObs;
    private List<ClientHandler> ClientsThreads;
    private static final int MAX_SIZE = 4000;
    private static final int TIMEOUT = 10000; //miliSegundos
    private ServerSocket server = null;
//    private final Socket socket = null;
    Socket nextClient = null;
    private DBConnection DB = null;
    
    private static String ADDR_IP;
    private boolean CONTINUE;
    
    public ServerComm(String DB_IP, int DB_Port) {
        try{
            DB = new DBConnection("admin","admin", DB_IP,DB_Port);
        }catch(SQLException | ClassNotFoundException e){System.err.println(e.getCause()); System.exit(1);}
        
        serverObs = new ServerObservable();
        ClientsThreads = new ArrayList<>();
        CONTINUE = true;
        
    }
    
    public void sendExit(){
        for(int i = 0; i < ClientsThreads.size(); i++)
            ClientsThreads.get(i).exit();  
    }
    
    public void exit(){
        CONTINUE = false;
        try{
            server.close();
        }catch(IOException e){
            System.out.println("could not close the socket!");
        }
    }
        
    @Override
    public void run(){
        ObjectInputStream in= null;
        ObjectOutputStream out = null;
         
        try {
            // BIND TO THE SERVICE PORT
            server = new ServerSocket(6001);
                    
//            server.setSoTimeout(TIMEOUT);
            System.out.println ("Server started, ip: + " + 
                    InetAddress.getLocalHost().getHostAddress() + " port: "
                    + server.getLocalPort());
            
            // LOOP INDEFINITELY, ACCEPTING CLIENTS
            while(CONTINUE){
                // GET THE NEXT TCP CLIENT
                nextClient = server.accept();
                System.out.println ("Received request from " + nextClient.getInetAddress() + ":" + nextClient.getPort() );
               
                // create a new thread object 
                ClientHandler t = new ClientHandler(nextClient, serverObs, DB); 
                // Invoking the start() method 
                t.start();
                ClientsThreads.add(t); 
            }

            
        }catch(IOException e){
            try{
                if(nextClient != null)
                    nextClient.close();
            }catch(IOException ex){
                System.out.println(ex);
            }
            System.out.println(e); 
        }
    }
}
