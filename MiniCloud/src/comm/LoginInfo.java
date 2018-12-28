/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comm;

import java.io.Serializable;

public class LoginInfo implements Serializable {
    private final String username;
    private final String password;
    private final ClientConnection connection;
    
    public LoginInfo(String username, String password, ClientConnection conn){
        this.username = username;
        this.password = password;
        this.connection = conn;
    }
    
    public String getUsername(){
        return username;
    }
    
    public String getPassword(){
        return password;
    }
    
    public int getKeepAlivePort(){
        return connection.getKeepAlivePort();
    }
    
    public int getNotificationPort(){
        return connection.getNotificationPort();
    }
    
    public int getTransferPort(){
        return connection.getTransferPort();
    }
}
