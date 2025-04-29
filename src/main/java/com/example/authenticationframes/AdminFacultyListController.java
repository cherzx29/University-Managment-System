package com.example.authenticationframes;

import com.example.real.utils.SceneLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AdminFacultyListController {

    private static final String FILE_PATH = "src/main/resources/data/File_Faculty.txt";

    @FXML private VBox facultyListContainer;
    @FXML private TextField searchField;
    @FXML private Button searchButton;
    @FXML private Button addFacultyButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private Button backButton;

    private Pane selectedFacultyPane = null;
    private static final List<String[]> facultyDataList = new ArrayList<>();
    private static final List<String[]> facultyDataListStatic = new ArrayList<>();

    @FXML
    private void initialize() {
        loadFaculty();

        searchButton.setOnAction(event -> searchFaculty());
        addFacultyButton.setOnAction(event -> openAddFacultyScreen());
        editButton.setOnAction(event -> handleEditButton());
        deleteButton.setOnAction(event -> handleDeleteButton());
        backButton.setOnAction(event -> handleBack());
    }

    private void loadFaculty() {
        facultyListContainer.getChildren().clear();
        facultyDataList.clear();
        facultyDataListStatic.clear();

        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.err.println("⚠ Faculty file not found!");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length < 6) continue;

                facultyDataList.add(parts);
                facultyDataListStatic.add(parts);

                Pane pane = createFacultyPane(parts);
                facultyListContainer.getChildren().add(pane);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Pane createFacultyPane(String[] data) {
        Pane pane = new Pane();
        pane.setPrefHeight(44);
        pane.setPrefWidth(683);
        pane.setStyle("-fx-border-radius: 30px; -fx-background-radius: 30px; -fx-border-style: solid; -fx-background-color: #2C2C2C;");

        Rectangle highlight = new Rectangle(683, 44, Color.TRANSPARENT);
        pane.getChildren().add(highlight);

        Text idText = new Text(data[0]);
        idText.setLayoutX(10);
        idText.setLayoutY(28);

        Button nameButton = new Button(data[1]);
        nameButton.setLayoutX(200);
        nameButton.setLayoutY(6);
        nameButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 16;");
        nameButton.setOnAction(e -> openFacultyProfile(data));

        Text deptText = new Text(data[2]);
        deptText.setLayoutX(350);
        deptText.setLayoutY(28);

        Text emailText = new Text(data[3]);
        emailText.setLayoutX(500);
        emailText.setLayoutY(28);

        Text phoneText = new Text(data[4]);
        phoneText.setLayoutX(700);
        phoneText.setLayoutY(28);

        for (Text text : new Text[]{idText, deptText, emailText, phoneText}) {
            text.setStyle("-fx-fill: white; -fx-font-size: 16;");
        }

        pane.getChildren().addAll(idText, nameButton, deptText, emailText, phoneText);
        pane.setOnMouseClicked(event -> handleFacultySelection(pane, highlight));
        return pane;
    }

    private void handleFacultySelection(Pane pane, Rectangle highlight) {
        if (selectedFacultyPane != null) {
            Rectangle prevHighlight = (Rectangle) selectedFacultyPane.getChildren().get(0);
            prevHighlight.setFill(Color.TRANSPARENT);
        }

        selectedFacultyPane = pane;
        highlight.setFill(Color.rgb(255, 255, 255, 0.2));
    }

    private void handleDeleteButton() {
        if (selectedFacultyPane == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a faculty to delete.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this faculty?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            int selectedIndex = facultyListContainer.getChildren().indexOf(selectedFacultyPane);
            facultyListContainer.getChildren().remove(selectedFacultyPane);
            facultyDataList.remove(selectedIndex);
            facultyDataListStatic.remove(selectedIndex);
            saveFacultyListStatic();
            selectedFacultyPane = null;
        }
    }

    private void handleEditButton() {
        if (selectedFacultyPane == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a faculty to edit.");
            return;
        }

        int selectedIndex = facultyListContainer.getChildren().indexOf(selectedFacultyPane);
        String[] selectedFacultyData = facultyDataList.get(selectedIndex);
        navigateToEditFaculty(selectedFacultyData, selectedIndex);
    }

    private void navigateToEditFaculty(String[] facultyData, int index) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/authenticationframes/adminEditFaculty.fxml"));
            Parent root = loader.load();

            AdminEditFacultyController controller = loader.getController();
            controller.setFacultyData(facultyData, index);

            Stage stage = (Stage) editButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the Edit Faculty page.");
        }
    }


    public static void updateFacultyData(int index, String[] updatedData) {
        if (index >= 0 && index < facultyDataListStatic.size()) {
            facultyDataListStatic.set(index, updatedData);
            saveFacultyListStatic();
        }
    }

    private static void saveFacultyListStatic() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String[] faculty : facultyDataListStatic) {
                writer.write(String.join("\t", faculty));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void searchFaculty() {
        String query = searchField.getText().trim().toLowerCase();
        facultyListContainer.getChildren().clear();

        for (String[] faculty : facultyDataList) {
            if (faculty[0].toLowerCase().contains(query) || faculty[1].toLowerCase().contains(query)) {
                Pane pane = createFacultyPane(faculty);
                facultyListContainer.getChildren().add(pane);
            }
        }
    }

    private void openAddFacultyScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("adminAddFaculty.fxml"));
            Scene scene = new Scene(loader.load());

            Stage newStage = new Stage();
            newStage.setTitle("Add Faculty");
            newStage.setScene(scene);
            newStage.showAndWait();

            loadFaculty();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleBack() {
        SceneLoader.loadScene("adminMainMenu.fxml", "Admin Main Menu", backButton);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void openFacultyProfile(String[] facultyData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("adminViewFaculty.fxml"));
            Parent root = loader.load();

            AdminViewFacultyController controller = loader.getController();
            controller.setFacultyData(facultyData);

            // Get the current stage from any UI element, e.g. the backButton
            Stage currentStage = (Stage) backButton.getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Faculty Profile");
            currentStage.show(); // Optional, since the window is already visible

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open faculty profile.");
        }
    }

}
