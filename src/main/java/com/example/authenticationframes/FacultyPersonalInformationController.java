package com.example.authenticationframes;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
import java.util.ResourceBundle;

public class FacultyPersonalInformationController implements Initializable {

    @FXML private TextField majorField, FacultyIDField, AddressField,
            TelephoneField, EmailAddressField, SemField, AcademicField;
    @FXML private Text nameText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setFieldsNonEditable();
    }

    public void setCurrentUserEmail(String email) {
        loadFacultyData(email.trim().toLowerCase());
    }

    private void loadFacultyData(String email) {
        clearFields();

        try (BufferedReader reader = new BufferedReader(new FileReader("loginTextFile.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",\\s*");
                if (data.length >= 9 && data[4].trim().equalsIgnoreCase(email)) {
                    displayFacultyData(data);
                    return;
                }
            }
            setDefaultValues();
        } catch (Exception e) {
            setDefaultValues();
        }
    }

    private void displayFacultyData(String[] data) {
        nameText.setText(data[0].trim());
        majorField.setText(data[6].trim());
        FacultyIDField.setText(data[1].trim());
        AddressField.setText(data[2].trim());
        TelephoneField.setText(data[3].trim());
        EmailAddressField.setText(data[4].trim());
        SemField.setText(data[7].trim());
        AcademicField.setText(data[5].trim());
    }

    private void clearFields() {
        nameText.setText("");
        majorField.setText("");
        FacultyIDField.setText("");
        AddressField.setText("");
        TelephoneField.setText("");
        EmailAddressField.setText("");
        SemField.setText("");
        AcademicField.setText("");
    }

    private void setDefaultValues() {
        nameText.setText("Student Information");
        majorField.setText("Not available");
        FacultyIDField.setText("Not available");
        AddressField.setText("Not available");
        TelephoneField.setText("Not available");
        EmailAddressField.setText("Not available");
        SemField.setText("Not available");
        AcademicField.setText("Not available");
    }

    private void setFieldsNonEditable() {
        majorField.setEditable(false);
        FacultyIDField.setEditable(false);
        AddressField.setEditable(false);
        TelephoneField.setEditable(false);
        EmailAddressField.setEditable(false);
        SemField.setEditable(false);
        AcademicField.setEditable(false);
    }
}