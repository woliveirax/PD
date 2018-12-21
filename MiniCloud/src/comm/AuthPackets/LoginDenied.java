package comm.AuthPackets;

import java.io.Serializable;

public class LoginDenied implements Serializable {
    private String reason;
    
    public LoginDenied(String msg){
        this.reason = msg;
    }

    public String getReason() {
        return reason;
    }
    
    @Override
    public String toString() {
        return reason;
    }
}
