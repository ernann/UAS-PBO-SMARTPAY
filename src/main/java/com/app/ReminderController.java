package com.app;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class ReminderController {

    @FXML private TextField txtJenis;
    @FXML private TextField txtNominal;
    @FXML private DatePicker dateJatuhTempo;
    @FXML private ListView<Reminder> listReminder;

    private List<Reminder> reminderList;

    @FXML
    public void initialize() {
        // Load data reminder dari file
        reminderList = ReminderStorage.load();
        setupListView();
        refreshList();
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
                    Button btnHapus = new Button("Hapus");
                    btnHapus.setStyle("-fx-background-color: #0B2A72; -fx-text-fill: white;");

                    btnHapus.setOnAction(event -> {
                        reminderList.remove(reminder);
                        ReminderStorage.save(reminderList);
                        refreshList();
                    });

                    HBox hbox = new HBox(10, lbl, btnHapus);
                    HBox.setHgrow(lbl, Priority.ALWAYS);
                    setGraphic(hbox);
                }
            }
        });
    }

    private void refreshList() {
        listReminder.getItems().clear();
        LocalDate now = LocalDate.now();
        for (Reminder r : reminderList) {
            // tampilkan hanya yang jatuh tempo bulan ini
            if (r.getJatuhTempo().getMonth() == now.getMonth()) {
                listReminder.getItems().add(r);
            }
        }
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
        } catch (NumberFormatException e) {
            showAlert("Input salah", "Nominal harus berupa angka!");
            return;
        }

        Reminder reminder = new Reminder(jenis, nominal, jatuhTempo);
        reminderList.add(reminder);
        ReminderStorage.save(reminderList);
        refreshList();

        txtJenis.clear();
        txtNominal.clear();
        dateJatuhTempo.setValue(null);
    }

    @FXML
    private void handleBack() {
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
