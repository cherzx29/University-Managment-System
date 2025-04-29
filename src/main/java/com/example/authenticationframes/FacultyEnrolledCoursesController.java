package com.example.authenticationframes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FacultyEnrolledCoursesController {

    private static final String USER_DIR = "src/main/resources/data/";
    private static final String TEACH_FILE_SUFFIX = "_teach.txt";
    private static ObservableList<String[]> teachingCourses = FXCollections.observableArrayList();

    @FXML
    private VBox courseListVBox;

    private String currentUserEmail;

    public void setCurrentUserEmail(String currentUserEmail) {
        this.currentUserEmail = currentUserEmail;
        loadFacultyCourses();
    }

    private void loadFacultyCourses() {
        courseListVBox.getChildren().clear();
        teachingCourses.clear();

        String username = currentUserEmail.split("@")[0];
        String filePath = USER_DIR + username + TEACH_FILE_SUFFIX;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] courseData = line.split("\t");
                if (courseData.length >= 8) {
                    teachingCourses.add(courseData);
                    Pane coursePane = createCoursePane(courseData);
                    courseListVBox.getChildren().add(coursePane);
                }
            }
        } catch (IOException e) {
            System.out.println("No assigned courses found or failed to load: " + e.getMessage());
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
        return pane;
    }

    private String truncateText(String text, int maxLength) {
        return text.length() > maxLength ? text.substring(0, maxLength) + "..." : text;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
