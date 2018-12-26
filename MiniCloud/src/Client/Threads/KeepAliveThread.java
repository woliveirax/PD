package Client.Threads;

import Client.DataObservable;
import comm.Packets.AddFileRequest;
import comm.Packets.AddUser;
import comm.Packets.InitialFilePackage;
import comm.Packets.InitialFilePackageNotification;
import comm.Packets.KeepAlive;
import comm.Packets.RemoveFileRequest;
import comm.Packets.RemoveUser;
import comm.Packets.UpdateFileRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class KeepAliveThread extends Thread {
    private final static int BUFFER_SIZE = 4096;
    
    private boolean CONTINUE;
    private final DatagramSocket socket;
    private final DataObservable observable;
    
    
    public KeepAliveThread(DataObservable obs) throws SocketException {
        socket = new DatagramSocket(0);
        socket.setSoTimeout(0);
        
        observable = obs;
        CONTINUE = true;
    }
    
    public int getPort(){
        return socket.getLocalPort();
    }
    
    public void exit(){
        CONTINUE = false;
        socket.close();
        this.interrupt();
    }
    
    private void handlePackets(DatagramPacket packet)
            throws IOException, ClassNotFoundException, Exception
    {    
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(packet.getData()));
        Object obj = in.readObject();
        
        System.out.println("Recebi qq coisa");
        
        if(obj instanceof String){
                    observable.sendChatMessage((String)obj);
                    
        } else if(obj instanceof KeepAlive){
            socket.send(packet);
        }
        else if(obj instanceof AddFileRequest){
            observable.addFileFromUser(((AddFileRequest)obj).getUsername(),
                                        ((AddFileRequest)obj).getFile());
            socket.send(packet);
        
        } else if (obj instanceof RemoveFileRequest) {
            observable.removeFileFromUser(((RemoveFileRequest)obj).getUsername(),
                                        ((RemoveFileRequest)obj).getFilename());
            socket.send(packet);
        
        } else if (obj instanceof UpdateFileRequest) {
            observable.updateFileFromUser(((UpdateFileRequest)obj).getUsername(),
                                        ((UpdateFileRequest)obj).getFile());
            socket.send(packet);
        
        } else if (obj instanceof RemoveUser){
            observable.removeUserFromFileList(((RemoveUser)obj).getUsername());
            socket.send(packet);
            
        } else if (obj instanceof AddUser){
            observable.addUserToFileList(((AddUser)obj).getUsername());
            socket.send(packet);
            
        } else if (obj instanceof InitialFilePackageNotification){
            observable.updateUserFiles(((InitialFilePackageNotification) obj).getUsername());
            socket.send(packet);
        }
    }
    
    @Override
    public void run() {
        byte[] buffer = new byte[BUFFER_SIZE];
        DatagramPacket packet = new DatagramPacket(buffer, BUFFER_SIZE);
        
        while(CONTINUE){
            try{
                System.out.println("ready to receive keep alive packets");
                socket.receive(packet);
                System.out.println("Recebi um pacote");
                handlePackets(packet);
                
            } catch(SocketException e){
            } catch(IOException | ClassNotFoundException e){
                System.out.println("Erro: " + e);
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }
}
