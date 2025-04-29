package com.example.authenticationframes;

import com.example.real.utils.SceneLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FacultyViewController {

    private static final String FILE_PATH = "loginTextFile.txt";

    @FXML private VBox facultyListContainer;
    @FXML private TextField searchField;
    @FXML private Button searchButton;
    @FXML private Button addFacultyButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private Button backButton;
    @FXML private Text nameText;
    @FXML private Text degreeText;
    @FXML private Text researchText;
    @FXML private Text officeText;
    @FXML private Text phoneText;
    @FXML private Text emailText;
    private String currentUserEmail;
    private String currentUserName;
    private Pane selectedFacultyPane = null;
    private static final List<String[]> facultyDataList = new ArrayList<>();
    private static final List<String[]> facultyDataListStatic = new ArrayList<>();
    @FXML private VBox coursesVBox;

    @FXML
    private void initialize() {
    }

    private void loadFacultyCourses(String facultyName) {
        // ... clear existing content ...

        String fileContent;
        try {
            fileContent = Files.readString(Path.of("File_Courses.txt"));

            // Simple contains check - for debugging
            if (fileContent.contains(facultyName)) {
                System.out.println("Found faculty name in file!");
            } else {
                System.out.println("Faculty name NOT found in file");
                System.out.println("Looking for: " + facultyName);
                System.out.println("File contains: " + fileContent);
            }

            // Parse courses
            Arrays.stream(fileContent.split("\n"))
                    .filter(line -> line.contains(facultyName))
                    .map(line -> line.split("\\s+")[0])
                    .forEach(this::addCourseToView);

        } catch (IOException e) {
            showErrorMessage();
        }
    }
    private boolean matchesFaculty(String line, String facultyName) {
        // Normalize both names
        String normalizedFaculty = facultyName.toLowerCase().replaceAll("[^a-z ]", "").trim();

        // Look for last name pattern (Doe) or full name (Jane Doe)
        String[] parts = line.split("\\s+");
        if (parts.length < 2) return false;

        // Try last two words as full name
        String courseFaculty = parts[parts.length-2] + " " + parts[parts.length-1];
        String normalizedCourseFaculty = courseFaculty.toLowerCase().replaceAll("[^a-z ]", "").trim();

        return normalizedCourseFaculty.equals(normalizedFaculty);
    }

    // Helper method for flexible name matching
    private boolean isFacultyMatch(String fileFacultyName, String loggedInFacultyName) {
        // Normalize names
        String file = fileFacultyName.trim().toLowerCase().replaceAll("[^a-z ]", "");
        String loggedIn = loggedInFacultyName.trim().toLowerCase().replaceAll("[^a-z ]", "");

        // Check if either name contains the other (for "Jane Doe" vs "Doe, Jane")
        return file.contains(loggedIn) || loggedIn.contains(file);
    }


    private boolean isNameMatch(String courseFaculty, String profileFaculty) {
        // Normalize both names
        String courseName = courseFaculty.trim().toLowerCase();
        String profileName = profileFaculty.trim().toLowerCase();

        // Exact match
        if (courseName.equals(profileName)) return true;

        // Partial match (e.g., "Jane Doe" vs "Doe, Jane")
        String[] courseParts = courseName.split("\\s+");
        String[] profileParts = profileName.split("\\s+");

        // Check if all parts exist in both names
        return Arrays.stream(courseParts).anyMatch(part -> profileName.contains(part)) ||
                Arrays.stream(profileParts).anyMatch(part -> courseName.contains(part));
    }

    private void addCourseToView(String courseName) {
        Label courseLabel = new Label(courseName);
        courseLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16;");
        coursesVBox.getChildren().add(courseLabel);
    }

    private void showNoCoursesMessage() {
        Label noCoursesLabel = new Label("No courses assigned");
        noCoursesLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16;");
        coursesVBox.getChildren().add(noCoursesLabel);
    }

    private void showErrorMessage() {
        Label errorLabel = new Label("Error loading courses");
        errorLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16;");
        coursesVBox.getChildren().add(errorLabel);
    }

    private void loadFaculty() {
        facultyListContainer.getChildren().clear();
        facultyDataList.clear();
        facultyDataListStatic.clear();

        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.err.println("⚠ Faculty file not found!");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length < 6) continue;

                facultyDataList.add(parts);
                facultyDataListStatic.add(parts);

                Pane pane = createFacultyPane(parts);
                facultyListContainer.getChildren().add(pane);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Pane createFacultyPane(String[] data) {
        Pane pane = new Pane();
        pane.setPrefHeight(44);
        pane.setPrefWidth(683);
        pane.setStyle("-fx-border-radius: 30px; -fx-background-radius: 30px; -fx-border-style: solid; -fx-background-color: #2C2C2C;");

        Rectangle highlight = new Rectangle(683, 44, Color.TRANSPARENT);
        pane.getChildren().add(highlight);

        Text idText = new Text(data[0]);
        idText.setLayoutX(10);
        idText.setLayoutY(28);

        Button nameButton = new Button(data[1]);
        nameButton.setLayoutX(200);
        nameButton.setLayoutY(6);
        nameButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 16;");
        nameButton.setOnAction(e -> openFacultyProfile(data));

        Text deptText = new Text(data[2]);
        deptText.setLayoutX(350);
        deptText.setLayoutY(28);

        Text emailText = new Text(data[3]);
        emailText.setLayoutX(500);
        emailText.setLayoutY(28);

        Text phoneText = new Text(data[4]);
        phoneText.setLayoutX(700);
        phoneText.setLayoutY(28);

        for (Text text : new Text[]{idText, deptText, emailText, phoneText}) {
            text.setStyle("-fx-fill: white; -fx-font-size: 16;");
        }

        pane.getChildren().addAll(idText, nameButton, deptText, emailText, phoneText);
        pane.setOnMouseClicked(event -> handleFacultySelection(pane, highlight));
        return pane;
    }

    private void handleFacultySelection(Pane pane, Rectangle highlight) {
        if (selectedFacultyPane != null) {
            Rectangle prevHighlight = (Rectangle) selectedFacultyPane.getChildren().get(0);
            prevHighlight.setFill(Color.TRANSPARENT);
        }

        selectedFacultyPane = pane;
        highlight.setFill(Color.rgb(255, 255, 255, 0.2));
    }

    private void handleDeleteButton() {
        if (selectedFacultyPane == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a faculty to delete.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this faculty?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            int selectedIndex = facultyListContainer.getChildren().indexOf(selectedFacultyPane);
            facultyListContainer.getChildren().remove(selectedFacultyPane);
            facultyDataList.remove(selectedIndex);
            facultyDataListStatic.remove(selectedIndex);
            saveFacultyListStatic();
            selectedFacultyPane = null;
        }
    }

    private void handleEditButton() {
        if (selectedFacultyPane == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a faculty to edit.");
            return;
        }

        int selectedIndex = facultyListContainer.getChildren().indexOf(selectedFacultyPane);
        String[] selectedFacultyData = facultyDataList.get(selectedIndex);
        navigateToEditFaculty(selectedFacultyData, selectedIndex);
    }

    private void navigateToEditFaculty(String[] facultyData, int index) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/real/AdminEditFaculty.fxml"));
            Parent root = loader.load();

            AdminEditFacultyController controller = loader.getController();
            controller.setFacultyData(facultyData, index);

            Stage stage = (Stage) editButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the Edit Faculty page.");
        }
    }

    public static void updateFacultyData(int index, String[] updatedData) {
        if (index >= 0 && index < facultyDataListStatic.size()) {
            facultyDataListStatic.set(index, updatedData);
            saveFacultyListStatic();
        }
    }

    private static void saveFacultyListStatic() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String[] faculty : facultyDataListStatic) {
                writer.write(String.join("\t", faculty));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void searchFaculty() {
        String query = searchField.getText().trim().toLowerCase();
        facultyListContainer.getChildren().clear();

        for (String[] faculty : facultyDataList) {
            if (faculty[0].toLowerCase().contains(query) || faculty[1].toLowerCase().contains(query)) {
                Pane pane = createFacultyPane(faculty);
                facultyListContainer.getChildren().add(pane);
            }
        }
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("facultyMainMenu.fxml"));
            Parent root = loader.load();

            FacultyMainMenuController mainMenuController = loader.getController();
            mainMenuController.setUserEmail(currentUserEmail);
            String firstName = currentUserName.split(" ")[0];
            mainMenuController.setUserName(firstName);

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.err.println("Error loading dashboard: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to navigate back");
        }
    }

    private String getFirstNameFromLoginFile(String email) throws IOException {
        File file = new File("loginTextFile.txt");
        if (!file.exists()) {
            return "Faculty";
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length > 4 && parts[4].equalsIgnoreCase(email)) {
                    return parts[0].split(" ")[0];
                }
            }
        }
        return "Faculty";
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void openFacultyProfile(String[] facultyData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/authenticationframes/FacultyViewFaculty.fxml"));
            Parent root = loader.load();

            FacultyViewController controller = loader.getController();
            controller.setFacultyData(facultyData);

            Stage currentStage = (Stage) backButton.getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Faculty Profile");
            currentStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open faculty profile.");
        }
    }

    public void setFacultyData(String[] facultyData) {
        if (facultyData.length >= 7) {
            nameText.setText(facultyData[0]);
            degreeText.setText(facultyData[1]);
            officeText.setText(facultyData[5]);
            phoneText.setText(facultyData[6]);
            emailText.setText(facultyData[3]);
            loadFacultyCourses(facultyData[0]);
            System.out.println("Loading courses for: " + facultyData[0]); // Debug
        }
    }

    public void setUserEmail(String email) {
        this.currentUserEmail = email;
        try {
            this.currentUserName = getFullNameFromLoginFile(email);
            loadFacultyData(email);
        } catch (IOException e) {
            this.currentUserName = "Faculty";
            e.printStackTrace();
        }
    }

    private String getFullNameFromLoginFile(String email) throws IOException {
        File loginFile = new File("loginTextFile.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(loginFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",\\s*");
                if (parts.length > 4 && parts[4].equalsIgnoreCase(email)) {
                    return parts[0].trim();
                }
            }
        }
        return "";
    }

    private void loadFacultyData(String email) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",\\s*");
                if (parts.length >= 5 && parts[4].trim().equalsIgnoreCase(email)) {
                    setFacultyData(new String[]{
                            parts[0].trim(),
                            parts[5].trim(),
                            parts[6].trim(),
                            parts[4].trim(),
                            "",
                            parts[2].trim(),
                            parts[3].trim()
                    });
                    break;
                }
            }
        }
    }

    private void updateCourses(String facultyName) {
        // Implement if needed
    }
}