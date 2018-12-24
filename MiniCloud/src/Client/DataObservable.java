package Client;

import Client.Threads.CommunicationThread;
import Client.Threads.KeepAliveThread;
import Client.Threads.NotificationThread;
import Client.Threads.UploadService;
import Client.WatchDog.WatchDog;
import Client.WatchDog.WatchDogException;
import Exceptions.DirectoryException;
import Exceptions.InvalidDirectoryException;
import comm.CloudData;
import comm.FileData;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;

public class DataObservable extends Observable {
    WatchDog watchdog;
    CommunicationThread comm;
    KeepAliveThread keepAlive;
    UploadService uploadService;
    NotificationThread notificationService;
    
    UserData userdata;
    String chat;
    String notification;
    
    
    public DataObservable(String ipAddress, int port) 
            throws WatchDogException, IOException, DirectoryException
    {
        try{
            userdata = new UserData();
            chat = null;
            notification = null;
            
            watchdog = new WatchDog(this);
            uploadService = new UploadService(this);
            notificationService = new NotificationThread(this);
                    
            comm = new CommunicationThread(port, ipAddress, this);
            keepAlive = new KeepAliveThread(this);
            
            comm.start();
            keepAlive.start();
            
        }catch (DirectoryException | WatchDogException ex) {
            throw ex;
        }
    }
    
    public void startServices(){
        watchdog.start();
        uploadService.start();
    }
    
    public int getKeepAlivePort(){
        return keepAlive.getPort();
    }
    
    public int getTransferPort(){
        return uploadService.getPort();
    }
    
    public int getNotificationPort(){
        return notificationService.getPort();
    }
    
    public void login(String username, String password) throws IOException{
        comm.login(username,password);
    }
    
    public void logout() throws IOException{
        comm.logout();
    }
    
    //UserData funcs
    public void removeFileFromUser(String user, String filename){
        userdata.removeFileFromUser(user, filename);
        
        setChanged();
        notifyObservers();
    }
    
    public void addFileFromUser(String user, FileData file){
        userdata.addFileToUser(user, file);
    }
    
    public void removeUserFromFileList(String username){
        userdata.RemoveUser(username);
        
        setChanged();
        notifyObservers();
    }
    
    public void addUserToFileList(CloudData data){
        userdata.addNewUser(data);
        
        setChanged();
        notifyObservers();
    }
    
    public ArrayList<CloudData> getUsers(){
        return userdata.getFileList();
    }
    
    public void setUploadPath(File file) throws InvalidDirectoryException{
        userdata.setUploadPath(file);
    }
    
    public void setDownloadPath(File file) throws InvalidDirectoryException{
        userdata.setDownloadPath(file);
    }
    
    public File getDownloadPath(){
        return userdata.getDownloadPath();
    }
    
    public File getUploadPath(){
        return userdata.getUploadPath();
    }
    //TODO: Create comm thread with methods to send data such as add files, remove, update.
    
    //TODO: method to send all info from files to server at once.
    
    //TODO: add this to server
    //File Handling
    public void addFileRequest(File file){
        //TODO: Send addFileRequest
    }
    
    public void removeFileRequest(File file){
        //TODO: Send removeFileRequest
    }
    
    public void updateFileRequest(File file){
        //TODO: Send UpdateFileInfoRequest
    }
    
    public void sendChatMessage(String msg) throws IOException{
        comm.sendChatMessage(msg);
    }
    
    public synchronized void receiveChatMessage(String msg){
        chat = msg;
        
        setChanged();
        notifyObservers();
    }
    
    public synchronized void receiveNotification(String notification){
        this.notification = notification;
        
        setChanged();
        notifyObservers();
    }
    
    //TODO: gere historicos
}
