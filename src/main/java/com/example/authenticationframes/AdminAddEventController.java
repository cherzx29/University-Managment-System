package com.example.authenticationframes;  // Package containing all JavaFX controllers

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;

public class AdminAddEventController {

    // File path where all event data will be stored
    private static final String FILE_PATH = "src/main/resources/data/File_Events.txt";

    // FXML fields from the Add Event UI
    @FXML private TextField eventNameField;
    @FXML private TextField eventCodeField;
    @FXML private TextField locationField;
    @FXML private TextField dateTimeField;
    @FXML private TextField capacityField;
    @FXML private TextField costField;
    @FXML private TextField descriptionField;
    @FXML private TextField enrollmentField;

    @FXML private Button addEventButton;
    @FXML private Button cancelButton;

    /**
     * This method is automatically called when the Add Event scene is loaded.
     * It sets up the button event handlers.
     */
    @FXML
    private void initialize() {
        System.out.println("AdminAddEventController initialized!");

        addEventButton.setOnAction(event -> addEvent());
        cancelButton.setOnAction(event -> closeWindow());
    }

    /**
     * Collects input, validates it, and saves the new event to File_Events.txt.
     */
    private void addEvent() {
        String eventName = eventNameField.getText().trim();
        String eventCode = eventCodeField.getText().trim();
        String location = locationField.getText().trim();
        String dateTime = dateTimeField.getText().trim();
        String capacity = capacityField.getText().trim();
        String cost = costField.getText().trim();
        String description = descriptionField.getText().trim();
        String enrollment = enrollmentField.getText().trim();

        // Validate that all fields are filled
        if (eventName.isEmpty() || eventCode.isEmpty() || location.isEmpty() || dateTime.isEmpty()
                || capacity.isEmpty() || cost.isEmpty() || description.isEmpty() || enrollment.isEmpty()) {
            System.err.println(" Error: All fields must be filled!");
            return;
        }

        // Format the event as a tab-separated line
        String newEventEntry = eventName + "\t" + eventCode + "\t" + location + "\t" + dateTime + "\t" +
                capacity + "\t" + cost + "\t" + description + "\t" + enrollment;

        // Write the event to the file
        try (FileWriter writer = new FileWriter(FILE_PATH, true)) {
            writer.write(newEventEntry + "\n");
            System.out.println(" Event added successfully: " + newEventEntry);
        } catch (IOException e) {
            System.err.println(" Error writing to file: " + e.getMessage());
        }

        // After saving, close this window and reopen event management
        closeWindow();
    }

    /**
     * Closes the current Add Event window and reopens the Event View.
     */
    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
//       loadAdminEventView();
    }

    /**
     * Loads the Admin Event View scene.
     */
    private void loadAdminEventView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/real/adminEventView.fxml"));
            Scene scene = new Scene(loader.load());

            Stage newStage = new Stage();
            newStage.setTitle("Event Management");
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            System.err.println(" Error loading adminEventView.fxml: " + e.getMessage());
        }
    }
}
