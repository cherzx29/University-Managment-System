package com.example.authenticationframes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FacultyEditProfile {
    @FXML private TextField currentPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private Button saveChangesButton;
    @FXML private Button cancelButton;
    @FXML private ImageView profileImageView;
    @FXML private Button changeProfilePicture;

    private String currentUserEmail;
    private String currentUserName;
    private String selectedImagePath;

    public void setCurrentUserEmail(String email) {
        this.currentUserEmail = email;
        loadProfilePicture();
    }

    public void setCurrentUserName(String name) {
        this.currentUserName = name;
    }

    private void loadProfilePicture() {
        try {
            File imageFile = new File("profile_pictures/" + currentUserEmail + ".png");
            if (imageFile.exists()) {
                Image image = new Image(imageFile.toURI().toString());
                profileImageView.setImage(image);
            } else {
                profileImageView.setImage(new Image(getClass().getResourceAsStream("/default_profile.png")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleChangeProfilePicture() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(profileImageView.getScene().getWindow());
        if (selectedFile != null) {
            try {
                selectedImagePath = selectedFile.getAbsolutePath();
                Image image = new Image(selectedFile.toURI().toString());
                profileImageView.setImage(image);
            } catch (Exception e) {
                showError("Error loading image", "The selected image could not be loaded.");
            }
        }
    }

    @FXML
    private void handleSaveChanges() {
        // Handle password change if fields are filled
        if (!currentPasswordField.getText().isEmpty() && !newPasswordField.getText().isEmpty()) {
            if (!changePassword(currentPasswordField.getText(), newPasswordField.getText())) {
                showError("Password Change Failed", "Current password is incorrect");
                return;
            }
        }

        // Handle profile picture change
        if (selectedImagePath != null && !selectedImagePath.isEmpty()) {
            saveProfilePicture(selectedImagePath);
        }

        returnToMainMenu(true);
    }

    private boolean changePassword(String currentPassword, String newPassword) {
        try {
            File file = new File("loginTextFile.txt");
            if (!file.exists()) {
                showError("Error", "Login file not found");
                return false;
            }

            Path path = Paths.get("loginTextFile.txt");
            String content = Files.readString(path);
            String[] lines = content.split(System.lineSeparator());
            boolean passwordChanged = false;

            for (int i = 0; i < lines.length; i++) {
                String[] parts = lines[i].split(", ");
                if (parts.length > 8 && parts[4].equals(currentUserEmail)) {
                    if (parts[8].equals(currentPassword)) {
                        parts[8] = newPassword;
                        lines[i] = String.join(", ", parts);
                        passwordChanged = true;
                        break;
                    } else {
                        return false;
                    }
                }
            }

            if (passwordChanged) {
                Files.write(path, String.join(System.lineSeparator(), lines).getBytes());
                showAlert("Success", "Password changed successfully");
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error", "Failed to update password");
        }
        return false;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void saveProfilePicture(String imagePath) {
        try {
            File directory = new File("profile_pictures");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            Path source = Paths.get(imagePath);
            Path destination = Paths.get("profile_pictures/" + currentUserEmail + ".png");

            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Image saved to: " + destination.toAbsolutePath());
        } catch (Exception e) {
            showError("Image Save Error", "Could not save profile picture");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel() {
        returnToMainMenu(false);
    }

    private void returnToMainMenu(boolean refreshPicture) {
        try {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/authenticationframes/facultyMainMenu.fxml"));
            Parent root = loader.load();

            FacultyMainMenuController controller = loader.getController();
            controller.setUserEmail(currentUserEmail);
            controller.setUserName(currentUserName);
            if (refreshPicture) {
                controller.loadProfilePicture(true);
            }

            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Navigation Error", "Failed to return to main menu");
            e.printStackTrace();
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}