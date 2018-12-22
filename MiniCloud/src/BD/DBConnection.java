package BD;

import com.mysql.cj.xdevapi.Result;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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