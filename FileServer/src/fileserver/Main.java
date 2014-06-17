
package fileserver;

import java.net.SocketException;

public class Main {

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
