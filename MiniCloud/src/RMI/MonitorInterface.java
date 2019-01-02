/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI;

import comm.CloudData;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface MonitorInterface extends Remote {
    static final String SERVICE_NAME = "MonitorService";
    
    void receiveData(ArrayList<CloudData> data) throws RemoteException;
}
