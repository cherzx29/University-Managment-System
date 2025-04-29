package com.example.authenticationframes;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AdminEditFacultyController {

    @FXML private TextField nameField;
    @FXML private TextField officeField;
    @FXML private TextField degreeField;
    @FXML private TextField emailField;
    @FXML private TextField researchField;

    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private String[] originalFacultyData;
    private int facultyIndex = -1; // in case you want to use it later

    private static final String FILE_PATH = "src/main/resources/data/File_Faculty.txt";

    // ✅ Called from AdminFacultyListController
    public void setFacultyData(String[] data, int index) {
        this.originalFacultyData = data;
        this.facultyIndex = index;

        nameField.setText(data[1]);
        officeField.setText(data[2]);
        emailField.setText(data[3]);
        degreeField.setText(data[4]);
        researchField.setText(data[5]);
    }

    @FXML
    public void initialize() {
        saveButton.setOnAction(event -> handleSaveChanges());
        cancelButton.setOnAction(event -> closeWindow());
    }

    private void handleSaveChanges() {
        if (!validateFields()) {
            showAlert("Validation Error", "Please fill in all fields.");
            return;
        }

        String updatedLine = String.join("\t",
                originalFacultyData[0],
                nameField.getText().trim(),
                officeField.getText().trim(),
                emailField.getText().trim(),
                degreeField.getText().trim(),
                researchField.getText().trim()
        );

        try {
            List<String> lines = new ArrayList<>();
            boolean facultyUpdated = false;

            try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("\t");
                    if (parts.length > 0 && parts[0].equals(originalFacultyData[0])) {
                        lines.add(updatedLine);
                        facultyUpdated = true;
                    } else {
                        lines.add(line);
                    }
                }
            }

            if (!facultyUpdated) {
                showAlert("Error", "Faculty not found for update.");
                return;
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            }

            showAlert("Success", "Faculty updated successfully.");
            closeWindow();

        } catch (IOException e) {
            showAlert("Error", "Failed to save changes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean validateFields() {
        return !nameField.getText().trim().isEmpty()
                && !officeField.getText().trim().isEmpty()
                && !emailField.getText().trim().isEmpty()
                && !degreeField.getText().trim().isEmpty()
                && !researchField.getText().trim().isEmpty();
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
