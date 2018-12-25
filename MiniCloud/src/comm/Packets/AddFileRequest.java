package comm.Packets;

import comm.FileData;
import java.io.Serializable;

public class AddFileRequest implements Serializable {
    private final FileData file;
    private String username;
    
    public AddFileRequest(FileData file){
        this.file = file;
        username = "";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public FileData getFile() {
        return file;
    }
}
