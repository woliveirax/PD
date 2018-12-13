package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerComm {
    
    public static final int MAX_SIZE = 4000;
    public static final int TIMEOUT = 5000; //miliSegundos
    private ServerSocket server = null;
    private Socket socket = null;
    Socket nextClient = null;

    public ServerComm() {
        try {
            // BIND TO THE SERVICE PORT
            server = new ServerSocket(0);
            System.out.println ("Server started");
            
            // LOOP INDEFINITELY, ACCEPTING CLIENTS
            for (;;){
                // GET THE NEXT TCP CLIENT
                nextClient = server.accept();
                System.out.println ("Received request from " + nextClient.getInetAddress() + ":" + nextClient.getPort() );
                
                //TODO: retrieve comm object and give an answer accordingly
                // obtaining input and out streams 
                DataInputStream dis = new DataInputStream(nextClient.getInputStream()); 
                DataOutputStream dos = new DataOutputStream(nextClient.getOutputStream()); 
                // create a new thread object 
                Thread t = new ClientHandler(nextClient, dis, dos); 
  
                // Invoking the start() method 
                t.start(); 
                
            }
        }catch(Exception e){
            try{
                nextClient.close();
            }catch(IOException ex){
                System.out.println("error: "+ ex);  //TODO: Send o exception object as message to client 
            }
            System.out.println("Error alert!!"); //TODO: Send o exception object as message to client 
        }
    }
    
    
    
}
