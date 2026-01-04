package com.app.Topup;

import java.util.Optional;

import com.app.App;
import com.app.SaldoManager;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;

public class TopupController {

    @FXML private Button backButton;
    @FXML private Label labelSaldo;

    @FXML
    public void initialize() {
        System.out.println("=== TOPUP CONTROLLER INITIALIZE ===");
        updateSaldoLabel();
        
        if (backButton != null) {
            backButton.setOnAction(event -> {
                System.out.println("Back button clicked");
                App.setRoot("Home");
            });
        }
        
        System.out.println("✓ TopupController initialized successfully");
    }
    
    private void updateSaldoLabel() {
        if (labelSaldo != null) {
            String formattedSaldo = SaldoManager.formatSaldo();
            labelSaldo.setText("Saldo: " + formattedSaldo);
            System.out.println("Saldo label updated: " + formattedSaldo);
        }
    }

    private Optional<Long> showNominalDialog(String metode) {
        TextInputDialog dialog = new TextInputDialog("50000");
        dialog.setTitle("Top Up " + metode);
        dialog.setHeaderText("Masukkan Nominal Top Up");
        dialog.setContentText("Nominal (Rp):");
        
        dialog.getDialogPane().setStyle(
            "-fx-background-color: white; " +
            "-fx-border-color: #114687ff; " +
            "-fx-border-width: 2px; " +
            "-fx-border-radius: 10px;"
        );
        
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                String input = result.get().trim();
                if (input.isEmpty()) {
                    showAlert("Gagal", "Nominal tidak boleh kosong!");
                    return Optional.empty();
                }
                
                long nominal = Long.parseLong(input);
                
                if (nominal < 10000) {
                    showAlert("Gagal", "Minimal Top Up Rp10.000");
                    return Optional.empty();
                }
                
                if (nominal > 10000000) {
                    showAlert("Gagal", "Maksimal Top Up Rp10.000.000");
                    return Optional.empty();
                }
                
                System.out.println("Nominal dipilih: Rp" + nominal + " untuk metode " + metode);
                return Optional.of(nominal);
                
            } catch (NumberFormatException e) {
                showAlert("Gagal", "Nominal harus berupa angka!");
                return Optional.empty();
            }
        }
        return Optional.empty();
    }
    
    @FXML
    private void handleBack() {
        System.out.println("Navigasi kembali ke Home");
        App.setRoot("Home");
    }

    @FXML
    private void handleBCAOneklik() {
        System.out.println("Metode BCA OneKlik dipilih");
        processTopupMethod("BCA OneKlik", "🏦");
    }

    @FXML
    private void handleBRIDirectDebit() {
        System.out.println("Metode BRI Direct Debit dipilih");
        processTopupMethod("BRI Direct Debit", "🏦");
    }

    @FXML
    private void handleBNI() {
        System.out.println("Metode BNI dipilih");
        processTopupMethod("BNI", "💳");
    }

    @FXML
    private void handleAlfamart() {
        System.out.println("Metode Alfamart dipilih");
        processTopupMethod("Alfamart", "🏪");
    }

    @FXML
    private void handleLawson() {
        System.out.println("Metode Lawson dipilih");
        processTopupMethod("Lawson", "🏪");
    }

    @FXML
    private void handleIndomaret() {
        System.out.println("Metode Indomaret dipilih");
        processTopupMethod("Indomaret", "🛒");
    }
    
    private void processTopupMethod(String metode, String icon) {
        System.out.println("Memproses top up dengan metode: " + metode);
        
        Optional<Long> nominalOpt = showNominalDialog(metode);
        
        if (nominalOpt.isPresent()) {
            long nominal = nominalOpt.get();
            
            boolean confirmed = showConfirmationDialog(metode, nominal);
            
            if (confirmed) {
                setDataKeTempData(metode, nominal, icon);
                App.setRoot("DetailTopup");
            } else {
                System.out.println("Top up dibatalkan oleh user");
            }
        }
    }
    
    private boolean showConfirmationDialog(String metode, long nominal) {
        String detailMethod = "";
        
        if (metode.contains("Alfamart") || metode.contains("Lawson") || metode.contains("Indomaret")) {
            detailMethod = "\n\n⚠️ Anda akan mendapat kode pembayaran untuk ditunjukkan di kasir.";
        } else if (metode.contains("BCA") || metode.contains("BRI") || metode.contains("Blu")) {
            detailMethod = "\n\n⚠️ Anda akan mendapat Virtual Account untuk pembayaran.";
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Top Up");
        alert.setHeaderText("Konfirmasi Top Up " + metode);
        alert.setContentText(
            "Apakah Anda yakin ingin melakukan top up?\n\n" +
            "Metode: " + metode + "\n" +
            "Nominal: " + SaldoManager.formatSaldo(nominal) + detailMethod
        );
        
        alert.getDialogPane().setStyle(
            "-fx-background-color: white; " +
            "-fx-border-color: #1a5fb4; " +
            "-fx-border-width: 2px; " +
            "-fx-border-radius: 10px;"
        );
        
        javafx.scene.control.ButtonType yesButton = new javafx.scene.control.ButtonType("Ya, Lanjutkan", 
            javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
        javafx.scene.control.ButtonType noButton = new javafx.scene.control.ButtonType("Batal", 
            javafx.scene.control.ButtonBar.ButtonData.CANCEL_CLOSE);
        
        alert.getButtonTypes().setAll(yesButton, noButton);
        
        java.util.Optional<javafx.scene.control.ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == yesButton;
    }
    
    // Di dalam method setDataKeTempData(), tambahkan semua data:
private void setDataKeTempData(String metode, long nominal, String icon) {
    // Set SEMUA data dengan benar
    TempData.setMetodeTopup(metode);
    TempData.setNominalTopup(nominal);
    TempData.setIconMetode(icon);
    
    // Generate dan set kode transaksi
    String kodeTransaksi = TempData.generateKodeTransaksi();
    TempData.setKodeTransaksi(kodeTransaksi);
    
    // Set judul dan info berdasarkan metode
    String judul = "Top Up " + metode;
    String info = "Pembayaran via " + metode;
    String detail = "Masukkan kode pembayaran setelah melakukan pembayaran";
    
    TempData.setJudulTopup(judul);
    TempData.setInfoMetode(info);
    TempData.setDetailInfo(detail);
    
    System.out.println("Data disimpan ke TempData:");
    System.out.println("  Metode: " + metode);
    System.out.println("  Nominal: " + nominal);
    System.out.println("  Judul: " + judul);
    System.out.println("  Kode Transaksi: " + kodeTransaksi);
}

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().setStyle(
            "-fx-background-color: white; " +
            "-fx-border-color: #114d98ff; " +
            "-fx-border-width: 2px; " +
            "-fx-border-radius: 10px;"
        );
        alert.showAndWait();
    }
}