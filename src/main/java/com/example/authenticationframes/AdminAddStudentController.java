package com.example.authenticationframes;

import javax.swing.JOptionPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.Parent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class AdminAddStudentController {

    @FXML
    private TextField studentNameField;

    @FXML
    private TextField studentIDField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField telephoneField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField semesterField;

    @FXML
    private TextField programField;

    @FXML
    private TextField academicLevelField;

    @FXML
    private Button saveChangesButton;

    @FXML
    private Button cancelButton;

    private boolean isEditMode = false;
    private String[] originalStudentData;

    @FXML
    public void initialize() {
        // Any setup logic if needed
    }

    public void setStudentData(String[] studentData) {
        if (studentData.length >= 7) {
            isEditMode = true;
            originalStudentData = studentData;

            studentNameField.setText(studentData[0]);
            studentIDField.setText(studentData[1]);
            addressField.setText(studentData[2]);
            telephoneField.setText(studentData[3]);
            emailField.setText(studentData[4]);
            academicLevelField.setText(studentData[5]);
            programField.setText(studentData[6]);
            semesterField.setText(studentData[7]);
        }
    }

    @FXML
    private void handleSaveChanges() {
        try {
            System.out.println("Save Changes button clicked.");

            // Get values from input fields
            String name = studentNameField.getText().trim();
            String studentID = studentIDField.getText().trim();
            String address = addressField.getText().trim();
            String telephone = telephoneField.getText().trim();
            String email = emailField.getText().trim();
            String academicLevel = academicLevelField.getText().trim();
            String program = programField.getText().trim();
            String semester = semesterField.getText().trim();

            // Check if any field is empty
            if (name.isEmpty() || studentID.isEmpty() || address.isEmpty() || telephone.isEmpty() ||
                    email.isEmpty() || academicLevel.isEmpty() || program.isEmpty() || semester.isEmpty()) {
                showAlert("Error", "All fields must be filled.");
                return;
            }

            // Show confirmation dialog
            int response = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to save the following student's information?\n\n" +
                            "Name: " + name + "\nStudent ID: " + studentID + "\nAddress: " + address +
                            "\nTelephone: " + telephone + "\nEmail: " + email +
                            "\nAcademic Level: " + academicLevel + "\nProgram: " + program + "\nSemester: " + semester,
                    "Confirm Save", JOptionPane.YES_NO_OPTION);

            if (response != JOptionPane.YES_OPTION) {
                return;
            }

            String generatedPassword = codeEmailGenerator.generateRandomPassword();
            System.out.println("Generated Password: " + generatedPassword);

            String studentData = String.join(", ", name, studentID, address, telephone, email,
                    academicLevel, program, semester, generatedPassword);

            Path filePath = Paths.get("loginTextFile.txt");
            System.out.println("Attempting to write to: " + filePath.toAbsolutePath());

            if (isEditMode) {
                updateStudentInFile(studentData);
            } else {
                try {
                    // Check if file exists
                    if (!Files.exists(filePath)) {
                        Files.createFile(filePath);
                        System.out.println("Created new file");
                    }

                    try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.APPEND)) {
                        writer.write(studentData);
                        writer.newLine();
                        writer.flush();
                        System.out.println("New student successfully written to file.");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert("Error", "Failed to write student data. Error: " + e.getMessage());
                    return;
                }
            }

            showAlert("Success", isEditMode ? "Student updated successfully!" : "Student added successfully!");
            navigateToStudentList();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while saving the student: " + e.getMessage());
        }
    }

    private void updateStudentInFile(String updatedStudentData) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("loginTextFile.txt"));

            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).startsWith(String.join(", ", originalStudentData))) {
                    lines.set(i, updatedStudentData);
                    break;
                }
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("loginTextFile.txt"))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
                System.out.println("Student data successfully updated in file.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while updating the student data.");
        }
    }

    @FXML
    private void handleCancel() {
        studentNameField.clear();
        studentIDField.clear();
        addressField.clear();
        telephoneField.clear();
        emailField.clear();
        semesterField.clear();
        programField.clear();
        academicLevelField.clear();
    }

    private void navigateToStudentList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("adminStudentList.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) saveChangesButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the student list.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}