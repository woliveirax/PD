package Client;

import java.io.Serializable;

public class TransferNotification {
    private String details;

    public TransferNotification(String details) {
        this.details = details;
    }

    public String getDetails() {
        return details;
    }
    
    @Override
    public String toString() {
        return details;
    }
}
