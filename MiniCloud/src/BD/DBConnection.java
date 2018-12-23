package BD;

import Exceptions.UserException;
import comm.ClientConnection;
import comm.LoginInfo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnection implements DBScripts, DatabaseConstants {    
    //Database credentials
    private String username = "admin";
    private String password = "admin";
    
    //Connection
    private Connection conn;
    private Statement stmt;
    
    public DBConnection(String user, String pass, String ip, int port) 
            throws SQLException, ClassNotFoundException
    {
        username = user;
        password = pass;
        
        try {
            //Register JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            //Build DB URL Connection
            String dbUrl = "jdbc:mysql://" + ip + ":" + port + "/";
            
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
    
    private void createDatabase() throws SQLException{
        stmt.executeUpdate(DBScripts.CREATE_DATABASE);
        stmt.executeUpdate(DBScripts.CREATE_TABLE_USERS);
        stmt.executeUpdate(DBScripts.CREATE_TABLE_AUTH_USERS);
        stmt.executeUpdate(DBScripts.CREATE_TABLE_FILES);
        stmt.executeUpdate(DBScripts.CREATE_TABLE_HISTORY);
    }
    
    private boolean databaseExists() throws SQLException{
        stmt.executeQuery(DOES_DB_EXISTS);
        return stmt.getResultSet().next();
    }
    
    /*
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
   */
    public User getUser(String username) 
            throws SQLException, UserException 
    {
        PreparedStatement st = conn.prepareStatement("SELECT * FROM miniclouddb.Users WHERE username = ?;");
        st.setString(1, username);
        st.executeQuery();
        
        ResultSet result = st.getResultSet();
        
        if(result.next()){
            return new User(result.getString(USERS_USERNAME), 
                    result.getString(USERS_PASSWORD),
                    result.getInt(USERS_ID));
        }
        throw new UserException("User does not exist!");
    }
    
    public void registerUser(String username, String password) 
            throws UserException, SQLException
    {
        try{
            PreparedStatement st = conn.prepareStatement(INSERT_USER);
            st.setString(1, username);
            st.setString(2, password);
        
            st.executeUpdate();
        }catch(SQLIntegrityConstraintViolationException e){
            throw new UserException("User Already exists!");
        }
    }
    
    public void userLogin(LoginInfo info, String ip)
            throws UserException, SQLException
    {
        try{
            User user = getUser(info.getUsername());
            if(!user.getPassword().equals(info.getPassword())){
                throw new UserException("Wrong password!");
            }
            
            PreparedStatement st = conn.prepareStatement(AUTHENTICATE_USER);
            st.setInt(1, user.getId());
            st.setInt(2, info.getKeepAlivePort());
            st.setInt(3, info.getTransferPort());
            st.setInt(4, info.getNotificationPort());
            st.setString(5, ip);
        
            st.executeUpdate();
        }catch(SQLIntegrityConstraintViolationException e){
            throw new UserException("Already logged in somewhere! please close the other instance and try again");
        }
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
            //conn.registerUser("wallace", "abcd");
            
            LoginInfo info = new LoginInfo("wallace", "abcd", new ClientConnection(1, 2, 3));
            conn.userLogin(info, "192.168.1.299");
        
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }
}