import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LostFoundFX extends Application {

    private TableView<String> table = new TableView<>();

    @Override
    public void start(Stage stage) {

        Label title = new Label("Campus Lost & Found Portal");
        title.setStyle("""
                -fx-font-size: 24px;
                -fx-font-weight: bold;
                -fx-text-fill: #2c3e50;
                """);

        BorderPane root = new BorderPane();
        root.setTop(createHeader(title));
        root.setCenter(createTabs());

        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("Lost & Found – Christ University");
        stage.setScene(scene);
        stage.show();
    }

    private Pane createHeader(Label title) {
        HBox header = new HBox(title);
        header.setPadding(new Insets(20));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("""
                -fx-background-color: #ecf0f1;
                -fx-border-color: #dcdcdc;
                -fx-border-width: 0 0 1 0;
                """);
        return header;
    }

    private TabPane createTabs() {

        TabPane tabs = new TabPane();
        tabs.getTabs().addAll(
                new Tab("Report Lost Item", createReportPane()),
                new Tab("View All Items", createViewPane()),
                new Tab("High Priority Items", createPriorityPane())
        );

        tabs.getTabs().forEach(t -> t.setClosable(false));
        return tabs;
    }

    private Pane createReportPane() {

        VBox card = new VBox(15);
        card.setPadding(new Insets(30));
        card.setMaxWidth(450);
        card.setStyle("""
                -fx-background-color: white;
                -fx-border-color: #dcdcdc;
                -fx-border-radius: 6;
                -fx-background-radius: 6;
                """);

        Label heading = new Label("Report a Lost Item");
        heading.setStyle("""
                -fx-font-size: 18px;
                -fx-font-weight: bold;
                """);

        TextField nameField = new TextField();
        nameField.setPromptText("Item name");

        TextField descField = new TextField();
        descField.setPromptText("Item description");

        ComboBox<String> zoneBox = new ComboBox<>();
        zoneBox.getItems().addAll(
                "Block 1", "Block 2", "Block 3", "Block 4",
                "Central Block", "R&D Block", "Audi Block",
                "Birds Park Canteen", "Gourmet", "KE Cafe",
                "Ivy Hall", "Main Auditorium"
        );
        zoneBox.getSelectionModel().selectFirst();

        CheckBox priorityBox = new CheckBox("Small item (High Priority)");

        Button submit = new Button("Submit Report");
        submit.setStyle("""
                -fx-background-color: #2c3e50;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-padding: 8 20 8 20;
                """);

        submit.setOnAction(e -> {
            LostItemDAO.insertItem(
                    nameField.getText(),
                    descField.getText(),
                    zoneBox.getValue(),
                    priorityBox.isSelected()
            );

            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setHeaderText("Report Submitted");
            a.setContentText("Lost item has been recorded.");
            a.show();

            nameField.clear();
            descField.clear();
            priorityBox.setSelected(false);
        });

        card.getChildren().addAll(
                heading,
                new Label("Item Name"), nameField,
                new Label("Description"), descField,
                new Label("Zone"), zoneBox,
                priorityBox,
                submit
        );

        StackPane wrapper = new StackPane(card);
        wrapper.setPadding(new Insets(40));
        wrapper.setStyle("-fx-background-color: #f7f9fb;");

        return wrapper;
    }

    private Pane createViewPane() {

        if (table.getColumns().isEmpty()) {
            TableColumn<String, String> col = new TableColumn<>("Lost Items");
            col.setCellValueFactory(d ->
                    new javafx.beans.property.SimpleStringProperty(d.getValue()));
            col.setPrefWidth(820);
            table.getColumns().add(col);
        }

        Button load = new Button("Load Items");
        load.setStyle("""
                -fx-background-color: #34495e;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-padding: 8 18 8 18;
                """);

        load.setOnAction(e -> {
            ObservableList<String> data =
                    FXCollections.observableArrayList(
                            LostItemDAO.getAllItems()
                    );
            table.setItems(data);
        });

        VBox box = new VBox(15, load, table);
        box.setPadding(new Insets(30));
        box.setStyle("-fx-background-color: #f7f9fb;");

        return box;
    }

    private Pane createPriorityPane() {

        ListView<String> list = new ListView<>();

        Button load = new Button("Show High Priority Items");
        load.setStyle("""
                -fx-background-color: #c0392b;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-padding: 8 18 8 18;
                """);

        load.setOnAction(e ->
                list.setItems(
                        FXCollections.observableArrayList(
                                LostItemDAO.getHighPriorityItems()
                        )
                )
        );

        VBox box = new VBox(15, load, list);
        box.setPadding(new Insets(30));
        box.setStyle("-fx-background-color: #f7f9fb;");

        return box;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
