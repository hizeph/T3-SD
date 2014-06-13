
package client;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
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
public class Client implements ICliente{
    private IServidor remoteFrontend;
    
    public Client(){
    }
    
    public void run(){
        try {
            remoteFrontend = (IServidor) Naming.lookup("frontend");
        } catch (NotBoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public byte[] deliver() throws RemoteException {
        byte[] b = "blablabla".getBytes();
        return b;
    }
}
