package BD;

import comm.FileData;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnection {
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private String dbUrl;
    
    //Database credentials
    private String username = "admin";
    private String password = "admin";
    
    //Connection
    private Connection conn;
    private Statement stmt;
    
    public DBConnection(String user, String pass, String ip, int port) 
            throws SQLException, ClassNotFoundException
    {
        try {
            //Register JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            //Build DB URL Connection
            dbUrl = "jdbc:mysql://" + ip + ":" + port + "/";
            
            //Open a connection
            conn = DriverManager.getConnection(dbUrl, username, password);
            
            System.out.println("Connection to the database established!");
            
            //Execute a query
            stmt = conn.createStatement();
            
            if(!databaseExists()){
                System.out.println("Database does not exists!\n creating...");
                createDatabase();
            }
            
        } catch (SQLException | ClassNotFoundException e) {
            //Handle errors for JDBC
            throw e;
            
        }
    }
    
    public boolean isConnected() throws SQLException {//TODO: verify this validation
        return conn.isValid(2) && !stmt.isClosed();
    }
    
    public void shutdown(){
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {}
        
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {}
    }
    
    public void createDatabase() throws SQLException{
        executeQuery(DBScripts.CREATION_SCRIPT);
    }
    
    public boolean databaseExists() throws SQLException{
        executeQuery(DBScripts.DOES_DB_EXISTS);
        return stmt.getResultSet().next();
    }
    
    public void executeQuery(String query) throws SQLException{
        try{
            stmt.execute(query);
        }catch(SQLException e){
            throw e;
        }
    }
    
    public Set<Integer> getLoggedUsers() throws SQLException{
        String sql = "SELECT * FROM AuthUsers";
        Set<Integer> loggedIds = new HashSet<>();
        
        try{
            ResultSet rs = stmt.executeQuery(sql);
            
            while(rs.next()){
                loggedIds.add(rs.getInt("userId"));
            }
            
            rs.close();
        }catch(SQLException e){
            throw e;
        }
        
        return loggedIds;
    }
    
    public Set<Integer> getRegisteredUsers() throws SQLException{
        String sql = "SELECT username FROM Users";
        Set<Integer> usersIds = new HashSet<>();
        
        try{
            ResultSet rs = stmt.executeQuery(sql);
            
            while(rs.next()){
                usersIds.add(rs.getInt("idUser"));
            }
            
            rs.close();
        }catch(SQLException e){
            throw e;
        }
        
        return usersIds;
    }
    
    public String getUserName(int userID) throws SQLException{
        String sql = "SELECT username FROM Users WHERE idUser = " + userID;
        String username;
        
        try{
            ResultSet rs = stmt.executeQuery(sql);
            
            username = rs.getString("username");
            
            rs.close();
        }catch(SQLException e){
            throw e;
        }
        
        return username;
    }
    
    public ArrayList<FileData> getFilesFromUser(String username)throws SQLException{
        String sql = "SELECT * FROM Files, Users WHERE Files.AuthUserId = Users.idUser AND Users.username = "+ username; 
        ArrayList<FileData> files = new ArrayList<>();
        
        try{
            ResultSet rs = stmt.executeQuery(sql);
            
            while(rs.next()){
                files.add( new FileData(rs.getString("name"), rs.getInt("AuthUserId")));
            }
            
            rs.close();
        }catch(SQLException e){
            throw e;
        }
        
        return files;
    }
    
    public ArrayList<TransferInfo> getTransfersRelatedToUser (String username) throws SQLException{
        String sql = "SELECT * FROM History as h, Users as u"
                + "WHERE h.source = u.idUser AND u.username = " 
                + username + " OR h.destination = u.idUser AND u.username = " + username;
        ArrayList<TransferInfo> transfers = new ArrayList<>();
        String sourceName, destName;
        try{
            ResultSet rs = stmt.executeQuery(sql);
            
            //TODO: Source and Destination must be converted to string
            while(rs.next()){
                sourceName = getUserName(rs.getInt("source"));
                destName = getUserName(rs.getInt("destination"));
                
                transfers.add(new TransferInfo(rs.getDate("date"), sourceName, destName, rs.getString("filename")));
            }
            
            rs.close();
        }catch(SQLException e){
            throw e;
        }
        
        return transfers;
    }
   
    public void addLoggedUser(int userID) throws SQLException{
        //Sting sql = "INSERT "
    }
    
    //public void addUser()
    
    //public void addFile()
    
    //public void removeFile()
    
    //public void updateFile()
    
    //public void addHistoryRegister
    
    //TODO: remove this main
    public static void main(String[] args) {
        try {
            DBConnection conn = new DBConnection("admin", "admin", "project-soralis.pro",55532);
        
        
        
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }
}