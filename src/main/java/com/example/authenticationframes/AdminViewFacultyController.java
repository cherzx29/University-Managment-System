package com.example.authenticationframes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminViewFacultyController {

    // Top name banner
    @FXML private Text nameText;

    // Profile detail section
    @FXML private Text degreeText;
    @FXML private Text researchText;
    @FXML private Text officeText;
    @FXML private Text phoneText;
    @FXML private Text emailText;

    @FXML private Button backButton;

    @FXML
    private void initialize() {
        if (backButton != null) {
            backButton.setOnAction(event -> goBack());
        } else {
            System.err.println("⚠ backButton is not injected. Check fx:id in FXML and ensure it's on the <Button> element.");
        }
    }

    public void setFacultyData(String[] data) {
        if (data == null || data.length < 6) {
            System.err.println("⚠ Invalid faculty data received in AdminViewFacultyController");
            return;
        }

        nameText.setText(data[1]);
        officeText.setText(data[2]);
        degreeText.setText(data[3]);
        emailText.setText(data[4]);
        researchText.setText(data[5]);
        phoneText.setText("(123) 456-7890"); // static placeholder
    }

    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/real/adminFacultyList.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) backButton.getScene().getWindow();
            Scene currentScene = stage.getScene();

            // Reuse dimensions
            Scene newScene = new Scene(root, currentScene.getWidth(), currentScene.getHeight());
            stage.setScene(newScene);
            stage.setTitle("Faculty List");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("❌ Failed to load adminFacultyList.fxml");
        }
    }
}
