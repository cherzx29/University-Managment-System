package com.example.authenticationframes;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDeleteEventController {

    @FXML
    private Button deleteButton;

    @FXML
    private Button cancelButton;

    private String[] eventToDelete;
    private AdminEventViewController parentController;

    // Called from AdminEventViewController
    public void setEventToDelete(String[] eventData) {
        this.eventToDelete = eventData;
    }

    public void setParentController(AdminEventViewController controller) {
        this.parentController = controller;
    }

    @FXML
    private void initialize() {
        deleteButton.setOnAction(e -> deleteEventFromFile());
        cancelButton.setOnAction(e -> closeWindow());
    }

    private void deleteEventFromFile() {
        File file = new File("src/main/resources/data/File_Events.txt");
        List<String> updatedLines = new ArrayList<>();
        String targetLine = String.join("\t", eventToDelete);

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

            System.out.println("✅ Event deleted successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Refresh parent event list
        if (parentController != null) {
            parentController.loadEventList();
        }

        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) deleteButton.getScene().getWindow();
        stage.close();
    }


}
