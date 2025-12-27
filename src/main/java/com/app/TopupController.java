package com.app;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

// ================= ABSTRACT & METODE (TIDAK DIUBAH) =================
abstract class MetodeTopup {
    protected String namaMetode;
    public MetodeTopup(String namaMetode) {
        this.namaMetode = namaMetode;
    }
    public abstract String prosesTopup();
}

class MetodeBank extends MetodeTopup {
    public MetodeBank(String namaMetode) {
        super(namaMetode);
    }
    @Override
    public String prosesTopup() {
        return "Top Up via BANK " + namaMetode + " berhasil diinisiasi.";
    }
}

class MetodeRetail extends MetodeTopup {
    public MetodeRetail(String namaMetode) {
        super(namaMetode);
    }
    @Override
    public String prosesTopup() {
        String kodeBayar = "RTL-" + (System.currentTimeMillis() % 10000);
        return "Top Up via RETAIL " + namaMetode +
               ". Silakan bayar dengan kode: " + kodeBayar;
    }
}

// ================= CONTROLLER =================
public class TopupController {

    @FXML private Button backButton;
    @FXML private VBox logContainer;

    private Label logLabel;

    @FXML
    public void initialize() {
        // Setup log
        logLabel = new Label("");
        logLabel.setWrapText(true);
        logContainer.getChildren().add(logLabel);

        // 🔁 REVISI FINAL: BACK KE HOME
        backButton.setOnAction(event -> {
            updateLog("Kembali ke Home");
            App.setRoot("Home"); 
        });
    }

    private void updateLog(String message) {
        String time = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        logLabel.setText(time + " - " + message + "\n" + logLabel.getText());
        System.out.println(message);
    }

    // ================= HANDLER METODE =================

     @FXML
    private void handleBack() {
        App.setRoot("Home");
    }

    @FXML
    private void handleBCAOneklik() {
        updateLog(new MetodeBank("BCA OneKlik").prosesTopup());
    }

    @FXML
    private void handleBRIDirectDebit() {
        updateLog(new MetodeBank("BRI Direct Debit").prosesTopup());
    }

    @FXML
    private void handleBluBCA() {
        updateLog(new MetodeBank("Blu BCA Digital").prosesTopup());
    }

    @FXML
    private void handleAlfamart() {
        updateLog(new MetodeRetail("Alfamart").prosesTopup());
        App.setRoot("AlfaTopup"); // ⬅️ NAVIGASI STABIL
    }

    @FXML
    private void handleLawson() {
        updateLog(new MetodeRetail("Lawson").prosesTopup());
    }

    @FXML
    private void handleIndomaret() {
        updateLog(new MetodeRetail("Indomaret").prosesTopup());
    }
}
