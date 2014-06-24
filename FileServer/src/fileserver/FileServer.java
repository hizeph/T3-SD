package fileserver;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.*;


public class FileServer {
    
    private Message message;
    private final int port;
    private final int bufferSize = 10000000;
    private final String databasePath;
    private FileInputStream music;
    private int nBytes;
    private ServerSocket serverSocket;
    private Socket socket;
    private ObjectInputStream objInputStream;
    private ObjectOutputStream objOutputStream;
    
    public FileServer(int port) throws SocketException, IOException {
        
        this.port = port;
        serverSocket = new ServerSocket(port); 
        databasePath = System.getProperty("user.dir") + System.getProperty("file.separator");
        
    }
    
    public void run(){
        System.out.println("Listening port "+port);
        try {
            while (true) {
                
                socket = serverSocket.accept();
                objInputStream = new ObjectInputStream(socket.getInputStream());
                
                message = (Message) objInputStream.readObject();

                System.out.println("> Request: " + message.getName());
                
                search(message.getName());
                
                deliver();
                System.out.println("> Sent to frontend");
                                
                socket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(FileServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FileServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void search(String musicName) throws IOException{
        try {
            byte[] output = new byte[bufferSize];
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
