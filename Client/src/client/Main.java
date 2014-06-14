
package client;

import java.rmi.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.setSecurityManager(new RMISecurityManager());
        
        Client c;
        try {
            c = new Client();
            c.run();
        } catch (RemoteException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
