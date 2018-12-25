package comm.Packets;

import java.io.Serializable;
import java.util.ArrayList;

public class TransferHistoryPackage implements Serializable {
    private final ArrayList<TransferInfo> history;

    public TransferHistoryPackage(ArrayList<TransferInfo> history) {
        this.history = history;
    }
    
    public ArrayList<TransferInfo> getHistory() {
        return history;
    }
}
