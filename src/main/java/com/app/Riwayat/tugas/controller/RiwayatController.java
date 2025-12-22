package com.app.Riwayat.tugas.controller;

import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class RegisterController {

    @FXML private TextField txtNama;
    @FXML private TextField txtSmartId;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtRepeatPassword;
    @FXML private Button btnNext;
    @FXML private Button btnBack;
    @FXML private Text checkLength;
    @FXML private Text checkUpper;
    @FXML private Text checkLower;
    @FXML private Text checkNumber;
    @FXML private Pane mainPane;

    @FXML
    public void initialize() {
        System.out.println("=== REGISTER CONTROLLER INITIALIZED ===");
        
        txtPassword.textProperty().addListener((obs, oldVal, newVal) -> {
            updatePasswordValidation(newVal);
            updateNextButtonState();
        });
        
        txtRepeatPassword.textProperty().addListener((obs, oldVal, newVal) -> {
            updateRepeatPasswordValidation();
            updateNextButtonState();
        });
        
        txtSmartId.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                txtSmartId.setText(oldVal);
            }
            updateNextButtonState();
        });
        
        txtEmail.textProperty().addListener((obs, oldVal, newVal) -> {
            updateNextButtonState();
        });
        
        txtNama.textProperty().addListener((obs, oldVal, newVal) -> {
            updateNextButtonState();
        });
        
        setupTooltips();
        
        btnNext.setDisable(true);
        btnNext.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-background-radius: 15;");
    }
    
    private void setupTooltips() {
        txtNama.setTooltip(new Tooltip("Masukkan nama lengkap Anda"));
        txtSmartId.setTooltip(new Tooltip("Masukkan Smart ID (hanya angka 3-20 digit)"));
        txtEmail.setTooltip(new Tooltip("Contoh: user@example.com"));
        txtPassword.setTooltip(new Tooltip("Minimal 8 karakter, huruf besar, huruf kecil, dan angka"));
        txtRepeatPassword.setTooltip(new Tooltip("Ketik ulang password yang sama"));
    }
    
    private void updatePasswordValidation(String password) {
        if (password == null) {
            resetIndicators();
            return;
        }
        
        checkLength.setText(password.length() >= 8 ? "✓ Minimal 8 karakter" : "✗ Minimal 8 karakter");
        checkLength.setStyle(password.length() >= 8 ? "-fx-fill: #27ae60;" : "-fx-fill: #c0392b;");
        
        checkUpper.setText(password.matches(".*[A-Z].*") ? "✓ Huruf besar (A-Z)" : "✗ Huruf besar (A-Z)");
        checkUpper.setStyle(password.matches(".*[A-Z].*") ? "-fx-fill: #27ae60;" : "-fx-fill: #c0392b;");
        
        checkLower.setText(password.matches(".*[a-z].*") ? "✓ Huruf kecil (a-z)" : "✗ Huruf kecil (a-z)");
        checkLower.setStyle(password.matches(".*[a-z].*") ? "-fx-fill: #27ae60;" : "-fx-fill: #c0392b;");
        
        checkNumber.setText(password.matches(".*[0-9].*") ? "✓ Angka (0-9)" : "✗ Angka (0-9)");
        checkNumber.setStyle(password.matches(".*[0-9].*") ? "-fx-fill: #27ae60;" : "-fx-fill: #c0392b;");
    }
    
    private void updateRepeatPasswordValidation() {
        String password = txtPassword.getText();
        String repeat = txtRepeatPassword.getText();
        
        if (password != null && repeat != null && !repeat.isEmpty()) {
            if (password.equals(repeat)) {
                txtRepeatPassword.setStyle("-fx-border-color: #27ae60; -fx-border-width: 1; -fx-border-radius: 10;");
            } else {
                txtRepeatPassword.setStyle("-fx-border-color: #c0392b; -fx-border-width: 1; -fx-border-radius: 10;");
            }
        } else {
            txtRepeatPassword.setStyle("");
        }
    }
    
    private void updateNextButtonState() {
        boolean isValid = isFormValid();
        btnNext.setDisable(!isValid);
        btnNext.setStyle(isValid ? 
            "-fx-background-color: #1f76b4ff; -fx-text-fill: #c3e6ffff; -fx-font-weight: bold; -fx-background-radius: 15;" :
            "-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-background-radius: 15;");
    }
    
    private boolean isFormValid() {
        String nama = txtNama.getText();
        String smartId = txtSmartId.getText();
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        String repeat = txtRepeatPassword.getText();
        
        if (nama == null || nama.trim().isEmpty()) return false;
        if (smartId == null || smartId.trim().isEmpty()) return false;
        if (email == null || email.trim().isEmpty()) return false;
        if (password == null || password.isEmpty()) return false;
        if (repeat == null || repeat.isEmpty()) return false;
        
        if (!smartId.matches("\\d{3,20}")) return false;
        if (!isValidEmail(email)) return false;
        if (!isPasswordValid()) return false;
        if (!password.equals(repeat)) return false;
        
        return true;
    }
    
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
    
    private boolean isPasswordValid() {
        String password = txtPassword.getText();
        if (password == null) return false;
        return password.length() >= 8 &&
               password.matches(".*[A-Z].*") &&
               password.matches(".*[a-z].*") &&
               password.matches(".*[0-9].*");
    }
    
    @FXML
    private void handleNext() {
        System.out.println("\n=== PROSES REGISTER DIMULAI ===");
        
        if (validateForm()) {
            createPinAndCompleteRegistration();
        }
    }
    
    private void createPinAndCompleteRegistration() {
        TextInputDialog pinDialog = new TextInputDialog();
        pinDialog.setTitle("Buat PIN");
        pinDialog.setHeaderText("Buat PIN 6 digit untuk keamanan transaksi");
        pinDialog.setContentText("Masukkan PIN (6 digit angka):");
        
        Optional<String> pinResult = pinDialog.showAndWait();
        
        if (pinResult.isPresent()) {
            String pin = pinResult.get();
            
            if (!isValidPin(pin)) {
                showAlert("PIN Tidak Valid", 
                         "PIN harus 6 digit angka!", 
                         Alert.AlertType.ERROR);
                return;
            }
            
            TextInputDialog confirmPinDialog = new TextInputDialog();
            confirmPinDialog.setTitle("Konfirmasi PIN");
            confirmPinDialog.setHeaderText("Masukkan ulang PIN untuk konfirmasi");
            confirmPinDialog.setContentText("Konfirmasi PIN:");
            
            Optional<String> confirmResult = confirmPinDialog.showAndWait();
            
            if (confirmResult.isPresent()) {
                String confirmPin = confirmResult.get();
                
                if (!pin.equals(confirmPin)) {
                    showAlert("PIN Tidak Cocok", 
                             "PIN konfirmasi tidak sama!", 
                             Alert.AlertType.ERROR);
                    return;
                }
                
                boolean saveSuccess = saveUserToDatabase(pin);
                
                if (saveSuccess) {
                    showSuccessAlertAndNavigate();
                } else {
                    showAlert("Error", 
                             "Smart ID atau Email sudah digunakan!", 
                             Alert.AlertType.ERROR);
                }
            }
        }
    }
    
    private boolean isValidPin(String pin) {
        return pin != null && pin.matches("\\d{6}");
    }
    
    private boolean saveUserToDatabase(String pin) {
        String nama = txtNama.getText().trim();
        String smartId = txtSmartId.getText().trim();
        String email = txtEmail.getText().trim().toLowerCase();
        String password = txtPassword.getText();
        
        System.out.println("\n=== MENYIMPAN USER BARU ===");
        System.out.println("Nama: " + nama);
        System.out.println("Smart ID: " + smartId);
        System.out.println("Email: " + email);
        System.out.println("PIN: " + pin);
        
        if (UserDatabase.isUserExists(smartId, email)) {
            System.out.println("✗ User sudah ada");
            return false;
        }
        
        User newUser = new User(smartId, email, password, nama);
        newUser.setPin(pin);
        
        boolean saved = UserDatabase.saveUser(newUser);
        
        if (saved) {
            UserData.setNama(nama);
            UserData.setSmartId(smartId);
            UserData.setEmail(email);
            UserData.setNomorRekening(newUser.getNomorRekening());
            UserData.setPin(pin);
            UserData.setSaldo(newUser.getSaldo()); // Saldo 0
            
            com.app.Transfer.SaldoManager.setSaldo(newUser.getSaldo()); // Saldo 0
            
            System.out.println("✓ User berhasil dibuat");
            System.out.println("  Nomor Rekening: " + newUser.getNomorRekening());
            System.out.println("  Saldo Awal: Rp 0 (SILAKAN TOP UP TERLEBIH DAHULU)"); // Update pesan
            System.out.println("  PIN: " + pin);
            
            UserDatabase.printAllUsers();
            UserDatabase.printAllRekening();
            
            return true;
        }
        
        return false;
    }
    
    private void showSuccessAlertAndNavigate() {
        String rekening = UserData.getNomorRekening();
        String formattedRekening = formatRekening(rekening);
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("REGISTER BERHASIL!");
        alert.setHeaderText("🎉 Akun SmartPay Anda Telah Dibuat!");
        alert.setContentText(
            "Nama: " + txtNama.getText() + "\n" +
            "Smart ID: " + txtSmartId.getText() + "\n" +
            "Email: " + txtEmail.getText() + "\n" +
            "Nomor Rekening: " + formattedRekening + "\n" +
            "Saldo Awal: Rp 0\n\n" + // Update: saldo 0
            "⚠️  CATATAN: \n" +
            "1. Saldo Anda saat ini Rp 0\n" +
            "2. Lakukan TOP UP terlebih dahulu untuk melakukan transaksi\n" +
            "3. PIN Anda: 6 digit yang sudah Anda buat\n\n" +
            "Catat nomor rekening Anda!\n" +
            "Untuk login gunakan:\n" +
            "• Smart ID: " + txtSmartId.getText() + " ATAU\n" +
            "• Email: " + txtEmail.getText()
        );
        
        ButtonType loginButton = new ButtonType("Login Sekarang", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(loginButton);
        
        alert.showAndWait().ifPresent(response -> {
            if (response == loginButton) {
                System.out.println("Navigasi ke Login...");
                try {
                    App.setRoot("Login");
                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
                }
            }
        });
    }
    
    private String formatRekening(String rekening) {
        if (rekening.length() == 10) {
            return rekening.substring(0, 3) + "-" + 
                   rekening.substring(3, 6) + "-" + 
                   rekening.substring(6);
        }
        return rekening;
    }
    
    @FXML
    private void handleBack() {
        System.out.println("Tombol Back diklik!");
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Kembali ke Login");
        alert.setHeaderText("Yakin ingin kembali?");
        alert.setContentText("Data yang sudah diisi akan hilang.");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                System.out.println("User memilih OK, kembali ke Login...");
                try {
                    App.setRoot("Login");
                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
                }
            } else {
                System.out.println("User membatalkan");
            }
        });
    }
    
    private boolean validateForm() {
        String nama = txtNama.getText().trim();
        if (nama.isEmpty()) {
            showAlert("Error", "Nama tidak boleh kosong!", Alert.AlertType.ERROR);
            txtNama.requestFocus();
            return false;
        }
        
        String smartId = txtSmartId.getText().trim();
        if (smartId.isEmpty()) {
            showAlert("Error", "Smart ID tidak boleh kosong!", Alert.AlertType.ERROR);
            txtSmartId.requestFocus();
            return false;
        }
        if (!smartId.matches("\\d+")) {
            showAlert("Error", "Smart ID harus angka!", Alert.AlertType.ERROR);
            txtSmartId.requestFocus();
            return false;
        }
        if (smartId.length() < 3 || smartId.length() > 20) {
            showAlert("Error", "Smart ID harus 3-20 digit!", Alert.AlertType.ERROR);
            txtSmartId.requestFocus();
            return false;
        }
        
        String email = txtEmail.getText().trim();
        if (email.isEmpty()) {
            showAlert("Error", "Email tidak boleh kosong!", Alert.AlertType.ERROR);
            txtEmail.requestFocus();
            return false;
        }
        if (!isValidEmail(email)) {
            showAlert("Error", "Format email tidak valid!", Alert.AlertType.ERROR);
            txtEmail.requestFocus();
            return false;
        }
        
        if (!isPasswordValid()) {
            showAlert("Error", "Password tidak memenuhi kriteria!", Alert.AlertType.ERROR);
            txtPassword.requestFocus();
            return false;
        }
        
        if (!txtPassword.getText().equals(txtRepeatPassword.getText())) {
            showAlert("Error", "Password tidak sama!", Alert.AlertType.ERROR);
            txtRepeatPassword.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void resetIndicators() {
        checkLength.setText("Minimal 8 karakter");
        checkUpper.setText("Huruf besar (A-Z)");
        checkLower.setText("Huruf kecil (a-z)");
        checkNumber.setText("Angka (0-9)");
        
        checkLength.setStyle("");
        checkUpper.setStyle("");
        checkLower.setStyle("");
        checkNumber.setStyle("");
    }
}