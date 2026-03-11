import javafx.animation.ScaleTransition;
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
import java.util.List;

public class AdminDashboardFX extends Application {

    private TextArea displayArea;

    @Override
    public void start(Stage stage) {

        Label title = new Label(" Admin Dashboard");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField newItemField = new TextField();
        Button addBtn = new Button("Add Found Item");
        Button viewBtn = new Button("View All Found Items");
        Button broadcastBtn = new Button("View Broadcast");

        displayArea = new TextArea();
        displayArea.setEditable(false);

        addBtn.setOnAction(e -> addItem(newItemField.getText()));
        viewBtn.setOnAction(e -> viewItems());
        broadcastBtn.setOnAction(e -> viewBroadcast());

        VBox root = new VBox(15,
                title,
                new Label("New Found Item:"), newItemField,
                addBtn,
                viewBtn,
                broadcastBtn,
                displayArea);

        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 500, 500);
        stage.setScene(scene);
        stage.setTitle("Admin Panel");
        stage.show();

        Timeline timeline = new Timeline(
    new KeyFrame(Duration.seconds(3), e -> viewBroadcast())
);
timeline.setCycleCount(Animation.INDEFINITE);
timeline.play();

        animate(title);
    }

    private void addItem(String item) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            AlertService service =
                    (AlertService) registry.lookup("AlertService");

            service.reportFoundItem(item);
            displayArea.setText("Item Added Successfully ");

        } catch (Exception e) {
            displayArea.setText("Error Connecting to Server ");
        }
    }

    private void viewItems() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            AlertService service =
                    (AlertService) registry.lookup("AlertService");

            List<String> items = service.getAllFoundItems();
            displayArea.setText(items.toString());

        } catch (Exception e) {
            displayArea.setText("Error ");
        }
    }

    private void viewBroadcast() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            AlertService service =
                    (AlertService) registry.lookup("AlertService");

            displayArea.setText(service.broadcastMessage());

        } catch (Exception e) {
            displayArea.setText("Error ");
        }
    }

    private void animate(Label label) {
        ScaleTransition st = new ScaleTransition(Duration.seconds(1.5), label);
        st.setFromX(0);
        st.setToX(1);
        st.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}