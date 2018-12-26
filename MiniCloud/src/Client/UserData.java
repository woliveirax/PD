package Client;

import Exceptions.InvalidDirectoryException;
import comm.CloudData;
import comm.FileData;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UserData implements Serializable {
    private File downloadPath; //TODO: add file verification
    private File uploadPath;
    
    private List<CloudData> fileList;
    
    public UserData() {
        fileList = new ArrayList<>();
    }
    
    public void addNewUser(CloudData user){
        fileList.add(user);
    }
    
    public void RemoveUser(String user){
        Iterator<CloudData> it = fileList.iterator();
        
        while(it.hasNext()){
            if(it.next().getUser().compareTo(user) == 0){
                it.remove();
                break;
            }
        }
    }
    
    public void updateUserFiles(String username, ArrayList<FileData> files){
        for(CloudData data : fileList)
            if(data.getUser().compareTo(username) == 0){
                data.setInitialFiles(files);
                return;
            }
    }
    
    private CloudData getUserData(String user){
        for(CloudData data : fileList)
            if(data.getUser().compareTo(user) == 0)
                return data;
        
        return null;
    }
    
    public void addFileToUser(String user, FileData file){
        CloudData data = getUserData(user);
        
        if(data == null)
            return;
        
        data.addFile(file);
    }
    
    public void removeFileFromUser(String user, String filename){
        CloudData data = getUserData(user);
        
        if(data == null)
            return;
        
        data.removeFile(filename);
    }
    
    public void updateFileFromUser(String user, FileData file){
        CloudData data = getUserData(user);
        
        if(data == null)
            return;
        
        data.updateFile(file);
    }
    
    public File getDownloadPath() {
        return downloadPath;
    }

    public File getUploadPath() {
        return uploadPath;
    }
    
    public ArrayList<CloudData> getFileList() {
        return (ArrayList<CloudData>)fileList;
    }

    public void setFileList(List<CloudData> fileList) {
        this.fileList = fileList;
    }
    
    public void setDownloadPath(File file) throws InvalidDirectoryException {
        if (!file.exists()) 
        {
            throw new InvalidDirectoryException("Directory \'"
                    + file.getAbsolutePath() + "\'does not exist!");
        }

        if (!file.canWrite())
        {
            throw new InvalidDirectoryException("Can not write to \'"
                    + file.getAbsolutePath() + "\'!");
        }
        
        this.downloadPath = file;
    }

    public void setUploadPath(File file) throws InvalidDirectoryException {
        if (!file.exists()) 
        {
            throw new InvalidDirectoryException("Directory \'"
                    + file.getAbsolutePath() + "\'does not exist!");
        }

        if (!file.canRead())
        {
            throw new InvalidDirectoryException("Can not write to \'"
                    + file.getAbsolutePath() + "\'!");
        }
        
        this.uploadPath = file;
    }
}