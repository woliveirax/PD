package Server;

import BD.ConnectedUser;
import BD.DBConnection;
import Exceptions.FileException;
import Exceptions.UserException;
import comm.CloudData;
import comm.FileData;
import comm.LoginInfo;
import comm.Packets.AddFileRequest;
import comm.Packets.AddUser;
import comm.Packets.InitialFilePackage;
import comm.Packets.InitialFilePackageNotification;
import comm.Packets.RemoveFileRequest;
import comm.Packets.RemoveUser;
import comm.Packets.TransferHistoryPackage;
import comm.Packets.TransferInfo;
import comm.Packets.UpdateFileRequest;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class ServerObservable extends Observable{
    
    private DBConnection DB;
    private ServerKeepAlive keepAlive;
    private List<ClientHandler> loggedUsers;
    
    public ServerObservable(String DB_IP,int DB_PORT) {
        try{
            DB = new DBConnection("admin","admin", DB_IP,DB_PORT);
            keepAlive = new ServerKeepAlive(this);
            keepAlive.start();
        }catch(ClassNotFoundException | SocketException e){System.err.println(e.getCause()); System.exit(1);}
         catch (SQLException e){
             System.out.println(e);
            DB.shutdown();
            System.exit(1);
        }
        
        loggedUsers = new ArrayList<>();
    }
    
    public void addLoggedThread(ClientHandler t){
        loggedUsers.add(t);
    }
    
    private boolean userDisconnect(String username){
        for(ClientHandler handler : loggedUsers){
            if(handler.getUsername().equals(username)){
                try{
                    handler.exit();
                    userLogout(username);
                    
                    return true;
                } catch (SQLException | UserException e){
                    System.out.println(e);
                }
            }
        }
        
        return false;
    }
    
    public boolean disconnectUser(String username){
        return userDisconnect(username);
    }
    
    public void userLogout(String username) throws UserException, SQLException{
        DB.userLogout(username);
        
        setChanged();
        notifyObservers(new RemoveUser(username));
    }
    
    public ConnectedUser getSingleAuthenticatedUser(String username) throws SQLException, UserException{
        return DB.getSingleAuthenticatedUser(username);
    }
    
    public boolean userExists(String username){
        return DB.userExists(username);
    }
    
    public ArrayList<ConnectedUser> getAuthenticatedUsers() throws SQLException{
        return DB.getAuthenticatedUsers();
    }
    
    public void registerUser(String username, String password) throws UserException, SQLException{
        DB.registerUser(username, password);
    }
    
    public CloudData getUserData(String username) throws SQLException, UserException{
        return DB.getUserData(username);
    }
    
    public List<ClientHandler> getLoggedUserThreads(){
        return loggedUsers;
    }
    
    public void addFile(String username, String filename, long filesize) throws UserException, SQLException, FileException{
        DB.addFile(username, filename, filesize);
        
        AddFileRequest notify = new AddFileRequest(new FileData(filename, filesize));
        notify.setUsername(username);
        
        setChanged();
        notifyObservers(notify);
    }
    
    public void addFilesBunk(String username, InitialFilePackage files){
        for(FileData file : files.getFiles()){
            try {
                DB.addFile(username, file.getName(),file.getSize());
            } catch (UserException | SQLException | FileException ex) {
                System.err.println(ex);
            }
        }
        
        setChanged();
        notifyObservers(new InitialFilePackageNotification(username));
    }
    
    public void removeFile(String username, String filename) throws UserException, SQLException, FileException{
        DB.removeFile(username, filename);
        
        RemoveFileRequest notify = new RemoveFileRequest(filename);
        notify.setUsername(username);
        
        setChanged();
        notifyObservers(notify);
    }
    
    public void updateFile(String username, FileData file) throws UserException, SQLException, FileException{
        DB.updateFile(username, file);
        
        UpdateFileRequest notify = new UpdateFileRequest(file);
        notify.setUsername(username);
        
        setChanged();
        notifyObservers(notify);
    }
    
    public void userLogin(LoginInfo info, String ip) throws UserException, SQLException{
        DB.userLogin(info, ip);
        
        setChanged();
        notifyObservers(new AddUser(info.getUsername()));
    }
    
    public ArrayList<CloudData> getAllUsersData() throws UserException, SQLException{
        return DB.getAllUsersData();
    }
    
    public int getStrikes(String username) throws SQLException, UserException{
        return DB.getStrikes(username);
    }
    
    public void setStrikes(String username, int strikes) throws SQLException, UserException{
        DB.setStrikes(username, strikes);
    }
    
    public ArrayList<TransferInfo> getDownloadHistory(String username) throws SQLException{
        return DB.getDownloadHistory(username);
    }
    
    public ArrayList<TransferInfo> getUploadHistory(String username) throws SQLException{
        return DB.getUploadHistory(username);
    }
    
    public TransferHistoryPackage getTransferHistory(String username) throws SQLException{
        ArrayList<TransferInfo> info = getDownloadHistory(username);
        info.addAll(getUploadHistory(username));
        
        return new TransferHistoryPackage(info);
    }
    
    public void registerTransferHistory(TransferInfo info) throws SQLException, UserException{
        DB.addHistoryRegister(info.getSourceName(), info.getDestinataryName(), info.getFileName());
    }
    
    public void sendUdpPacketToClient(ConnectedUser user, Object obj){
        keepAlive.sendUdpPacketToClient(user, obj);
    }
    
    public void broadcastUdpPacketToClients(Object obj){
        keepAlive.broadcastToUdpClients(obj);
    }
    
    public void shutdownServer(){
        keepAlive.exit();
        
        for(ClientHandler user : loggedUsers){
            try {
                DB.userLogout(user.getUsername());
            } catch (UserException | SQLException e) {
                System.out.println(e);
            }
            user.exit();
        }
        DB.shutdown();
    }
}
