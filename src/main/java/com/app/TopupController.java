package com.app;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Abstract class untuk metode Top Up
abstract class MetodeTopup {
    protected String namaMetode;
    public MetodeTopup(String namaMetode) { this.namaMetode = namaMetode; }
    public abstract String prosesTopup();
}

// BANK
class MetodeBank extends MetodeTopup {
    public MetodeBank(String namaMetode) { super(namaMetode); }
    @Override
    public String prosesTopup() {
        return "Top Up via BANK " + namaMetode + " berhasil diinisiasi.";
    }
}

// RETAIL
class MetodeRetail extends MetodeTopup {
    public MetodeRetail(String namaMetode) { super(namaMetode); }
    @Override
    public String prosesTopup() {
        String kodeBayar = "RTL-" + (System.currentTimeMillis() % 10000);
        return "Top Up via RETAIL " + namaMetode + ". Silakan bayar dengan kode: " + kodeBayar;
    }
}

public class TopupController {

    @FXML private Button backButton;
    @FXML private VBox logContainer;

    private Label logLabel;

    @FXML
    public void initialize() {
        // Siapkan log
        logLabel = new Label("");
        logLabel.setWrapText(true);
        logContainer.getChildren().add(logLabel);

        // Tombol Back → kembali ke Home
        backButton.setOnAction((ActionEvent event) -> {
            updateLog("Tombol Kembali ditekan");
            try {
                App.setRoot("Home");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void updateLog(String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        logLabel.setText(timestamp + " - " + message + "\n" + logLabel.getText());
        System.out.println(message);
    }

    // Event handler metode Top Up
    @FXML private void handleBCAOneklik() { updateLog(new MetodeBank("BCA OneKlik").prosesTopup()); }
    @FXML private void handleBRIDirectDebit() { updateLog(new MetodeBank("BRI Direct Debit").prosesTopup()); }
    @FXML private void handleBluBCA() { updateLog(new MetodeBank("Blu BCA Digital").prosesTopup()); }
    @FXML private void handleAlfamart() { updateLog(new MetodeRetail("Alfamart").prosesTopup()); }
    @FXML private void handleLawson() { updateLog(new MetodeRetail("Lawson").prosesTopup()); }
    @FXML private void handleIndomaret() { updateLog(new MetodeRetail("Indomaret").prosesTopup()); }
}
