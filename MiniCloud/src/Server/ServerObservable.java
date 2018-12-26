package Server;

import BD.ConnectedUser;
import BD.DBConnection;
import Exceptions.UserException;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class ServerObservable extends Observable{
    
    private DBConnection DB;
    private ServerKeepAlive keepAlive;
    private List<ClientHandler> loggedUsers;
    
    public ServerObservable(String DB_IP,int DB_PORT) {
        try{
            DB = new DBConnection("admin","admin", DB_IP,DB_PORT);
            keepAlive = new ServerKeepAlive(this);
            keepAlive.start();
        }catch(ClassNotFoundException | SocketException e){System.err.println(e.getCause()); System.exit(1);}
         catch (SQLException e){
             System.out.println(e);
            DB.shutdown();
            System.exit(1);
        }
        
        loggedUsers = new ArrayList<>();
    }
    
    public void addLoggedThread(ClientHandler t){
        loggedUsers.add(t);
    }

    public DBConnection getDB() {
        return DB;
    }
    
    public boolean disconnectUser(String username){
        for(ClientHandler handler : loggedUsers){
            if(handler.getUsername().equals(username)){
                try{
                    handler.exit();
                    DB.userLogout(username);
                    return true;
                } catch (SQLException | UserException e){
                    System.out.println(e);
                }
            }
        }
        return false;
    }
    
    public List<ClientHandler> getLoggedUserThreads(){
        return loggedUsers;
    }
    
    public void sendUdpPacketToClient(ConnectedUser user, Object obj){
        keepAlive.sendUdpPacketToClient(user, obj);
    }
    
    public void broadcastUdpPacketToClients(Object obj){
        keepAlive.broadcastToUdpClients(obj);
    }
    
    public void sendExit(){
        for(int i = 0; i < loggedUsers.size(); i++)
            loggedUsers.get(i).exit();  
    }
}
