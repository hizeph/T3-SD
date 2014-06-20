
package frontend;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Message;
import t3interfaces.ICliente;


public class ServerController extends Thread implements Serializable{
    
    private final int localPort, fileServerPort;
    private String music;
    private ICliente client;
    private DatagramSocket socket;
    private DatagramPacket packet;
    private Message message;
    private byte[] buffer;
    private byte[] buffer_in;
    
    //tcp
    private Socket sock;
    private InputStream input;
    private OutputStream output;
    private FileOutputStream os;
    private BufferedOutputStream bos;
    
    public ServerController(int port){
        this.localPort = port;
        this.fileServerPort = port+1;
    }
    
    public ServerController(int port, String music, ICliente c) throws IOException{
        
        this.localPort = port;
        this.fileServerPort = port+1;
        this.sock = new Socket("127.0.0.1",fileServerPort);
        this.music = music;
        this.client = c;
        message = new Message(null,0,music);
        System.out.println("> musica a ser buscada : " + music );
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
            buffer_in= new byte[10000000];
            input = sock.getInputStream();
            output = sock.getOutputStream();
            System.out.println("> Sent to file server: " + fileServerPort);
            buffer = Message.toByte(message);
            output.write(buffer);
            System.out.println("> Enviado!");
//            os.write(buffer);
//            bos = new BufferedOutputStream(os);
//            bos.write(buffer);
            input.read(buffer_in,0,10000000);
            System.out.println("> Recebido no server");
            message = Message.toMessage(buffer_in);
            System.out.println("> conteudo : " + message.getName());
            
            this.client.deliver(message.getMusicBytes());
            System.out.println("> Delivered to client");
            sock.close();
            
//            buffer = new byte[10000000];
//            buffer = Message.toByte(message);
//            packet = new DatagramPacket(buffer, buffer.length, InetAddress.getLocalHost(),fileServerPort);
//            
//            socket.send(packet);
//            System.out.println("> Sent to file server: " + fileServerPort);
//            
//            socket.receive(packet);
//            message = Message.toMessage(packet.getData());
//            System.out.println("> Received from file server");
//            
//            this.client.deliver(message.getMusicBytes());
//            System.out.println("> Delivered to client");
//            
//            socket.close();
            this.interrupt();
            
        } catch (RemoteException ex) {
            Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
