import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PortalInterface extends Remote {

    void registerClient(String username, ClientCallbackInterface client)
            throws RemoteException;

    void reportLostItem(String username, String item)
            throws RemoteException;

    void markItemFound(String item)
            throws RemoteException;
}