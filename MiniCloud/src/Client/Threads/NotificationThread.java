package Client.Threads;

import Client.DataObservable;
import comm.Packets.AddFileRequest;
import comm.Packets.AddUser;
import comm.Packets.InitialFilePackageNotification;
import comm.Packets.RemoveFileRequest;
import comm.Packets.RemoveUser;
import comm.Packets.ServerShutdown;
import comm.Packets.UpdateFileRequest;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NotificationThread extends Thread {
    private final DataObservable observable;
    
    private final ServerSocket server;
    private  Socket socket;
    
    private ObjectInputStream in;
    
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
    }
    
    @Override
    public void run(){
        try {
            System.out.println("Port: " + server.getLocalPort());
            socket = server.accept();
            
            in = new ObjectInputStream(socket.getInputStream());
            
            while(CONTINUE){
                Object obj = in.readObject();
                System.out.println("received a Notification Packet");
                
                if(obj instanceof String){
                    System.out.println("Chat");
                            observable.sendChatMessage((String)obj);

                } else if(obj instanceof AddFileRequest){
                    System.out.println("Add File");
                    observable.addFileFromUser(((AddFileRequest)obj).getUsername(),
                                                ((AddFileRequest)obj).getFile());
                    

                } else if (obj instanceof RemoveFileRequest) {
                    System.out.println("remove file");
                    observable.removeFileFromUser(((RemoveFileRequest)obj).getUsername(),
                                                ((RemoveFileRequest)obj).getFilename());


                } else if (obj instanceof UpdateFileRequest) {
                    System.out.println("update file");
                    observable.updateFileFromUser(((UpdateFileRequest)obj).getUsername(),
                                                ((UpdateFileRequest)obj).getFile());


                } else if (obj instanceof RemoveUser){
                    System.out.println("remove user");
                    observable.removeUserFromFileList(((RemoveUser)obj).getUsername());


                } else if (obj instanceof AddUser){
                    System.out.println("######### add userrrrr ");
                    observable.addUserToFileList(((AddUser)obj).getUsername());

                } else if (obj instanceof InitialFilePackageNotification){
                    System.out.println("add init file");
                    observable.updateUserFiles(((InitialFilePackageNotification) obj).getUsername());
                    
                } else if (obj instanceof ServerShutdown){
                    observable.logout();
                    observable.shutdownClient();
                } else {
                    System.out.println("I DON'T FEAR ANYTHING BUT THIS THING, THIS THING SCARES ME!");
                }
            }
            
        } catch (IOException ex) {
            try {
                System.out.println("Erro notification: " + ex);
                System.out.println("Server timed out");
                observable.logout();
                observable.shutdownClient();
            } catch (IOException ex1) {
                System.out.println("couldn't log out");
            }
        } catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }
}