import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

    public static void main(String[] args) {

        try {
            AlertServiceImpl service = new AlertServiceImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("AlertService", service);

            System.out.println(" Lost & Found Server Running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}