package com.app;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    
    @FXML
    private void handleLogin() {
        String username = txtEmail.getText().trim();
        String password = txtPassword.getText();
        
        System.out.println("\n=== PROSES LOGIN ===");
        System.out.println("Username: " + username);
        
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Login Gagal", "Username dan password wajib diisi!");
            return;
        }
        
        User user = UserDatabase.authenticate(username, password);
        
        if (user != null) {
            System.out.println("✅ Login BERHASIL!");
            
            // Set user data
            UserData.setNama(user.getNama());
            UserData.setSmartId(user.getSmartId());
            UserData.setEmail(user.getEmail());
            UserData.setNomorRekening(user.getNomorRekening());
            UserData.setSaldo(user.getSaldo());
            UserData.setPin(user.getPin());
            SaldoManager.setSaldo(user.getSaldo());
            
            System.out.println("   Nama: " + UserData.getNama());
            System.out.println("   Smart ID: " + UserData.getSmartId());
            System.out.println("   Rekening: " + UserData.getNomorRekening());
            System.out.println("   Saldo: " + UserData.getSaldoFormatted());
            
            // Debug info
            UserDatabase.printAllUsers();
            UserDatabase.printAllRekening();
            
            // Navigasi ke Home
            try {
                App.setRoot("Home");
            } catch (Exception e) {
                System.err.println("❌ Error saat navigasi ke Home: " + e.getMessage());
                showAlert("Error", "Gagal membuka halaman utama: " + e.getMessage());
            }
            
        } else {
            showAlert("Login Gagal", "Username atau password salah!");
            System.out.println("❌ Login GAGAL: Username/password salah");
        }
    }
    
    @FXML
    private void handleForgotPassword() {
        showAlert("Lupa Password", "Fitur ini belum tersedia. Hubungi admin.");
    }
    
    @FXML
    private void handleRegister() {
        App.setRoot("Register");
    }
    
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }
}