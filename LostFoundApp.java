import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.sql.*;
import java.io.File;
import java.io.FileInputStream;

public class LostFoundApp extends Application {

    Connection con;
    Statement stmt;
    ResultSet rs;

    private String userRole = "ADMIN";

    TextField tfName = new TextField();
    TextArea taDesc = new TextArea();
    TextField tfZone = new TextField();
    CheckBox cbPriority = new CheckBox("High Priority");
    Button btnUpload = new Button("Upload Image");
    Button btnAdd = new Button("Add Lost Item");

    ImageView imageView = new ImageView();
    File selectedImageFile;

    ComboBox<String> cbStatus = new ComboBox<>();
    Button btnUpdate = new Button("Update Status");

    Button btnFirst = new Button("First");
    Button btnPrev = new Button("Previous");
    Button btnNext = new Button("Next");
    Button btnLast = new Button("Last");

    @Override
    public void start(Stage stage) {

        // ================= HEADER =================
        Label headerTitle = new Label("Lost and Found Management Dashboard");
        headerTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        HBox header = new HBox(headerTitle);
        header.setPadding(new Insets(20));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: #1E3A8A;");

        // ================= ADD CARD =================
        Label addTitle = new Label("Add New Item");
        addTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        tfName.setPromptText("Item Name");
        taDesc.setPromptText("Description");
        tfZone.setPromptText("Zone");

        taDesc.setPrefRowCount(3);

        btnUpload.setStyle("-fx-background-color: #64748B; -fx-text-fill: white;");
        btnAdd.setStyle("-fx-background-color: #2563EB; -fx-text-fill: white;");

        VBox addCard = new VBox(10,
                addTitle,
                new Label("Item Name"), tfName,
                new Label("Description"), taDesc,
                new Label("Zone"), tfZone,
                cbPriority,
                btnUpload,
                btnAdd
        );

        addCard.setPadding(new Insets(20));
        addCard.setPrefWidth(400);
        addCard.setStyle("""
                -fx-background-color: white;
                -fx-background-radius: 12;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 12,0,0,4);
                """);

        // ================= MANAGE CARD =================
        Label manageTitle = new Label("Manage Records");
        manageTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        cbStatus.getItems().addAll("Lost", "Found");

        imageView.setFitWidth(280);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);
        imageView.setStyle("-fx-border-color: #CBD5E1; -fx-border-width: 2;");

        btnUpdate.setStyle("-fx-background-color: #16A34A; -fx-text-fill: white;");

        HBox navBar = new HBox(10, btnFirst, btnPrev, btnNext, btnLast);
        navBar.setAlignment(Pos.CENTER);

        navBar.getChildren().forEach(btn ->
                btn.setStyle("-fx-background-color: #E2E8F0; -fx-font-weight: bold;")
        );

        VBox manageCard = new VBox(12,
                manageTitle,
                new Label("Status"), cbStatus,
                btnUpdate,
                imageView,
                navBar
        );

        manageCard.setPadding(new Insets(20));
        manageCard.setPrefWidth(400);
        manageCard.setStyle("""
                -fx-background-color: white;
                -fx-background-radius: 12;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 12,0,0,4);
                """);

        // ================= MAIN CONTENT =================
        HBox content = new HBox(40, addCard, manageCard);
        content.setPadding(new Insets(30));
        content.setAlignment(Pos.CENTER);
        content.setStyle("-fx-background-color: #F1F5F9;");

        BorderPane root = new BorderPane();
        root.setTop(header);
        root.setCenter(content);

        root.setStyle("-fx-font-family: Times New Roman;-fx-font-size: 14px;");


        Scene scene = new Scene(root, 1100, 650);
        stage.setScene(scene);
        stage.setTitle("Lost and Found Portal");
        stage.show();

        // ================= LOGIC =================
        applyRolePermissions();
        btnUpload.setOnAction(e -> chooseImage(stage));

        connectDatabase();
        showMetaData();
        performJoin();

        try {
            if (rs.next()) displayRecord();
        } catch (Exception e) { e.printStackTrace(); }

