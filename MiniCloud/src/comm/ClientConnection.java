/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comm;

import java.io.Serializable;

public class ClientConnection implements Serializable {
    int keepAlivePort;
    int transferPort;
    int notificationPort;

    public ClientConnection(int keepAlivePort, int transferPort, int notificationPort) {
        this.keepAlivePort = keepAlivePort;
        this.transferPort = transferPort;
        this.notificationPort = notificationPort;
    }

    public int getKeepAlivePort() {
        return keepAlivePort;
    }

    public int getTransferPort() {
        return transferPort;
    }
    
    public int getNotificationPort(){
        return notificationPort;
    }
}
