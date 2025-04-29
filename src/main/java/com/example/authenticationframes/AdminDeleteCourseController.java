package com.example.authenticationframes;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDeleteCourseController {

    @FXML
    private Button deleteButton;

    @FXML
    private Button cancelButton;

    private String[] courseToDelete;
    private AdminCourseDescriptionController parentController;

    // Called from AdminEventViewController
    public void setCourseToDelete(String[] courseData) {
        this.courseToDelete = courseData;
    }

    public void setParentController(AdminCourseDescriptionController controller) {
        this.parentController = controller;
    }

    @FXML
    private void initialize() {
        deleteButton.setOnAction(e -> deleteCourseFromFile());
        cancelButton.setOnAction(e -> closeWindow());
    }

    private void deleteCourseFromFile() {
        File file = new File("src/main/resources/data/File_Courses.txt");
        List<String> updatedLines = new ArrayList<>();
        String targetLine = String.join("\t", courseToDelete);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().equals(targetLine)) {
                    updatedLines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine);
                writer.newLine();
            }

            System.out.println("✅ Course deleted successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Refresh parent event list
        if (parentController != null) {
            parentController.loadCourseList();
        }

        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) deleteButton.getScene().getWindow();
        stage.close();
    }
}
