package Server;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class ServerObservable extends Observable{
    
    private List<String> loggedUsernames;
    private List<ClientHandler> loggedUsers;
    
    public ServerObservable() {
        loggedUsernames = new ArrayList<>();
        loggedUsers = new ArrayList<>();
    }

    public List<String> getLoggedClients() {
        return loggedUsernames;
    }

    public void addLoggedClient(String client) {
        loggedUsernames.add(client);
    }

    public void addLoggedThread(ClientHandler t){
        loggedUsers.add(t);
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
