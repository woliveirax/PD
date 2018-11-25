package Threads;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * @version 1.0
 */
public class UploadService extends Thread {
    private static final int MAX_PACKET_SIZE = 10000;
    private static final int TIMEOUT = 2000;
    
    private ServerSocket server;
    
    File directory;
    int port;
    
    //TODO: must receive the handler for the Upload file counter
    public UploadService(String localDirectory,int port)
        throws DirectoryException, IOException
    {
        server = new ServerSocket(port, 5);
        directory = new File(localDirectory.trim());       
        
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
    }
    
    private void HandleRequest(Socket socket){
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
            
        }catch(SocketTimeoutException e){
            throw new DownloadException("Connection timed out!" +
                    " file transfer might be incomplete" + e);
        }catch(IOException e){
            throw new DownloadException("Error accessing socket or local file: "
                    + e);
        }finally{
            try{
                fileToWrite.close();
            }catch(IOException e){
                System.out.println("error closing file!" + e);
            }
        }
    }
    
    @Override
    public void run() {
        try{
            //TODO: add 1 to file upload counter
            while(true){
                Socket clientSocket = server.accept();
                HandleRequest(clientSocket);
            }
            
            //TODO: ask server to write to keep log
        }catch(IOException e){
            System.out.println("Server server error! " +e);
            System.out.println("Terminating connection!");
        } 
    }
}
