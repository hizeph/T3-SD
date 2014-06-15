
package frontend;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import t3interfaces.ICliente;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import message.Message;


public class ServerController extends Thread implements Serializable{
    
    private final int localPort, fileServerPort;
    private String music;
    private ICliente client;
    private DatagramSocket socket;
    private DatagramPacket packet;
    private Message message;
    private byte[] buffer;
    
    public ServerController(int port){
        this.localPort = port;
        this.fileServerPort = port+1;
    }
    
    public ServerController(int port, String music, ICliente c){
        this.localPort = port;
        this.fileServerPort = port+1;
        this.music = music;
        this.client = c;
        message = new Message(null,0,music);
        try {
            socket = new DatagramSocket(localPort);
        } catch (SocketException ex) {
            Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getPort(){
        return localPort;
    }
    
    @Override
    public void run(){
        try {
            // Busca no servidor de arquivos de porta "port"
            // usando DatagramSocket
            // Entrega a música pelo método client.deliver

            buffer = new byte[10000000];
            buffer = Message.toByte(message);
            packet = new DatagramPacket(buffer, buffer.length, InetAddress.getLocalHost(),fileServerPort);
            
            socket.send(packet);
            System.out.println("sent");
            
            socket.receive(packet);
            message = Message.toMessage(packet.getData());
            System.out.println("received");
            
            this.client.deliver(message.getMusicBytes());
            System.out.println("delivered");
            this.interrupt();
        } catch (RemoteException ex) {
            Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
