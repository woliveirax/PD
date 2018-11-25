/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

public class DataTransfer {

    private int download;
    private int upload;
    
    public DataTransfer(){
        download = 0;
        upload = 0;
    }
    
    public synchronized void addDownload(){
        download++;
    }
    
    public synchronized void removeDownload(){
        download--;
    }
    
    public synchronized void addUpload(){
        upload++;
    }
    
    public synchronized void removeUpliad(){
        upload--;
    }
    
    public int getDownload() {
        return download;
    }

    public int getUpload() {
        return upload;
    }
}
