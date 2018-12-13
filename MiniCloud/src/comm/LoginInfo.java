/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comm;

public class LoginInfo {
    private final String username;
    private final String password;
    private final ClientConnection connection;
    
    public LoginInfo(String username, String password, ClientConnection conn){
        //TODO: add name and password validation (Password can't be empty)
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
    
    public int getTransferPort(){
        return connection.getTransferPort();
    }
}
