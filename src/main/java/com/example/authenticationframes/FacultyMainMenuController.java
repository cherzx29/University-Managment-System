package com.example.authenticationframes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FacultyMainMenuController {

    @FXML private Text email;
    @FXML private Button logoutButton;
    @FXML private Button editProfile;
    @FXML private Button grades;
    @FXML private Button profileManagementButton;
    @FXML private ImageView profileImageView;
    @FXML private Button CourseManagementButton;

    private String currentUserEmail;

    public void setUserName(String firstName) {
        if (email != null) {
            email.setText(firstName);
        }
    }



    @FXML
    private void handleProfileManagement() {
        try {
            // Load the FXML first
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FacultyViewFaculty.fxml"));
            Parent root = loader.load();

            // Get controller AFTER loading
            FacultyViewController controller = loader.getController();
            controller.setUserEmail(currentUserEmail);

            // Get current stage
            Stage stage = (Stage) profileManagementButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load profile page", e);
        }
    }

    @FXML
    private void handleCourseManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FacultyCourseManagament.fxml"));
            Parent root = loader.load();

            FacultyCourseManagament controller = loader.getController();
            controller.setUserEmail(currentUserEmail); // Changed from setUserName to setUserEmail

            Stage stage = (Stage) CourseManagementButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showError("Failed to load course management page", e);
        }
    }

    @FXML
    public void initialize() {
        if (currentUserEmail != null) {
            loadProfilePicture(true);
            email.setText(currentUserEmail);
        }

        // Set the action for profile management button
        profileManagementButton.setOnAction(event -> handleProfileManagement());
    }

    public void setUserEmail(String email) {
        this.currentUserEmail = email;
    }

    public void loadProfilePicture(boolean forceRefresh) {
        try {
            File imageFile = new File("profile_pictures/" + currentUserEmail + ".png");
            if (imageFile.exists()) {
                String imageUrl = imageFile.toURI().toString();
                if (forceRefresh) {
                    imageUrl += "?t=" + System.currentTimeMillis();
                }
                Image image = new Image(imageUrl);
                profileImageView.setImage(image);
            } else {
                profileImageView.setImage(
                        new Image(getClass().getResourceAsStream("/images/default_profile.png"))
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
            profileImageView.setImage(null);
        }
    }

    @FXML
    private void handleGrades() {

    }

    @FXML
    private void handleLogout() {
        int response = JOptionPane.showConfirmDialog(null,
                "Do you want to log out?",
                "Logout Confirmation",
                JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            navigateTo("login.fxml");
        }
    }

    @FXML
    private void handleEditProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("facultyEditProfile.fxml"));
            Parent root = loader.load();
            FacultyEditProfile controller = loader.getController();
            controller.setCurrentUserEmail(currentUserEmail);
            controller.setCurrentUserName(email.getText());
            Stage stage = (Stage) editProfile.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showError("Failed to load edit profile page", e);
        }
    }


    private void navigateTo(String fxmlFile) {
        try {
            Stage stage = (Stage) email.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Failed to load " + fxmlFile, e);
        }
    }

    private void showError(String message, Exception e) {
        System.err.println(message + ": " + e.getMessage());
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Additional faculty-specific methods can be added here
    private String getUserFullNameFromFile(String email) throws IOException {
        File file = new File("loginTextFile.txt");
        if (!file.exists()) {
            return "Unknown Faculty";
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length > 4 && parts[4].equalsIgnoreCase(email)) {
                    return parts[0]; // Return the full name
                }
            }
        }
        return "Unknown Faculty";
    }
}