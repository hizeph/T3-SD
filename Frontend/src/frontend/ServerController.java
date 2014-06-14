
package frontend;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import t3interfaces.ICliente;


public class ServerController extends Thread implements Serializable{
    
    private final int port;
    private String music;
    private ICliente client;
    
    public ServerController(int port){
        this.port = port;
    }
    
    public ServerController(int port, String music, ICliente c){
        this.port = port;
        this.music = music;
        this.client = c;
    }

    public int getPort(){
        return port;
    }
    
    @Override
    public void run(){
        try {
            System.out.println("batata");
            // Busca no servidor de arquivos de porta "port"
            // usando DatagramSocket
            // Entrega a música pelo método client.deliver
            
            this.client.deliver(music.getBytes());
            this.interrupt();
        } catch (RemoteException ex) {
            Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
