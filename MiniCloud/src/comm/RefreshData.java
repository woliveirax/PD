package comm;

import Client.WatchDog.FileAlterationTypes;
import java.io.Serializable;

public class RefreshData implements Serializable{
    private final FileAlterationTypes type; //replace with enumm with add remove or change
    private final String userInQuestion;
    private final FileData file;

    public RefreshData(FileAlterationTypes type, String userInQuestion, FileData file) {
        this.type = type;
        this.userInQuestion = userInQuestion;
        this.file = file;
    }

    public FileAlterationTypes getType() {
        return type;
    }

    public String getUserInQuestion() {
        return userInQuestion;
    }

    public FileData getFile() {
        return file;
    }  
}
