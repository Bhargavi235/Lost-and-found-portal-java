import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.Scanner;

public class LostFoundClient extends UnicastRemoteObject
        implements ClientCallbackInterface {

    protected LostFoundClient() throws RemoteException {
        super();
    }

    public void notifyUser(String message)
            throws RemoteException {
        System.out.println("\n CALLBACK RECEIVED: " + message);
    }

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            PortalInterface portal =
                    (PortalInterface) registry.lookup("LostFoundPortal");

            LostFoundClient client = new LostFoundClient();

            Scanner sc = new Scanner(System.in);

            System.out.print("Enter username: ");
            String username = sc.nextLine();

            portal.registerClient(username, client);

            while (true) {
                System.out.println("\n1. Report Lost Item");
                System.out.println("2. Exit");
                int choice = sc.nextInt();
                sc.nextLine();

                if (choice == 1) {
                    System.out.print("Enter item name: ");
                    String item = sc.nextLine();
                    portal.reportLostItem(username, item);
                } else {
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}