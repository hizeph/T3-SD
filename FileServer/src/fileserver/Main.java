
package fileserver;

import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            FileServer fs = new FileServer(2021);
            fs.run();
        } catch (SocketException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
