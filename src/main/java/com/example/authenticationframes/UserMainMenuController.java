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
import java.net.URL;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class UserMainMenuController {

    @FXML private Text email;
    @FXML private Button logoutButton;
    @FXML private Button courseManagement;
    @FXML private Button editProfile;
    @FXML private Button grades;
    @FXML private Button tuition;
    @FXML private Button enrolledCourses;
    @FXML private ImageView profileImageView;

    private String currentUserEmail;

    public void setUserName(String firstName) {
        if (email != null) {
            email.setText(firstName);
        }
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
    private void handleEnrolledCourses() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userEnrolledClasses.fxml"));
            Parent root = loader.load();
            UserEnrolledClassesController controller = loader.getController();
            controller.setCurrentUserEmail(this.currentUserEmail);
            Stage stage = (Stage) enrolledCourses.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Failed to load enrolled courses page", e);
        }
    }

    @FXML
    private void handleStudentInfo() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userPersonalInformation.fxml"));
            Parent root = loader.load();
            UserPersonalInformationController controller = loader.getController();
            controller.setCurrentUserEmail(this.currentUserEmail);
            Stage stage = (Stage) email.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Failed to load student info", e);
        }
    }

    @FXML
    private void handleGrades() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userStudentGrades.fxml"));
            Parent root = loader.load();
            UserStudentGradesController controller = loader.getController();
            controller.setCurrentUserEmail(this.currentUserEmail);
            Stage stage = (Stage) grades.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Failed to load grades page", e);
        }
    }

    @FXML
    private void handleTuition() {
        try {
            // Try loading with a URL to get better error reporting
            URL fxmlUrl = getClass().getResource("/com/example/authenticationframes/userStudentTuition.fxml");
            System.out.println("FXML URL: " + fxmlUrl); // Debug

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            // Get the controller and set the user data
            UserStudentTuitionController controller = loader.getController();
            controller.setCurrentUserEmail(this.currentUserEmail);

            // Get full name from file
            String fullName = getUserFullNameFromFile(currentUserEmail);
            controller.setUserFullName(fullName);

            // Get the current stage and set the new scene
            Stage stage = (Stage) tuition.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load tuition page", e);
        }
    }

    private String getUserFullNameFromFile(String email) throws IOException {
        File file = new File("loginTextFile.txt");
        if (!file.exists()) {
            return "Unknown User";
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
        return "Unknown User";
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
    private void handleCourseManagement() {
        try {
            // Load the FXML file for course management
            URL fxmlUrl = getClass().getResource("userCourseDescription.fxml");
            if (fxmlUrl == null) {
                throw new IOException("FXML file not found");
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            // Get the controller and pass current user data if needed
            UserCourseDescriptionController controller = loader.getController();
            controller.setCurrentUserEmail(this.currentUserEmail);

            // Get the current stage and set the new scene
            Stage stage = (Stage) courseManagement.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            System.err.println("Error loading course management screen:");
            e.printStackTrace();
            showError("Failed to load course management page. Please try again.", e);
        }
    }


    @FXML
    private void handleEditProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userEditProfile.fxml"));
            Parent root = loader.load();
            UserEditProfileController controller = loader.getController();
            controller.setCurrentUserEmail(currentUserEmail);
            controller.setCurrentUserName(email.getText());
            Stage stage = (Stage) editProfile.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showError("Failed to load edit profile page", e);
        }
    }

    @FXML
    public void initialize() {
        if (currentUserEmail != null) {
            loadProfilePicture(true);
            email.setText(currentUserEmail);
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

    public void setCurrentUserEmail(String currentUserEmail) {
        this.currentUserEmail = currentUserEmail;

        // Fetch name directly from file (without a separate method)
        String userName = "User"; // Default fallback
        try (BufferedReader reader = new BufferedReader(new FileReader("loginTextFile.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length >= 5 && parts[4].equalsIgnoreCase(currentUserEmail)) {
                    userName = parts[0]; // Set the actual name (e.g., "Cherith Boya")
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Update UI
        if (email != null) {
            email.setText(userName); // Example: "Hello, Cherith Boya"
        }
        loadProfilePicture(true);
    }
}
