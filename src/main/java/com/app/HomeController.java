// [file name]: HomeController.java
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
        labelSaldo.setText(UserData.getSaldoFormatted());
        
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
        // Only show reminder notification once per session
        if (hasShownReminder) {
            System.out.println("⏭️ Reminder sudah ditampilkan sebelumnya, dilewati");
            return;
        }
        
        String smartId = UserData.getSmartId();
        if (smartId == null || smartId.isEmpty()) {
            System.out.println("⚠️ User ID tidak ditemukan, skip reminder check");
            return;
        }
        
        List<Reminder> userReminders = ReminderStorage.loadForUser(smartId);
        System.out.println("🔍 Memeriksa reminder untuk user: " + smartId);
        System.out.println("   Total reminder: " + userReminders.size());
        
        // Filter for active (unpaid) reminders that are due or upcoming
        List<Reminder> activeReminders = userReminders.stream()
            .filter(r -> !r.isPaid())
            .filter(r -> {
                LocalDate today = LocalDate.now();
                LocalDate dueDate = r.getJatuhTempo();
                return dueDate.isEqual(today) || 
                       dueDate.isBefore(today) || 
                       dueDate.isBefore(today.plusDays(3));
            })
            .collect(java.util.stream.Collectors.toList());
        
        if (!activeReminders.isEmpty()) {
            StringBuilder reminderMessage = new StringBuilder();
            reminderMessage.append("📌 Anda memiliki ").append(activeReminders.size()).append(" tagihan yang perlu diperhatikan:\n\n");
            
            for (Reminder r : activeReminders) {
                String status;
                LocalDate dueDate = r.getJatuhTempo();
                LocalDate today = LocalDate.now();
                
                if (dueDate.isBefore(today)) {
                    long daysLate = java.time.temporal.ChronoUnit.DAYS.between(dueDate, today);
                    status = "TERLAMBAT " + daysLate + " hari";
                } else if (dueDate.isEqual(today)) {
                    status = "HARI INI";
                } else {
                    long daysLeft = java.time.temporal.ChronoUnit.DAYS.between(today, dueDate);
                    status = daysLeft + " hari lagi";
                }
                
                reminderMessage.append("• ").append(r.getJenis())
                    .append(" - ").append(r.getFormattedNominal())
                    .append("\n   Jatuh tempo: ").append(dueDate)
                    .append(" (").append(status).append(")\n");
            }
            
            reminderMessage.append("\n📱 Segera bayar melalui menu Reminder!");
            
            System.out.println("🔔 Menampilkan notifikasi reminder dengan " + activeReminders.size() + " tagihan");
            
            // Show alert in a separate thread to avoid blocking UI initialization
            javafx.application.Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Reminder Tagihan");
                alert.setHeaderText("⏰ Tagihan Aktif");
                alert.setContentText(reminderMessage.toString());
                alert.setWidth(600);
                alert.setHeight(400);
                
                // Set flag to prevent duplicate notifications
                hasShownReminder = true;
                
                // Auto-close after 8 seconds
                new Thread(() -> {
                    try {
                        Thread.sleep(8000);
                        if (alert.isShowing()) {
                            javafx.application.Platform.runLater(alert::close);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
                
                alert.show();
            });
        } else {
            System.out.println("✅ Tidak ada tagihan aktif yang perlu diperhatikan");
            hasShownReminder = true; // Still set flag even if no reminders
        }
    }
    
    @FXML
    private void handleLogout() {
        System.out.println("\n=== LOGOUT DIPILIH ===");
        System.out.println("User: " + UserData.getNama() + " keluar");
        
        // Reset reminder flag for next login
        hasShownReminder = false;
        
        // Clear user data (optional)
        UserData.setNama("User");
        UserData.setSmartId("");
        UserData.setEmail("");
        UserData.setNomorRekening("");
        UserData.setSaldo(0);
        
        // Navigate to login
        App.logout();
    }
    
    @FXML
    private void handleTopUp() {
        System.out.println("Tombol Top Up diklik");
        // Implementasi top up
        App.setRoot("TopUp"); // Jika ada halaman TopUp
    }
    
    @FXML
    private void handlePembayaran() {
        System.out.println("Tombol Transfer/Pembayaran diklik");
        App.setRoot("Pembayaran");
    }
    
    @FXML
    private void handleReminder() {
        System.out.println("Tombol Reminder diklik");
        App.setRoot("Reminder");
    }
    
    @FXML
    private void handleRiwayat() {
        System.out.println("Tombol Riwayat diklik");
        // Bisa mengarahkan ke halaman riwayat transaksi khusus
        // Atau langsung ke tab riwayat di Reminder
        App.setRoot("Reminder");
    }
    
    // Method untuk refresh data jika diperlukan
    public void refreshUserData() {
        labelNama.setText("Hello, " + UserData.getNama());
        labelRekening.setText(UserData.getNomorRekening());
        labelSaldo.setText(UserData.getSaldoFormatted());
        System.out.println("🔄 Data user di-refresh di Home");
    }
    
    // Method untuk reset reminder flag (misal saat kembali dari halaman lain)
    public void resetReminderFlag() {
        this.hasShownReminder = false;
    }
}