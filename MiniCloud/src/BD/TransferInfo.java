package BD;

import java.util.Date;

public class TransferInfo {
    private final Date date;
    private final String sourceName;
    private final String destinataryName;
    private final String fileName;

    public TransferInfo(Date date, String sourceName, String destinataryName, String fileName) {
        this.date = date;
        this.sourceName = sourceName;
        this.destinataryName = destinataryName;
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "TransferInfo{" + "date=" + date + ", sourceName=" + sourceName + ", destinataryName=" + destinataryName + ", fileName=" + fileName + '}';
    }
    
}
