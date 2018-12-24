package Server;

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

            //TODO: iniciar thread p comunicação com a base de dados
            //TODO: criar thread p iniciar server port
            server = new ServerComm(args[0],portBD);
            server.start();
            
            Scanner scan = new Scanner(System.in);
            
            while(true){
                System.out.print("> ");
                String msg = scan.nextLine();
                if(msg.equalsIgnoreCase("exit"))
                    break;
            }
            server.exit();
//        }catch(IOException e){
//            System.err.println("Host not correctly identified:\t" + e);
        }catch(NumberFormatException e){
            System.err.println("The BD port should be an unsigned int:\t"+e);
       }
        
       
    }
}
