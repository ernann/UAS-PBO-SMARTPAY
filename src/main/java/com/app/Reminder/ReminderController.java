// [file name]: ReminderController.java
package com.app.Reminder;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.app.App;
import com.app.UserData;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class ReminderController {

    @FXML private TextField txtJenis;
    @FXML private TextField txtNominal;
    @FXML private DatePicker dateJatuhTempo;
    @FXML private ListView<Reminder> listReminder;
    @FXML private Button btnReminder;
    @FXML private Button btnRiwayat;
    @FXML private Label lblJumlahAktif;
    @FXML private Label lblJumlahRiwayat;
    @FXML private Label lblTotalDibayar;
    @FXML private Label lblTagihanSelesai;

    private List<Reminder> userReminderList;
    private String currentUserId;

    @FXML
    public void initialize() {
        // Get current user ID from UserData
        currentUserId = UserData.getSmartId();
        
        if (currentUserId == null || currentUserId.isEmpty()) {
            showAlert("Error", "User tidak terautentikasi!");
            return;
        }
        
        // Load reminders for current user
        userReminderList = ReminderStorage.loadForUser(currentUserId);
        setupListView();
        showReminder(); // Default show active reminders
        updateStatistics();
    }

    private void setupListView() {
        listReminder.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Reminder reminder, boolean empty) {
                super.updateItem(reminder, empty);

                if (empty || reminder == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Label lbl = new Label(reminder.toString());
                    lbl.setStyle("-fx-font-size: 12px; -fx-text-fill: #333;");
                    
                    HBox hbox = new HBox(10);
                    HBox.setHgrow(lbl, Priority.ALWAYS);
                    
                    if (!reminder.isPaid()) {
                        // For active reminders: show Pay and Delete buttons
                        Button btnBayar = new Button("Bayar");
                        btnBayar.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 11px;");
                        btnBayar.setOnAction(event -> handleBayarReminder(reminder));
                        
                        Button btnHapus = new Button("Hapus");
                        btnHapus.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 11px;");
                        btnHapus.setOnAction(event -> handleHapusReminder(reminder));
                        
                        hbox.getChildren().addAll(lbl, btnBayar, btnHapus);
                    } else {
                        // For history: show only label
                        lbl.setStyle("-fx-font-size: 12px; -fx-text-fill: #666; -fx-font-style: italic;");
                        hbox.getChildren().add(lbl);
                    }
                    
                    setGraphic(hbox);
                }
            }
        });
    }

    @FXML
    private void handleAddReminder() {
        String jenis = txtJenis.getText();
        String nominalText = txtNominal.getText();
        LocalDate jatuhTempo = dateJatuhTempo.getValue();

        if (jenis.isEmpty() || nominalText.isEmpty() || jatuhTempo == null) {
            showAlert("Data belum lengkap", "Judul, nominal, dan tanggal wajib diisi!");
            return;
        }

        double nominal;
        try {
            nominal = Double.parseDouble(nominalText);
            if (nominal <= 0) {
                showAlert("Input salah", "Nominal harus lebih dari 0!");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Input salah", "Nominal harus berupa angka!");
            return;
        }

        // Create reminder with current user ID
        Reminder reminder = new Reminder(jenis, nominal, jatuhTempo, currentUserId);
        userReminderList.add(reminder);
        ReminderStorage.saveForUser(currentUserId, userReminderList);
        refreshActiveReminders();

        // Reset form
        txtJenis.clear();
        txtNominal.clear();
        dateJatuhTempo.setValue(null);
        
        updateStatistics();
    }

    private void handleBayarReminder(Reminder reminder) {
        // Mark as paid
        reminder.setPaid(true);
        reminder.setPaidDate(LocalDate.now());
        ReminderStorage.saveForUser(currentUserId, userReminderList);
        
        // Update UI
        if (btnReminder.getStyle().contains("white")) {
            refreshActiveReminders();
        } else {
            refreshRiwayat();
        }
        
        updateStatistics();
    }

    private void handleHapusReminder(Reminder reminder) {
        userReminderList.remove(reminder);
        ReminderStorage.saveForUser(currentUserId, userReminderList);
        
        if (btnReminder.getStyle().contains("white")) {
            refreshActiveReminders();
        } else {
            refreshRiwayat();
        }
        
        updateStatistics();
    }

    @FXML
    private void showReminder() {
        btnReminder.setStyle("-fx-background-color: white; -fx-text-fill: #1f5bb4ff;");
        btnRiwayat.setStyle("-fx-background-color: transparent; -fx-text-fill: #1f5bb4ff;");
        refreshActiveReminders();
    }

    @FXML
    private void showRiwayat() {
        btnReminder.setStyle("-fx-background-color: transparent; -fx-text-fill: #1f5bb4ff;");
        btnRiwayat.setStyle("-fx-background-color: white; -fx-text-fill: #1f5bb4ff;");
        refreshRiwayat();
    }

    private void refreshActiveReminders() {
        listReminder.getItems().clear();
        
        // Filter active reminders (not paid)
        List<Reminder> activeReminders = userReminderList.stream()
            .filter(r -> !r.isPaid())
            .collect(Collectors.toList());
        
        listReminder.getItems().addAll(activeReminders);
        lblJumlahAktif.setText(activeReminders.size() + " tagihan aktif");
    }

    private void refreshRiwayat() {
        listReminder.getItems().clear();
        
        // Filter paid reminders (history)
        List<Reminder> paidReminders = userReminderList.stream()
            .filter(Reminder::isPaid)
            .collect(Collectors.toList());
        
        listReminder.getItems().addAll(paidReminders);
        lblJumlahRiwayat.setText(paidReminders.size() + " pembayaran");
    }

    private void updateStatistics() {
        // Calculate total paid and count paid reminders
        double totalDibayar = userReminderList.stream()
            .filter(Reminder::isPaid)
            .mapToDouble(Reminder::getNominal)
            .sum();
        
        long tagihanSelesai = userReminderList.stream()
            .filter(Reminder::isPaid)
            .count();
        
        lblTotalDibayar.setText(String.format("Rp %,d", (int) totalDibayar));
        lblTagihanSelesai.setText(String.valueOf(tagihanSelesai));
    }

    @FXML
    public void handleBack() {
        App.setRoot("Home");
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }
}