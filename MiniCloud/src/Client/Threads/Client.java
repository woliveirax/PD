package Client.Threads;

import Client.DataObservable;
import Client.WatchDog.WatchDogException;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
    
    public static void main(String[] args) {
        
        try {
            //CloudLogin interfaceStartUp = new CloudLogin();
            //interfaceStartUp.setVisible(true);
            DataObservable x = new DataObservable("project-soralis.pro",6001);
            
            Scanner scan = new Scanner(System.in);
            while(true){ 
                System.out.print("> "); 
                String msg = scan.nextLine(); 

                if(msg.equalsIgnoreCase("exit")){ 
                    break; 
                }
                x.sendChatMessage(msg); 
            }
        } catch (WatchDogException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
