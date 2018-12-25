package comm.Packets;

import java.io.Serializable;

public class RemoveFileRequest implements Serializable {
    private final String filename;
    private String username;
    
    public RemoveFileRequest(String filename){
        this.filename = filename;
        username = "";
    }

    public String getFilename() {
        return filename;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
