package Server;

public class ServerMain {
    
    public static void main(String[] args) {
        int portBD;
        
        if(args.length != 2){
            System.err.println("The arguments weren't introduced correctly: BD's IP and BD's port.");
            return;
        }
        try{
            portBD = Integer.parseInt(args[1]);

            //TODO: iniciar thread p comunicação com a base de dados
            //TODO: criar thread p iniciar server port
            ServerComm server = new ServerComm(args[0],portBD);
            server.startComm();
        }catch(NumberFormatException e){
            System.err.println("The BD port should be an unsigned int:\n\t"+e);
       }
        
       
    }
}
