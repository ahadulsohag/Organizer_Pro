/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package organizer.pro;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.util.Date;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class FXMLDocumentController {

    @FXML
    private CheckBox login_checkBox;

    @FXML
    private AnchorPane login_form;

    @FXML
    private Button login_loginBtn;

    @FXML
    private PasswordField login_password;

    @FXML
    private Hyperlink login_registerHere;

    @FXML
    private TextField login_showPassword;

    @FXML
    private ComboBox<?> login_user;

    @FXML
    private TextField login_username;

    @FXML
    private AnchorPane main_form;

    @FXML
    private CheckBox register_checkBox;

    @FXML
    private TextField register_email;

    @FXML
    private AnchorPane register_form;

    @FXML
    private Hyperlink register_loginHere;

    @FXML
    private PasswordField register_password;

    @FXML
    private TextField register_showPassword;

    @FXML
    private Button register_signupBtn;

    @FXML
    private TextField register_username;
    
    //database tools
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private AlertMessage alert = new AlertMessage();

    public void loginAccount() {
        System.out.println("Login button clicked!"); // Debug line
        
        if (login_username.getText().isEmpty() || login_password.getText().isEmpty()) {
            alert.errorMessage("Please fill in both username and password!");
            return;
        }

        String sql = "SELECT * FROM admin WHERE username = ? AND password = ?";
        connect = Database.connectDB();

        if (connect == null) {
            alert.errorMessage("Database connection failed! Please check if XAMPP MySQL is running.");
            return;
        }

        try {
            // Sync password fields
            if (!login_showPassword.isVisible()) {
                if (!login_showPassword.getText().equals(login_password.getText())) {
                    login_showPassword.setText(login_password.getText());
                }
            } else {
                if (!login_showPassword.getText().equals(login_password.getText())) {
                    login_password.setText(login_showPassword.getText());
                }
            }

            prepare = connect.prepareStatement(sql);
            prepare.setString(1, login_username.getText().trim());
            prepare.setString(2, login_password.getText().trim());
            
            System.out.println("Executing login query..."); // Debug line
            result = prepare.executeQuery();

            if (result.next()) {
                // Store user data
                Data.admin_username = login_username.getText().trim();
                Data.admin_id = result.getInt("admin_id");
                
                System.out.println("Login successful for user: " + Data.admin_username); // Debug line

                alert.SuccessMessage("Login Successfully!");

                // Load the main dashboard
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("MainPage.fxml"));
                    Parent root = loader.load();
                    
                    // Get the controller and set the welcome message
                    MainDashboardController dashboardController = loader.getController();
                    dashboardController.setWelcomeUser(login_username.getText().trim());
                    
                    Stage stage = new Stage();
                    stage.setTitle("Organizer Pro - Dashboard");
                    stage.setScene(new Scene(root));
                    stage.setMaximized(true);
                    stage.show();

                    // Hide the login window
                    login_loginBtn.getScene().getWindow().hide();
                    
                } catch (Exception dashboardError) {
                    System.out.println("Error loading dashboard: " + dashboardError.getMessage());
                    dashboardError.printStackTrace();
                    alert.errorMessage("Error loading dashboard: " + dashboardError.getMessage());
                }
            } else {
                alert.errorMessage("Incorrect Username/Password!");
            }

        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
            e.printStackTrace();
            alert.errorMessage("Login error: " + e.getMessage());
        } finally {
            try {
                if (result != null) result.close();
                if (prepare != null) prepare.close();
                if (connect != null) connect.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void registerAccount() {
        if (register_email.getText().isEmpty()
                || register_username.getText().isEmpty()
                || register_password.getText().isEmpty()) {
            alert.errorMessage("Please fill all the blank fields");
            return;
        }

        connect = Database.connectDB();
        
        if (connect == null) {
            alert.errorMessage("Database connection failed! Please check if XAMPP MySQL is running.");
            return;
        }

        try {
            // Sync password fields
            if (!register_showPassword.isVisible()) {
                if (!register_showPassword.getText().equals(register_password.getText())) {
                    register_showPassword.setText(register_password.getText());
                }
            } else {
                if (!register_showPassword.getText().equals(register_password.getText())) {
                    register_password.setText(register_showPassword.getText());
                }
            }

            // Check if username already exists
            String checkUsername = "SELECT * FROM admin WHERE username = ?";
            prepare = connect.prepareStatement(checkUsername);
            prepare.setString(1, register_username.getText().trim());
            result = prepare.executeQuery();

            if (result.next()) {
                alert.errorMessage(register_username.getText() + " already exists!");
            } else if (register_password.getText().length() < 8) {
                alert.errorMessage("Invalid Password, at least 8 characters needed");
            } else {
                String insertData = "INSERT INTO admin(email, username, password, date) VALUES(?, ?, ?, ?)";
                Date date = new Date();
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                prepare = connect.prepareStatement(insertData);
                prepare.setString(1, register_email.getText().trim());
                prepare.setString(2, register_username.getText().trim());
                prepare.setString(3, register_password.getText().trim());
                prepare.setDate(4, sqlDate);

                prepare.executeUpdate();
                alert.SuccessMessage("Registered Successfully!");
                registerClear();
                login_form.setVisible(true);
                register_form.setVisible(false);
            }
        } catch (Exception e) {
            System.out.println("Registration error: " + e.getMessage());
            e.printStackTrace();
            alert.errorMessage("Registration error: " + e.getMessage());
        } finally {
            try {
                if (result != null) result.close();
                if (prepare != null) prepare.close();
                if (connect != null) connect.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void registerClear() {
        register_email.clear();
        register_username.clear();
        register_password.clear();
        register_showPassword.clear();
    }

    public void loginShowPassword() {
        if (login_checkBox.isSelected()) {
            login_showPassword.setText(login_password.getText());
            login_showPassword.setVisible(true);
            login_password.setVisible(false);
        } else {
            login_password.setText(login_showPassword.getText());
            login_showPassword.setVisible(false);
            login_password.setVisible(true);
        }
    }

    public void registerShowPassword() {
        if (register_checkBox.isSelected()) {
            register_showPassword.setText(register_password.getText());
            register_showPassword.setVisible(true);
            register_password.setVisible(false);
        } else {
            register_password.setText(register_showPassword.getText());
            register_showPassword.setVisible(false);
            register_password.setVisible(true);
        }
    }

    public static class Data {
        public static String admin_username;
        public static int admin_id;
    }

    public void switchForm(ActionEvent event) {
        if (event.getSource() == login_registerHere) {
            login_form.setVisible(false);
            register_form.setVisible(true);
        } else if (event.getSource() == register_loginHere) {
            login_form.setVisible(true);
            register_form.setVisible(false);
        }
    }
}