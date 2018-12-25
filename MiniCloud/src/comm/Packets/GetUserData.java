package comm.Packets;

import java.io.Serializable;

public class GetUserData implements Serializable {    
    private final String username;

    public GetUserData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
