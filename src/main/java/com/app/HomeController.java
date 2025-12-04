package com.app;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class HomeController {

    @FXML
    private void handleTopUp(ActionEvent event) throws IOException {
        System.out.println("Tombol Top Up ditekan!");
        App.setRoot("Topup"); // pindah ke Topup.fxml
    }

    @FXML
    private void handlePembayaran(ActionEvent event) throws IOException {
        System.out.println("Tombol Pembayaran ditekan!");
        // App.setRoot("Pembayaran");
    }

    @FXML
    private void handleReminder(ActionEvent event) throws IOException {
        System.out.println("Tombol Reminder ditekan!");
        // App.setRoot("Reminder");
    }

    @FXML
    private void handleRiwayat(ActionEvent event) throws IOException {
        System.out.println("Tombol Riwayat ditekan!");
        // App.setRoot("Riwayat");
    }

    @FXML
    private void initialize() {
        System.out.println("Dashboard dimuat. Siap menampilkan data.");
    }
}
