package comm;

import java.io.File;
import java.util.ArrayList;

public class InitialFilePackage {
    final private ArrayList<FileData> files;
    
    public InitialFilePackage(String directory){
        files = new ArrayList<>();
        
        File[] temp = new File(directory).listFiles();
        
        for(File file : temp)
            files.add(new FileData(file.getName(), file.length()));
    }
    
    public ArrayList<FileData> getFiles() {
        return files;
    }
}
