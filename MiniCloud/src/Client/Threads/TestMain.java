package Client.Threads;

import Client.DataObservable;
import Client.GUI.CloudLogin;
import java.util.Scanner;

public class TestMain {
    
    public static void main(String[] args) {
        
        //CloudLogin interfaceStartUp = new CloudLogin();
        //interfaceStartUp.setVisible(true);
        System.out.println("sada");
        DataObservable x = new DataObservable();
        System.out.println("ola11");
        x.start();
        Scanner scan = new Scanner(System.in);
        System.out.println("Ola");
        
        while(true){
            System.out.print("> ");
            String msg = scan.nextLine();
            
            if(msg.equalsIgnoreCase("login"))
                x.login();
            
            if(msg.equalsIgnoreCase("exit")){
                break;
            }
            
            x.sendMsg(msg);
        }
    }
}
