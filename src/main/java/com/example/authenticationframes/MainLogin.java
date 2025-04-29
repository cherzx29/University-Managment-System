package com.example.authenticationframes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

//Video: https://www.youtube.com/watch?v=P_rfO1mJveE&ab_channel=OpenSourceDevops

public class MainLogin extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Load the FXML file for the Login page
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));

        // Create a Scene with the loaded FXML
        Scene scene = new Scene(loader.load());

        primaryStage.setTitle("Login");

        //FXMLLoader loader = new FXMLLoader(getClass().getResource("adminStudentList.fxml"));

        //Scene scene = new Scene(loader.load());

        //primaryStage.setTitle("Add Student");

        // Set up the Stage (the main window)
        primaryStage.setScene(scene);
        //Userr can see the window
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }
}