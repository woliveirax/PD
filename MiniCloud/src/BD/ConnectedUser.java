package BD;

public class ConnectedUser {
    private final String username;
    private final String ip;
    private final int transferPort;
    private final int keepAlivePort;

    public ConnectedUser(String username, String ip, int transferPort, int keepAlivePort) {
        this.username = username;
        this.ip = ip;
        this.transferPort = transferPort;
        this.keepAlivePort = keepAlivePort;
    }

    public String getUsername() {
        return username;
    }

    public String getIp() {
        return ip;
    }

    public int getTransferPort() {
        return transferPort;
    }

    public int getKeepAlivePort() {
        return keepAlivePort;
    }

    @Override
    public String toString() {
        return "user: " + username + "; ip: {" + ip + "}, keepalivePort: " + keepAlivePort;
    }
}