package Server;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class ServerObservable extends Observable{
    private List<Thread> loggedClients;
    
    public ServerObservable() {
        loggedClients = new ArrayList<>();
        
    }

    public List<Thread> getLoggedClients() {
        return loggedClients;
    }

    public void addLoggedClient(Thread client) {
        loggedClients.add(client);
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
