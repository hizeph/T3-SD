package t3interfaces;


import java.rmi.Remote;
import java.rmi.RemoteException;


public interface ICliente extends Remote{
    // método para receber a música
    public void deliver(byte[] music) throws RemoteException;
}
