package com.example.authenticationframes;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AdminEditEventController {

    @FXML private TextField eventNameField;
    @FXML private TextField eventCodeField;
    @FXML private TextField locationField;
    @FXML private TextField dateTimeField;
    @FXML private TextField capacityField;
    @FXML private TextField costField;
    @FXML private TextField descriptionField;
    @FXML private TextField enrollmentField;

    @FXML private Button saveChangesButton;
    @FXML private Button cancelEditButton;
    @FXML private Button deleteEventButton;

    private String[] originalEventData; // The event line as it existed before editing
    private AdminEventViewController parentController;

    private static final String FILE_PATH = "src/main/resources/data/File_Events.txt";

    public void setOriginalEventData(String[] eventData) {
        this.originalEventData = eventData;

        // Prefill fields with existing data
        eventNameField.setText(eventData[0]);
        eventCodeField.setText(eventData[1]);
        locationField.setText(eventData[2]);
        dateTimeField.setText(eventData[3]);
        capacityField.setText(eventData[4]);
        costField.setText(eventData[5]);
        descriptionField.setText(eventData[6]);
        enrollmentField.setText(eventData[7]);
    }

    public void setParentController(AdminEventViewController controller) {
        this.parentController = controller;
    }

    @FXML
    private void initialize() {
        cancelEditButton.setOnAction(e -> closeWindow());
        saveChangesButton.setOnAction(e -> saveChanges());
        deleteEventButton.setOnAction(e -> deleteEvent());
    }

    private void saveChanges() {
        String updatedLine = String.join("\t",
                eventNameField.getText().trim(),
                eventCodeField.getText().trim(),
                locationField.getText().trim(),
                dateTimeField.getText().trim(),
                capacityField.getText().trim(),
                costField.getText().trim(),
                descriptionField.getText().trim(),
                enrollmentField.getText().trim()
        );

        String originalLine = String.join("\t", originalEventData);

        List<String> updatedLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().equals(originalLine)) {
                    updatedLines.add(updatedLine);
                } else {
                    updatedLines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String line : updatedLines) {
                writer.write(line);
                writer.newLine();
            }
            System.out.println("✅ Event updated successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (parentController != null) {
            parentController.loadEventList(); // Refresh the list
        }

        closeWindow();
    }

    private void deleteEvent() {
        String targetLine = String.join("\t", originalEventData);
        List<String> updatedLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
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

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String line : updatedLines) {
                writer.write(line);
                writer.newLine();
            }
            System.out.println("🗑️ Event deleted successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (parentController != null) {
            parentController.loadEventList(); // Refresh the list
        }

        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) cancelEditButton.getScene().getWindow();
        stage.close();
    }
}
