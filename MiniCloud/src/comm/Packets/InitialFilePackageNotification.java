package comm.Packets;

import java.io.Serializable;

public class InitialFilePackageNotification implements Serializable {
    private final String username;
    
    public InitialFilePackageNotification(String username){
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
