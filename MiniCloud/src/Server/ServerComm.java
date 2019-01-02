package Server;

import Exceptions.UserException;
import RMI.MonitorInterface;
import RMI.MonitorService;
import comm.CloudData;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;

public class ServerComm extends Thread{
    private final ServerObservable serverObs;
    private ServerSocket server = null;
    private Socket nextClient = null;
    private boolean CONTINUE;
    private MonitorService service;
    private static String serviceName = "rmi://localhost/" + MonitorInterface.SERVICE_NAME;
    
    public ServerComm(String DB_IP, int DB_Port) {
        serverObs = new ServerObservable(DB_IP,DB_Port);
        CONTINUE = true;
        
        launchRegistry();
        startService();
    }
    
    public void launchRegistry() {
        try {
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        } catch (RemoteException ex) {
            System.out.println("Registry já esta lançado, ou nao" + ex);
        }
    }
    
    public void startService(){
        try {
            service = new MonitorService(serverObs);
            Naming.rebind(serviceName, service);
            
        } catch (RemoteException | MalformedURLException ex) {
            System.out.println("Error: " + ex);
        }
    }
    
    public void shutdown(){
        CONTINUE = false;
        serverObs.shutdownServer();
        
        try{
            server.close();
        }catch(IOException e){
            System.out.println("could not close the socket!");
        }
        
        try {
            UnicastRemoteObject.unexportObject(service, true);
        } catch (NoSuchObjectException ex) {
        }
        
        try {
            Naming.unbind(serviceName);
        } catch (RemoteException | NotBoundException | MalformedURLException ex) {
        }
    }
    
    public ArrayList<CloudData> getUsersData(){
        try{
            return serverObs.getAllUsersData();
        }catch (UserException | SQLException e){
            System.out.println("Erro: " + e);
        }
        return new ArrayList<>();
    }
        
    @Override
    public void run(){
        ObjectInputStream in= null;
        ObjectOutputStream out = null;
         
        try {
            // BIND TO THE SERVICE PORT
            server = new ServerSocket(0,20);
                    
//            server.setSoTimeout(TIMEOUT);
            System.out.println ("Server started, ip: + " + 
                    InetAddress.getLocalHost().getHostAddress() + " port: "
                    + server.getLocalPort());
            
            // LOOP INDEFINITELY, ACCEPTING CLIENTS
            while(CONTINUE){
                // GET THE NEXT TCP CLIENT
                nextClient = server.accept();
                System.out.println ("Received request from " + nextClient.getInetAddress() + ":" + nextClient.getPort() );
               
                // create a new thread object 
                ClientHandler t = new ClientHandler(nextClient, serverObs); 
                // Invoking the start() method 
                t.start();
            }

            
        }catch(IOException e){
            try{
                if(nextClient != null)
                    nextClient.close();
            }catch(IOException ex){
                System.out.println(ex);
            }
            System.out.println(e);
        }
    }
}
