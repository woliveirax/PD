/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.Threads;

import Exceptions.DirectoryException;
import Exceptions.DownloadException;
import Exceptions.FileException;
import Client.DataObservable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;


public class DownloadService extends Thread {
    private static final int MAX_PACKET_SIZE = 10000;
    private static final int TIMEOUT = 2000;
    
    private Socket socket;
    private DataObservable observable;
    
    private FileOutputStream fileToWrite;
    private final File directory;
    private final File file;
    
    private final String address;
    private final int port;

    /**
     * Download a file from a selected source
     * 
     * @param obs observable object
     * @param filename name of the file to download
     * @param localDirectory directory where the file will be placed
     * @param address address of the download source
     * @param port port of the source
     * @throws FileException if the file already exists the system will throw this
     * @throws DirectoryException  if the directory already exists the system will throw this
     */
    
    public DownloadService(DataObservable obs, String filename, String address, int port)
        throws FileException, DirectoryException
    {
        try{
            this.address = address.trim();
            this.port = port;

            this.directory = observable.getDownloadPath();
            this.file = new File(observable.getDownloadPath() + File.separator + filename.trim());

            if(!directory.exists()){
                throw new DirectoryException("Directory \'" + directory.getPath() 
                        + "\'" + "does not exist!");
            }

            if(!directory.isDirectory()){
                throw new DirectoryException("Path: \'" + directory.getPath() +
                        "\' not a valid directory!");
            }

            if(!directory.canWrite()){
                throw new DirectoryException("Can not write to: \'" + 
                        directory.getPath() + "\' check permissions!");
            }

            if(file.exists()){
                throw new FileException("File: \'" + file.getName()
                        + " already exists!");
            }
        } catch (DirectoryException | FileException e){
            throw e;
        }
    }
    
    private void connectToPeer() throws ConnectException{
        try{
            socket = new Socket(address, port);
            socket.setSoTimeout(TIMEOUT);
            
        }catch(SocketTimeoutException e){
            file.delete();
            throw new ConnectException("Connection timeout: " + e);
        }catch(SocketException e){
            file.delete();
            throw new ConnectException("Socket error: " + e);
        }catch(UnknownHostException e){
            file.delete();
            throw new ConnectException("Host unknown: " + e);
        }catch(IOException e){
            file.delete();
            throw new ConnectException("Could not connect to host:" + e);
        }
    }
    
    
    private void downloadFile()
            throws DownloadException
    {
        try{
            int nbytes = 0;
            byte[] chunk = new byte[MAX_PACKET_SIZE];
            
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            
            InputStream in = socket.getInputStream();
            out.println(file.getName());
            out.flush();
            
            while((nbytes = in.read(chunk)) > 0){
                fileToWrite.write(chunk, 0, nbytes);
            }
            System.out.println("Client with Request: - Download completed");
        }catch(SocketTimeoutException e){
            if(file.exists())
                file.delete();
            throw new DownloadException("Connection timed out!" +
                    " file transfer might be incomplete" + e);
        }catch(IOException e){
            if(file.exists())
                file.delete();
            throw new DownloadException("Error accessing socket or local file: "
                    + e);
        }finally{
            try{
                fileToWrite.close();
            }catch(IOException e){
                if(file.exists())
                    file.delete();
                System.out.println("error closing file!" + e);
            }
        }
    }
    
    @Override
    public void run() {
        try{
            fileToWrite = new FileOutputStream(file);
            connectToPeer();
            downloadFile();
            
            //TODO:
            //observable.addFileTransfer(info);
            
        }catch(FileNotFoundException e){
            System.out.println("Could not create output file! Error: " + e);
            file.delete();
        }catch(ConnectException e){
            System.out.println("Host connection error: " + e);
            file.delete();
        }catch(DownloadException e){
            System.out.println("Error while trying to download the file: " + e);
            file.delete();
        }finally{
            try{
                socket.close();
            }catch(NullPointerException e){
                System.out.println("Socket is not initialized.");
            }catch(IOException e){
                System.out.println("error closing socket!" + e);
            }
        }
    }
}