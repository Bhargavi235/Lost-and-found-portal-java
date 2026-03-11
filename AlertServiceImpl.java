import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.*;

public class AlertServiceImpl extends UnicastRemoteObject
        implements AlertService {

    private List<String> foundItems;
    private String lastBroadcast;

    protected AlertServiceImpl() throws RemoteException {
        foundItems = new ArrayList<>();
        foundItems.add("Laptop");
        foundItems.add("ID Card");
        foundItems.add("Water Bottle");

        lastBroadcast = " System Ready";
    }

    public synchronized String sendLostAlert(String item, String student)
        throws RemoteException {

    lastBroadcast = " New Lost Alert: " + student +
                    " reported " + item;

    if (foundItems.contains(item)) {

        lastBroadcast = " MATCH FOUND: " + student +
                        "'s " + item + " is available!";

        return "MATCH FOUND  Please collect your " + item;
    }

    return " Alert Registered. Admin has been notified.";
}

    public synchronized void reportFoundItem(String item)
            throws RemoteException {

        foundItems.add(item);
        lastBroadcast = "New item reported: " + item;
    }

    public synchronized List<String> getAllFoundItems()
            throws RemoteException {
        return foundItems;
    }

    public synchronized String broadcastMessage()
            throws RemoteException {
        return lastBroadcast;
    }
}