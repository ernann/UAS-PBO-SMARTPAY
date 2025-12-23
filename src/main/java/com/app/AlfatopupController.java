package com.app;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.event.ActionEvent;

public class AlfaTopupController {

    @FXML private TextField txtNominal;
    @FXML private Label lblSaldo;

    @FXML
    public void initialize() {
        updateSaldoLabel();

        long nominal = TempData.getNominalTopup();
        if (nominal > 0) {
            txtNominal.setText(String.valueOf(nominal));
        }
    }

    @FXML
    private void handleNominalQuick(ActionEvent event) {
        Object source = event.getSource();
        if (source instanceof Button) {
            Button btn = (Button) source;
            String text = btn.getText().replace(".", "").replace("Rp", "").trim();
            txtNominal.setText(text);
        }
    }

    @FXML
    private void handleNext() {
        String input = txtNominal.getText().trim();
        if (input.isEmpty()) {
            showAlert("Gagal", "Nominal harus diisi!");
            return;
        }

        try {
            long nominal = Long.parseLong(input);

            if (nominal < 10000) {
                showAlert("Gagal", "Minimal Top Up Rp10.000");
                return;
            }

            String kodeStruk = "ALF-" + (System.currentTimeMillis() % 100000);

            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Konfirmasi Struk");
            dialog.setHeaderText("Transaksi Top-Up");
            dialog.setContentText(
                "Kode Struk: " + kodeStruk + "\n" +
                "Nominal: Rp" + String.format("%,d", nominal) + "\n\n" +
                "Masukkan kode struk untuk konfirmasi:"
            );

            String inputStruk = dialog.showAndWait().orElse("");

            if (!inputStruk.equals(kodeStruk)) {
                showAlert("Gagal", "Kode struk salah. Top Up dibatalkan.");
                return;
            }

            // Tambah saldo
            SaldoManager.tambahSaldo(nominal);
            updateSaldoLabel();

            // 🔹 Update HomeController supaya saldo langsung bertambah
            HomeController home = App.getHomeController();
            if (home != null) {
                home.updateSaldoHome();
            }

            // Simpan sementara
            TempData.setNominalTopup(nominal);
            TempData.setKodeStruk(kodeStruk);

            showAlert(
                "Top-Up Berhasil",
                "✅ Top-Up berhasil!\n" +
                "Nominal: Rp" + String.format("%,d", nominal) + "\n" +
                "Kode Struk: " + kodeStruk + "\n" +
                "Saldo Saat Ini: " + SaldoManager.formatSaldo()
            );

        } catch (NumberFormatException e) {
            showAlert("Gagal", "Nominal harus berupa angka!");
        }
    }

    // ===============================
    // HANYA BAGIAN INI YANG DIREVISI
    // ===============================
    @FXML
    private void handleBack() {
        try {
            // Kembali ke layar pilihan Top-Up
            App.setRoot("Topup");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Gagal kembali ke layar pilihan Top-Up");
        }
    }

    // ===============================
    // END REVISI
    // ===============================

    private void updateSaldoLabel() {
        lblSaldo.setText("Saldo Saat Ini: " + SaldoManager.formatSaldo());
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
