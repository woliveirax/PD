package Client.Threads;

import Client.DataObservable;
import comm.Packets.AddFileRequest;
import comm.Packets.AddUser;
import comm.Packets.InitialFilePackageNotification;
import comm.Packets.RemoveFileRequest;
import comm.Packets.RemoveUser;
import comm.Packets.UpdateFileRequest;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class NotificationThread extends Thread {
    private final DataObservable observable;
    
    private final ServerSocket server;
    private  Socket socket;
    
    private ObjectInputStream in;
    private ObjectOutputStream out;
    
    private boolean CONTINUE;
    
    public NotificationThread(DataObservable obs) throws IOException{
            this.observable = obs;
            CONTINUE = true;
            
            server = new ServerSocket(0,5);
    }
    
    public int getPort(){
        return server.getLocalPort();
    }
    
    public void exit(){
        CONTINUE = false;
        
        try {
            if(socket != null)
                socket.close();
        } catch (IOException ex) {
            System.out.println("Socket can't be closed!");
        }
        
        try {
            server.close();
        } catch (IOException ex) {
            System.out.println("Server Socket can't be closed!");
        }
        
        try {
            if(in != null)
                in.close();
        } catch (IOException ex) {
            System.out.println("Error closing input stream");
        }
        
        try {
            if(out != null)
                out.close();
        } catch (IOException ex) {
            System.out.println("Error closing output stream");
        }
    }
    
    @Override
    public void run(){
        try {
            socket = server.accept();
            
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            
            while(CONTINUE){
                Object obj = in.readObject();
        
                if(obj instanceof String){
                            observable.sendChatMessage((String)obj);

                } else if(obj instanceof AddFileRequest){
                    observable.addFileFromUser(((AddFileRequest)obj).getUsername(),
                                                ((AddFileRequest)obj).getFile());


                } else if (obj instanceof RemoveFileRequest) {
                    observable.removeFileFromUser(((RemoveFileRequest)obj).getUsername(),
                                                ((RemoveFileRequest)obj).getFilename());


                } else if (obj instanceof UpdateFileRequest) {
                    observable.updateFileFromUser(((UpdateFileRequest)obj).getUsername(),
                                                ((UpdateFileRequest)obj).getFile());


                } else if (obj instanceof RemoveUser){
                    observable.removeUserFromFileList(((RemoveUser)obj).getUsername());


                } else if (obj instanceof AddUser){
                    observable.addUserToFileList(((AddUser)obj).getUsername());

                } else if (obj instanceof InitialFilePackageNotification){
                    observable.updateUserFiles(((InitialFilePackageNotification) obj).getUsername());
                } else {
                    System.out.println("I DON'T FEAR ANYTHING BUT THIS THING, THIS THING SCARES ME!");
                }
            }
            
        } catch (IOException ex) {
            try {
                observable.logout();
                throw new RuntimeException("Error accepting the socket");
            } catch (IOException ex1) {
                throw new RuntimeException("Error accepting the socket, couldn't end threads");
            }
        } catch(Exception e){
            System.out.println("Error: " + e);
        }
    }
}