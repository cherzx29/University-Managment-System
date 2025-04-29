package com.example.authenticationframes;

import com.example.real.utils.SceneLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AdminSubjectListController {

    private static final String FILE_PATH = "src/main/resources/data/File_Subjects.txt";

    @FXML private VBox subjectListContainer;
    @FXML private TextField searchField;
    @FXML private Button searchButton;
    @FXML private Button addSubjectButton;
    @FXML private ScrollPane subjectScrollPane;
    @FXML private Button editSubjectButton;
    @FXML private Button backButton;

    private List<String> subjectLines = new ArrayList<>();

    @FXML
    private void initialize() {
        loadSubjects();

        if (addSubjectButton != null) addSubjectButton.setOnAction(event -> handleAddSubject());
        if (editSubjectButton != null) editSubjectButton.setOnAction(event -> handleEditSubject());
        if (searchButton != null) searchButton.setOnAction(event -> searchSubjects());
        if (backButton != null) backButton.setOnAction(event -> handleBack());
    }

    public void loadSubjects() {
        subjectListContainer.getChildren().clear();
        subjectLines.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                subjectLines.add(line);
                String[] parts = line.split("\t");
                if (parts.length < 2) continue;

                String subjectCode = parts[0];
                String subjectName = parts[1];

                Label subjectLabel = new Label(subjectCode + " - " + subjectName);
                subjectLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: black; -fx-background-color: #D3D3D3; -fx-padding: 5px;");
                addContextMenu(subjectLabel, line);
                subjectListContainer.getChildren().add(subjectLabel);
            }
        } catch (IOException e) {
            System.err.println(" Error reading file: " + e.getMessage());
        }
    }

    private void addContextMenu(Label subjectLabel, String subjectLine) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem editItem = new MenuItem("Edit");
        MenuItem deleteItem = new MenuItem("Delete");

        // Delete action
        deleteItem.setOnAction(e -> {
            int confirm = JOptionPane.showConfirmDialog(null, "Delete this subject?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                subjectLines.remove(subjectLine);
                saveSubjects();
                loadSubjects();
                JOptionPane.showMessageDialog(null, "Subject deleted successfully.");
            }
        });

        // Edit action
        editItem.setOnAction(e -> {
            String[] parts = subjectLine.split("\t");
            if (parts.length < 2) {
                JOptionPane.showMessageDialog(null, "Invalid subject format. Cannot edit.");
                return;
            }

            String newCode = JOptionPane.showInputDialog("Edit Subject Code:", parts[0]);
            String newName = JOptionPane.showInputDialog("Edit Subject Name:", parts[1]);

            if (newCode != null && !newCode.trim().isEmpty() && newName != null && !newName.trim().isEmpty()) {
                String updatedLine = newCode.trim() + "\t" + newName.trim();
                int index = subjectLines.indexOf(subjectLine);
                if (index >= 0) {
                    subjectLines.set(index, updatedLine);
                    saveSubjects();
                    loadSubjects();
                    JOptionPane.showMessageDialog(null, "Subject updated successfully.");
                }
            }
        });

        contextMenu.getItems().addAll(editItem, deleteItem);

        subjectLabel.setOnContextMenuRequested((ContextMenuEvent event) -> {
            contextMenu.show(subjectLabel, event.getScreenX(), event.getScreenY());
        });
    }

    private void saveSubjects() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String subjectLine : subjectLines) {
                writer.write(subjectLine);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println(" Error saving subjects: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddSubject() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/authenticationframes/adminAddSubject.fxml"));
            Scene scene = new Scene(loader.load());
            Stage newStage = new Stage();
            newStage.setTitle("Add Subject");
            newStage.setScene(scene);
            newStage.showAndWait();
            loadSubjects();
        } catch (IOException e) {
            System.err.println(" Error loading adminAddSubject.fxml: " + e.getMessage());
        }
    }

    @FXML
    private void handleEditSubject() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/authenticationframes/adminEditSubject.fxml"));
            Scene scene = new Scene(loader.load());
            Stage editStage = new Stage();
            editStage.setTitle("Edit Subject");
            editStage.setScene(scene);
            editStage.show();
        } catch (IOException e) {
            System.err.println(" Error loading adminEditSubject.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void openDeleteSubjectPopup(String[] subjectData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/authenticationframes/adminDeleteSubject.fxml"));
            Scene scene = new Scene(loader.load());

            AdminDeleteSubjectController controller = loader.getController();
            controller.setSubjectToDelete(subjectData);
            controller.setParentController(this);

            Stage stage = new Stage();
            stage.setTitle("Delete Subject");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void searchSubjects() {
        String query = searchField.getText().trim().toLowerCase();
        subjectListContainer.getChildren().clear();

        for (String line : subjectLines) {
            String[] parts = line.split("\t");
            if (parts.length < 2) continue;
            String subjectCode = parts[0];
            String subjectName = parts[1];

            if (subjectCode.toLowerCase().contains(query) || subjectName.toLowerCase().contains(query)) {
                Label subjectLabel = new Label(subjectCode + " - " + subjectName);
                subjectLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: black; -fx-background-color: #D3D3D3; -fx-padding: 5px;");
                addContextMenu(subjectLabel, line);
                subjectListContainer.getChildren().add(subjectLabel);
            }
        }
    }

    @FXML
    private void handleBack() {
        SceneLoader.loadScene("adminMainMenu.fxml", "Admin Main Menu", backButton);
    }
}
