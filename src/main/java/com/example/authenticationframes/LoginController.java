package com.example.authenticationframes;

import java.io.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;

    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        String[] userInfo = validateLogin(email, password);
        if (userInfo == null) {
            showAlert("Login Failed", "Invalid email or password.");
            return;
        }

        if (userInfo[1].equals("admin")) {
            loadScene("adminMainMenu.fxml", "Admin Dashboard", userInfo[0], userInfo[2]);
        } else if (userInfo[1].equals("faculty")) {
            loadScene("facultyMainMenu.fxml", "Faculty Dashboard", userInfo[0], userInfo[2]);
        } else {
            loadScene("userMainMenu.fxml", "User Dashboard", userInfo[0], userInfo[2]);
        }
    }

    private String[] validateLogin(String email, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("loginTextFile.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",\\s*");
                if (data.length >= 9) {
                    String storedEmail = data[4].trim();
                    String storedPassword = data[8].trim();

                    if (storedEmail.equalsIgnoreCase(email) && storedPassword.equals(password)) {
                        // Determine user type
                        String userType;
                        if (storedEmail.toLowerCase().contains("admin")) {
                            userType = "admin";
                        } else if (storedEmail.toLowerCase().contains("faculty")) {
                            userType = "faculty";
                        } else {
                            userType = "user";
                        }

                        return new String[]{
                                data[0].trim(), // Full name
                                userType,       // User type
                                storedEmail     // Actual email from file
                        };
                    }
                }
            }
        } catch (IOException e) {
            showAlert("Error", "Could not access login file.");
        }
        return null;
    }

    private void loadScene(String fxmlFile, String title, String fullName, String email) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(loader.load());

            // Handle different controller types
            if (fxmlFile.equals("adminMainMenu.fxml")) {
                AdminMainMenuController controller = loader.getController();
                controller.setUserName(fullName.split(" ")[0]);
                controller.setUserEmail(email);
            } else if (fxmlFile.equals("facultyMainMenu.fxml")) {
                FacultyMainMenuController controller = loader.getController();
                controller.setUserName(fullName.split(" ")[0]);
                controller.setUserEmail(email);
            } else {
                UserMainMenuController controller = loader.getController();
                controller.setUserName(fullName.split(" ")[0]);
                controller.setUserEmail(email);
            }

            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Failed to load " + title);
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}