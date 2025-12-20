package com.app.Transfer;

import com.app.App;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class BuktiTransaksiController {

    @FXML
    private Label lblBukti;

    @FXML
    public void initialize() {
        lblBukti.setText(
            "Nama       : " + PembayaranData.getNama() + "\n" +
            "Bank       : " + PembayaranData.getBank() + "\n" +
            "No Rek     : " + PembayaranData.getNoRek() + "\n" +
            "Nominal    : Rp " + PembayaranData.getNominal() + "\n" +
            "Admin      : Rp " + PembayaranData.getAdmin() + "\n" +
            "TOTAL      : Rp " + PembayaranData.getTotal()
        );
    }

    @FXML
    private void handleKembali() {
        App.setRoot("Home");
    }
}
