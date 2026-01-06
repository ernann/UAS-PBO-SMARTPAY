package com.app.Registrasi;

import com.app.App;
import com.app.SaldoManager;
import com.app.Transfer.RiwayatPembayaran;
import com.app.User;
import com.app.UserData;
import com.app.UserDatabase;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;

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
            RiwayatPembayaran.load();
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

    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle("Forgot Password");
    dialog.setHeaderText("Reset Password");
    dialog.setContentText("Masukkan Email atau Smart ID:");

    dialog.showAndWait().ifPresent(input -> {
        input = input.trim();

        if (input.isEmpty()) {
            showAlert("Error", "Email atau Smart ID tidak boleh kosong!");
            return;
        }

        User user = UserDatabase.getUser(input);

        if (user == null) {
            showAlert("Gagal", "User tidak ditemukan!");
            return;
        }

        PasswordField pfNew = new PasswordField();
        pfNew.setPromptText("Password baru");

        PasswordField pfConfirm = new PasswordField();
        pfConfirm.setPromptText("Konfirmasi password");

        VBox box = new VBox(10, pfNew, pfConfirm);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Reset Password");
        alert.setHeaderText("Masukkan password baru");
        alert.getDialogPane().setContent(box);

        alert.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) {
                String newPass = pfNew.getText();
                String confirm = pfConfirm.getText();

                PasswordValidator validator = new PasswordValidator();

                if (!validator.validatePassword(newPass)) {
                    showAlert("Password Lemah",
                            String.join("\n", validator.getPasswordRequirements()));
                    return;
                }

                if (!newPass.equals(confirm)) {
                    showAlert("Error", "Konfirmasi password tidak cocok!");
                    return;
                }

                user.setPassword(newPass);
                UserDatabase.updateUser(user);

                showAlert("Berhasil", "Password berhasil direset.\nSilakan login kembali.");
            }
        });
    });
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