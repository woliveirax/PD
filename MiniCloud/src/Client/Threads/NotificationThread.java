package Client.Threads;

import Client.DataObservable;
import java.net.Socket;

public class NotificationThread extends Thread {
    private Socket socket;
    DataObservable observable;
    
    public NotificationThread(DataObservable obs){
        this.observable = obs;
        
        //TODO: Create server socket and other great things
    }
    
    public int getPort(){
        return socket.getLocalPort();
    }
    
    @Override
    public void run(){
    }
}