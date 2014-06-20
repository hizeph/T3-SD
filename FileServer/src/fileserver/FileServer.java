
package fileserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
    private DatagramSocket socket;
    private DatagramPacket packet;
    private InetAddress frontendAddr;
    
    private ServerSocket servsock;
    private Socket s;
    private InputStream is;
    private OutputStream os;
    
    public FileServer(int port) throws SocketException, IOException {
        this.port=port;
        
        //tcp
        servsock = new ServerSocket(port); 
        databasePath = System.getProperty("user.dir") + System.getProperty("file.separator");
        
        
        //end tcp
//        this.port = port;
//        buffer = new byte[bufferSize];
//        databasePath = System.getProperty("user.dir") + System.getProperty("file.separator");
//        socket = new DatagramSocket(port);
//        socket.close(); // sim, precisa disso
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
                //tcp
                s= servsock.accept();
                is = s.getInputStream();
                
                
                buffer = new byte[bufferSize];
                is.read(buffer,0,bufferSize);
                System.out.println("> recebido ");
                
                message = Message.toMessage(buffer);
                System.out.println("> mensage:" + message.getName());
                search(message.getName());
                System.out.println("> feito Search");
                deliver(message);
                System.out.println("> em teoria, enviado");
                                
                s.close();
                //end
                
//                socket = new DatagramSocket(port);
//                packet = new DatagramPacket(buffer, buffer.length);
//                
//                System.out.println("> Waiting request: " + port); // from frontend
//                socket.receive(packet);
//                buffer = packet.getData();
//                message = Message.toMessage(buffer);
//                System.out.println("> Request received");
//                
//                search(message.getName());
//                deliver(message);
//                socket.close();
                
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
    
    private void deliver(Message message){
        try {
            //tcp
            
            
            os = s.getOutputStream();
//            os = s.getOutputStream();
            System.out.println("> Enviando... ");
            byte[] b = Message.toByte(message);
            System.out.println("> . ");
            os.write(b,0,b.length);
            System.out.println("> . ");
            os.close();
//            os.write(Message.toByte(message),0,bufferSize);
            System.out.println("> Enviado ");
//            os.flush();
            
            //end
            
            //antigo
//            packet = new DatagramPacket(buffer, buffer.length, frontendAddr, port-1);
//            packet.setData(Message.toByte(message));
//            socket.send(packet);
        } catch (IOException ex) {
            Logger.getLogger(FileServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
