package com.app.Transfer;

import com.app.App;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class BuktiTransaksiController {

    @FXML private Label lblNama, lblBank, lblNominal, lblTotal;

    @FXML
    public void initialize() {
        lblNama.setText(PembayaranData.getNama());
        lblBank.setText(PembayaranData.getBank());
        lblNominal.setText("Rp " + PembayaranData.getNominal());
        lblTotal.setText("Rp " + PembayaranData.getTotal());
    }

    @FXML
    private void handleKembali() {
        PembayaranData.clear();
        App.setRoot("Home");
    }
}
