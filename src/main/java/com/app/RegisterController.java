package com.app;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class RegisterController extends BaseController {

    @FXML private TextField txtSmartId;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtRepeatPassword;
    @FXML private CheckBox cbAgreement;
    @FXML private Button btnNext;

    @FXML private Text checkLength;
    @FXML private Text checkUpper;
    @FXML private Text checkLower;
    @FXML private Text checkNumber;

    private PasswordValidator validator = new PasswordValidator();

    @FXML
    private void initialize() {
        txtPassword.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> obs, String oldVal, String newVal) {
                validatePassword(newVal);
            }
        });

        btnNext.setDisable(true);
    }

    private void validatePassword(String text) {
        boolean r1 = validator.rules.get(0).validate(text);
        boolean r2 = validator.rules.get(1).validate(text);
        boolean r3 = validator.rules.get(2).validate(text);
        boolean r4 = validator.rules.get(3).validate(text);

        checkLength.setStrikethrough(r1);
        checkUpper.setStrikethrough(r2);
        checkLower.setStrikethrough(r3);
        checkNumber.setStrikethrough(r4);

        btnNext.setDisable(!(r1 && r2 && r3 && r4));
    }

    @FXML
    private void handleNext() {
        String smartId = txtSmartId.getText().trim();
        String email = txtEmail.getText().trim();
        String pass = txtPassword.getText().trim();
        String rpt  = txtRepeatPassword.getText().trim();

        if (smartId.isEmpty() || email.isEmpty() || pass.isEmpty() || rpt.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Semua field harus diisi!");
            return;
        }

        if (!pass.equals(rpt)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Password tidak sama!");
            return;
        }

        if (!cbAgreement.isSelected()) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Anda harus menyetujui persyaratan!");
            return;
        }

        User user = new User(smartId, email, pass);
        SharedData.setRegisteredUser(user);

        showAlert(Alert.AlertType.INFORMATION, "Sukses", "Register berhasil ✔");

        try {
            App.setRoot("Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
