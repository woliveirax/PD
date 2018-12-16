package comm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

public class CloudData implements Serializable {
    private final String user;
    private ArrayList<FileData> files;
    
    public CloudData(String username){
        user = username;
        files = new ArrayList<>();
    }
    
    public String getUser() {
        return user;
    }
    
    public ArrayList<FileData> getFiles() {
        return files;
    }
    
    public void setInitialFiles(ArrayList<FileData> files){
        this.files = files;
    }
    
    public void updateFile(FileData file){
        ListIterator<FileData> it = files.listIterator();
        
        while(it.hasNext()){
            if(it.next().getName().compareTo(file.getName()) == 0){
                it.set(file);
                break;
            }
        }
    }
    
    public void removeFile(String filename){
        Iterator<FileData> it = files.iterator();
        
        while(it.hasNext()){
            if(it.next().getName().equals(filename)){
                it.remove();
                break;
            }
        }
    }
    
    public void addFile(FileData file){
        files.add(file);
    }
}
