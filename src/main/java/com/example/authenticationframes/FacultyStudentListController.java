package com.example.authenticationframes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FacultyStudentListController {

    @FXML private ScrollPane scrollPane;
    @FXML private AnchorPane contentPane;
    @FXML private TextField searchField;
    @FXML private Button backButton;

    private List<StudentData> students = new ArrayList<>();
    private String currentCourseId;
    private String currentCourseName;

    // **New method to receive course information**
    public void setCourseInfo(String courseName, String courseCode) {
        this.currentCourseName = courseName;
        this.currentCourseId = courseCode;
        System.out.println("Received Course: " + courseName + " (" + courseCode + ")");
        initializeWithCourse(courseCode);
    }

    public void initializeWithCourse(String courseId) {
        this.currentCourseId = courseId;
        loadMockStudents();
        displayStudents();
    }

    private static class StudentData {
        String id;
        String fullName;
        String degree;
        String email;
        String currentCourse;
        String location;

        StudentData(String id, String fullName, String degree, String email, String currentCourse, String location) {
            this.id = id;
            this.fullName = fullName;
            this.degree = degree;
            this.email = email;
            this.currentCourse = currentCourse;
            this.location = location;
        }

        boolean matchesSearch(String term) {
            return fullName.toLowerCase().contains(term) ||
                    email.toLowerCase().contains(term) ||
                    degree.toLowerCase().contains(term) ||
                    currentCourse.toLowerCase().contains(term);
        }
    }

    private void loadMockStudents() {
        students.clear();
        if ("MATH101".equals(currentCourseId)) {
            students.add(new StudentData("1001", "John Doe", "Computer Science", "john@uni.edu", "MATH101", "Dorm A"));
            students.add(new StudentData("1002", "Jane Smith", "Mathematics", "jane@uni.edu", "MATH101", "Dorm B"));
        } else if ("PHYS201".equals(currentCourseId)) {
            students.add(new StudentData("1003", "Bob Johnson", "Physics", "bob@uni.edu", "PHYS201", "Dorm C"));
        }
    }

    private void displayStudents() {
        contentPane.getChildren().clear();
        double yPos = 10.0;
        for (StudentData student : students) {
            Pane studentEntry = createStudentEntry(student, yPos);
            contentPane.getChildren().add(studentEntry);
            yPos += 50.0;
        }
    }

    private Pane createStudentEntry(StudentData student, double yPos) {
        Pane entry = new Pane();
        entry.setLayoutY(yPos);
        entry.setPrefHeight(44.0);
        entry.setPrefWidth(1109.0);
        entry.setStyle("-fx-border-radius: 50px; -fx-background-radius: 50px; -fx-background-color: #373737;");

        Button nameButton = new Button(student.fullName);
        nameButton.setLayoutX(12.0);
        nameButton.setLayoutY(6.0);
        nameButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-underline: true;");
        nameButton.setOnAction(e -> viewStudentProfile(student));

        Text degreeText = new Text(student.degree);
        degreeText.setLayoutX(170.0);
        degreeText.setLayoutY(28.0);
        degreeText.setFill(Color.WHITE);

        Text courseText = new Text(student.currentCourse);
        courseText.setLayoutX(364.0);
        courseText.setLayoutY(28.0);
        courseText.setFill(Color.WHITE);

        Text emailText = new Text(student.email);
        emailText.setLayoutX(661.0);
        emailText.setLayoutY(28.0);
        emailText.setFill(Color.WHITE);

        Text locationText = new Text(student.location);
        locationText.setLayoutX(926.0);
        locationText.setLayoutY(28.0);
        locationText.setFill(Color.WHITE);

        entry.getChildren().addAll(nameButton, degreeText, courseText, emailText, locationText);
        return entry;
    }

    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText().toLowerCase();
        contentPane.getChildren().clear();
        double yPos = 10.0;
        for (StudentData student : students) {
            if (student.matchesSearch(searchTerm)) {
                Pane studentEntry = createStudentEntry(student, yPos);
                contentPane.getChildren().add(studentEntry);
                yPos += 50.0;
            }
        }
    }

    private void viewStudentProfile(StudentData student) {
        System.out.println("Viewing profile for: " + student.fullName);
        showAlert("Student Profile",
                "Name: " + student.fullName + "\n" +
                        "ID: " + student.id + "\n" +
                        "Degree: " + student.degree + "\n" +
                        "Email: " + student.email + "\n" +
                        "Course: " + student.currentCourse + "\n" +
                        "Location: " + student.location);
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FacultyCourseManagement.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not return to course management");
        }
    }

    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
