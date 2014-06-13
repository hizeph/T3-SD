package t3interfaces;


import java.rmi.Remote;
import java.rmi.RemoteException;


public interface IServidor extends Remote {
    // método para pesquisar uma música
    public void search(String music, ICliente refClient) throws RemoteException;
}
