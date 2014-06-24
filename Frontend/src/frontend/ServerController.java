package frontend;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Message;
import t3interfaces.ICliente;

public class ServerController extends Thread implements Serializable {

    private final int localPort, fileServerPort;
    private ICliente client;
    private Message message;
    private Socket socket;
    private ObjectInputStream objInputStream;
    private ObjectOutputStream objOutputStream;

    public ServerController(int port) {
        this.localPort = port;
        this.fileServerPort = port + 1;
    }

    public ServerController(int port, String music, ICliente c) throws IOException {

        this.localPort = port;
        this.fileServerPort = port + 1;
        this.client = c;
        message = new Message(null, 0, music);
        System.out.println("> Request: " + music);
    }

    public int getPort() {
        return localPort;
    }

    @Override
    public void run() {
        try {
            // Busca no servidor de arquivos de porta "port"
            // usando DatagramSocket
            // Entrega a música pelo método client.deliver
            socket = new Socket("127.0.0.1", fileServerPort);

            objOutputStream = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("> Sent to file server: " + fileServerPort);

            objOutputStream.flush();
            objOutputStream.writeObject(message);
            objOutputStream.flush();

            objInputStream = new ObjectInputStream(socket.getInputStream());
            message = (Message) objInputStream.readObject();

            client.deliver(message.getMusicBytes());
            System.out.println("> Delivered to client");
            
            socket.close();
            this.interrupt();

        } catch (RemoteException ex) {
            Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
