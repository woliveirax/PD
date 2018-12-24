package Client.Threads;

import Client.DataObservable;
import comm.KeepAlive;
import comm.RefreshData;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class KeepAliveThread extends Thread {
    private final static int BUFFER_SIZE = 4096;
    private final static int MIN_BUFFER_SIZE = 10;
    
    private boolean CONTINUE;
    private final DatagramSocket socket;
    private final DataObservable observable;
    
    
    public KeepAliveThread(DataObservable obs) throws SocketException {
        socket = new DatagramSocket(0);
        socket.setSoTimeout(0);
        
        observable = obs;
        CONTINUE = true;
    }
    
    public int getPort(){
        return socket.getLocalPort();
    }

    public void setCONTINUE(boolean CONTINUE) {
        this.CONTINUE = CONTINUE;
    }
    
    public void exitThread(){
        setCONTINUE(false);
        socket.close();
    }
    
    private void handlePackets(DatagramPacket packet)
            throws IOException, ClassNotFoundException
    {    
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(packet.getData()));
        Object obj = in.readObject();
        
        if(obj instanceof String){
                    System.out.println((String)obj);
                    
        } else if(obj instanceof KeepAlive){
            packet.setData(new byte[MIN_BUFFER_SIZE]);
            socket.send(packet);
        }
        else if(obj instanceof RefreshData){
            //TODO: Waiting on teachers reply
            
            packet.setData(new byte[MIN_BUFFER_SIZE]);
            socket.send(packet);
        }
    }
    
    @Override
    public void run() {
        byte[] buffer = new byte[BUFFER_SIZE];
        DatagramPacket packet = new DatagramPacket(buffer, BUFFER_SIZE);
        
        while(CONTINUE){
            try{
                socket.receive(packet);
                handlePackets(packet);

            }catch(IOException | ClassNotFoundException e){
                System.out.println("Erro:" + e);
            }
        }
    }
}
