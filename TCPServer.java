import java.io.*;
import java.net.*;

public class TCPServer {

    public static void main(String[] args) {

        System.out.println("TCP Server Started on Port 5000...");

        try (ServerSocket serverSocket = new ServerSocket(5000)) {

            while (true) {

                Socket clientSocket = serverSocket.accept();
                System.out.println("Client Connected!");

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));

                PrintWriter out = new PrintWriter(
                        clientSocket.getOutputStream(), true);

                String message = in.readLine();
                System.out.println("Received: " + message);

                out.println("Server Received: " + message);

                clientSocket.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
