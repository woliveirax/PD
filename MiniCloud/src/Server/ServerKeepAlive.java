/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import BD.ConnectedUser;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 *
 * @author Skully
 */
public class ServerKeepAlive extends Thread {
    
    private DatagramSocket socket;
    
    private boolean CONTINUE;
    
    public ServerKeepAlive() throws SocketException{
        socket = new DatagramSocket(0);
    }
    
    public void exit() {
        CONTINUE = false;
        socket.close();
    }
    
    private ByteArrayOutputStream serializeObject(Object obj) throws IOException{
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bStream);

        out.writeObject(obj);
        out.flush();
        out.close();
        
        return bStream;
    }
    
    
    private void sendUDPPacket(ConnectedUser receiver,Object msg){
        DatagramPacket packet;
        
        try {
            ByteArrayOutputStream bStream = serializeObject(msg);
            
            packet = new DatagramPacket(bStream.toByteArray(), bStream.size(), InetAddress.getByName(receiver.getIp()), receiver.getKeepAlivePort());
            //dSckt.send(dPkt);
            
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
