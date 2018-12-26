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
    
    
    public DataObservable() 
            throws WatchDogException, IOException, DirectoryException
    {
        try{
           initData();
            
        }catch (WatchDogException ex) {
            throw ex;
        }
    }
    
    //Connection and processes handlers
    public void startServerConnection(String ipAddress, int port) throws IOException{
        comm = new CommunicationHandler(port, ipAddress, this);
    }
    
    public void startServices(){
        watchdog.start();
        uploadService.start();
    }
    
    //Get connection settings
    public int getKeepAlivePort(){
        return keepAlive.getPort();
    }
    
    public int getTransferPort(){
        return uploadService.getPort();
    }
    
    public int getNotificationPort(){
        return notificationService.getPort();
    }
    
    //Login/logout
    public boolean login(String username, String password) throws IOException, Exception{
        return comm.login(username,password);
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
        
        for(CloudData x : userdata.getFileList()){
            System.out.println("User: " + x.getUser());
            for(FileData f : x.getFiles()){
                System.out.println("\t- " + f.toString());
            }
        }
        
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
        
        try{
            comm.sendInitialFilePackage();
        }catch(IOException e){
            System.out.println("erro ao enviar inital file package: " + e);
        }
        
        startServices();
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
    
    //Requests
    public void addFileRequest(File file) throws IOException{
        FileData myFile = new FileData(file.getName(), file.length());
        comm.addFileRequest(myFile);
    }
    
    public void removeFileRequest(File file) throws IOException{
        FileData myFile = new FileData(file.getName(), file.length());
        comm.removeFileRequest(myFile.getName());
    }
    
    public void updateFileRequest(File file) throws IOException{
        FileData myFile = new FileData(file.getName(), file.length());
        comm.updateFileRequest(myFile);
    }
    
    public void sendChatMessage(String msg) throws IOException{
        comm.sendChatMessage(msg);
    }
    
    public void receiveChatMessage(String msg){
        
        setChanged();
        notifyObservers(msg);
    }
    
    public void receiveNotification(String notification){
        
        setChanged();
        notifyObservers(new TransferNotification(notification));
    }
    
    public void setFileList(ArrayList<CloudData> list){
        userdata.setFileList(list);
        
        setChanged();
        notifyObservers(FILE_UPDATE);
    }
    
    public void addFileTransfer(TransferInfo info) throws IOException{
        comm.addFileTransfer(info);
    }
    
    public  ArrayList<TransferInfo> getTransferHistory(String username) throws Exception{
        return comm.getTransferHistory(username);
    }
    
    public void updateUserFiles(String username) throws Exception{
        userdata.updateUserFiles(username, comm.getUserData(username).getFiles());
        
        setChanged();
        notifyObservers(FILE_UPDATE);
    }
    
    public boolean registerUser(String username, String password) throws Exception{
        return comm.registerNewUser(username, password);
    }
    
    private void initData() throws WatchDogException, DirectoryException, IOException {
        userdata = new UserData();
        
        watchdog = new WatchDog(this);
        uploadService = new UploadService(this);
        notificationService = new NotificationThread(this);
        notificationService.start();

        keepAlive = new KeepAliveThread(this);
        keepAlive.start();
    }
    
    public void shutdownClient(){
        watchdog.exit();
        uploadService.exit();
        notificationService.exit();
        keepAlive.exit();
        comm.closeLink();
    }
}
