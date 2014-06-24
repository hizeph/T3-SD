package client;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.server.*;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import t3interfaces.ICliente;
import t3interfaces.IServidor;


/*
 * Vai pegar um objeto remoto Server, criado pelo frontend
 * usar o remoteFrontend.search(music, ICliente)
 * usar o this.deliver()
 */
public class Client extends UnicastRemoteObject implements ICliente {

    private IServidor remoteFrontend;
    private String request;

    public Client() throws RemoteException {
    }

    public void run(String request) {
        try {
            this.request = request;
            remoteFrontend = (IServidor) Naming.lookup("Frontend");
            System.out.println("> Searching");
            remoteFrontend.search(request, this);
        } catch (NotBoundException | MalformedURLException | RemoteException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void deliver(byte[] musicBytes) throws RemoteException {
        if (musicBytes != null) {
            try {
                System.out.println("> Received");
                
                FileOutputStream music;
                String path = System.getProperty("user.dir") + System.getProperty("file.separator") + request;
                music = new FileOutputStream(path);
                music.write(musicBytes, 0, musicBytes.length);
                music.close();
                
                System.out.println("> Saved on: " + path);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("!> Music not found on file server");
        }
    }
}
