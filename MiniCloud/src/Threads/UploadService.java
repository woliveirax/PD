package Threads;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * @version 1.0
 */
public class UploadService extends Thread {
    private static final int MAX_PACKET_SIZE = 10000;
    private static final int TIMEOUT = 2000;
    
    private final ServerSocket server;
    
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
    
    private void HandleRequest(Socket socket)
            throws DownloadException, FileNotFoundException, IOException
    {
        int nbytes;
        byte[] chunk = new byte[MAX_PACKET_SIZE];
        BufferedReader in;
        OutputStream out;
        String requestedFileName,requestedCanonicalFilePath;
        FileInputStream requestedFileInputStream;
        
        try{
            socket.setSoTimeout(TIMEOUT);
            
            in = new BufferedReader(new InputStreamReader(socket
                    .getInputStream()));
            out = socket.getOutputStream();

            requestedFileName = in.readLine();

            System.out.println("Received request for the file: " + requestedFileName);

            requestedCanonicalFilePath = new File(directory+File.separator
                    +requestedFileName).getCanonicalPath();
            
            if(!requestedCanonicalFilePath.startsWith(directory.
                    getCanonicalPath()+File.separator)){
                System.out.println("Restricted file access " 
                        + requestedCanonicalFilePath + "!");
                System.out.println("The base directory does not correspond to" 
                        + directory.getCanonicalPath()+"!");
                return;
            }
            
            requestedFileInputStream = new FileInputStream(
                    requestedCanonicalFilePath);
            System.out.println("File " + requestedCanonicalFilePath 
                    + " opened for reading.");

            while((nbytes = requestedFileInputStream.read(chunk))>0){                        
                out.write(chunk, 0, nbytes);
                out.flush();
            }     

            System.out.println("Client with file to transfer - upload finished");
           
        }catch(FileNotFoundException e){   //Subclasse de IOException                 
            throw new FileNotFoundException("The exceptio {" + e + 
                    "} ocorred while trying to open the file requested");                   
                              
        }catch(SocketTimeoutException e){
            throw new DownloadException("Connection timed out!" +
                    " file transfer might be incomplete" + e);
        }catch(IOException e){
            throw new DownloadException("Error accessing socket or local file: "
                    + e);
        }
        try{
            requestedFileInputStream.close();
        }catch(IOException e){
            throw new IOException("Error while closing the file.");
        }
        try{
            socket.close();
        } catch (IOException e) {
            throw new IOException("Error while closing the socket.");
        }
    }
    
    @Override
    public void run() {
        Socket clientSocket = null;
        try{
            //TODO: add 1 to file upload counter
            while(true){
                clientSocket = server.accept();
                HandleRequest(clientSocket);
            }
            
            //TODO: ask server to write to keep log
        }catch(DownloadException e){
            System.out.println("Error while trying to download the file: " + e);
        }catch(ConnectException e){
            System.out.println("Host connection error: " + e);
        }catch(FileNotFoundException e){
            System.out.println("Could not create output file! Error: " + e);
        }catch(IOException e){
            System.out.println("Server server error! " +e);
            System.out.println("Terminating connection!");
        }/*finally{
            try{
                clientSocket.close();
            }catch(NullPointerException e){
                System.out.println("Socket is not initialized.");
            }
            catch(IOException e){
                System.out.println("Error while closing socket!" + e);
            }
        }*/
    }
}
