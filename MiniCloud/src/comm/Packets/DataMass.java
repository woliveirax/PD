package comm.Packets;

import comm.CloudData;
import java.io.Serializable;
import java.util.ArrayList;

public class DataMass implements Serializable{
    private final ArrayList<CloudData> data;

    public DataMass(ArrayList<CloudData> data) {
        this.data = data;
    }

    public ArrayList<CloudData> getData() {
        return data;
    }
}
