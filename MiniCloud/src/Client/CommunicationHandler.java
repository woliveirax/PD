package Client;

import comm.AuthPackets.LoginAccepted;
import comm.AuthPackets.Logout;
import comm.ClientConnection;
import comm.CloudData;
import comm.FileData;
import comm.Packets.InitialFilePackage;
import comm.LoginInfo;
import comm.Packets.AddFileRequest;
import comm.Packets.DataMass;
import comm.Packets.GetUserData;
import comm.AuthPackets.RegisterUser;
import comm.AuthPackets.Success;
import comm.Packets.RemoveFileRequest;
import comm.Packets.TransferHistoryPackage;
import comm.Packets.TransferInfo;
import comm.Packets.UpdateFileRequest;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class CommunicationHandler {
    private final Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    
    private final DataObservable observable;
    
    public CommunicationHandler(int port, String srvAddr, DataObservable obs) 
            throws UnknownHostException, IOException
    {
        this.observable = obs;
        socket = new Socket(srvAddr,port);
        
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());

    }
    
    public boolean login(String user, String password) throws IOException, Exception{
            sendMsg(new LoginInfo(user, password,new ClientConnection(
                    observable.getKeepAlivePort(),
                    observable.getTransferPort(),
                    observable.getNotificationPort())));
            
            Object answer = receiveMsg();
            if(answer instanceof LoginAccepted){
                return true;
            } else {
                throw ((Exception)answer);
            }
    }
    
    public void logout() throws IOException{
        sendMsg(new Logout());
    }
    
    //Requests
    public void sendChatMessage(String message) throws IOException{
        sendMsg(message);
    }
    
    public void addFileRequest(FileData file) throws IOException{
        sendMsg(new AddFileRequest(file));
    }
    
    public void removeFileRequest(String filename) throws IOException{
        sendMsg(new RemoveFileRequest(filename));
    }
    
    public void updateFileRequest(FileData file) throws IOException{
        sendMsg(new UpdateFileRequest(file));
    }
    
    public void addFileTransfer(TransferInfo info) throws IOException{
        sendMsg(info);
    }
    
    public void sendInitialFilePackage() throws IOException{
        InitialFilePackage files = new InitialFilePackage(observable.getUploadPath().toString());
        if(files.getFiles().size() > 0)
            sendMsg(files);
    }
    
    public ArrayList<TransferInfo> getTransferHistory(String username) throws IOException, Exception{
    
        sendMsg(new TransferHistoryPackage(new ArrayList<>()));

        Object obj = receiveMsg();
        if(obj instanceof TransferHistoryPackage){
            return ((TransferHistoryPackage)obj).getHistory();
        } else {
            throw ((Exception) obj);
        }
    }
    
    public CloudData getUserData(String username) throws IOException, Exception{
        sendMsg(new GetUserData(username));
        System.out.println("wating for user data");
        Object obj = receiveMsg();
        if(obj instanceof CloudData){
            return (CloudData)obj;
        } else {
           throw ((Exception) obj);
        }
    }
    
    public void getAllDataFromServer() throws IOException, Exception{
        sendMsg(new DataMass(null));
        
        Object obj = receiveMsg();
        if(obj instanceof DataMass){
            observable.setFileList(((DataMass)obj).getData());
        } else {
            throw ((Exception) obj);
        }
    }
    
    public boolean registerNewUser(String username, String password) throws IOException, Exception{
        sendMsg(new RegisterUser(username, password));
        
        Object obj = receiveMsg();
        if(obj instanceof Success){
            return true;
        } else {
            throw ((Exception)obj);
        }
    }
    
    private synchronized void sendMsg(Object msg) throws IOException{
        try{
            out.writeObject(msg);
            out.flush();
        }catch(IOException e){
            throw e;
        }        
    }
    
    private synchronized Object receiveMsg() 
            throws IOException
    {
        try{
            return in.readObject();
            
        }catch(IOException e){
            throw e;
        } catch (ClassNotFoundException ex) {
            throw new IOException("Class not found!");
        }
    }
    
    public void closeLink(){
        try{
            socket.close();
        }catch(IOException e){
            System.out.println("could not close the socket!");
        }
    }
}
