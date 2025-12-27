package com.app.Reminder;

import java.util.List;

import com.app.App;
import com.app.UserData;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

public class ReminderListController {

    @FXML private ListView<Reminder> listReminder;

    private List<Reminder> reminders;
    private String userId;

    @FXML
    public void initialize() {
        loadData();

        listReminder.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Reminder r, boolean empty) {
                super.updateItem(r, empty);

                if (empty || r == null) {
                    setGraphic(null);
                    return;
                }

                Label lbl = new Label(
                        "Jenis : " + r.getJenis() + "\n" +
                        "Nominal : " + r.getFormattedNominal() + "\n" +
                        "Jatuh Tempo : " + r.getJatuhTempo() + "\n" +
                        "Status : " + r.getStatus()
                );
                lbl.setWrapText(true);

                Button btnEdit = new Button("✏ Edit");
                btnEdit.setOnAction(e -> {
                    ReminderFormController.setEditReminder(r);
                    App.setRoot("ReminderForm");
                });

                Button btnHapus = new Button("🗑 Hapus");
                btnHapus.setOnAction(e -> handleDelete(r));

                VBox card = new VBox(8, lbl, btnEdit, btnHapus);
                card.setStyle(
                        "-fx-padding:12;" +
                        "-fx-background-color:white;" +
                        "-fx-border-color:#ddd;" +
                        "-fx-border-radius:10;" +
                        "-fx-background-radius:10;"
                );

                setGraphic(card);
            }
        });
    }

    private void loadData() {
        userId = UserData.getSmartId();
        reminders = ReminderStorage.loadForUser(userId);

        listReminder.getItems().clear();
        listReminder.getItems().setAll(reminders);
    }

    private void handleDelete(Reminder r) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Hapus");
        confirm.setHeaderText(null);
        confirm.setContentText("Yakin hapus reminder?");

        confirm.showAndWait().ifPresent(res -> {
            if (res.getText().equals("OK")) {
                reminders.remove(r);
                ReminderStorage.saveForUser(userId, reminders);
                loadData();
            }
        });
    }

    @FXML
    private void handleAdd() {
        ReminderFormController.setEditReminder(null);
        App.setRoot("ReminderForm");
    }

    @FXML
    private void handleBack() {
        App.setRoot("Home");
    }
}
