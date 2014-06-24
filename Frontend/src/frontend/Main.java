package frontend;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Main {
   
    public static void main(String[] args) {
        
        String hostURL = "Frontend";
        
        try {
            // argumendo é o número de FileServers
            // aumentar só pra testar o Round Robin (ta funcionando no UDP)
            Frontend frontend = new Frontend(1); 
            
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            
            Naming.bind(hostURL, frontend);
            
        } catch (RemoteException | AlreadyBoundException | MalformedURLException ex) {
            Logger.getLogger(Frontend.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
