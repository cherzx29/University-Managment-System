package com.example.authenticationframes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UserCourseDescriptionController {

    private static final String FILE_PATH = "src/main/resources/data/File_Courses.txt";
    private static final String USER_DIR = "src/main/resources/data/";
    private static final String COURSE_FILE_SUFFIX = "_courses.txt";

    private String[] selectedCourseData = null;
    private String currentUserEmail;
    private static ObservableList<String[]> enrolledCourses = FXCollections.observableArrayList();

    @FXML
    private VBox courseListVBox;
    @FXML private Text detailName;
    @FXML private Text detailCode;
    @FXML private Text detailLocation;
    @FXML private Text detailSection;
    @FXML private Text detailCapacity;
    @FXML private Text detailExam;
    @FXML private Text detailTeacher;
    @FXML private Text detailTime;
    @FXML
    private Button registerCourse;
    @FXML private Button backButton;

    @FXML
    private void initialize() {
        loadCourseList();
        registerCourse.setOnAction(event -> registerForCourse());
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

    public void loadCourseList() {
        courseListVBox.getChildren().clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] courseData = line.split("\t");
                if (courseData.length >= 8) {
                    Pane coursePane = createCoursePane(courseData);
                    courseListVBox.getChildren().add(coursePane);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load courses: " + e.getMessage());
        }
    }

    private Pane createCoursePane(String[] courseData) {
        Pane pane = new Pane();
        pane.setPrefHeight(60);
        pane.setPrefWidth(1100);
        pane.setStyle("-fx-border-radius: 50px; -fx-background-radius: 50px; -fx-border-style: solid; -fx-background-color: #373737;");

        Rectangle highlight = new Rectangle(1100, 60, Color.TRANSPARENT);
        pane.getChildren().add(highlight);

        String textStyle = "-fx-fill: white; -fx-font-size: 12; -fx-font-family: 'Arial';";

        Text name = new Text("Name: " + courseData[0]);
        name.setLayoutX(10);
        name.setLayoutY(30);
        name.setStyle(textStyle);

        Text code = new Text("Code: " + courseData[1]);
        code.setLayoutX(150);
        code.setLayoutY(30);
        code.setStyle(textStyle);

        Text location = new Text("Location: " + courseData[2]);
        location.setLayoutX(260);
        location.setLayoutY(30);
        location.setStyle(textStyle);

        Text date = new Text("DateTime: " + courseData[3]);
        date.setLayoutX(370);
        date.setLayoutY(30);
        date.setStyle(textStyle);

        Text capacity = new Text("Capacity: " + courseData[4]);
        capacity.setLayoutX(510);
        capacity.setLayoutY(30);
        capacity.setStyle(textStyle);

        Text cost = new Text("Cost: $" + courseData[5]);
        cost.setLayoutX(610);
        cost.setLayoutY(30);
        cost.setStyle(textStyle);

        Text desc = new Text("Desc: " + truncateText(courseData[6], 25));
        desc.setLayoutX(710);
        desc.setLayoutY(30);
        desc.setStyle(textStyle);

        Text enrolled = new Text("Enrolled: " + courseData[7]);
        enrolled.setLayoutX(950);
        enrolled.setLayoutY(30);
        enrolled.setStyle(textStyle);

        pane.getChildren().addAll(name, code, location, date, capacity, cost, desc, enrolled);

        pane.setOnMouseClicked(event -> {
            selectedCourseData = courseData;
            updateCourseDetails();
        });

        return pane;
    }

    private void updateCourseDetails() {
        if (selectedCourseData != null) {
            detailName.setText(selectedCourseData[0]);
            detailName.setFont(new Font(17));

            detailCode.setText(selectedCourseData[1]);
            detailCode.setFont(new Font(17));

            detailSection.setText(selectedCourseData[2]);
            detailSection.setFont(new Font(17));

            detailTime.setText(selectedCourseData[3]);
            detailTime.setFont(new Font(17));

            detailCapacity.setText(selectedCourseData[4]);
            detailCapacity.setFont(new Font(17));

            detailExam.setText("$" + selectedCourseData[5]);
            detailExam.setFont(new Font(17));

            detailLocation.setText(selectedCourseData[6]);
            detailLocation.setFont(new Font(17));

            detailTeacher.setText(selectedCourseData[7]);
            detailTeacher.setFont(new Font(17));
        }
    }

    private void registerForCourse() {
        if (selectedCourseData == null) {
            showAlert("No Selection", "Please select a course first.");
            return;
        }

        if (currentUserEmail == null || currentUserEmail.isEmpty()) {
            showAlert("Error", "No user logged in.");
            return;
        }

        if (isAlreadyRegistered(selectedCourseData[1])) {
            showAlert("Already Registered", "You are already registered for this course.");
            return;
        }

        enrolledCourses.add(selectedCourseData);

        try {
            updateCourseCapacity();
            saveCourseToUserFile();
            showAlert("Success", "Successfully registered for " + selectedCourseData[0]);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to register: " + e.getMessage());
        }
    }

    private void saveCourseToUserFile() throws IOException {
        if (currentUserEmail == null || currentUserEmail.isEmpty()) {
            throw new IOException("No user email available to save course");
        }

        // Extract username from email (e.g., "nupa@student.ca" -> "nupa")
        String username = currentUserEmail.split("@")[0];
        String userCourseFileName = USER_DIR + username + COURSE_FILE_SUFFIX;

        // Create directory if it doesn't exist
        Path path = Paths.get(USER_DIR);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        // Append the course to the user's course file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userCourseFileName, true))) {
            writer.write(String.join("\t", selectedCourseData));
            writer.newLine();
        }
    }

    private boolean isAlreadyRegistered(String courseCode) {
        // First check in memory
        boolean inMemory = enrolledCourses.stream()
                .anyMatch(course -> course[1].equals(courseCode));

        if (inMemory) {
            return true;
        }

        // Then check in file if user is logged in
        if (currentUserEmail != null && !currentUserEmail.isEmpty()) {
            String username = currentUserEmail.split("@")[0];
            String userCourseFileName = USER_DIR + username + COURSE_FILE_SUFFIX;

            try (BufferedReader reader = new BufferedReader(new FileReader(userCourseFileName))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] courseData = line.split("\t");
                    if (courseData.length > 1 && courseData[1].equals(courseCode)) {
                        return true;
                    }
                }
            } catch (IOException e) {
                // File might not exist yet, which is fine
            }
        }

        return false;
    }

    private void updateCourseCapacity() throws IOException {
        File inputFile = new File(FILE_PATH);
        File tempFile = new File(FILE_PATH + ".tmp");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] courseData = line.split("\t");
                if (courseData.length >= 8 && courseData[1].equals(selectedCourseData[1])) {
                    String[] capacityParts = courseData[4].split("/");
                    if (capacityParts.length == 2) {
                        try {
                            int current = Integer.parseInt(capacityParts[0]);
                            int max = Integer.parseInt(capacityParts[1]);
                            if (current < max) {
                                current++;
                                courseData[4] = current + "/" + max;
                                selectedCourseData[4] = courseData[4];
                            }
                        } catch (NumberFormatException e) {
                            // Ignore invalid format
                        }
                    }
                    line = String.join("\t", courseData);
                }
                writer.write(line);
                writer.newLine();
            }
        }

        if (inputFile.delete()) {
            tempFile.renameTo(inputFile);
        } else {
            throw new IOException("Could not update course capacity");
        }

        loadCourseList();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String truncateText(String text, int maxLength) {
        return text.length() > maxLength ? text.substring(0, maxLength) + "..." : text;
    }

    public static ObservableList<String[]> getEnrolledCourses() {
        return enrolledCourses;
    }

    public void setCurrentUserEmail(String currentUserEmail) {
        this.currentUserEmail = currentUserEmail;
        System.out.println("Current user email set to: " + currentUserEmail);

        // Load user's courses from file when email is set
        if (currentUserEmail != null && !currentUserEmail.isEmpty()) {
            loadUserCourses();
        }
    }

    private void loadUserCourses() {
        String username = currentUserEmail.split("@")[0];
        String userCourseFileName = USER_DIR + username + COURSE_FILE_SUFFIX;

        enrolledCourses.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(userCourseFileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] courseData = line.split("\t");
                if (courseData.length >= 8) {
                    enrolledCourses.add(courseData);
                }
            }
        } catch (IOException e) {
            // File might not exist yet, which is fine
            System.out.println("No existing course file for user: " + e.getMessage());
        }
    }

    private void showErrorAlert(String message, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }
}