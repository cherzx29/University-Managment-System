package com.example.authenticationframes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class AdminEditCourseController {

    @FXML private TextField courseNameField;
    @FXML private TextField courseCodeField;
    @FXML private TextField sectionField;
    @FXML private TextField locationField;
    @FXML private TextField timeField;
    @FXML private TextField capacityField;
    @FXML private TextField examField;
    @FXML private TextField teacherField;

    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private Button deleteButton;

    private String[] originalCourseData;
    private AdminCourseDescriptionController parentController;
    private static final String FILE_PATH = "src/main/resources/data/File_Courses.txt";

    @FXML
    public void initialize() {
        System.out.println("AdminEditCourseController initialized");
        System.out.println("Fields status:");
        System.out.println("courseNameField: " + (courseNameField != null ? "OK" : "NULL"));
        System.out.println("courseCodeField: " + (courseCodeField != null ? "OK" : "NULL"));
        System.out.println("sectionField: " + (sectionField != null ? "OK" : "NULL"));
        System.out.println("locationField: " + (locationField != null ? "OK" : "NULL"));
        System.out.println("timeField: " + (timeField != null ? "OK" : "NULL"));
        System.out.println("capacityField: " + (capacityField != null ? "OK" : "NULL"));
        System.out.println("examField: " + (examField != null ? "OK" : "NULL"));
        System.out.println("teacherField: " + (teacherField != null ? "OK" : "NULL"));
    }

    public void setOriginalCourseData(String[] courseData) {
        if (courseData == null || courseData.length < 8) {
            showAlert("Error", "Invalid course data provided");
            return;
        }

        this.originalCourseData = courseData;

        // Safe null checks
        if (courseNameField != null) courseNameField.setText(courseData[0]);
        if (courseCodeField != null) courseCodeField.setText(courseData[1]);
        if (locationField != null) locationField.setText(courseData[2]);
        if (timeField != null) timeField.setText(courseData[3]);
        if (capacityField != null) capacityField.setText(courseData[4]);
        if (sectionField != null) sectionField.setText(courseData[5]);
        if (examField != null) examField.setText(courseData[6]);
        if (teacherField != null) teacherField.setText(courseData[7]);
    }

    public void setParentController(AdminCourseDescriptionController controller) {
        this.parentController = controller;
    }

    @FXML
    private void handleSaveChanges() {
        if (!validateFields()) {
            return;
        }
        saveChanges();
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    @FXML
    private void handleDelete() {
        deleteCourse();
    }

    private boolean validateFields() {
        if (courseNameField.getText().isEmpty() ||
                courseCodeField.getText().isEmpty() ||
                locationField.getText().isEmpty() ||
                timeField.getText().isEmpty()) {

            showAlert("Validation Error", "Please fill in all required fields");
            return false;
        }

        // Validate capacity is a number
        try {
            Integer.parseInt(capacityField.getText());
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Capacity must be a number");
            return false;
        }

        return true;
    }

    private void backupFile() throws IOException {
        File original = new File(FILE_PATH);
        File backup = new File(FILE_PATH + ".bak");
        Files.copy(original.toPath(), backup.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    private void saveChanges() {
        try {
            // Validate fields before proceeding
            if (!validateFields()) {
                return;
            }

            // Create the updated course line
            String updatedLine = String.join("\t",
                    courseNameField.getText().trim(),
                    courseCodeField.getText().trim(),
                    locationField.getText().trim(),
                    timeField.getText().trim(),
                    capacityField.getText().trim(),
                    sectionField.getText().trim(),
                    examField.getText().trim(),
                    teacherField.getText().trim()
            );

            List<String> lines = new ArrayList<>();
            boolean courseUpdated = false;

            // Read the file and replace the specific course
            try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("\t");
                    if (parts.length >= 2 && parts[1].equals(originalCourseData[1])) {
                        // Found the course to update (match by course code)
                        lines.add(updatedLine);
                        courseUpdated = true;
                    } else {
                        lines.add(line);
                    }
                }
            }

            // Ensure the course is updated, not duplicated
            if (!courseUpdated) {
                showAlert("Error", "Course not found for update.");
                return;
            }

            // Write back to file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            }

            // Refresh parent view
            if (parentController != null) {
                parentController.loadCourseList();
            }

            showAlert("Success", "Course updated successfully");
            closeWindow();

        } catch (IOException e) {
            showAlert("Error", "Failed to save changes: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void deleteCourse() {
        try {
            List<String> lines = new ArrayList<>();
            String targetLine = String.join("\t", originalCourseData);

            // Read all lines except the one to delete
            try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().equals(targetLine)) {
                        lines.add(line);
                    }
                }
            }

            // Write remaining lines back
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            }

            // Refresh parent view
            if (parentController != null) {
                parentController.loadCourseList();
            }

            showAlert("Success", "Course deleted successfully");
            closeWindow();

        } catch (IOException e) {
            showAlert("Error", "Failed to delete course: " + e.getMessage());
            e.printStackTrace();
        }
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

    public void handleSaveButtonClick(ActionEvent actionEvent) {
        handleSaveChanges();
    }
}