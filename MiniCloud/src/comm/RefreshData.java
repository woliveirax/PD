package comm;

import Client.WatchDog.FileAlterationTypes;
import java.io.Serializable;

public class RefreshData implements Serializable{
    private FileAlterationTypes type; //replace with enumm with add remove or change
    private FileData file;

    public RefreshData(FileAlterationTypes type, FileData file) {
        this.type = type;
        this.file = file;
    }

    public FileAlterationTypes getType() {
        return type;
    }

    public FileData getFile() {
        return file;
    }
    
}
