package Server;

import comm.CloudData;
import comm.FileData;
import java.util.Scanner;

public class ServerMain {
    
    public static void main(String[] args) {
        int portBD;
        ServerComm server;
        
        if(args.length != 2){
            System.err.println("The arguments weren't introduced correctly: BD's IP and BD's port.");
            return;
        }
        try{
            portBD = Integer.parseInt(args[1]);
            
            server = new ServerComm(args[0],portBD);
            server.start();
            
            System.out.println("Bem vindo ao servidor, escreva exit para terminar!");
            Scanner scan = new Scanner(System.in);
            
            while(true){
                System.out.print("> ");
                String msg = scan.next();
                
                if(msg.equalsIgnoreCase("showdata")){
                    for(CloudData user : server.getUsersData()){
                        System.out.println("User: " + user.getUser());
                        
                        for(FileData file : user.getFiles()){
                            System.out.println("\t• " + file.toString());
                        }
                    }
                    
                } else if(msg.equalsIgnoreCase("exit")){
                    server.shutdown();
                    break;
                }
            }
            
        }catch(NumberFormatException e){
            System.err.println("The BD port should be an unsigned int:\t"+e);
        }
        
       
    }
}
