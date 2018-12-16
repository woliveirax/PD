package Client.Threads;

import Client.DataObservable;
import java.net.Socket;

public class CommunicationThread extends Thread {
    private Socket socket;
    private final DataObservable observable;

    public CommunicationThread(DataObservable obs) {
        this.observable = obs;
    }
}
