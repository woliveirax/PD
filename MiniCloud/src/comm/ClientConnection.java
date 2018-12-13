/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comm;

public class ClientConnection {
    int keepAlivePort;
    int transferPort;

    public ClientConnection(int keepAlivePort, int transferPort) {
        this.keepAlivePort = keepAlivePort;
        this.transferPort = transferPort;
    }

    public int getKeepAlivePort() {
        return keepAlivePort;
    }

    public int getTransferPort() {
        return transferPort;
    }
}
