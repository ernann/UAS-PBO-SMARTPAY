package com.app;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;

import java.time.LocalDate;
import java.util.List;

import javafx.event.ActionEvent;

public class HomeController {

    @FXML
    private void initialize() {
        System.out.println("Dashboard dimuat. Siap menampilkan data.");

        // cek notifikasi Reminder langsung di Home
        checkReminderNotifications();
    }

    private void checkReminderNotifications() {
        List<Reminder> reminderList = ReminderStorage.load();
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        for (Reminder r : reminderList) {
            if (r.getJatuhTempo().equals(today)) {
                showNotification("Reminder Hari Ini", 
                                 r.getJenis() + " | Rp" + r.getNominal() + " jatuh tempo hari ini!");
            } else if (r.getJatuhTempo().equals(tomorrow)) {
                showNotification("Reminder Besok", 
                                 r.getJenis() + " | Rp" + r.getNominal() + " jatuh tempo besok!");
            }
        }
    }

    private void showNotification(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }

    @FXML
    private void handleReminder(ActionEvent event) {
        App.setRoot("Reminder");
    }

    @FXML
    private void handleTopUp(ActionEvent event) {
        App.setRoot("Topup");
    }

    @FXML
    private void handlePembayaran(ActionEvent event) {
        // App.setRoot("Pembayaran");
    }

    @FXML
    private void handleRiwayat(ActionEvent event) {
        // App.setRoot("Riwayat");
    }
}
