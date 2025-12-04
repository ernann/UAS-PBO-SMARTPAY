package com.app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController extends BaseController {

    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;

    @FXML
    private void handleLogin(ActionEvent event) {
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText().trim();

        if (email.isEmpty()) {
            showAlert(javafx.scene.control.Alert.AlertType.WARNING, "Peringatan", "Email tidak boleh kosong!");
            return;
        }

        if (!isPasswordValid(password)) {
            showAlert(javafx.scene.control.Alert.AlertType.ERROR, "Password Tidak Valid",
                    "Password harus mengandung:\n" +
                            "- Min 8 karakter\n" +
                            "- Huruf besar (A-Z)\n" +
                            "- Huruf kecil (a-z)\n" +
                            "- Angka (0-9)");
            return;
        }

        // ✅ Ambil user dari SharedData
        User user = SharedData.getRegisteredUser();
        if (user == null || !email.equals(user.getEmail()) || !password.equals(user.getPassword())) {
            showAlert(javafx.scene.control.Alert.AlertType.ERROR, "Login Gagal", "Email atau password salah!");
            return;
        }

        // Login sukses → masuk Home
        try {
            App.setRoot("Home");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        try {
            App.setRoot("Register");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleForgotPassword(ActionEvent event) {
        try {
            App.setRoot("Verif");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isPasswordValid(String pass) {
        if (pass.length() < 8) return false;
        if (!pass.matches(".*[A-Z].*")) return false;   // huruf besar
        if (!pass.matches(".*[a-z].*")) return false;   // huruf kecil
        if (!pass.matches(".*[0-9].*")) return false;   // angka
        return true;
    }
}
