package BD;

import java.sql.Date;
import java.sql.Timestamp;

public class TransferInfo {
    private final Timestamp date;
    private final String sourceName;
    private final String destinataryName;
    private final String fileName;

    public TransferInfo(Timestamp date, String sourceName, String destinataryName, String fileName) {
        this.date = date;
        this.sourceName = sourceName;
        this.destinataryName = destinataryName;
        this.fileName = fileName;
    }

    public Timestamp getDate() {
        return date;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getDestinataryName() {
        return destinataryName;
    }

    public String getFileName() {
        return fileName;
    }
    
    @Override
    public String toString() {
        return "TransferInfo{" + "date=" + date + ", sourceName=" + sourceName + ", destinataryName=" + destinataryName + ", fileName=" + fileName + '}';
    }
    
}
