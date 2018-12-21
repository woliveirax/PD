package Server;

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
            in = new ObjectInputStream(s.getInputStream());
            out = new ObjectOutputStream(s.getOutputStream());
        }catch(IOException e){
            e.printStackTrace();
            return;
        }finally{
            try{
                s.close();
            }catch(IOException ex){
                ex.printStackTrace();
            }        
        }
        
        while (true)  
        { 
            try { 
                // receive the answer from client 
                received = in.readObject();
                
                if(received instanceof String){
                    System.out.println((String)received);
                    
                    if(received.equals("Exit")) 
                    {  
                        System.out.println("Client " + s + " sends exit..."); 
                        System.out.println("Closing this connection."); 
                        s.close(); 
                        System.out.println("Connection closed"); 
                        break; 
                    }
                }else if(received instanceof LoginInfo){//TODO: compare to all object types
                    //TODO: check in DATABASE IF USER HAS VALID CREDENCIALS
                    out.writeObject(new String("Login Accepted"));
                    System.out.println("Login accepted");
                }
                
            } catch (IOException e) { 
                e.printStackTrace(); 
            }catch (ClassNotFoundException e){
                e.printStackTrace();
            }finally{
                try{ 
                    // closing resources 
                    this.in.close(); 
                    this.out.close(); 

                }catch(IOException e){ 
                    e.printStackTrace(); 
                } 
            }
        }
    }
    
    
}
