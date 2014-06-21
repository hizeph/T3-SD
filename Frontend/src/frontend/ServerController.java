
package frontend;

import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
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
    private Message message;
    private byte[] bufferOut;
    private byte[] bufferIn;
    private Socket socket;
    private ObjectInputStream objInputStream;
    private OutputStream outputStream;
    private FileOutputStream outputFileStream;
    private BufferedOutputStream outputBufferedStream;
    
    public ServerController(int port){
        this.localPort = port;
        this.fileServerPort = port+1;
    }
    
    public ServerController(int port, String music, ICliente c) throws IOException{
        
        this.localPort = port;
        this.fileServerPort = port+1;
        this.music = music;
        this.client = c;
        message = new Message(null,0,music);
        System.out.println("> musica a ser buscada : " + music );
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
            socket = new Socket("127.0.0.1",fileServerPort);
            
            bufferOut = new byte[11000000];
            bufferIn = new byte[11000000];
            
            outputStream = socket.getOutputStream();
            System.out.println("> Sent to file server: " + fileServerPort);
            bufferOut = Message.toByte(message);
            outputStream.flush();
            outputStream.write(bufferOut);
            outputStream.flush();
            System.out.println("> Enviado!");

            objInputStream = new ObjectInputStream(socket.getInputStream());
            
            try {
                objInputStream.readFully(bufferIn);
            } catch (EOFException e){
                System.out.println("> Recebido no server: " + bufferIn.length);
            }
            
            message = Message.toMessage(bufferIn);
            //System.out.println("> conteudo : " + message.getMusicBytes().length);
            
            this.client.deliver(message.getMusicBytes());
            System.out.println("> Delivered to client");
            socket.close();
            
            this.interrupt();
            
        } catch (RemoteException ex) {
            Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
