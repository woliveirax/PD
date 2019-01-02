/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI;

import Exceptions.UserException;
import Server.ServerObservable;
import comm.CloudData;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

public class MonitorService extends UnicastRemoteObject implements ServerInterface, Observer {
    ArrayList<MonitorInterface> listeners;
    ServerObservable observable;
    
    private MonitorService()throws RemoteException {
        listeners = new ArrayList<>();
    }
    
    public MonitorService(ServerObservable observable)throws RemoteException{
        listeners = new ArrayList<>();
        this.observable = observable;    
        observable.addObserver(this);
    }
    
    
    @Override
    public void addListener(MonitorInterface listener) throws RemoteException {
        try {
            listeners.add(listener);
            listener.receiveData(observable.getAllUsersData());
        } catch (UserException | SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    
    @Override
    public void removeListener(MonitorInterface listener) throws RemoteException {
        listeners.remove(listener);
    }

    @Override
    public void update(Observable o, Object arg) {
        try {
            ArrayList<CloudData> data = observable.getAllUsersData();
            Iterator<MonitorInterface> it = listeners.iterator();
            MonitorInterface monitor;
            
            while(it.hasNext()){
                monitor = it.next();
                
                try{
                    monitor.receiveData(data);
                }catch(RemoteException e){
                    it.remove();
                    System.out.println("Error: " + e);
                }
            }
        } catch (UserException | SQLException ex) {
            System.out.println("Error: " + ex);
        }
    }
    
}
