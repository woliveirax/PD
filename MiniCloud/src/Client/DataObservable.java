package Client;

import comm.Packets.TransferInfo;
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

public class DataObservable extends Observable implements UpdateType {
    private WatchDog watchdog;
    private CommunicationHandler comm;
    private KeepAliveThread keepAlive;
    private UploadService uploadService;
    private NotificationThread notificationService;
    
    private UserData userdata;
    private String chat;
    private String notification;
    
    
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
                    
            comm = new CommunicationHandler(port, ipAddress, this);
            keepAlive = new KeepAliveThread(this);
            
            keepAlive.start();
            
        }catch (WatchDogException ex) {
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
    
    public void login(String username, String password) throws IOException, Exception{
        comm.login(username,password);
    }
    
    public void logout() throws IOException{
        comm.logout();
        
        shutdownClient();
    }
    
    //UserData funcs
    public void updateFileFromUser(String user, FileData file){
        userdata.updateFileFromUser(user, file);
        
        setChanged();
        notifyObservers(FILE_UPDATE);
    }
    
    public void removeFileFromUser(String user, String filename){
        userdata.removeFileFromUser(user, filename);
        
        setChanged();
        notifyObservers(FILE_UPDATE);
    }
    
    public void addFileFromUser(String user, FileData file){
        userdata.addFileToUser(user, file);
        
        setChanged();
        notifyObservers(FILE_UPDATE);
    }
    
    public void removeUserFromFileList(String username){
        userdata.RemoveUser(username);
        
        setChanged();
        notifyObservers(FILE_UPDATE);
    }
    
    public void addUserToFileList(String username) throws Exception{
        userdata.addNewUser(comm.getUserData(username));
        
        setChanged();
        notifyObservers(FILE_UPDATE);
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
    //TODO: method to send all info from files to server at once.
    
    //File Handling
    public void addFileRequest(File file) throws IOException{
        FileData myFile = new FileData(file.getName(), file.length());
        comm.addFileRequest(myFile);
    }
    
    public void removeFileRequest(File file) throws IOException{
        FileData myFile = new FileData(file.getName(), file.length());
        comm.removeFileRequest(chat);
    }
    
    public void updateFileRequest(File file) throws IOException{
        FileData myFile = new FileData(file.getName(), file.length());
        comm.updateFileRequest(myFile);
    }
    
    public void sendChatMessage(String msg) throws IOException{
        comm.sendChatMessage(msg);
    }
    
    public synchronized void receiveChatMessage(String msg){
        chat = msg;
        
        setChanged();
        notifyObservers(CHAT_UPDATE);
    }
    
    public synchronized void receiveNotification(String notification){
        this.notification = notification;
        
        setChanged();
        notifyObservers(NOTIFICATION_UPDATE);
    }
    
    public  ArrayList<TransferInfo> getTransferHistory(String username) throws Exception{
        return comm.getTransferHistory(username);
    }
    
    public void addFileTransfer(TransferInfo info) throws IOException{
        comm.addFileTransfer(info);
    }
    
    public void shutdownClient(){
        watchdog.exit();
        uploadService.exit();
        notificationService.exit();
        keepAlive.exit();
    }
}
