package com.example.authenticationframes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserPersonalInformationController implements Initializable {

    @FXML private TextField majorField, studentIDField, AddressField,
            TelephoneField, EmailAddressField, SemField, AcademicField;
    @FXML private Text nameText;
    @FXML private Button backButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setFieldsNonEditable();
    }

    private String currentUserEmail;
    public void setCurrentUserEmail(String email) {
        this.currentUserEmail = email;
        loadStudentData(email.trim().toLowerCase());
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


    private void loadStudentData(String email) {
        clearFields();

        try (BufferedReader reader = new BufferedReader(new FileReader("loginTextFile.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",\\s*");
                if (data.length >= 9 && data[4].trim().equalsIgnoreCase(email)) {
                    displayStudentData(data);
                    return;
                }
            }
            setDefaultValues();
        } catch (Exception e) {
            setDefaultValues();
        }
    }

    private void displayStudentData(String[] data) {
        nameText.setText(data[0].trim());
        majorField.setText(data[6].trim());
        studentIDField.setText(data[1].trim());
        AddressField.setText(data[2].trim());
        TelephoneField.setText(data[3].trim());
        EmailAddressField.setText(data[4].trim());
        SemField.setText(data[7].trim());
        AcademicField.setText(data[5].trim());
    }

    private void clearFields() {
        nameText.setText("");
        majorField.setText("");
        studentIDField.setText("");
        AddressField.setText("");
        TelephoneField.setText("");
        EmailAddressField.setText("");
        SemField.setText("");
        AcademicField.setText("");
    }

    private void setDefaultValues() {
        nameText.setText("Student Information");
        majorField.setText("Not available");
        studentIDField.setText("Not available");
        AddressField.setText("Not available");
        TelephoneField.setText("Not available");
        EmailAddressField.setText("Not available");
        SemField.setText("Not available");
        AcademicField.setText("Not available");
    }

    private void setFieldsNonEditable() {
        majorField.setEditable(false);
        studentIDField.setEditable(false);
        AddressField.setEditable(false);
        TelephoneField.setEditable(false);
        EmailAddressField.setEditable(false);
        SemField.setEditable(false);
        AcademicField.setEditable(false);
    }

    private void showErrorAlert(String message, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }
}