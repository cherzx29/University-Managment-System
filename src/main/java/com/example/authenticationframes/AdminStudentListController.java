package com.example.authenticationframes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javax.swing.JOptionPane;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminStudentListController {

    @FXML
    private VBox studentContainer;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;

    private Pane selectedStudentPane = null;
    private List<String[]> studentDataList = new ArrayList<>();

    @FXML
    public void initialize() {
        loadStudentList();
        deleteButton.setOnAction(event -> handleDeleteButton());
        editButton.setOnAction(event -> handleEditButton());
    }

    private void loadStudentList() {
        studentContainer.getChildren().clear();
        studentDataList.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader("loginTextFile.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] studentData = line.split(", ");
                if (studentData.length >= 8) {
                    studentDataList.add(studentData);
                    Pane studentPane = createStudentPane(studentData);
                    studentContainer.getChildren().add(studentPane);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading student data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Pane createStudentPane(String[] studentData) {
        Pane pane = new Pane();
        pane.setPrefHeight(50);  // Slightly taller to accommodate text
        pane.setPrefWidth(1100);
        pane.setStyle("-fx-border-radius: 50px; -fx-background-radius: 50px; -fx-border-style: solid; -fx-background-color: #373737;");

        Rectangle highlight = new Rectangle(1100, 50, Color.TRANSPARENT);
        pane.getChildren().add(highlight);

        // Common style for all text elements with smaller font
        String textStyle = "-fx-fill: white; -fx-font-size: 12; -fx-font-family: 'Arial';";

        // Create and position all text fields with optimized spacing
        Text nameText = new Text(truncateText(studentData[0], 15));
        nameText.setLayoutX(10);
        nameText.setLayoutY(30);
        nameText.setStyle(textStyle);

        Text idText = new Text(studentData[1]);
        idText.setLayoutX(120);
        idText.setLayoutY(30);
        idText.setStyle(textStyle);

        Text addressText = new Text(truncateText(studentData[2], 20));
        addressText.setLayoutX(190);
        addressText.setLayoutY(30);
        addressText.setStyle(textStyle);

        Text phoneText = new Text(studentData[3]);
        phoneText.setLayoutX(320);
        phoneText.setLayoutY(30);
        phoneText.setStyle(textStyle);

        Text emailText = new Text(truncateText(studentData[4], 25));
        emailText.setLayoutX(420);
        emailText.setLayoutY(30);
        emailText.setStyle(textStyle);

        Text academicLevelText = new Text(studentData[5]);
        academicLevelText.setLayoutX(600);
        academicLevelText.setLayoutY(30);
        academicLevelText.setStyle(textStyle);

        Text programText = new Text(truncateText(studentData[6], 15));
        programText.setLayoutX(700);
        programText.setLayoutY(30);
        programText.setStyle(textStyle);

        Text semesterText = new Text(studentData[7]);
        semesterText.setLayoutX(850);
        semesterText.setLayoutY(30);
        semesterText.setStyle(textStyle);

        pane.getChildren().addAll(nameText, idText, addressText, phoneText, emailText,
                academicLevelText, programText, semesterText);

        pane.setOnMouseClicked(event -> handleStudentSelection(pane, highlight));
        return pane;
    }

    // Helper method to truncate long text with ellipsis
    private String truncateText(String text, int maxLength) {
        if (text.length() > maxLength) {
            return text.substring(0, maxLength) + "...";
        }
        return text;
    }

    private void handleStudentSelection(Pane pane, Rectangle highlight) {
        if (selectedStudentPane != null) {
            ((Rectangle) selectedStudentPane.getChildren().get(0)).setFill(Color.TRANSPARENT);
        }
        selectedStudentPane = pane;
        highlight.setFill(Color.rgb(255, 255, 255, 0.2));
    }

    private void handleDeleteButton() {
        if (selectedStudentPane == null) {
            JOptionPane.showMessageDialog(null, "Please select a student to delete.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to delete this student?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            int selectedIndex = studentContainer.getChildren().indexOf(selectedStudentPane);
            studentContainer.getChildren().remove(selectedStudentPane);
            studentDataList.remove(selectedIndex);
            saveStudentList();
            selectedStudentPane = null;
        }
    }

    private void handleEditButton() {
        if (selectedStudentPane == null) {
            JOptionPane.showMessageDialog(null, "Please select a student to edit.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int selectedIndex = studentContainer.getChildren().indexOf(selectedStudentPane);
        String[] selectedStudentData = studentDataList.get(selectedIndex);
        navigateToAddStudent(selectedStudentData);
    }

    private void navigateToAddStudent(String[] studentData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("adminAddStudent.fxml"));
            Parent root = loader.load();

            AdminAddStudentController controller = loader.getController();
            controller.setStudentData(studentData);

            Stage stage = (Stage) editButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load the add student page.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveStudentList() {
        try (FileWriter writer = new FileWriter("loginTextFile.txt")) {
            for (String[] studentData : studentDataList) {
                writer.write(String.join(", ", studentData) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saving student data",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}