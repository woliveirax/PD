package BD;

public class ConnectedUser {
    private final String username;
    private final String ip;
    private final int port;

    public ConnectedUser(String username, String ip, int port) {
        this.username = username;
        this.ip = ip;
        this.port = port;
    }
    
    public String getUsername() {
        return username;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return "user: " + username + "; ip: {" + ip + "}, port: " + port;
    }
}