package com.example.authenticationframes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UserEnrolledClassesController {

    @FXML private VBox enrolledCoursesVBox;
    @FXML private Button backButton;
    private String currentUserEmail;

    // More reliable path handling
    private static final String DATA_DIR = Paths.get("src", "main", "resources", "data").toString();
    private static final String COURSE_FILE_SUFFIX = "_courses.txt";

    public void setCurrentUserEmail(String email) {
        this.currentUserEmail = email;
        System.out.println("[DEBUG] Loading courses for: " + email);
        loadEnrolledCourses();
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

    private void loadEnrolledCourses() {
        enrolledCoursesVBox.getChildren().clear();

        if (currentUserEmail == null || currentUserEmail.isEmpty()) {
            showError("No user logged in");
            return;
        }

        String username = currentUserEmail.split("@")[0];
        Path courseFilePath = Paths.get(DATA_DIR, username + COURSE_FILE_SUFFIX);
        File courseFile = courseFilePath.toFile();

        System.out.println("[DEBUG] Checking for course file at: " + courseFilePath.toAbsolutePath());

        if (!courseFile.exists()) {
            addNoCoursesMessage();
            return;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(courseFile));
            String line;
            boolean hasCourses = false;

            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    displayCourse(line);
                    hasCourses = true;
                }
            }

            reader.close();

            if (!hasCourses) {
                addNoCoursesMessage();
            }

        } catch (IOException e) {
            System.err.println("[ERROR] Failed to read courses: " + e.getMessage());
            showError("Failed to load your courses");
        }
    }

    private void displayCourse(String courseLine) {
        String[] courseData = courseLine.split("\t");

        if (courseData.length < 8) {
            System.err.println("[WARN] Invalid course format: " + courseLine);
            return;
        }

        // Course Title (bold)
        Text title = new Text(courseData[0] + " - " + courseData[1]);
        title.setStyle("-fx-fill: black; -fx-font-size: 18; -fx-font-weight: bold;");

        // Course Details
        Text details = new Text(String.format(
                "Location: %s\nTime: %s\nCapacity: %s\nCost: $%s\nTeacher: %s",
                courseData[2], courseData[3], courseData[4], courseData[5], courseData[7]
        ));
        details.setStyle("-fx-fill: black; -fx-font-size: 14;");

        // Separator
        Text separator = new Text("────────────────────");
        separator.setStyle("-fx-fill: #5D7BA5;");

        // Add to VBox
        enrolledCoursesVBox.getChildren().addAll(title, details, separator);
    }

    private void addNoCoursesMessage() {
        Text message = new Text("You haven't enrolled in any courses yet.");
        message.setStyle("-fx-fill: black; -fx-font-size: 16;");
        enrolledCoursesVBox.getChildren().add(message);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String message, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }
}