import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkUtilityApp extends Application {

    private TextArea outputArea = new TextArea();
    private ExecutorService executor = Executors.newCachedThreadPool();

    @Override
    public void start(Stage stage) {

        Label header = new Label("Notifications");
        header.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");

        HBox headerBox = new HBox(header);
        headerBox.setPadding(new Insets(15));
        headerBox.setStyle("-fx-background-color: #1E293B;");
        headerBox.setAlignment(Pos.CENTER_LEFT);

        // ================= DNS =================
        TextField tfHost = new TextField();
        tfHost.setPromptText("Hostname (e.g., google.com)");
        Button btnResolve = new Button("Resolve DNS");

        btnResolve.setOnAction(e -> {
            try {
                InetAddress address = InetAddress.getByName(tfHost.getText());
                outputArea.appendText("Host: " + address.getHostName() + "\n");
                outputArea.appendText("IP: " + address.getHostAddress() + "\n\n");
            } catch (Exception ex) {
                outputArea.appendText("DNS Error: " + ex.getMessage() + "\n\n");
            }
        });

        VBox dnsBox = new VBox(5, new Label("InetAddress (DNS Lookup)"), tfHost, btnResolve);

        // ================= URL =================
        TextField tfUrl = new TextField();
        tfUrl.setPromptText("https://example.com");
        Button btnConnect = new Button("Connect URL");

        btnConnect.setOnAction(e -> {
            try {
                URL url = new URI(tfUrl.getText()).toURL();
                URLConnection connection = url.openConnection();
                connection.connect();

                outputArea.appendText("Content Type: " + connection.getContentType() + "\n");
                outputArea.appendText("Content Length: " + connection.getContentLength() + "\n\n");

            } catch (Exception ex) {
                outputArea.appendText("URL Error: " + ex.getMessage() + "\n\n");
            }
        });

        VBox urlBox = new VBox(5, new Label("URLConnection"), tfUrl, btnConnect);

        // ================= TCP CLIENT =================
        TextField tfMessage = new TextField();
        tfMessage.setPromptText("Message to send to TCP server");

        Button btnSendTCP = new Button("Send via TCP");

        btnSendTCP.setOnAction(e -> {
            executor.execute(() -> {
                try (Socket socket = new Socket("localhost", 5000);
                     PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                    out.println(tfMessage.getText());
                    String response = in.readLine();

                    outputArea.appendText("TCP Response: " + response + "\n\n");

                } catch (Exception ex) {
                    outputArea.appendText("TCP Error: " + ex.getMessage() + "\n\n");
                }
            });
        });

        VBox tcpBox = new VBox(5, new Label("TCP Socket Client (Port 5000)"), tfMessage, btnSendTCP);

        // ================= UDP MULTICAST =================
        Button btnStartMulticast = new Button("Start UDP Multicast Listener");

        btnStartMulticast.setOnAction(e -> {
            executor.execute(() -> {
               try (MulticastSocket socket = new MulticastSocket(4446)) {

    InetAddress group = InetAddress.getByName("230.0.0.0");

    InetSocketAddress groupAddress =
            new InetSocketAddress(group, 4446);

    NetworkInterface networkInterface =
            NetworkInterface.getByInetAddress(InetAddress.getLocalHost());

    socket.joinGroup(groupAddress, networkInterface);

    outputArea.appendText("Listening for multicast messages...\n");

    byte[] buffer = new byte[1024];
    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

    socket.receive(packet);
    String received =
            new String(packet.getData(), 0, packet.getLength());

    outputArea.appendText("Multicast Received: " + received + "\n\n");

    socket.leaveGroup(groupAddress, networkInterface);

} catch (Exception ex) {
    outputArea.appendText("Multicast Error: " + ex.getMessage() + "\n\n");
}

            });
        });

        VBox udpBox = new VBox(5, new Label("UDP Multicast (Group 230.0.0.0:4446)"), btnStartMulticast);

        // ================= OUTPUT =================
        outputArea.setPrefHeight(300);

        VBox content = new VBox(15, dnsBox, urlBox, tcpBox, udpBox, new Label("Output:"), outputArea);
        content.setPadding(new Insets(20));

        BorderPane root = new BorderPane();
        root.setTop(headerBox);
        root.setCenter(content);

        Scene scene = new Scene(root, 800, 750);
        stage.setScene(scene);
        stage.setTitle("Networking Suite");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
