package com.example.authenticationframes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserStudentGradesController {
    private String currentUserEmail;
    private String studentProgram;
    private List<Pane> allCoursePanes = new ArrayList<>();

    @FXML private Text studentNameText;
    @FXML private VBox allCoursesContainer;
    @FXML private TextField searchField;
    @FXML private Button backButton;

    public void setCurrentUserEmail(String email) {
        this.currentUserEmail = email;
        loadStudentData();
        loadProgramCourses();
        setupSearchFunctionality();
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

    private void setupSearchFunctionality() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterCourses(newValue);
        });
    }

    private void filterCourses(String searchText) {
        for (Pane pane : allCoursePanes) {
            Text courseText = (Text) pane.getChildren().get(0);
            boolean match = courseText.getText().toLowerCase().contains(searchText.toLowerCase());

            pane.setStyle(match
                    ? "-fx-border-radius: 50px; -fx-background-radius: 50px; -fx-background-color: #000000; -fx-border-color: white;"
                    : "-fx-border-radius: 50px; -fx-background-radius: 50px; -fx-background-color: #373737; -fx-border-color: transparent;"
            );
        }
    }

    private void loadStudentData() {
        try (BufferedReader reader = new BufferedReader(new FileReader("loginTextFile.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length >= 8 && parts[4].equalsIgnoreCase(currentUserEmail)) {
                    studentNameText.setText(parts[0] + "'s Grades");
                    this.studentProgram = parts[6].split(" - ")[0].trim();
                    return;
                }
            }
            // Fallback if email not found
            String nameFromEmail = currentUserEmail.split("@")[0];
            nameFromEmail = nameFromEmail.substring(0, 1).toUpperCase() + nameFromEmail.substring(1);
            studentNameText.setText(nameFromEmail + "'s Grades");
        } catch (IOException e) {
            System.err.println("Error reading student data: " + e.getMessage());
            showErrorAlert("Failed to load student data", e);
        }
    }

    private void loadProgramCourses() {
        allCoursePanes.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader("courses.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",\\s*");

                if (parts.length < 3 || !parts[0].equalsIgnoreCase(studentProgram) || (parts.length - 1) % 2 != 0) {
                    System.err.println("Skipping line: " + line);
                    continue;
                }

                allCoursesContainer.getChildren().clear();

                for (int i = 1; i < parts.length; i += 2) {
                    if (i + 1 >= parts.length) break;
                    String course = parts[i];
                    String grade = parts[i + 1];
                    Pane coursePane = createCourseGradePane(course, grade);
                    allCoursesContainer.getChildren().add(coursePane);
                    allCoursePanes.add(coursePane);
                }
                break;
            }
        } catch (IOException e) {
            System.err.println("Error reading courses.txt: " + e.getMessage());
            showErrorAlert("Failed to load grades", e);
        }
    }

    private Pane createCourseGradePane(String course, String grade) {
        Pane pane = new Pane();
        pane.setStyle("-fx-border-radius: 50px; -fx-background-radius: 50px; -fx-background-color: #373737;");
        pane.setPrefHeight(44.0);
        pane.setPrefWidth(1137.0);

        Text courseText = new Text(course);
        courseText.setFill(javafx.scene.paint.Color.WHITE);
        courseText.setLayoutX(24.0);
        courseText.setLayoutY(30.0);
        courseText.setFont(new Font(20.0));

        Text gradeText = new Text(grade);
        gradeText.setFill(javafx.scene.paint.Color.WHITE);
        gradeText.setLayoutX(952.0);
        gradeText.setLayoutY(29.0);
        gradeText.setTextAlignment(javafx.scene.text.TextAlignment.RIGHT);
        gradeText.setFont(new Font(20.0));

        pane.getChildren().addAll(courseText, gradeText);
        return pane;
    }

    private void showErrorAlert(String message, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }
}