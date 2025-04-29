package com.example.authenticationframes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

public class AdminMainMenuController {

    @FXML
    private Text email;

    @FXML
    private Button logoutButton;

    @FXML
    private Button courseManagementButton;

    @FXML
    private Button studentManagementButton;

    @FXML
    private Button facultyManagementButton;


    @FXML
    private Button subjectManagementButton; // Make sure this matches your FXML

    public void setUserName(String firstName) {
        if (email != null) {
            email.setText(firstName);
        } else {
            System.out.println("Email not found");
        }
    }

    public void setUserEmail(String email) {
        // Optional: for future use
    }

    @FXML
    private void handleLogout() {
        int response = JOptionPane.showConfirmDialog(null, "Do you want to log out?", "Logout Confirmation", JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.close();

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
                Stage loginStage = new Stage();
                loginStage.setScene(new Scene(loader.load()));
                loginStage.setTitle("Login");
                loginStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleSubjectManagement() {
        try {
            // ⚠️ Update this path if your FXML is in a subfolder
            FXMLLoader loader = new FXMLLoader(getClass().getResource("adminSubjectList.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Subject Management");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading Subject Management: " + e.getMessage());
        }
    }


    @FXML
    private void handleCourseManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("adminCourseDescription.fxml")); // ✅ update path if needed
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Course Management");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading Course Management: " + e.getMessage());
        }
    }

    @FXML
    private void handleStudentManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("adminStudentList.fxml")); // 🔁 Adjust path if needed
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Student Management");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading Student Management: " + e.getMessage());
        }
    }

    @FXML
    private void handleFacultyManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("adminFacultyList.fxml")); // ✅ adjust if in subfolder
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Faculty Management");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading Faculty Management: " + e.getMessage());
        }
    }

    @FXML
    private Button eventManagementButton;

    @FXML
    private void handleEventManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("adminEventView.fxml")); // or correct path
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Event Management");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading Event Management: " + e.getMessage());
        }
    }



}
