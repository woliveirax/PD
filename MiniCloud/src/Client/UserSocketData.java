package Client;


public class UserSocketData {
    private final String ip;
    private final int port;

    public UserSocketData(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
