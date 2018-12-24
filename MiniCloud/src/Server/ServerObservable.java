package Server;

import BD.DBConnection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class ServerObservable extends Observable{
    
    private DBConnection DB;
    private List<ClientHandler> loggedUsers;
    
    public ServerObservable(String DB_IP,int DB_PORT) {
        try{
            DB = new DBConnection("admin","admin", DB_IP,DB_PORT);
        }catch(SQLException | ClassNotFoundException e){System.err.println(e.getCause()); System.exit(1);}
        loggedUsers = new ArrayList<>();
    }

    public void addLoggedThread(ClientHandler t){
        loggedUsers.add(t);
    }

    public DBConnection getDB() {
        return DB;
    }
    
    public List<ClientHandler> getLoggedUserThreads(){
        return loggedUsers;
    }
    
    public void sendExit(){
        for(int i = 0; i < loggedUsers.size(); i++)
            loggedUsers.get(i).exit();  
    }
    
    @Override
    public void notifyObservers(Object arg) {
        super.notifyObservers(arg); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void notifyObservers() {
        super.notifyObservers(); //To change body of generated methods, choose Tools | Templates.
    }
    

}
