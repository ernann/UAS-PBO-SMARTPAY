package com.app;

import java.time.LocalDate;
import java.util.List;

import com.app.Reminder.Reminder;
import com.app.Reminder.ReminderStorage;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;

public class HomeController {
    
    @FXML private Label labelNama;
    @FXML private Label labelRekening;
    @FXML private Label labelSaldo;
    @FXML private Button btnLogout;
    @FXML private Button btnReminder;
    @FXML private AnchorPane rootPane;
    
    private boolean hasShownReminder = false;
    
    @FXML
    public void initialize() {
        System.out.println("=== HOME CONTROLLER INITIALIZE ===");
        
        if (UserData.getNama() != null && !UserData.getNama().isEmpty()) {
            labelNama.setText("Hello, " + UserData.getNama());
        } else {
            labelNama.setText("Hello, User");
        }
        
        if (UserData.getNomorRekening() != null && !UserData.getNomorRekening().isEmpty()) {
            labelRekening.setText(UserData.getNomorRekening());
        } else {
            labelRekening.setText("1889782637");
        }
        
        labelSaldo.setText(SaldoManager.formatSaldo());
        
        Tooltip logoutTooltip = new Tooltip("Logout");
        logoutTooltip.setStyle("-fx-font-size: 12px; -fx-background-color: #333; -fx-text-fill: white;");
        btnLogout.setTooltip(logoutTooltip);
        
        System.out.println("✓ HomeController initialized successfully");
        
        checkAndShowReminders();
    }
    
   private void checkAndShowReminders() {

    if (UserData.isReminderNotifShown()) {
        return;
    }

    String smartId = UserData.getSmartId();
    if (smartId == null || smartId.isEmpty()) {
        return;
    }

    List<Reminder> userReminders = ReminderStorage.loadForUser(smartId);
    List<Reminder> activeReminders = userReminders.stream()
            .filter(r -> !r.getJatuhTempo().isAfter(LocalDate.now()))
            .collect(java.util.stream.Collectors.toList());

    if (activeReminders.isEmpty()) {
        return;
    }

    StringBuilder reminderMessage = new StringBuilder();
    reminderMessage.append("📌 Anda memiliki ")
            .append(activeReminders.size())
            .append(" tagihan:\n\n");

    for (Reminder r : activeReminders) {
        LocalDate dueDate = r.getJatuhTempo();
        LocalDate today = LocalDate.now();

        String status;
        if (dueDate.isBefore(today)) {
            long daysLate = java.time.temporal.ChronoUnit.DAYS.between(dueDate, today);
            status = "TERLAMBAT " + daysLate + " hari";
        } else {
            status = "HARI INI";
        }

        reminderMessage.append("• ")
                .append(r.getJenis())
                .append(" - ")
                .append(r.getFormattedNominal())
                .append("\n   Jatuh tempo: ")
                .append(dueDate)
                .append(" (")
                .append(status)
                .append(")\n\n");
    }

    javafx.application.Platform.runLater(() -> {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Pengingat Tagihan");
        alert.setHeaderText("Reminder Pembayaran");
        alert.setContentText(reminderMessage.toString());
        alert.show();

        UserData.setReminderNotifShown(true);
    });
}

    
    @FXML
    public void handleLogout() {
        System.out.println("\n=== LOGOUT DIPILIH ===");
        App.logout();
    }
    
    @FXML
    public void handleTopUp() {
        System.out.println("Tombol Top Up diklik");
        App.setRoot("Topup");
    }
    
    @FXML
    public void handlePembayaran() {
        System.out.println("Tombol Transfer diklik");
        App.setRoot("Pembayaran");
    }
    
    @FXML
    public void handleReminder() {
        System.out.println("Tombol Reminder diklik");
        App.setRoot("ReminderList");
    }
    
    @FXML
    public void handleRiwayat() {
        System.out.println("Tombol Riwayat diklik");
        
        // Periksa login
        if (UserData.getSmartId() == null || UserData.getSmartId().isEmpty()) {
            showAlert("Login Required", "Anda harus login terlebih dahulu!");
            return;
        }
        
        System.out.println("User: " + UserData.getNama() + " mengakses riwayat");
        System.out.println("Smart ID: " + UserData.getSmartId());
        
        try {
            App.showRiwayat();
        } catch (Exception e) {
            System.err.println("✗ Gagal navigasi ke Riwayat: " + e.getMessage());
            showAlert("Error", "Gagal membuka halaman riwayat");
        }
    }
    
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    
    public void updateSaldoHome() {
        labelSaldo.setText(SaldoManager.formatSaldo());
        System.out.println("🔄 Saldo di Home di-update: " + SaldoManager.formatSaldo());
    }
    
    public AnchorPane getRootNode() {
        return rootPane;
    }
}