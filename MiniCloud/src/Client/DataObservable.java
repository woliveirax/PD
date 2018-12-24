package Client;

import Client.Threads.CommunicationThread;
import Client.WatchDog.WatchDog;
import Client.WatchDog.WatchDogException;
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
    
    UserData userdata;
    
    public DataObservable(String ipAddress, int port) 
            throws WatchDogException, IOException
    {
        try{
            userdata = new UserData();
            watchdog = new WatchDog(this);
            comm = new CommunicationThread(port, ipAddress, this);
            comm.start();
            
        }catch(WatchDogException e){
            throw e;
        }
    }
    
    public void startWatchDogService(){
        watchdog.start();
    }
    
    public void login(String username, String password) throws IOException{
        comm.login(username,password);
    }
    
    //UserData funcs
    public void removeFileFromUser(String user, String filename){
        userdata.removeFileFromUser(user, filename);
    }
    
    public void addFileFromUser(String user, FileData file){
        userdata.addFileToUser(user, file);
    }
    
    public void removeUserFromFileList(String username){
        userdata.RemoveUser(username);
    }
    
    public void addUserToFileList(CloudData data){
        userdata.addNewUser(data);
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
        comm.sendChatMessage("Testing this!");
    }
}