        // NAVIGATION
        btnFirst.setOnAction(e -> {
            try { if (rs.first()) displayRecord(); }
            catch (Exception ex) { ex.printStackTrace(); }
        });

        btnLast.setOnAction(e -> {
            try { if (rs.last()) displayRecord(); }
            catch (Exception ex) { ex.printStackTrace(); }
        });

        btnNext.setOnAction(e -> {
            try { if (rs.next()) displayRecord(); else rs.last(); }
            catch (Exception ex) { ex.printStackTrace(); }
        });

        btnPrev.setOnAction(e -> {
            try { if (rs.previous()) displayRecord(); else rs.first(); }
            catch (Exception ex) { ex.printStackTrace(); }
        });

        // UPDATE STATUS
        btnUpdate.setOnAction(e -> {
            try {
                String newStatus = cbStatus.getValue();
                if (newStatus != null) {
                    rs.updateString("status", newStatus);
                    rs.updateRow();
                    new Alert(Alert.AlertType.INFORMATION,
                            "Status Updated Successfully!").show();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // INSERT ITEM
        btnAdd.setOnAction(e -> {
            try {
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO lost_items (item_name, description, zone, priority, status, image) VALUES (?, ?, ?, ?, ?, ?)"
                );

                ps.setString(1, tfName.getText());
                ps.setString(2, taDesc.getText());
                ps.setString(3, tfZone.getText());
                ps.setBoolean(4, cbPriority.isSelected());
                ps.setString(5, "Lost");

                if (selectedImageFile != null) {
                    FileInputStream fis = new FileInputStream(selectedImageFile);
                    ps.setBinaryStream(6, fis, (int) selectedImageFile.length());
                } else {
                    ps.setNull(6, Types.BLOB);
                }

                ps.executeUpdate();
                new Alert(Alert.AlertType.INFORMATION,
                        "Item Added Successfully!").show();

                rs = stmt.executeQuery("SELECT * FROM lost_items");

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private void applyRolePermissions() {
        if (userRole.equals("USER")) {
            cbStatus.setDisable(true);
            btnUpdate.setDisable(true);
        }
    }

    private void chooseImage(Stage stage) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select Item Image");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        selectedImageFile = chooser.showOpenDialog(stage);
        if (selectedImageFile != null) {
            imageView.setImage(
                    new javafx.scene.image.Image(selectedImageFile.toURI().toString())
            );
        }
    }

    private void connectDatabase() {
        try {
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/lostandfound",
                    "root",
                    "Bhargavi2006"
            );

            stmt = con.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );

            rs = stmt.executeQuery("SELECT * FROM lost_items");
            System.out.println("Connected Successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayRecord() {
        try {
            tfName.setText(rs.getString("item_name"));
            taDesc.setText(rs.getString("description"));
            tfZone.setText(rs.getString("zone"));
            cbPriority.setSelected(rs.getBoolean("priority"));
            cbStatus.setValue(rs.getString("status"));

            byte[] imgBytes = rs.getBytes("image");
            if (imgBytes != null) {
                javafx.scene.image.Image img =
                        new javafx.scene.image.Image(
                                new java.io.ByteArrayInputStream(imgBytes)
                        );
                imageView.setImage(img);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showMetaData() {
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            System.out.println("Total Columns: " + columnCount);

            for (int i = 1; i <= columnCount; i++) {
                System.out.println(
                        "Column: " + metaData.getColumnName(i) +
                                " | Type: " + metaData.getColumnTypeName(i)
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void performJoin() {
        try {
            String query = "SELECT lost_items.item_name, lost_items.status, users.name " +
                    "FROM lost_items JOIN users ON lost_items.user_id = users.user_id";

            Statement joinStmt = con.createStatement();
            ResultSet joinRs = joinStmt.executeQuery(query);

            System.out.println("JOIN RESULT:");
            while (joinRs.next()) {
                System.out.println(
                        "Item: " + joinRs.getString("item_name") +
                                " | Status: " + joinRs.getString("status") +
                                " | User: " + joinRs.getString("name")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
