package Client.Threads;

import Client.DataObservable;
import comm.AuthPackets.LoginAccepted;
import comm.AuthPackets.LoginDenied;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class CommunicationThread extends Thread {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    
    private final DataObservable observable;
    private boolean CONTINUE;
    
    public CommunicationThread(int port, String srvAddr, DataObservable obs) 
            throws UnknownHostException, IOException
    {
        this.observable = obs;
        socket = new Socket(srvAddr,port);
        
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        
        CONTINUE = true;
    }
    
    private void sendMsg(Object msg) throws IOException{
        try{
            out.writeObject(msg);
            out.flush();
        }catch(IOException e){
            throw e;
        }        
    }
    
    public void sendChatMessage(String message) throws IOException{
        sendMsg(message);
    }
    
    public void exit(){
        CONTINUE = false;
        try{
            socket.close();
        }catch(IOException e){
            System.out.println("could not close the socket!");
        }
    }
    
    @Override
    public void run() {
        try{
            Object obj;
            
            while(CONTINUE)
            {
                obj = in.readObject();
                
                if(obj instanceof LoginAccepted){
                    System.out.println("Logged In the server!");
                }
                else if(obj instanceof LoginDenied){
                    System.out.println("Login Denied!");
                }
            }
            
        }catch(IOException e){
            System.out.println("Error getting input stream!");
        }catch(ClassNotFoundException e){
            System.out.println("class unknown!");
        }
    }
}
