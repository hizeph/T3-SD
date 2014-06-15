
package frontend;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Main {

    /**
     * @param args the command line arguments
     */
   
    public static void main(String[] args) {
        //System.setSecurityManager(new RMISecurityManager());
        String hostURL = "Frontend";
        
        try {
            
            Frontend frontend = new Frontend(2);
            
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            
            Naming.bind(hostURL, frontend);
            
        } catch (RemoteException | AlreadyBoundException | MalformedURLException ex) {
            Logger.getLogger(Frontend.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
