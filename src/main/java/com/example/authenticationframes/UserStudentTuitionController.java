package com.example.authenticationframes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class UserStudentTuitionController {
    @FXML private TabPane tuitionTabPane;
    @FXML private Text userNameText;
    @FXML private Text programText;
    @FXML private Text firstCharge;
    @FXML private Text totalCharge;
    @FXML private Text programText1; // For F25 tab
    @FXML private Text firstCharge1; // For F25 tab
    @FXML private Text totalCharge1; // For F25 tab
    @FXML private Button backButton;
    @FXML private Tab W25;
    @FXML private Tab F25;

    private String currentUserEmail;

    public void setCurrentUserEmail(String email) {
        this.currentUserEmail = email;
        loadUserData();
    }

    @FXML
    private void handleBack() {
        try {
            // Load the previous scene (assuming it's UserDashboard.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserMainMenu.fxml"));
            Parent root = loader.load();

            // Pass the current user email to the dashboard controller
            UserMainMenuController dashboardController = loader.getController();
            dashboardController.setCurrentUserEmail(currentUserEmail);

            // Get the current stage
            Stage stage = (Stage) backButton.getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading dashboard: " + e.getMessage());
            showErrorAlert("Failed to navigate back", e);
        }
    }

    private void loadUserData() {
        try {
            File file = new File("loginTextFile.txt");
            if (!file.exists()) {
                System.err.println("Login file not found");
                return;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(", ");
                    if (parts.length > 6 && parts[4].equalsIgnoreCase(currentUserEmail)) {
                        // Set user name
                        String userNameTextValue = parts[0] + " Tuition";
                        userNameText.setText(userNameTextValue);

                        // Extract program name (everything after last hyphen)
                        String programField = parts[6];
                        String programName;
                        int lastHyphenIndex = programField.lastIndexOf("-");
                        if (lastHyphenIndex >= 0) {
                            programName = programField.substring(lastHyphenIndex + 1).trim();
                        } else {
                            programName = programField.trim(); // if no hyphen, use whole string
                        }

                        String programTextValue = programName + " Tuition";
                        programText.setText(programTextValue);
                        programText1.setText(programTextValue);

                        // Set tuition charges based on degree level
                        String degreeLevel = parts[5]; // "Undergraduate" or "Graduate"
                        if (degreeLevel.equalsIgnoreCase("Undergraduate")) {
                            firstCharge.setText("$4000");
                            totalCharge.setText("$5000"); // $4000 + $1000 compulsory fees
                            firstCharge1.setText("$4000");
                            totalCharge1.setText("$5000");
                        } else if (degreeLevel.equalsIgnoreCase("Graduate")) {
                            firstCharge.setText("$3000");
                            totalCharge.setText("$4000"); // $3000 + $1000 compulsory fees
                            firstCharge1.setText("$3000");
                            totalCharge1.setText("$4000");
                        }

                        // Set active tab
                        String semester = parts.length > 7 ? parts[7] : "W25";
                        if (semester.equals("W25")) {
                            tuitionTabPane.getSelectionModel().select(W25);
                        } else if (semester.equals("F25")) {
                            tuitionTabPane.getSelectionModel().select(F25);
                        }
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        // Initialization if needed
    }

    public void setUserFullName(String fullName) {
        // Implementation if needed
    }

    private void showErrorAlert(String message, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }
}