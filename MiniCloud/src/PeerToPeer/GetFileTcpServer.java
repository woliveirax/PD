import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 *
 * @author Jose' Marinho
 */
public class GetFileTcpServer {

    public static final int MAX_SIZE = 4000;
    public static final int TIMEOUT = 5; //segundos
   
    protected ServerSocket serverSocket;
    protected File localDirectory;

    public GetFileTcpServer(int listeningPort, File localDirectory) throws SocketException, IOException 
    {
        serverSocket = null;
        serverSocket = new ServerSocket(listeningPort);
        this.localDirectory = localDirectory;
    }
    
    public void processRequests()
    {        
        BufferedReader in;
        OutputStream out;
        Socket socketToClient;        
        byte []fileChunk = new byte[MAX_SIZE];
        int nbytes;
        String requestedFileName, requestedCanonicalFilePath = null;
        FileInputStream requestedFileInputStream = null;
        
        if(serverSocket == null){
            return;
        }

        System.out.println("Servidor de carregamento de ficheiros iniciado...");
        
        try{
            
            while(true){
                
                try{
                        
                    socketToClient = serverSocket.accept();
                
                }catch(IOException e){
                    System.out.println("Ocorreu uma excepcao no socket enquanto aguardava por um pedido de ligacao: \n\t" + e);
                    System.out.println("O servidor vai terminar...");
                    return;
                }
                
                try{
                    
                    socketToClient.setSoTimeout(1000*TIMEOUT);
                    
                    in = new BufferedReader(new InputStreamReader(socketToClient.getInputStream()));
                    out = socketToClient.getOutputStream();
                    
                    requestedFileName = in.readLine();
                     
                    System.out.println("Recebido pedido para: " + requestedFileName);

                    requestedCanonicalFilePath = new File(localDirectory+File.separator+requestedFileName).getCanonicalPath();

                    if(!requestedCanonicalFilePath.startsWith(localDirectory.getCanonicalPath()+File.separator)){
                        System.out.println("Nao e' permitido aceder ao ficheiro " + requestedCanonicalFilePath + "!");
                        System.out.println("A directoria de base nao corresponde a " + localDirectory.getCanonicalPath()+"!");
                        continue;
                    }
                    
                    requestedFileInputStream = new FileInputStream(requestedCanonicalFilePath);
                    System.out.println("Ficheiro " + requestedCanonicalFilePath + " aberto para leitura.");
                    
                    while((nbytes = requestedFileInputStream.read(fileChunk))>0){                        
                        
                        out.write(fileChunk, 0, nbytes);
                        out.flush();
                                                
                    }     
                    
                    System.out.println("Transferencia concluida");
                    
                }catch(FileNotFoundException e){   //Subclasse de IOException                 
                    System.out.println("Ocorreu a excepcao {" + e + "} ao tentar abrir o ficheiro " + requestedCanonicalFilePath + "!");                   
                }catch(IOException e){
                    System.out.println("Ocorreu a excepcao de E/S: \n\t" + e);                       
                }
                
                if(requestedFileInputStream != null){
                    try {
                        requestedFileInputStream.close();
                    } catch (IOException ex) {}
                }

                try{
                     socketToClient.close();
                 } catch (IOException e) {}
                
             } //while(true)
           
        }finally{
            try {
                serverSocket.close();
            } catch (IOException e) {}
        }
    }
    
    public static void main(String[] args){
        
        int listeningPort = 6001; //JOANA: Coloquei porto, em vez de receber por argumento
        File localDirectory;

//JOANA: Comentei para n s receber info por arg 

//        if(args.length != 2){ 
//            System.out.println("Sintaxe: java GetFileTcpServer listeningPort localRootDirectory");
//            return;
//        }        
        
//        localDirectory = new File(args[1]);
          localDirectory = new File("C:\\Users\\Skully\\Desktop"); //Joana: Coloquei nome diretamente, dpois vamos ter de alterar
          
        if(!localDirectory.exists()){
           System.out.println("A directoria " + localDirectory + " nao existe!");
           return;
       }

       if(!localDirectory.isDirectory()){
           System.out.println("O caminho " + localDirectory + " nao se refere a uma directoria!");
           return;
       }

       if(!localDirectory.canRead()){
           System.out.println("Sem permissoes de leitura na directoria " + localDirectory + "!");
           return;
       }
        
       try {
           
//            listeningPort = Integer.parseInt(args[0]);
            if(listeningPort <= 0) throw new NumberFormatException("Porto TCP de escuta indicado <= 0 (" + listeningPort + ")");
                        
            new GetFileTcpServer(listeningPort, localDirectory).processRequests();            
        
        }catch(NumberFormatException e){
            System.out.println("O porto de escuta deve ser um inteiro positivo.");
        }catch(SocketException e){
            System.out.println("Ocorreu uma excepcao ao nivel do socket TCP:\n\t"+e);
        }catch(IOException e){
            System.out.println("Ocorreu uma excepcao ao nivel do socket TCP:\n\t"+e);
        }
       
    }
}
