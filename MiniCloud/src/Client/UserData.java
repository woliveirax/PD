package Client;

import Exceptions.InvalidDirectoryException;
import comm.CloudData;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class UserData implements Serializable {
    private File downloadPath; //TODO: add file verification
    private File uploadPath;
    
    //TODO: Add methods to handle these calls
    private DataTransfer fileTransfers;
    private ArrayList<CloudData> fileList;

    public UserData() {
        this.fileTransfers = new DataTransfer();
    }

    public File getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(File file) throws InvalidDirectoryException {
        if (!file.exists()) 
        {
            throw new InvalidDirectoryException("Directory \'"
                    + file.getAbsolutePath() + "\'does not exist!");
        }

        if (!file.canRead())
        {
            throw new InvalidDirectoryException("Can not read files from \'"
                    + file.getAbsolutePath() + "\'!");
        }

        if (!file.canWrite())
        {
            throw new InvalidDirectoryException("Can not write to \'"
                    + file.getAbsolutePath() + "\'!");
        }
        
        this.downloadPath = file;
    }

    public File getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(File uploadPath) {
        this.uploadPath = uploadPath;
    }
}