package comm.Packets;

import java.io.Serializable;

public class FileTransfersHistoryRequest implements Serializable{
    private final String username;
    
    public FileTransfersHistoryRequest(String username){
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
    
}
