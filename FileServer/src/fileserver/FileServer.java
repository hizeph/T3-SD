
package fileserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
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
    private InetAddress frontendAddr;
    private ServerSocket serverSocket;
    private Socket socket;
    private InputStream inputStream;
    private ObjectOutputStream objOutputStream;
    
    public FileServer(int port) throws SocketException, IOException {
        
        this.port = port;
        serverSocket = new ServerSocket(port); 
        databasePath = System.getProperty("user.dir") + System.getProperty("file.separator");
        
        try {
            frontendAddr = InetAddress.getByName("localhost");
        } catch (UnknownHostException ex) {
            Logger.getLogger(FileServer.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public void run(){
        System.out.println("Listening port "+port);
        try {
            while (true) {
                
                socket = serverSocket.accept();
                inputStream = socket.getInputStream();
                
                buffer = new byte[bufferSize];
                inputStream.read(buffer,0,bufferSize);
                
                message = Message.toMessage(buffer);
                System.out.println("> Request: " + message.getName());
                
                search(message.getName());
                
                deliver();
                System.out.println("> Sent to frontend");
                                
                socket.close();
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
            nBytes = music.read(output);        
            message = new Message(output, nBytes, musicName);
            music.close();
            System.out.println("> Request found on server");
        } catch (FileNotFoundException ex) {
            message = new Message();
            System.out.println("!> Request not found on server " + port);
        }
    }
    
    private void deliver(){
        try {
            objOutputStream = new ObjectOutputStream(socket.getOutputStream());

            objOutputStream.flush();
            objOutputStream.writeObject(message);
            objOutputStream.flush();
            objOutputStream.close();
            
        } catch (IOException ex) {
            Logger.getLogger(FileServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
