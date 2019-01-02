package RMI;

import static Client.GUI.CloudMainScreen.format;
import comm.CloudData;
import comm.FileData;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class MonitorClient extends UnicastRemoteObject implements MonitorInterface {
    
    private JTable Jhistory;
    private MonitorClient() throws RemoteException{};
    
    public MonitorClient(JTable tabela) throws RemoteException
    {
        Jhistory = tabela;
    }

    @Override
    public void receiveData(ArrayList<CloudData> arg) throws RemoteException {
        ArrayList<CloudData> data = (ArrayList<CloudData>) arg;
        DefaultTableModel model = new DefaultTableModel(){
                        @Override
                        public boolean isCellEditable(int row, int column) {
                            return false;
                        }
                    };

        model.addColumn("User");
        model.addColumn("File Name");
        model.addColumn("Size");

        for(CloudData user : data){
            ArrayList<FileData> files = user.getFiles();
            for(FileData file : files){
                model.addRow(new Object[]{user.getUser(),file.getName(), format(file.getSize(), 0)});
            }
        }
        Jhistory.setModel(model);
    }
}
