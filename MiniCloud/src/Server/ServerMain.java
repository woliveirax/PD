package Server;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerMain {
    public static void main(String[] args) {
        InetAddress addrBD;
        int portBD;
        
        if(args.length != 3){
            System.err.println("The arguments weren't introduced correctly: BD's IP and BD's port.");
            return;
        }
        try{
            addrBD = InetAddress.getByName(args[1]);
            portBD = Integer.parseInt(args[2]);
            
            //TODO: iniciar thread p comunicação com a base de dados
            //TODO: criar thread p iniciar server port
        }catch(UnknownHostException e){
            System.err.println("Destino desconhecido:\n\t"+e);
        }catch(NumberFormatException e){
            System.err.println("O porto do servidor deve ser um inteiro positivo:\n\t"+e);
       }
    }
}
