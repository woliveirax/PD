package comm.Packets;

import java.io.Serializable;

public class AddUser implements Serializable {
    private final String username;

    public AddUser(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
