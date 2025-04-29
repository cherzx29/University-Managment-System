package com.example.authenticationframes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Controller for adding and editing faculty members in the Admin panel.
 */

//Nikita
public class AdminAddFacultyController {

    private static final String FILE_PATH = "loginTextFile.txt";

    @FXML private TextField nameField;
    @FXML private TextField officeField;
    @FXML private TextField degreeField;
    @FXML private TextField researchField;
    @FXML private TextField emailField;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private boolean isEditMode = false;
    private String[] originalFacultyData;

    @FXML
    private void initialize() {
        saveButton.setOnAction(event -> handleSave());
        cancelButton.setOnAction(event -> handleCancel());
    }

    public void setFacultyData(String[] facultyData) {
        if (facultyData.length >= 5) {
            isEditMode = true;
            originalFacultyData = facultyData;

            nameField.setText(facultyData[1]);
            officeField.setText(facultyData[2]);
            emailField.setText(facultyData[3]);
            degreeField.setText(facultyData[4]);
            researchField.setText(facultyData.length >= 6 ? facultyData[5] : "");
        }
    }

    private void handleSave() {
        String name = nameField.getText().trim();
        String office = officeField.getText().trim();
        String degree = degreeField.getText().trim();
        String research = researchField.getText().trim();
        String email = emailField.getText().trim();

        if (name.isEmpty() || office.isEmpty() || degree.isEmpty() || research.isEmpty() || email.isEmpty()) {
            showAlert("Error", "All fields must be filled!");
            return;
        }

        if (isEditMode) {
            updateFacultyInFile(name, office, email, degree, research);
        } else {
            addNewFaculty(name, office, email, degree, research);
        }

        // ✅ Close the window after saving
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }


    private void addNewFaculty(String name, String office, String email, String degree, String research) {
        String id = generateFacultyId();
        String entry = String.join("\t", id, name, office, email, degree, research);

        if (!confirmAction("Confirm Save", "Are you sure you want to add this faculty?\n\n" + entry)) {
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(entry);
            writer.newLine();
            showAlert("Success", "Faculty added successfully!");
        } catch (IOException e) {
            showAlert("Error", "Failed to save faculty: " + e.getMessage());
        }
    }

    private void updateFacultyInFile(String name, String office, String email, String degree, String research) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));
            String facultyId = originalFacultyData[0];
            boolean updated = false;

            for (int i = 0; i < lines.size(); i++) {
                String[] parts = lines.get(i).split("\t");
                if (parts.length > 0 && parts[0].equals(facultyId)) {
                    String updatedLine = String.join("\t", facultyId, name, office, email, degree, research);
                    lines.set(i, updatedLine);
                    updated = true;
                    break;
                }
            }

            if (updated) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
                    for (String line : lines) {
                        writer.write(line);
                        writer.newLine();
                    }
                }
                showAlert("Success", "Faculty updated successfully!");
            } else {
                showAlert("Error", "Faculty entry not found for update.");
            }

        } catch (IOException e) {
            showAlert("Error", "An error occurred while updating the faculty: " + e.getMessage());
        }
    }

    private void refreshFacultyView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("src/main/resources/adminFacultyList.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Failed to reload faculty list view: " + e.getMessage());
        }
    }

    private String generateFacultyId() {
        return "FAC" + System.currentTimeMillis();
    }

    @FXML
    private void handleCancel() {
        ((Stage) cancelButton.getScene().getWindow()).close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean confirmAction(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait().filter(btn -> btn == ButtonType.OK).isPresent();
    }
}
