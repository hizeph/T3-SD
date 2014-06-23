package frontend;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import t3interfaces.ICliente;
import t3interfaces.IServidor;
import java.rmi.*;
import java.rmi.server.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Frontend extends UnicastRemoteObject implements IServidor, Serializable {

    private ArrayList<ServerController> serverList;
    private ServerController fileServer;
    private int roundRobinIndex, port = 2020;
    

    public Frontend(int nServers) throws RemoteException {
        
        serverList = new ArrayList<>();
        
        for (int i=0;i<nServers;i++){
            fileServer = new ServerController(port+(i*2));
            serverList.add(fileServer);
        }
        
        roundRobinIndex = 0;
    }

    @Override
    public void search(String music, ICliente c) throws RemoteException {
        
        fileServer = serverList.get(roundRobinIndex);
        
        try {
            fileServer = new ServerController(fileServer.getPort(), music, c);
        } catch (IOException ex) {
            Logger.getLogger(Frontend.class.getName()).log(Level.SEVERE, null, ex);
        }
        fileServer.start();
        roundRobin();
    }

    private void roundRobin() {
        roundRobinIndex++;
        if (roundRobinIndex == serverList.size()) {
            roundRobinIndex = 0;
        }
    }
}
