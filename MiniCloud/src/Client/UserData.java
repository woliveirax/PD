package Client;

import comm.CloudData;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class UserData implements Serializable {
    File downloadPath; //TODO: add file verification
    File uploadPath;
    
    //TODO: Add methods to handle these calls
    DataTransfer transfers;
    ArrayList<CloudData> fileList;
}