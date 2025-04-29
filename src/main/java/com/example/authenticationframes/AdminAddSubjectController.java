package com.example.authenticationframes;  // This package contains all JavaFX controllers for managing application features.

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;

public class AdminAddSubjectController {

    //  File path where all subject data is stored
    private static final String FILE_PATH = "src/main/resources/data/File_Subjects.txt";

    @FXML private TextField subjectNameField;  // Subject Name input field
    @FXML private TextField subjectCodeField;  // Subject Code input field
    @FXML private Button addSubjectButton;     // "Add Subject" button
    @FXML private Button cancelButton;         // "Cancel" button

    /**
     * This method is automatically called when the scene loads.
     * It initializes event listeners for buttons.
     */
    @FXML
    private void initialize() {
        System.out.println(" AdminAddSubjectController initialized!");

        // When the "Add Subject" button is clicked, call handleAddSubject()
        addSubjectButton.setOnAction(event -> handleAddSubject());

        // When the "Cancel" button is clicked, close the window
        cancelButton.setOnAction(event -> handleCancel());
    }

    /**
     * Handles the process of adding a new subject.
     * It validates input, formats the subject details, and writes them to the file.
     */
    @FXML
    private void handleAddSubject() {
        // Get user input from text fields and remove extra spaces
        String subjectName = subjectNameField.getText().trim();
        String subjectCode = subjectCodeField.getText().trim();

        //  Validate that both fields are filled before proceeding
        if (subjectName.isEmpty() || subjectCode.isEmpty()) {
            System.err.println(" Error: All fields must be filled!");
            return; // Stop execution if validation fails
        }

        //  Format the subject data into a tab-separated string for storage
        String newSubjectEntry = subjectCode + "\t" + subjectName;

        //  Append the new subject entry to the file
        try (FileWriter writer = new FileWriter(FILE_PATH, true)) { // 'true' means append mode (don't overwrite)
            writer.write(newSubjectEntry + "\n");  // Write subject details followed by a new line
            System.out.println(" Subject added successfully: " + newSubjectEntry);
        } catch (IOException e) {
            System.err.println(" Error writing to file: " + e.getMessage());
        }

        //  Close the Add Subject window and return to the Subject List
        closeWindow();
    }

    /**
     * Closes the current "Add Subject" window and loads the subject management screen.
     */
    @FXML
    private void handleCancel() {
        closeWindow(); // Close the window without saving
    }

    /**
     * Closes the current window and reloads the subject management scene.
     * This ensures the subject list is updated with the new entry.
     */
    private void closeWindow() {
        // Get the current window (stage) using the Cancel button
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close(); // Close the window

        //  Reload the Subject Management screen so the new subject appears in the list
        try {
            //  Load the adminSubjectList.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/real/adminSubjectList.fxml"));

            //  Create a new scene using the loaded FXML layout
            Scene scene = new Scene(loader.load());

            //  Create a new stage (window) for the Subject Management page
            Stage newStage = new Stage();
            newStage.setTitle("Subject Management"); // Set window title
            newStage.setScene(scene); // Set the new scene
            newStage.show(); // Show the window
        } catch (IOException e) {
            System.err.println(" Error loading adminSubjectList.fxml: " + e.getMessage());
        }
    }
}
