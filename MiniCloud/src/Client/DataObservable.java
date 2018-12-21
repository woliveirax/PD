/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import Client.Threads.CommunicationThread;
import Client.WatchDog.WatchDog;
import comm.ClientConnection;
import comm.LoginInfo;
import java.io.File;
import java.io.IOException;
import java.util.Observable;

public class DataObservable extends Observable {
    WatchDog watchdog;
    CommunicationThread comm;
    
    public DataObservable(){
        
    }
    
    public void start(){
        try{
            comm = new CommunicationThread(6001, "192.168.1.71", this);
            comm.start();
            
        }catch(IOException e){
            System.out.println("Erro: " +e);
        }
    }
    
    public void sendMsg(String msg){
        comm.sendMsg(msg);
    }
    
    public void login(){
        comm.sendMsg(new LoginInfo("josefina","ccc", new ClientConnection(0, 0, 0)));
    }
    
    //TODO: Create comm thread with methods to send data such as add files, remove, update.
    
    //TODO: method to send all info from files to server at once.
    
    //TODO: add this to server
    //File Handling
    public void addFile(File file){
        //TODO: Send addFileRequest
    }
    
    public void removeFile(File file){
        //TODO: Send removeFileRequest
    }
    
    public void updateFile(File file){
        //TODO: Send UpdateFileInfoRequest
    }
}
