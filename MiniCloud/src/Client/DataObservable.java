/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import Client.WatchDog.WatchDog;
import java.io.File;
import java.util.Observable;

public class DataObservable extends Observable {
    WatchDog watchdog;
    
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
