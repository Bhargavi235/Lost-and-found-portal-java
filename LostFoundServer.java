import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.*;

public class LostFoundServer extends UnicastRemoteObject
        implements PortalInterface {

    private HashMap<String, ClientCallbackInterface> clients;
    private HashMap<String, String> lostItems;

    protected LostFoundServer() throws RemoteException {
        clients = new HashMap<>();
        lostItems = new HashMap<>();
    }

    public void registerClient(String username,
                               ClientCallbackInterface client)
            throws RemoteException {
        clients.put(username, client);
        System.out.println(username + " registered successfully.");
    }

    public void reportLostItem(String username, String item)
            throws RemoteException {
        lostItems.put(item, username);
        System.out.println(username + " reported lost item: " + item);
    }

    public void markItemFound(String item)
            throws RemoteException {

        if (lostItems.containsKey(item)) {
            String owner = lostItems.get(item);
            ClientCallbackInterface client = clients.get(owner);

            if (client != null) {
                client.notifyUser("🎉 Your item '" + item + "' has been FOUND!");
            }

            lostItems.remove(item);
        }
    }

    public static void main(String[] args) {
    try {
        LostFoundServer server = new LostFoundServer();
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.rebind("LostFoundPortal", server);

        System.out.println("Server running...");

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("Enter item to mark as FOUND: ");
            String item = sc.nextLine();
            server.markItemFound(item);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}
}