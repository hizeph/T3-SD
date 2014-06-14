
package client;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.*;
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
public class Client extends UnicastRemoteObject implements ICliente{
    private IServidor remoteFrontend;
    
    public Client() throws RemoteException{
    }
    
    public void run(){
        try {
            remoteFrontend = (IServidor) Naming.lookup("Frontend");
            remoteFrontend.search("s", this);
        } catch (NotBoundException | MalformedURLException | RemoteException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void deliver(byte[] musicBytes) throws RemoteException {
        System.out.println("received");
    }
}
