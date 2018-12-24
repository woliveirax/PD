package Client.Threads;

import Client.DataObservable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NotificationThread extends Thread {
    private ServerSocket server;
    private Socket socket;
    private DataObservable observable;
    
    private boolean CONTINUE;
    
    public NotificationThread(DataObservable obs) throws IOException{
            this.observable = obs;
            CONTINUE = true;
            
            server = new ServerSocket(0,5);
    }
    
    public void exit(){
        try {
            server.close();
        } catch (IOException ex) {
            System.out.println("Socket can't close!");
        }
        
        CONTINUE = false;
    }
    
    public int getPort(){
        return server.getLocalPort();
    }
    
    @Override
    public void run(){
        try {
            socket = server.accept();
            
            //while(CONTINUE){
            //}
        } catch (IOException ex) {
            throw new RuntimeException("Error accepting the socket");
        }
    }
}