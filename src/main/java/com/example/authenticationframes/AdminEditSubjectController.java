package com.example.authenticationframes;  // This package contains all JavaFX controllers related to subject management.

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminEditSubjectController {

    //  FXML elements linked from the Admin Edit Subject UI.
    @FXML private Button cancelButton; // Button to cancel editing and go back to the subject list.
    @FXML private Button deleteSubjectButton; // Button to delete a subject from the system.

    /**
     * This method is automatically called when the scene loads.
     * It initializes event listeners for the buttons and ensures they work correctly.
     */
    @FXML
    private void initialize() {
        System.out.println(" AdminEditSubjectController initialized!");

        //  Attach an event listener to the Cancel button (if it exists).
        if (cancelButton != null) {
            cancelButton.setOnAction(event -> handleCancel());
        } else {
            System.out.println(" Warning: cancelButton is null. Check FXML file.");
        }

        //  Attach an event listener to the Delete button (if it exists).
        if (deleteSubjectButton != null) {
            deleteSubjectButton.setOnAction(event -> handleDeleteSubject());
        } else {
            System.out.println(" Warning: deleteSubjectButton is null. Check FXML file.");
        }
    }

    /**
     * Handles the Cancel button click event.
     * Closes the current window, returning the user to the Admin Subject List.
     */
    @FXML
    private void handleCancel() {
        System.out.println(" Cancel button clicked! Closing window...");

        //  Get the current window (stage) and close it.
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Handles the Delete button click event.
     * Opens the Admin Delete Subject screen, allowing the admin to remove a subject.
     */
    @FXML
    private void handleDeleteSubject() {
        System.out.println(" Delete Subject button clicked! Opening Admin Delete Subject...");

        try {
            //  Load the Admin Delete Subject screen from the FXML file.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/real/adminDeleteSubject.fxml"));
            Scene scene = new Scene(loader.load());

            //  Create a new window (stage) to display the delete subject screen.
            Stage deleteStage = new Stage();
            deleteStage.setTitle("Delete Subject");
            deleteStage.setScene(scene);
            deleteStage.show();
        } catch (IOException e) {
            System.err.println(" Error loading adminDeleteSubject.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
