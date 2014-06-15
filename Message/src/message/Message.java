
package message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Message implements Serializable {
    private int size;
    private byte[] musicBytes;
    private final int MAX_SIZE = 8338608;
    private String name;
    private boolean valid = false;
    
    public Message() {
        valid = false;
    }
    
    public Message(byte[] musicBytes, int size, String name) {
        this.name = name;
        this.size = size;
        this.musicBytes = new byte[size];
        this.musicBytes = musicBytes;
        valid = true;
    }

    public boolean isValid() {
        return valid;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public byte[] getMusicBytes() {
        return musicBytes;
    }
    
    public static byte[] toByte(Message m) {
        ObjectOutputStream os = null;
        try {
            
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            os = new ObjectOutputStream(byteStream);
            os.flush();
            os.writeObject(m);
            os.flush();
            return byteStream.toByteArray();
            
        } catch (IOException ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public static Message toMessage(byte[] b) {
        ObjectInputStream os = null;
        try {
            ByteArrayInputStream byteStream = new ByteArrayInputStream(b);
            os = new ObjectInputStream(byteStream);
            return (Message) os.readObject();

        } catch (IOException ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
}
