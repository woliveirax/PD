package PeerToPeer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 *
 * @author Jose' Marinho
 */
public class GetFileTcpClient {
    public static final int MAX_SIZE = 4000;
    public static final int TIMEOUT = 5;
    
    public static void main(String[] args) 
    {
        File localDirectory;
        String fileName, localFilePath = null;
        FileOutputStream localFileOutputStream = null;
        int serverPort;
        Socket socketToServer = null;
        PrintWriter pout;
        InputStream in;
        byte []fileChunk = new byte[MAX_SIZE];
        int nbytes;                
        int contador = 0;
        
        if(args.length != 4){
            System.out.println("Sintaxe: java GetFileTcpClient serverAddress serverTcpPort fileToGet localDirectory");
            return;
        }        
        
        fileName = args[2].trim();
        localDirectory = new File(args[3].trim());
        
        if(!localDirectory.exists()){
            System.out.println("A directoria " + localDirectory + " nao existe!");
            return;
        }
        
        if(!localDirectory.isDirectory()){
            System.out.println("O caminho " + localDirectory + " nao se refere a uma directoria!");
            return;
        }
        
        if(!localDirectory.canWrite()){
            System.out.println("Sem permissoes de escrita na directoria " + localDirectory);
            return;
        }
        
        try{
            
            try{

                localFilePath = localDirectory.getCanonicalPath()+File.separator+fileName;
                localFileOutputStream = new FileOutputStream(localFilePath);
                System.out.println("Ficheiro " + localFilePath + " criado.");

            }catch(IOException e){

                if(localFilePath == null){
                    System.out.println("Ocorreu a excepcao {" + e +"} ao obter o caminho canonico para o ficheiro local!");   
                }else{
                    System.out.println("Ocorreu a excepcao {" + e +"} ao tentar criar o ficheiro " + localFilePath + "!");
                }

                return;
            }

            try{
                
                serverPort = Integer.parseInt(args[1]);
                
                socketToServer = new Socket(args[0], serverPort);
                
                socketToServer.setSoTimeout(TIMEOUT*1000);
                
                in = socketToServer.getInputStream();
                pout = new PrintWriter(socketToServer.getOutputStream(), true);
                
                pout.println(fileName);
                pout.flush();

                while((nbytes = in.read(fileChunk)) > 0){                    
                    //System.out.println("Recebido o bloco n. " + ++contador + " com " + nbytes + " bytes.");
                    localFileOutputStream.write(fileChunk, 0, nbytes);
                    //System.out.println("Acrescentados " + nbytes + " bytes ao ficheiro " + localFilePath+ ".");                    
                }                    
                
                System.out.println("Transferencia concluida.");

            }catch(UnknownHostException e){
                 System.out.println("Destino desconhecido:\n\t"+e);
            }catch(NumberFormatException e){
                System.out.println("O porto do servidor deve ser um inteiro positivo:\n\t"+e);
            }catch(SocketTimeoutException e){
                System.out.println("Não foi recebida qualquer bloco adicional, podendo a transferencia estar incompleta:\n\t"+e);
            }catch(SocketException e){
                System.out.println("Ocorreu um erro ao nível do socket TCP:\n\t"+e);
            }catch(IOException e){
                System.out.println("Ocorreu um erro no acesso ao socket ou ao ficheiro local " + localFilePath +":\n\t"+e);
            }
            
        }finally{
            
            if(socketToServer != null){
                try {
                    socketToServer.close();
                } catch (IOException ex) {}
            }
            
            if(localFileOutputStream != null){
                try{
                    localFileOutputStream.close();
                }catch(IOException e){}
            }
            
        }               

   } //main
}
