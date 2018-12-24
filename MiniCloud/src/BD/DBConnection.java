package BD;

import Exceptions.FileException;
import Exceptions.UserException;
import comm.CloudData;
import comm.FileData;
import comm.LoginInfo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnection implements DBScripts, DatabaseConstants {

    //Database credentials
    private String username = "admin";
    private String password = "admin";

    //Connection
    private Connection conn;

    public DBConnection(String user, String pass, String ip, int port)
            throws SQLException, ClassNotFoundException {
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
            Statement stmt = conn.createStatement();

            if (!databaseExists()) {
                System.out.println("Database does not exists!\n creating...");
                createDatabase();
            }

        } catch (SQLException | ClassNotFoundException e) {
            //Handle errors for JDBC
            throw e;

        }
    }

    public boolean isConnected() throws SQLException {//TODO: verify this validation
        Statement stmt = conn.createStatement();
        return conn.isValid(2) && !stmt.isClosed();
    }

    public void shutdown() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
        }
    }

    private void createDatabase() throws SQLException {
        Statement stmt = conn.createStatement();

        stmt.executeUpdate(CREATE_DATABASE);
        stmt.executeUpdate(CREATE_TABLE_USERS);
        stmt.executeUpdate(CREATE_TABLE_AUTH_USERS);
        stmt.executeUpdate(CREATE_TABLE_FILES);
        stmt.executeUpdate(DBScripts.CREATE_TABLE_HISTORY);
    }

    private boolean databaseExists() throws SQLException {
        Statement stmt = conn.createStatement();

        stmt.executeQuery(DOES_DB_EXISTS);
        return stmt.getResultSet().next();
    }

    public boolean userExists(String username) {
        try {
            PreparedStatement st = conn.prepareStatement(GET_USER);
            st.setString(1, username);
            st.executeQuery();

            ResultSet result = st.getResultSet();

            return result.next();
        } catch (SQLException e) {
            return false;
        }
    }

    //public CloudData getUser(String username)
    
    private User getUser(String username)
            throws SQLException, UserException {
        PreparedStatement st = conn.prepareStatement(GET_USER);
        st.setString(1, username);
        st.executeQuery();

        ResultSet result = st.getResultSet();

        if (result.next()) {
            return new User(result.getString(USERS_USERNAME),
                    result.getString(USERS_PASSWORD),
                    result.getInt(USERS_ID));
        }
        throw new UserException("User does not exist!");
    }

    public void registerUser(String username, String password)
            throws UserException, SQLException {
        try {
            PreparedStatement st = conn.prepareStatement(INSERT_USER);
            st.setString(1, username);
            st.setString(2, password);

            st.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new UserException("User Already exists!");
        }
    }

    public void userLogin(LoginInfo info, String ip)
            throws UserException, SQLException {
        try {
            User user = getUser(info.getUsername());
            if (!user.getPassword().equals(info.getPassword())) {
                throw new UserException("Wrong password!");
            }

            PreparedStatement st = conn.prepareStatement(AUTHENTICATE_USER);
            st.setLong(1, user.getId());
            st.setInt(2, info.getKeepAlivePort());
            st.setInt(3, info.getTransferPort());
            st.setInt(4, info.getNotificationPort());
            st.setString(5, ip);

            st.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new UserException("Already logged in somewhere! please close the other instance and try again!");
        }
    }

    public void userLogout(String username)
            throws UserException, SQLException {
        try {
            User user = getUser(username);
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM miniclouddb.authusers WHERE userId = ?");

            stmt.setInt(1, user.getId());
            stmt.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new UserException("Already logged in somewhere! please close the other instance and try again!");
        }
    }

    public FileData getFile(String username, String filename)
            throws UserException, SQLException, FileException {
        try {
            User user = getUser(username);

            PreparedStatement st = conn.prepareStatement(GET_FILE);
            st.setInt(1, user.getId());
            st.setString(2, filename);
            st.executeUpdate();

            ResultSet res = st.getResultSet();
            if (res.next()) {
                return new FileData(res.getString("name"), res.getLong("size"));
            }

            throw new FileException("File does not exist!");
        } catch (UserException e) {
            throw new FileException("User does not exist!");
        }
    }

    public boolean fileExists(int userid, String filename)
            throws UserException, SQLException, FileException {
        try {

            PreparedStatement st = conn.prepareStatement(GET_FILE);
            st.setInt(1, userid);
            st.setString(2, filename);
            st.executeQuery();

            ResultSet res = st.getResultSet();
            return res.next();

        } catch (SQLException e) {
            throw e;
        }
    }

    public void addFile(String username, String filename, long filesize)
            throws UserException, SQLException, FileException {
        try {
            User user = getUser(username);

            if (fileExists(user.getId(), filename)) {
                throw new FileException("File alreadt exists!");
            }

            PreparedStatement st = conn.prepareStatement(ADD_FILE);
            st.setInt(1, user.getId());
            st.setString(2, filename);
            st.setLong(3, filesize);

            st.executeUpdate();
        } catch (UserException e) {
            throw new UserException("can not add file: " + e);
        }
    }

    public void removeFile(String username, String filename)
            throws UserException, SQLException, FileException {
        try {
            User user = getUser(username);

            if (!fileExists(user.getId(), filename)) {
                throw new FileException("File does not exist!");
            }

            PreparedStatement st = conn.prepareStatement(REMOVE_FILE);
            st.setInt(1, user.getId());
            st.setString(2, filename);

            st.executeUpdate();

        } catch (UserException e) {
            throw new UserException("Can not add a file to non existing user!");
        }
    }

    public void updateFile(String username, FileData file)
            throws UserException, SQLException, FileException {
        try {
            User user = getUser(username);

            if (!fileExists(user.getId(), file.getName())) {
                throw new FileException("File does not exist!");
            }

            PreparedStatement st = conn.prepareStatement(UPDATE_FILE);
            st.setLong(1, file.getSize());
            st.setInt(2, user.getId());

            st.executeUpdate();

        } catch (UserException e) {
            throw new UserException("Can not add a file to non existing user!");
        }
    }

    //public void addHistoryRegister
    public void addHistoryRegister(String sourceName, String destName, String filename)
            throws SQLException, UserException {
        Calendar calendar = Calendar.getInstance();
        Timestamp date = new java.sql.Timestamp(calendar.getTime().getTime());
        int source = getUser(sourceName).getId();
        int dest = getUser(destName).getId();

        PreparedStatement st = conn.prepareStatement(ADD_TRANSFER_HISTORY);
        st.setInt(1, source);
        st.setInt(2, dest);
        st.setTimestamp(3, date);
        st.setString(4, filename);

        st.executeUpdate();
    }

    public ArrayList<TransferInfo> getDownloadHistory(String username)
            throws SQLException {
        ArrayList<TransferInfo> history = new ArrayList<>();

        PreparedStatement st = conn.prepareStatement(GET_DOWNLOAD_HISTORY);
        st.setString(1, username);
        st.executeQuery();

        ResultSet result = st.getResultSet();

        while (result.next()) {
            history.add(new TransferInfo(result.getTimestamp("date"),
                    result.getString("source"),
                    result.getString("dest"),
                    result.getString("filename")));
        }
        return history;
    }

    private ArrayList<TransferInfo> getUploadHistory(String username)
            throws SQLException {
        ArrayList<TransferInfo> history = new ArrayList<>();

        PreparedStatement st = conn.prepareStatement(GET_UPLOAD_HISTORY);
        st.setString(1, username);
        st.executeQuery();

        ResultSet result = st.getResultSet();

        while (result.next()) {
            history.add(new TransferInfo(result.getTimestamp("date"),
                    result.getString("source"),
                    result.getString("dest"),
                    result.getString("filename")));
        }
        return history;
    }

    public ArrayList<ConnectedUser> getAuthenticatedUsers() throws SQLException {
        ArrayList<ConnectedUser> connectedUsers = new ArrayList<>();

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(GET_LOGGED_USERS);

            while (rs.next()) {

                ConnectedUser user = new ConnectedUser(
                        rs.getString("username"),
                        rs.getString("ipAddress"),
                        rs.getInt("transferPort"));

                connectedUsers.add(user);
            }

            return connectedUsers;
        } catch (SQLException e) {
            throw e;
        }
    }

    public ConnectedUser getSingleAuthenticatedUser(String username)
            throws SQLException, UserException {
        try {
            PreparedStatement st = conn.prepareStatement(GET_LOGGED_USER_BY_NAME);
            st.setString(1, username);
            st.executeQuery();

            ResultSet rs = st.getResultSet();
            if (rs.next()) {
                ConnectedUser user = new ConnectedUser(
                        rs.getString("username"),
                        rs.getString("ipAddress"),
                        rs.getInt("transferPort"));

                return user;
            }
            throw new UserException("User is not logged in the system!");
        } catch (SQLException e) {
            throw e;
        }
    }

    public int getStrikes(String username)
            throws SQLException, UserException {
        try {
            PreparedStatement st = conn.prepareStatement(GET_STRIKES);
            st.setString(1, username);
            st.executeQuery();

            ResultSet rs = st.getResultSet();
            if (rs.next()) {
                return rs.getInt("strikes");
            }

            throw new UserException("User is not logged on!");
        } catch (SQLException e) {
            throw e;
        }
    }

    public void setStrikes(String username, int strikes)
            throws SQLException, UserException {
        try {
            PreparedStatement st = conn.prepareStatement(SET_STRIKES);
            st.setInt(1, strikes);
            st.setString(2, username);
            st.executeUpdate();

        } catch (SQLException e) {
            throw e;
        }
    }

    public ArrayList<FileData> getFilesFromUser(String username)
            throws SQLException {
        try {
            ArrayList<FileData> files = new ArrayList<>();

            PreparedStatement st = conn.prepareStatement(GET_FILES_FROM_USER);
            st.setString(1, username);
            st.executeQuery();

            ResultSet rs = st.getResultSet();
            while (rs.next()) {
                files.add(new FileData(rs.getString("name"), rs.getLong("size")));
            }
            return files;

        } catch (SQLException e) {
            throw e;
        }
    }

    public ArrayList<CloudData> getAllUsersData()
            throws UserException, SQLException {
        try {
            ArrayList<CloudData> users = new ArrayList<>();

            PreparedStatement st = conn.prepareStatement(GET_LOGGED_USERS);
            st.executeQuery();

            ResultSet rs = st.getResultSet();
            while (rs.next()) {
                CloudData user = new CloudData(
                        rs.getString("username"),
                        rs.getString("ipAddress"),
                        rs.getInt("transferPort"));

                user.setInitialFiles(getFilesFromUser(user.getUser()));
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            throw e;
        }
    }

    //TODO: remove this main
    public static void main(String[] args) {
        try {
            DBConnection conn = new DBConnection("admin", "admin", "project-soralis.pro", 55532);
            //conn.registerUser("wallace", "abcd");
            //conn.registerUser("joana", "1234");

            //LoginInfo info = new LoginInfo("wallace", "abcd", new ClientConnection(1, 2, 3));
            //conn.userLogin(info, "192.168.1.299");
            //LoginInfo x = new LoginInfo("joana", "1234", new ClientConnection(1, 2, 3));
            //conn.userLogin(x, "192.168.1.22");
            //conn.addFile("wallace","myfile", 22333L);
            //conn.addFile("joana","myfile", 22333L);
            //conn.addFile("wallace","xpto", 22333L);
            //conn.removeFile("wallace", "myfile");
            //conn.updateFile("wallace", new FileData("myfile", 200));
            //conn.addHistoryRegister("wallace","joana","myfich");
            //System.out.println(conn.getDownloadHistory("wallace"));
            //System.out.println(conn.getUploadHistory("wallace"));
            //conn.setStrikes("j", 3);
            //System.out.println(conn.getStrikes("joana"));
            System.out.println(conn.getAuthenticatedUsers());

            ArrayList<CloudData> x = conn.getAllUsersData();

            for (CloudData user : x) {
                System.out.println(user.getUser());
                System.out.println(user.getFiles());
            }

        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }// catch (FileException ex) {
        //  Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        //}
    }
}
