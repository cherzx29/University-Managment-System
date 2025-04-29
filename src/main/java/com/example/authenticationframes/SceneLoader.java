package com.example.real.utils;  // This package stores utility/helper classes for the project.

import javafx.fxml.FXMLLoader;   // Imports the FXMLLoader, which helps load UI layouts from .fxml files.
import javafx.scene.Parent;      // Parent represents the root node of our scene (the main UI container).
import javafx.scene.Scene;       // Scene represents the full window containing UI elements.
import javafx.scene.control.Button;  // Button is used to get the current window.
import javafx.stage.Stage;       // Stage represents the application window.
import java.io.IOException;      // Handles input/output errors (e.g., file not found).

/**
 * The SceneLoader class is responsible for handling scene transitions across the application.
 * Instead of writing scene-changing logic in multiple places, this class provides a single
 * method to load and switch between different screens, improving code organization and efficiency.
 */
public class SceneLoader {

    /**
     * This method loads and switches to a new scene (screen) when a button is clicked.
     *
     * @param fxmlFile The name of the FXML file that defines the new screen layout.
     * @param title The title of the new window.
     * @param button The button that triggered the scene change (used to get the current window).
     */
    public static void loadScene(String fxmlFile, String title, Button button) {
        try {
            // Print a message to the console to indicate that we are attempting to load a new scene.
            System.out.println("Attempting to load FXML: " + fxmlFile);

            // Create an FXMLLoader instance to load the UI from the specified FXML file.
            FXMLLoader loader = new FXMLLoader(SceneLoader.class.getResource("/com/example/real/" + fxmlFile));

            // Load the FXML file and store the root node of the scene
            // root holds the entire UI structure defined in the FXML file.
            // It acts as the starting point (aka "root node") of the JavaFX scene.
            Parent root = loader.load();

            // Get the current window (Stage) using the button that was clicked.
            Stage stage = (Stage) button.getScene().getWindow();

            // If the stage is null, print an error message and stop the function.
            if (stage == null) {
                System.out.println("Error: Stage is NULL!");
                return;
            }

            // Set the new scene title (shown at the top of the window).
            stage.setTitle(title);

            // Create a new scene with the loaded UI and apply it to the window.
            stage.setScene(new Scene(root));

            // Make the updated scene visible to the user.
            stage.show();

            // Print a success message to the console to confirm the scene change.
            System.out.println("Scene changed successfully to: " + fxmlFile);
        } catch (IOException e) {
            // If an error occurs (e.g., file not found), print the error details.
            e.printStackTrace();
            System.out.println("Error: Cannot load " + fxmlFile);
        }
    }
}
