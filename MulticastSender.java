import java.net.*;

public class MulticastSender {

    public static void main(String[] args) {

        try (DatagramSocket socket = new DatagramSocket()) {

            InetAddress group = InetAddress.getByName("230.0.0.0");
            String message = "New Lost Item Found in Zone A!";
            byte[] buffer = message.getBytes();

            DatagramPacket packet =
                    new DatagramPacket(buffer, buffer.length, group, 4446);

            socket.send(packet);

            System.out.println("Multicast message sent.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
