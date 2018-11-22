/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Threads;

public class TestMain {
    
    public static void main(String[] args) {
        
        try{
            Thread t = new DownloadService("Hello.txt","C:\\Users\\Skully\\Documents","localhost", 6001);
            t.start();
        }catch(FileException e){
            System.out.println(e);
        }catch(DirectoryException e){
            System.out.println(e);
        }
    }
}
