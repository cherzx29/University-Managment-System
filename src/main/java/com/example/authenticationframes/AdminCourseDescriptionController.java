package com.example.authenticationframes;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

public class AdminCourseDescriptionController {

    private static final String FILE_PATH = "src/main/resources/data/File_Courses.txt";

    private String[] selectedCourseData = null;


    @FXML
    private VBox courseListVBox;

    @FXML
    private Button addCourseButton;

    @FXML private Text detailName;
    @FXML private Text detailCode;
    @FXML private Text detailLocation;
    @FXML private Text detailSection;
    @FXML private Text detailCapacity;
    @FXML private Text detailExam;
    @FXML private Text detailTeacher;
    @FXML private Text detailTime;
    @FXML private Button editCourseButton;
    @FXML private Button deleteCourseButton;


    @FXML
    private void initialize() {
        loadCourseList();

        if (addCourseButton != null) {
            addCourseButton.setOnAction(event -> openAddCourseForm());
        }

        if (editCourseButton != null) {
            editCourseButton.setOnAction(event -> openEditCourseForm());
        }

        if (deleteCourseButton != null) {
            deleteCourseButton.setOnAction(event -> openDeleteCourseForm());
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
            System.err.println("Error loading event data: " + e.getMessage());
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

            detailName.setText(courseData[0]);
            detailName.setFont(new Font(17));

            detailCode.setText(courseData[1]);
            detailCode.setFont(new Font(17));

            detailSection.setText(courseData[2]);
            detailSection.setFont(new Font(17));

            detailTime.setText(courseData[3]);
            detailTime.setFont(new Font(17));

            detailCapacity.setText(courseData[4]);
            detailCapacity.setFont(new Font(17));

            detailExam.setText("$" + courseData[5]);
            detailExam.setFont(new Font(17));

            detailLocation.setText(courseData[6]);
            detailLocation.setFont(new Font(17)); // Slightly smaller for long text

            detailTeacher.setText(courseData[7]);
            detailTeacher.setFont(new Font(17));
        });


        return pane;
    }

    private String truncateText(String text, int maxLength) {
        return text.length() > maxLength ? text.substring(0, maxLength) + "..." : text;
    }

    private void openAddCourseForm() {
        try {
            URL fxmlLocation = getClass().getResource("/com/example/authenticationframes/adminAddCourse.fxml");
            if (fxmlLocation == null) {
                System.err.println("FXML FILE NOT FOUND AT: /com/example/authenticationframes/adminAddCourse.fxml");
                System.err.println("Current class location: " + getClass().getResource("."));
                return;
            }
            System.out.println("Loading FXML from: " + fxmlLocation);

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();
            stage.setTitle("Add New Course");
            stage.setOnHiding(event -> loadCourseList());
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.err.println("FULL ERROR DETAILS:");
            e.printStackTrace();
            System.err.println("Attempted path: " + new File(".").getAbsolutePath());
        }
    }
    @FXML
    private void openEditCourseForm() {
        if (selectedCourseData == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Course Selected");
            alert.setContentText("Please select a course to edit.");
            alert.showAndWait();
            return;
        }

        try {
            System.out.println("Attempting to load edit course FXML...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/authenticationframes/adminEditCourse.fxml"));
            AnchorPane root = loader.load();
            System.out.println("FXML loaded successfully");

            AdminEditCourseController controller = loader.getController();
            controller.setParentController(this);
            controller.setOriginalCourseData(selectedCourseData);

            Stage editStage = new Stage();
            editStage.setTitle("Edit Course");
            editStage.setScene(new Scene(root));
            editStage.show();

            System.out.println("Edit window opened successfully");

        } catch (IOException e) {
            System.err.println("Error loading edit course window:");
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load the edit window");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


    private void openDeleteCourseForm() {
        if (selectedCourseData == null) {
            System.out.println("⚠ Please select an course to delete.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/authenticationframes/adminDeleteCourse.fxml"));
            Scene scene = new Scene(loader.load());

            AdminDeleteCourseController controller = loader.getController();
            controller.setCourseToDelete(selectedCourseData);
            controller.setParentController(this); // Pass this controller to allow refreshing

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Confirm Delete");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
