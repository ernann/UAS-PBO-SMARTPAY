package com.app;

import java.time.LocalDate;
import java.util.List;

import com.app.Reminder.Reminder;
import com.app.Reminder.ReminderStorage;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

public class HomeController {
    
    @FXML private Label labelNama;
    @FXML private Label labelRekening;
    @FXML private Label labelSaldo;
    @FXML private Button btnLogout;
    @FXML private Button btnReminder;
    
    private boolean hasShownReminder = false; // Flag untuk mencegah notifikasi berulang
    
    @FXML
    public void initialize() {
        // Load user data
        labelNama.setText("Hello, " + UserData.getNama());
        labelRekening.setText(UserData.getNomorRekening());
        labelSaldo.setText(SaldoManager.formatSaldo());
        
        // Set tooltip for logout button
        Tooltip logoutTooltip = new Tooltip("Logout");
        logoutTooltip.setStyle("-fx-font-size: 12px; -fx-background-color: #333; -fx-text-fill: white;");
        btnLogout.setTooltip(logoutTooltip);
        
        System.out.println("✓ HomeController initialized for user: " + UserData.getNama());
        System.out.println("  Rekening: " + UserData.getNomorRekening());
        System.out.println("  Saldo: " + UserData.getSaldoFormatted());
        
        // Check for reminders (will show on first load)
        checkAndShowReminders();
    }
    
  private void checkAndShowReminders() {

    if (hasShownReminder) {
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
            long daysLate =
                    java.time.temporal.ChronoUnit.DAYS.between(dueDate, today);
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
        javafx.scene.control.Alert alert =
                new javafx.scene.control.Alert(
                        javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Pengingat Tagihan");
        alert.setHeaderText("Reminder Pembayaran");
        alert.setContentText(reminderMessage.toString());
        alert.show();
    });

    hasShownReminder = true;
}

    
    @FXML
    private void handleLogout() {
        System.out.println("\n=== LOGOUT DIPILIH ===");
        System.out.println("User: " + UserData.getNama() + " keluar");
        
        hasShownReminder = false;
        
        UserData.setNama("User");
        UserData.setSmartId("");
        UserData.setEmail("");
        UserData.setNomorRekening("");
        UserData.setSaldo(0);
        App.logout();
    }
    
    @FXML
    private void handleTopUp() {
        System.out.println("Tombol Top Up diklik");
        App.setRoot("TopUp"); 
    }
    
    @FXML
    private void handlePembayaran() {
        System.out.println("Tombol Transfer/Pembayaran diklik");
        App.setRoot("Pembayaran");
    }
    
    @FXML
    private void handleReminder() {
        System.out.println("Tombol Reminder diklik");
        App.setRoot("ReminderList");
    }
    
    @FXML
    private void handleRiwayat() {
        System.out.println("Tombol Riwayat diklik");
        App.setRoot("Riwayat");
    }
    
    public void refreshUserData() {
        labelNama.setText("Hello, " + UserData.getNama());
        labelRekening.setText(UserData.getNomorRekening());
        labelSaldo.setText(UserData.getSaldoFormatted());
        System.out.println("🔄 Data user di-refresh di Home");
    }
    
    public void resetReminderFlag() {
        this.hasShownReminder = false;
    }

    @FXML
private javafx.scene.layout.AnchorPane rootPane; 

public javafx.scene.layout.AnchorPane getRootNode() {
    return rootPane;
}


   public void updateSaldoHome() {
       labelSaldo.setText(SaldoManager.formatSaldo());
       System.out.println("🔄 Saldo di Home di-update: " + SaldoManager.formatSaldo());
   }

}
