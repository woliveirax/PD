/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comm.Packets;

import java.io.Serializable;

public class RemoveUser implements Serializable {
    private final String username;

    public RemoveUser(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
