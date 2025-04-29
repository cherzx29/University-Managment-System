package com.example.authenticationframes;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDeleteSubjectController {

    @FXML
    private Button deleteButton;

    @FXML
    private Button cancelButton;

    private String[] subjectToDelete;
    private AdminSubjectListController parentController;

    // Setter method to receive subject info from the list controller
    public void setSubjectToDelete(String[] subjectData) {
        this.subjectToDelete = subjectData;
    }

    public void setParentController(AdminSubjectListController controller) {
        this.parentController = controller;
    }

    @FXML
    private void initialize() {
        deleteButton.setOnAction(e -> deleteSubjectFromFile());
        cancelButton.setOnAction(e -> closeWindow());
    }

    private void deleteSubjectFromFile() {
        File file = new File("src/main/resources/data/File_Subjects.txt");
        List<String> updatedLines = new ArrayList<>();
        String targetLine = String.join("\t", subjectToDelete);

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

            System.out.println("✅ Subject deleted successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Refresh the subject list in the parent controller
        if (parentController != null) {
            parentController.loadSubjects();
        }

        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) deleteButton.getScene().getWindow();
        stage.close();
    }
}
