package Client.Threads;

import Client.DataObservable;
import Client.GUI.CloudLogin;
import Client.WatchDog.WatchDogException;
import Exceptions.DirectoryException;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
    
    public static void main(String[] args) throws Exception {
        
//        try {
            //CloudLogin interfaceStartUp = new CloudLogin();
            //interfaceStartUp.setVisible(true);
            DataObservable x = new DataObservable();
            x.startServerConnection("project-soralis.pro", 6001);
            
            try{
                x.login("wallace", "abcd");
            }catch(Exception e){
                System.out.println(e);
            }
            x.setUploadPath(new File("C:\\Users\\Skully\\Downloads\\Upload"));
            
//            Scanner scan = new Scanner(System.in);
//            while(true){ 
//                System.out.print("> "); 
//                String msg = scan.nextLine();
//                
//                if(msg.equalsIgnoreCase("exit")){ 
//                    break; 
//                }
//                
//                if(msg.equalsIgnoreCase("login wallace")){
//                    x.login("wallace", "abcd");
//                }
//                
//                if(msg.equalsIgnoreCase("login joana")){
//                    x.login("joana", "1234");
//                }
//                
//                x.sendChatMessage(msg); 
//            }
//        } catch (WatchDogException ex) {
//            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (DirectoryException ex) {
//            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
}
