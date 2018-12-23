package BD;

public interface DatabaseConstants {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    
    final static String INSERT_USER = "INSERT INTO miniclouddb.Users (username, password) VALUES (?,?);";
    //final static String GET_USER = "(username, password) VALUES (?,?);";
    final static String AUTHENTICATE_USER = "INSERT INTO miniclouddb.AuthUsers (userId, keepAlivePort, transferPort,notificationPort, ipAddress) VALUES (?,?,?,?,?);";
    
    
    static final String USERS_USERNAME = "username";
    static final String USERS_PASSWORD = "password";
    static final String USERS_ID = "idUser";
    
    
}
