
package fileserver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.*;


public class FileServer {
    
    private Message message;
    private final int port;
    private byte[] buffer;
    private final int bufferSize = 10000000;
    private final String databasePath;
    private FileInputStream music;
    private byte[] output;
    private int nBytes;
    private DatagramSocket socket;
    private DatagramPacket packet;
    private InetAddress frontendAddr;
    
    
    public FileServer(int port) throws SocketException {
        this.port = port;
        buffer = new byte[bufferSize];
        databasePath = System.getProperty("user.dir") + System.getProperty("file.separator");
        socket = new DatagramSocket(port);
        
        try {
            frontendAddr = InetAddress.getByName("localhost");
        } catch (UnknownHostException ex) {
            Logger.getLogger(FileServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void run(){
        try {
            while (true) {
                packet = new DatagramPacket(buffer, buffer.length);
                
                socket.receive(packet);
                System.out.println("receive");
                buffer = packet.getData();
                message = Message.toMessage(buffer);
                
                search(message.getName());
                deliver();
                
            }
        } catch (IOException ex) {
            Logger.getLogger(FileServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void search(String musicName) throws IOException{
        try {
            output = new byte[bufferSize];
            // look in database
            music = new FileInputStream(databasePath + musicName);
            nBytes = music.read(output, 0, bufferSize);
            message = new Message(output, nBytes, musicName);
            music.close();
        } catch (FileNotFoundException ex) {
            message = new Message();
            System.out.println("!> Request not found on server " + port);
        }
    }
    
    private void deliver(){
        try {
            packet = new DatagramPacket(buffer, buffer.length, frontendAddr, 2020);
            packet.setData(Message.toByte(message));
            socket.send(packet);
        } catch (IOException ex) {
            Logger.getLogger(FileServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
