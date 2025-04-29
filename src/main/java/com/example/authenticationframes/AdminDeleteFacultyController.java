package com.example.authenticationframes;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for confirming and handling deletion of a faculty member.
 */

public class AdminDeleteFacultyController {

    private static final String FILE_PATH = "src/main/resources/data/File_Faculty.txt";

    @FXML private Text facultyNameText;
    @FXML private Button confirmDeleteButton;
    @FXML private Button cancelButton;

    // The name of the faculty member to delete (passed in from previous screen)
    private String facultyNameToDelete;

    @FXML
    private void initialize() {
        confirmDeleteButton.setOnAction(e -> deleteFaculty());
        cancelButton.setOnAction(e -> closeWindow());
    }

    public void setFacultyName(String name) {
        this.facultyNameToDelete = name;
        facultyNameText.setText(name);
    }

    private void deleteFaculty() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            showAlert("Error", "Faculty file not found!");
            return;
        }

        List<String> updatedLines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(facultyNameToDelete)) {
                    found = true;
                    continue; // Skip this line
                }
                updatedLines.add(line);
            }
        } catch (IOException e) {
            showAlert("Error", "Error reading file: " + e.getMessage());
            return;
        }

        if (!found) {
            showAlert("Warning", "Faculty not found!");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : updatedLines) {
                writer.write(line);
                writer.newLine();
            }
            showAlert("Success", "Faculty deleted successfully.");
        } catch (IOException e) {
            showAlert("Error", "Error writing to file: " + e.getMessage());
        }

        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
