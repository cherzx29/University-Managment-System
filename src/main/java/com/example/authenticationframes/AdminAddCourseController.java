package com.example.authenticationframes;  // Package containing all JavaFX controllers

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AdminAddCourseController {

    // File path where all event data will be stored
    private static final String FILE_PATH = "File_Courses.txt";

    // FXML fields from the Add Event UI
    @FXML private TextField courseNameField;
    @FXML private TextField courseCodeField;
    @FXML private TextField locationField;
    @FXML private TextField TimeField;
    @FXML private TextField capacityField;
    @FXML private TextField sectionField;
    @FXML private TextField examField;
    @FXML private TextField teacherField;

    @FXML private Button addCourseButton;
    @FXML private Button cancelButton;

    /**
     * This method is automatically called when the Add Event scene is loaded.
     * It sets up the button event handlers.
     */
    @FXML
    private void initialize() {
        System.out.println("AdminAddEventController initialized!");

        addCourseButton.setOnAction(event -> addEvent());
        cancelButton.setOnAction(event -> closeWindow());
    }

    /**
     * Collects input, validates it, and saves the new event to File_Events.txt.
     */
    private void addEvent() {
        String eventName = courseNameField.getText().trim();
        String eventCode = courseCodeField.getText().trim();
        String location = locationField.getText().trim();
        String dateTime = TimeField.getText().trim();
        String capacity = capacityField.getText().trim();
        String cost = sectionField.getText().trim();
        String description = examField.getText().trim();
        String enrollment = teacherField.getText().trim();

        if (eventName.isEmpty() || eventCode.isEmpty() || location.isEmpty() || dateTime.isEmpty()
                || capacity.isEmpty() || cost.isEmpty() || description.isEmpty() || enrollment.isEmpty()) {
            System.err.println(" Error: All fields must be filled!");
            return;
        }

        String newEventEntry = eventName + "\t" + eventCode + "\t" + location + "\t" + dateTime + "\t" +
                capacity + "\t" + cost + "\t" + description + "\t" + enrollment;

        try (FileWriter writer = new FileWriter(FILE_PATH, true)) {
            // Add a newline before writing if file isn't empty
            if (new File(FILE_PATH).length() > 0) {
                writer.write("\n"); // Ensure we start on a new line
            }
            writer.write(newEventEntry); // No extra newline here (added above)
            System.out.println(" Event added successfully: " + newEventEntry);
        } catch (IOException e) {
            System.err.println(" Error writing to file: " + e.getMessage());
        }

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
    private void loadAdminCourseView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/real/adminCourseDesctription.fxml"));
            Scene scene = new Scene(loader.load());

            Stage newStage = new Stage();
            newStage.setTitle("Course Management");
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            System.err.println(" Error loading adminCourseDescription.fxml: " + e.getMessage());
        }
    }
}
