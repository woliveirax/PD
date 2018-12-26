/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import BD.ConnectedUser;
import Exceptions.UserException;
import comm.Packets.KeepAlive;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Skully
 */
public class ServerKeepAlive extends Thread {
    private final int TIMEOUT = 1500;
    private final int SLEEP_TIME = 5000;
    
    private DatagramSocket socket;
    private ServerObservable observable;
    
    private boolean CONTINUE;
    
    public ServerKeepAlive(ServerObservable obs) throws SocketException {
        socket = new DatagramSocket(0);
        socket.setSoTimeout(TIMEOUT);
        observable = obs;
        CONTINUE = true;
    }
    
    public void exit() {
        CONTINUE = false;
        socket.close();
        this.interrupt();
    }
    
    private ByteArrayOutputStream serializeObject(Object obj) throws IOException{
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bStream);

        out.writeObject(obj);
        out.flush();
        out.close();
        
        return bStream;
    }
    
    private void addStrikes(String username) {
        try{
            int strikes = observable.getDB().getStrikes(username) + 1;
            
            if(strikes > 2){
                if(!observable.disconnectUser(username))
                    observable.getDB().userLogout(username);
                
            } else {
                observable.getDB().setStrikes(username, strikes);
                
            }
            
        } catch(SQLException | UserException e){
            System.out.println(e);
        } //Nothing we can do
    }
    
    public void sendUdpPacketToClient(ConnectedUser receiver,Object msg){
        DatagramPacket packet;
        
        try {
            ByteArrayOutputStream bStream = serializeObject(msg);
            packet = new DatagramPacket(bStream.toByteArray(), bStream.size(), InetAddress.getByName(receiver.getIp()), receiver.getKeepAlivePort());
            socket.send(packet);
            
            socket.receive(packet);
            
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
            addStrikes(receiver.getUsername());
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void broadcastToUdpClients(Object obj){
        ArrayList<ConnectedUser> users;
        DatagramPacket packet;
        
        try {
            ByteArrayOutputStream content = serializeObject(obj);
            users = getUPDUsers();
            
            for(ConnectedUser user : users){
                packet = new DatagramPacket(content.toByteArray(),content.size(),InetAddress.getByName(user.getIp()), user.getKeepAlivePort());
                socket.send(packet);

                try{
                    socket.receive(packet);
                } catch (SocketTimeoutException e){
                    addStrikes(user.getUsername());
                }

                observable.getDB().setStrikes(user.getUsername(), 0);
            }
            
        } catch(IOException | SQLException | UserException e){
        }
    }
    
    public  ArrayList<ConnectedUser> getUPDUsers(){
        ArrayList<ConnectedUser> users = new ArrayList<>();
        
        try {
            users = observable.getDB().getAuthenticatedUsers();
            Iterator<ConnectedUser> it = users.iterator();
            
            ConnectedUser user;
            while(it.hasNext()){
                user = it.next();
                
                for(ClientHandler tcpUser : observable.getLoggedUserThreads()){
                    if (user.getUsername().equals(tcpUser.getUsername())) {
                        it.remove();
                    }
                }
            }
            
            return users;
        } catch(SQLException e){
            return users;
        }
    }
    
    @Override
    public void run() {
        try{
            ArrayList<ConnectedUser> users;
            DatagramPacket packet;
            ByteArrayOutputStream content = serializeObject(new KeepAlive());
            
            while(CONTINUE){
                users = getUPDUsers();
                
                for(ConnectedUser user : users){
                    packet = new DatagramPacket(content.toByteArray(),content.size(),InetAddress.getByName(user.getIp()), user.getKeepAlivePort());
                    socket.send(packet);
                    try{
                        socket.receive(packet);
                        observable.getDB().setStrikes(user.getUsername(), 0);
                    } catch (SocketTimeoutException e){
                        addStrikes(user.getUsername());
                    }
                }
                Thread.sleep(SLEEP_TIME);
            }
        } catch(InterruptedException | IOException | SQLException | UserException e){
        }//Nothing we can do here
    }
        
}
