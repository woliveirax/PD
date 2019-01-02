package RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
    void addListener(MonitorInterface service) throws RemoteException;
    void removeListener(MonitorInterface service) throws RemoteException;
}
