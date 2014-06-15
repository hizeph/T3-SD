
package fileserver;

import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int startingPort = 2021;
        FileServer fs;
        boolean work = false;
        do {
            try {
                fs = new FileServer(startingPort);
                work=true;
                fs.run();
            } catch (SocketException ex) {
                startingPort+=2;
            }
        } while(!work);
       
    }
    
}
