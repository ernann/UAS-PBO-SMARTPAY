package com.app.Reminder;

import java.util.List;

import com.app.App;
import com.app.UserData;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class ReminderFormController {

    @FXML private TextField txtJenis;
    @FXML private TextField txtNominal;
    @FXML private DatePicker dateJatuhTempo;

    private static Reminder editReminder;

    public static void setEditReminder(Reminder r) {
        editReminder = r;
    }

    @FXML
    public void initialize() {
        if (editReminder != null) {
            txtJenis.setText(editReminder.getJenis());
            txtNominal.setText(String.valueOf(editReminder.getNominal()));
            dateJatuhTempo.setValue(editReminder.getJatuhTempo());
        }
    }

    @FXML
private void handleSave() {

    if (txtJenis.getText().isEmpty()
            || txtNominal.getText().isEmpty()
            || dateJatuhTempo.getValue() == null) {
        showAlert("Error", "Semua data wajib diisi!");
        return;
    }

    double nominal;
    try {
        nominal = Double.parseDouble(txtNominal.getText());
    } catch (Exception e) {
        showAlert("Error", "Nominal harus angka!");
        return;
    }

    String userId = UserData.getSmartId();
    List<Reminder> list = ReminderStorage.loadForUser(userId);

    if (editReminder == null) {

        list.add(new Reminder(
                txtJenis.getText(),
                nominal,
                dateJatuhTempo.getValue(),
                userId
        ));

    } else {

        for (Reminder r : list) {
            if (r.getId().equals(editReminder.getId())) {

                r.setJenis(txtJenis.getText());
                r.setNominal(nominal);
                r.setJatuhTempo(dateJatuhTempo.getValue());

                break;
            }
        }
    }

    ReminderStorage.saveForUser(userId, list);
    editReminder = null;
    App.setRoot("ReminderList");
}

    @FXML
    private void handleBack() {
        editReminder = null;
        App.setRoot("ReminderList");
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }
}
