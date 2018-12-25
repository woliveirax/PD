package comm.Packets;

import comm.FileData;
import java.io.Serializable;

public class UpdateFileRequest implements Serializable {
    private final FileData  file;
    private String username;
    
    public UpdateFileRequest(FileData file){
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
