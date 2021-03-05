import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIInterface extends Remote {
    String lookup(String address) throws RemoteException;
    String register(String address, String hostname) throws RemoteException;
}
