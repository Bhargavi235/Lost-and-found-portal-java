import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.Animation;
import javafx.util.Duration;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientFX extends Application {

    private TextArea responseArea;

    @Override
    public void start(Stage stage) {

        Label title = new Label("Lost and Found Portal");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        TextField nameField = new TextField();
        TextField itemField = new TextField();

        Button sendBtn = new Button("Send Alert");

        responseArea = new TextArea();
        responseArea.setEditable(false);

        sendBtn.setOnAction(e -> sendAlert(nameField.getText(), itemField.getText()));

        VBox root = new VBox(15,
                title,
                new Label("Name:"), nameField,
                new Label("Lost Item:"), itemField,
                sendBtn,
                responseArea);

        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 450, 450);
        stage.setScene(scene);
        stage.setTitle("Student Client");
        stage.show();

        Timeline timeline = new Timeline(
    new KeyFrame(Duration.seconds(3), e -> checkBroadcast())
);
timeline.setCycleCount(Animation.INDEFINITE);
timeline.play();

        animate(title);
    }

    private void sendAlert(String name, String item) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            AlertService service =
                    (AlertService) registry.lookup("AlertService");

            String response = service.sendLostAlert(item, name);
            responseArea.setText(response);

        } catch (Exception ex) {
            responseArea.setText("Server Not Reachable ");
        }
    }

    private void checkBroadcast() {
    try {
        Registry registry = LocateRegistry.getRegistry("localhost", 1099);
        AlertService service =
                (AlertService) registry.lookup("AlertService");

        String broadcast = service.broadcastMessage();
        responseArea.setText(broadcast);

    } catch (Exception e) {
        responseArea.setText("Server not reachable");
    }
}

    private void animate(Label label) {
        FadeTransition ft = new FadeTransition(Duration.seconds(2), label);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}