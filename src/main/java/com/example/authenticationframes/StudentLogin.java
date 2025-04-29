package com.example.authenticationframes;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class StudentLogin {
    private String email;
    private String password;
    private int code;

    public StudentLogin(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public StudentLogin(StudentLogin otherLogin) {
        this(otherLogin.getEmail(), otherLogin.getPassword());
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static boolean checkEmail(String email) {
        return email.contains("student") || email.contains("admin");
    }

    public void displayDetails() {
        System.out.println("Welcome! Here's your login information: \n\nEmail: " + email);
        System.out.println("Password: " + password);
    }

    public void saveToFile() {
        try (FileWriter writer = new FileWriter("StudentLogin.txt", true)) {
            writer.write("Login Info --> Email: " + email + ", Password: " + password + "\n");
        } catch (IOException e) {
            System.out.println("Error saving login details to file.");
        }
    }

    public static String extractFirstName(String email) {
        String firstName = email.split("@")[0]; // Extract before '@'
        return firstName.substring(0, 1).toUpperCase() + firstName.substring(1); // Capitalize the first letter
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter your email (example123@student.ca or example123@admin.ca): ");
            String email = scanner.nextLine();

            while (!checkEmail(email)) {
                JOptionPane.showMessageDialog(null, "Invalid Email! You must use '@student' or '@admin' to log in.", "Login Error", JOptionPane.ERROR_MESSAGE);
                System.out.println("\nEnter Email: ");
                email = scanner.nextLine();
            }

            System.out.println("Password: ");
            String password = scanner.nextLine();

            System.out.println("------------------------------\n");

            if (email.contains("admin")) {
                System.out.println("Welcome, Admin!");
            } else {
                System.out.println("\nYou're Logged in!!");
            }

            StudentLogin s1 = new StudentLogin(email, password);
            s1.displayDetails();
            s1.saveToFile();

            System.out.println("Login again? (Y/N)");
            String login = scanner.nextLine().trim().toUpperCase();

            if (!login.equals("Y")) {
                System.out.println("Exiting program... Goodbye!");
                scanner.close();
                System.exit(0);
            }
        }
    }
}