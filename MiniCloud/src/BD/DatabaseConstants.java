package BD;

public interface DatabaseConstants {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    
    final static String GET_LOGGED_USERS = "SELECT username, ipAddress, transferPort,keepAlivePort FROM miniclouddb.users, miniclouddb.authusers WHERE userId = idUser;";
    final static String GET_LOGGED_USER_BY_NAME = "SELECT username, ipAddress, transferPort, keepAlivePort FROM miniclouddb.users, miniclouddb.authusers WHERE userId = idUser AND username = ?;";
    
    final static String GET_STRIKES = "SELECT strikes FROM miniclouddb.AuthUsers, miniclouddb.Users WHERE userId = idUser AND username = ?;";
    final static String SET_STRIKES = "UPDATE miniclouddb.AuthUsers, miniclouddb.Users SET strikes = ? WHERE userId = idUser AND username = ?;";
    
    final static String GET_USER = "SELECT * FROM miniclouddb.Users WHERE username = ?;";
    final static String GET_USER_BY_ID = "SELECT * FROM miniclouddb.Users WHERE idUser = ?;";
    final static String INSERT_USER = "INSERT INTO miniclouddb.Users (username, password) VALUES (?,?);";
    final static String AUTHENTICATE_USER = "INSERT INTO miniclouddb.AuthUsers (userId, keepAlivePort, transferPort,notificationPort, ipAddress) VALUES (?,?,?,?,?);";
    final static String LOGOUT_USER = "INSERT INTO miniclouddb.Users (username, password) VALUES (?,?,?);";
    
    final static String GET_FILES_FROM_USER = "SELECT name, size FROM miniclouddb.Files, miniclouddb.users WHERE owner = idUser AND username = ?;";
    
    final static String GET_FILE = "SELECT * FROM miniclouddb.Files WHERE owner = ? AND name = ?;";
    final static String ADD_FILE = "INSERT INTO miniclouddb.Files (owner, name, size) VALUES (?,?,?);";
    final static String REMOVE_FILE = "DELETE FROM miniclouddb.Files WHERE owner = ? AND name = ?;";
    final static String UPDATE_FILE = "UPDATE miniclouddb.Files SET size = ? WHERE owner = ? AND name = ?;";
    
    final static String ADD_TRANSFER_HISTORY = "INSERT INTO miniclouddb.History (source, destination, date, filename) VALUES (?,?,?,?);";
    final static String GET_DOWNLOAD_HISTORY = "SELECT tab1.username AS source, tab2.username AS dest, date, filename"
            + "                                 FROM miniclouddb.Users tab1, miniclouddb.Users tab2, miniclouddb.History h"
            + "                                 WHERE tab1.idUser = h.source AND tab2.idUser = h.destination AND tab2.username = ?";
    
    final static String GET_UPLOAD_HISTORY = "SELECT tab1.username AS source, tab2.username AS dest, date, filename"
            + "                                 FROM miniclouddb.Users tab1, miniclouddb.Users tab2, miniclouddb.History h"
            + "                                 WHERE tab1.idUser = h.source AND tab2.idUser = h.destination AND tab1.username = ?";;
    
    //DB_Fields: TABLENAME_FIELD
    static final String USERS_USERNAME = "username";
    static final String USERS_PASSWORD = "password";
    static final String USERS_ID = "idUser";
    
    
}
