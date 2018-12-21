package Server;

import comm.AuthPackets.LoginAccepted;
import comm.LoginInfo;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class ClientHandler extends Thread{
    private ObjectInputStream in; 
    private ObjectOutputStream out; 
    private final Socket s; 

    public ClientHandler(Socket s)
    { 
        this.s = s;
    } 
    
    @Override
    public void run()  
    { 
        Object received; 
        Object toreturn; 
        
        try{//initialize stream reader and writer
            out = new ObjectOutputStream(s.getOutputStream());
            in = new ObjectInputStream(s.getInputStream());
        }catch(IOException e){
            e.printStackTrace();
            
            try{
                s.close();
            }catch(IOException ex){
                ex.printStackTrace();
            }
            
            return;
        }
           
        while (true)  
        { 
            try {
                // receive the answer from client 
                received = in.readObject();
                
                if(received instanceof String){
                    System.out.println((String)received);
                    
                    if(((String)received).equalsIgnoreCase("exit")) 
                    {  
                        System.out.println("Client " + s + " sends exit..."); 
                        s.close(); 
                        System.out.println("Connection closed"); 
                        break; 
                    }
                }else if(received instanceof LoginInfo){//TODO: compare to all object types
                    //TODO: check in DATABASE IF USER HAS VALID CREDENCIALS
                    out.writeObject(new LoginAccepted());
                    out.flush();
                    System.out.println("Login accepted");
                }
            } catch (IOException e) { 
                System.out.println("IO"+e);
                break;
            }catch (ClassNotFoundException e){
                System.out.println("CNF"+e);
                break;
            }
        }
    }
    
    
}
