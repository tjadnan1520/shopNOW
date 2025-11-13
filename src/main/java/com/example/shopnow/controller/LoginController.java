package com.example.shopnow.controller;

import com.example.shopnow.DPApply.Singleton;
import com.example.shopnow.Utill.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController implements Controller {

    private Stage stage;

    @FXML private TextField username;
    @FXML private PasswordField password;

    @Override
    public String getTitle() {
        return "ShopNow: Login";
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        if (this.stage != null) {
            this.stage.setTitle(getTitle());
            this.stage.setOnCloseRequest(event -> {
                try {
                    DatabaseConnection.getInstance().closeConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private Connection getConnection() throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        if (conn == null || conn.isClosed()) {
            throw new SQLException("Database connection is null or closed");
        }
        return conn;
    }

    private String getUsername() {
        return username.getText().trim();
    }

    private String getPassword() {
        return password.getText();
    }

    private boolean handleLogin() {
        String enteredUsername = getUsername();
        String enteredPassword = getPassword();

        if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Missing Information", "Please enter both username and password!");
            return false;
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT id, password, role FROM users WHERE email = ? OR name = ?")) {

            stmt.setString(1, enteredUsername);
            stmt.setString(2, enteredUsername);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int userId = rs.getInt("id");
                    String storedHash = rs.getString("password");
                    String role = rs.getString("role");

                    if (BCrypt.checkpw(enteredPassword, storedHash)) {
                        showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + role + "!");

                        Singleton session = Singleton.getInstance();
                        session.setUserId(userId);
                        session.setUserRole(role);
                        session.setUserName(enteredUsername);

                        try (PreparedStatement insertStmt = conn.prepareStatement(
                                "INSERT INTO login_history (user_id) VALUES (?)")) {
                            insertStmt.setInt(1, userId);
                            insertStmt.executeUpdate();
                        }

                        return true;
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Login Failed", "Wrong password!");
                        return false;
                    }
                } else {
                    showAlert(Alert.AlertType.ERROR, "Login Failed", "User not found!");
                    return false;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not connect to database: " + e.getMessage());
            return false;
        }
    }

    @FXML
    protected void clickLogin() {
        if (handleLogin()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/shopnow/Homepage.fxml"));
                Parent root = loader.load();

                HomeController home = loader.getController();
                home.setStage(stage);

                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("ShopNow: Home");
                stage.show();
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to load Home page: " + e.getMessage());
            }
        }
    }

    @FXML
    private void goToForgotPassword() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/shopnow/ForgetPassword.fxml"));
            Parent root = loader.load();

            ForgetPasswordController controller = loader.getController();
            Stage popupStage = new Stage();
            controller.setStage(popupStage);

            popupStage.setScene(new Scene(root));
            popupStage.setTitle("ShopNow: Reset Password");
            popupStage.setResizable(false);
            popupStage.initOwner(stage);
            popupStage.initModality(javafx.stage.Modality.WINDOW_MODAL);
            popupStage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load Forgot Password page: " + e.getMessage());
        }
    }

    @FXML
    protected void backHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/shopnow/EntryPage.fxml"));
            Parent root = loader.load();

            EntryController entryController = loader.getController();
            entryController.setStage(stage);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("ShopNow: Entry");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load Entry page: " + e.getMessage());
        }
    }

    @FXML
    private void onHover(MouseEvent event) {
        Button btn = (Button) event.getSource();
        btn.setStyle("-fx-background-color: #8e8e8e8e; -fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold; -fx-background-radius: 30;");
    }

    @FXML
    private void onExit(MouseEvent event) {
        Button btn = (Button) event.getSource();
        btn.setStyle("-fx-background-color: #8e8e8e8e; -fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold; -fx-background-radius: 30;");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
