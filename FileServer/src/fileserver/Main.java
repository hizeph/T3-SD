
package fileserver;

import java.io.IOException;
import java.net.SocketException;

public class Main {

    public static void main(String[] args) throws IOException {
        int startingPort = 2021;
        FileServer fs;
        boolean work = false;
        // gambiarrinha pra usar uma porta diferente pra cada FileServer (somente impares, começando da 2021
        // a porta startingPort-1 é utilizada pelo Frontend para receber a resposta
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
