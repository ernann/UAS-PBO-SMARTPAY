package com.app.Transfer;

import com.app.App;
import com.app.UserData;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;

public class FormPembayaranController {

    @FXML
    private TextField tfNama, tfNoRek, tfNominal;

    @FXML
    private ComboBox<String> cbBank;

    @FXML
    private Label lblAdmin, lblTotal;

    @FXML
    public void handleBack() {
        App.setRoot("Pembayaran");
    }

    @FXML
    public void initialize() {

        cbBank.getItems().addAll("SmartPay", "BNI", "BCA", "BSI");

        if (PembayaranData.getNama() != null) {
            tfNama.setText(PembayaranData.getNama());
        }

        if (PembayaranData.getBank() != null) {
            cbBank.setValue(PembayaranData.getBank());
            lblAdmin.setText("Admin: Rp " + PembayaranData.getAdmin());
        }

        if (PembayaranData.getNoRek() != null) {
            tfNoRek.setText(PembayaranData.getNoRek());
        }

        cbBank.setOnAction(e -> {
            PembayaranData.setBank(cbBank.getValue());
            lblAdmin.setText("Admin: Rp " + PembayaranData.getAdmin());
            updateTotal();
        });

        tfNominal.textProperty().addListener((obs, o, n) -> updateTotal());
    }

    private void updateTotal() {
        try {
            if (!tfNominal.getText().isEmpty()) {
                int nominal = Integer.parseInt(tfNominal.getText());
                PembayaranData.setNominal(nominal);
                lblTotal.setText("Total Bayar: Rp " + PembayaranData.getTotal());
            }
        } catch (NumberFormatException e) {
            lblTotal.setText("Total Bayar: -");
        }
    }

    @FXML
    private void handleBayar() {

   if (tfNama.getText().isEmpty()) {
        showError("Nama belum diisi!");
        return;
    }
        if (cbBank.getValue() == null) {
            showError("Pilih bank terlebih dahulu!");
            return;
        } 

        long nominal;
        try {
            nominal = Long.parseLong(tfNominal.getText());
        } catch (NumberFormatException e) {
            showError("Nominal harus berupa angka!");
            return;
        }

        if (nominal > PembayaranData.LIMIT_TRANSFER) {
            showError("Melebihi limit transfer!\nMax Rp " + PembayaranData.LIMIT_TRANSFER);
            return;
        }

        if (nominal < PembayaranData.MIN_TRANSFER) {
            showError("Nominal terlalu kecil!\nMin Rp " + PembayaranData.MIN_TRANSFER);
            return;
        }

        String bank = cbBank.getValue();
        String noRek = tfNoRek.getText();

        String kodeAwal = PembayaranData.getKodeAwalRek();
        int panjang = 0;

        if (bank.equals("SmartPay")) {
            panjang = 10;
        } else if (bank.equals("BNI")) {
            panjang = 11;
        } else if (bank.equals("BCA")) {
            panjang = 12;
        } else if (bank.equals("BSI")) {
            panjang = 13;
        }

        if (!noRek.startsWith(kodeAwal)) {
            showError("No rekening harus diawali dengan " + kodeAwal);
            return;
        }

        if (noRek.length() != panjang) {
            showError("Panjang No Rek " + bank + " harus " + panjang + " digit");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Verifikasi PIN");
        dialog.setHeaderText("Masukkan PIN untuk melanjutkan");
        dialog.setContentText("PIN:");

        dialog.showAndWait().ifPresent(pin -> {
            if (pin.equals(UserData.PIN)) {

                PembayaranData.setNama(tfNama.getText());
                PembayaranData.setNoRek(noRek);
                PembayaranData.setNominal(Integer.parseInt(tfNominal.getText()));

               RiwayatPembayaran.tambah(
                new RiwayatItem(
                    PembayaranData.getNama(),
                    PembayaranData.getBank(),
                    PembayaranData.getNoRek()
                )
            );

                App.setRoot("BuktiTransaksi");

            } else {
                showError("PIN Salah! Transaksi dibatalkan.");
            }
        });
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Gagal");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.show();
    }
}
