/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Threads;

import java.io.IOException;

public class TestMain {
    
    public static void main(String[] args) {
        
        try{
            Thread t1 = new UploadService("D:\\Documents\\lic_inf\\3ano", 6002);
            Thread t = new DownloadService("Hello.txt","D:\\Documents\\lic_inf\\3ano\\PD","localhost", 6002);
            t1.start();
            t.start();
        }catch(IOException | FileException | DirectoryException e){
            System.out.println(e);
        }
    }
}
