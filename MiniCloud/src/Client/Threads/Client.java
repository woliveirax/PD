package Client.Threads;

import Client.DataObservable;
import java.io.File;

public class Client {
    
    public static void main(String[] args) throws Exception {
        
//        try {
            //CloudLogin interfaceStartUp = new CloudLogin();
            //interfaceStartUp.setVisible(true);
            DataObservable x = new DataObservable();
            x.startServerConnection("192.168.1.68", 6001);
            
            try{
                x.login("wallace", "abcd");
                
                x.setUploadPath(new File("C:\\Users\\Skully\\Downloads\\Upload"));
                x.setDownloadPath(new File("C:\\Users\\Skully\\Downloads"));
                
                System.out.println("-----------");
//                //System.out.println(x.getTransferHistory("joana"));
//                
//                x.DownloadFile("moira", "joana", "abc.pdf", "192.168.1.71", 54093);
            }catch(Exception e){
                System.out.println(e);
                x.logout();
            }
            
            //Thread.sleep(5000);
            
            //x.logout();
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
