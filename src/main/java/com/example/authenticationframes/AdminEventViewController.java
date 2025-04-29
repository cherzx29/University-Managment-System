package com.example.authenticationframes;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AdminEventViewController {

    private static final String FILE_PATH = "src/main/resources/data/File_Events.txt";

    private String[] selectedEventData = null;


    @FXML
    private VBox eventListVBox;

    @FXML
    private Button addEventButton;

    @FXML private Text detailName;
    @FXML private Text detailCode;
    @FXML private Text detailLocation;
    @FXML private Text detailDate;
    @FXML private Text detailCapacity;
    @FXML private Text detailCost;
    @FXML private Text detailDescription;
    @FXML private Button editEventButton;
    @FXML private Button deleteEventButton;


    @FXML
    private void initialize() {
        loadEventList();

        if (addEventButton != null) {
            addEventButton.setOnAction(event -> openAddEventForm());
        }

        if (editEventButton != null) {
            editEventButton.setOnAction(event -> openEditEventForm());
        }

        if (deleteEventButton != null) {
            deleteEventButton.setOnAction(event -> openDeleteEventForm());
        }

        Platform.runLater(() -> {
            Stage stage = (Stage) addEventButton.getScene().getWindow();
            stage.addEventHandler(WindowEvent.WINDOW_SHOWN, e -> loadEventList());
        });
    }


    public void loadEventList() {
        eventListVBox.getChildren().clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] eventData = line.split("\t");
                if (eventData.length >= 8) {
                    Pane eventPane = createEventPane(eventData);
                    eventListVBox.getChildren().add(eventPane);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading event data: " + e.getMessage());
        }
    }

    private Pane createEventPane(String[] eventData) {
        Pane pane = new Pane();
        pane.setPrefHeight(60);
        pane.setPrefWidth(1100);
        pane.setStyle("-fx-border-radius: 50px; -fx-background-radius: 50px; -fx-border-style: solid; -fx-background-color: #373737;");

        Rectangle highlight = new Rectangle(1100, 60, Color.TRANSPARENT);
        pane.getChildren().add(highlight);

        String textStyle = "-fx-fill: white; -fx-font-size: 12; -fx-font-family: 'Arial';";

        Text name = new Text("Name: " + eventData[0]);
        name.setLayoutX(10);
        name.setLayoutY(30);
        name.setStyle(textStyle);

        Text code = new Text("Code: " + eventData[1]);
        code.setLayoutX(150);
        code.setLayoutY(30);
        code.setStyle(textStyle);

        Text location = new Text("Location: " + eventData[2]);
        location.setLayoutX(260);
        location.setLayoutY(30);
        location.setStyle(textStyle);

        Text date = new Text("DateTime: " + eventData[3]);
        date.setLayoutX(370);
        date.setLayoutY(30);
        date.setStyle(textStyle);

        Text capacity = new Text("Capacity: " + eventData[4]);
        capacity.setLayoutX(510);
        capacity.setLayoutY(30);
        capacity.setStyle(textStyle);

        Text cost = new Text("Cost: $" + eventData[5]);
        cost.setLayoutX(610);
        cost.setLayoutY(30);
        cost.setStyle(textStyle);

        Text desc = new Text("Desc: " + truncateText(eventData[6], 25));
        desc.setLayoutX(710);
        desc.setLayoutY(30);
        desc.setStyle(textStyle);

        Text enrolled = new Text("Enrolled: " + eventData[7]);
        enrolled.setLayoutX(950);
        enrolled.setLayoutY(30);
        enrolled.setStyle(textStyle);

        pane.getChildren().addAll(name, code, location, date, capacity, cost, desc, enrolled);

        pane.setOnMouseClicked(event -> {

            selectedEventData = eventData;

            detailName.setText(eventData[0]);
            detailName.setFont(new Font(17));

            detailCode.setText(eventData[1]);
            detailCode.setFont(new Font(17));

            detailLocation.setText(eventData[2]);
            detailLocation.setFont(new Font(17));

            detailDate.setText(eventData[3]);
            detailDate.setFont(new Font(17));

            detailCapacity.setText(eventData[4]);
            detailCapacity.setFont(new Font(17));

            detailCost.setText("$" + eventData[5]);
            detailCost.setFont(new Font(17));

            detailDescription.setText(eventData[6]);
            detailDescription.setFont(new Font(17)); // Slightly smaller for long text
        });


        return pane;
    }

    private String truncateText(String text, int maxLength) {
        return text.length() > maxLength ? text.substring(0, maxLength) + "..." : text;
    }

    private void openAddEventForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/authenticationframes/adminAddEvent.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();
            stage.setTitle("Add New Event");
            stage.setOnHiding(event -> loadEventList()); // ✔ already added to auto-refresh
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading adminAddEvent.fxml: " + e.getMessage());
        }
    }


    private void openEditEventForm() {
        if (selectedEventData == null) {
            System.out.println("⚠ Please select an event to edit.");
            return;
        }

        try {
            // ✅ Correct full path to the FXML in resources
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/authenticationframes/adminEditEvent.fxml"));
            Scene scene = new Scene(loader.load());

            AdminEditEventController controller = loader.getController();
            controller.setOriginalEventData(selectedEventData);
            controller.setParentController(this); // For refreshing the event list

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Edit Event");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("❌ Error loading adminEditEvent.fxml: " + e.getMessage());
        }
    }



    private void openDeleteEventForm() {
        if (selectedEventData == null) {
            System.out.println("⚠ Please select an event to delete.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/authenticationframes/adminDeleteEvent.fxml"));
            Scene scene = new Scene(loader.load());

            AdminDeleteEventController controller = loader.getController();
            controller.setEventToDelete(selectedEventData);
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
