package comm;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class CloudData implements Serializable {
    private final String user;
    private ArrayList<File> files;
    
    public CloudData(String username){
        user = username;
        files = new ArrayList<>();
    }
    
    public String getUser() {
        return user;
    }
    
    public ArrayList<File> getFiles() {
        return files;
    }
}
