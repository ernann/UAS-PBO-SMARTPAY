package com.app;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class VerifController {

    @FXML private TextField txtKodeOTP;
    @FXML private Button btnVerifikasi;
    @FXML private Button btnKirimUlang;

    private final String kodeOTPBenar = "123456";

    @FXML
    private void handleVerifikasi() {
        String kodeInput = txtKodeOTP.getText().trim();
        if (kodeInput.isEmpty()) {
            showAlert(AlertType.WARNING, "Peringatan", "Kode verifikasi tidak boleh kosong!");
        } else if (kodeInput.equals(kodeOTPBenar)) {
            showAlert(AlertType.INFORMATION, "Sukses", "Akun berhasil diverifikasi!");
        } else {
            showAlert(AlertType.ERROR, "Error", "Kode verifikasi salah!");
        }
    }

    @FXML
    private void handleKirimUlang() {
        showAlert(AlertType.INFORMATION, "Info", "Kode verifikasi baru telah dikirim ke email Anda.");
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
