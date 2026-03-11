import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface AlertService extends Remote {

    String sendLostAlert(String itemName, String studentName)
            throws RemoteException;

    void reportFoundItem(String itemName)
            throws RemoteException;

    List<String> getAllFoundItems()
            throws RemoteException;

    String broadcastMessage()
            throws RemoteException;
}