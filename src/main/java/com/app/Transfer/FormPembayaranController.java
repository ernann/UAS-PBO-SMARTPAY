package com.app.Transfer;

import java.util.Optional;

import com.app.App;
import com.app.SaldoManager;
import com.app.UserData;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;

public class FormPembayaranController {

    @FXML private TextField tfNama, tfNoRek, tfNominal;
    @FXML private ComboBox<String> cbBank;
    @FXML private Label lblAdmin, lblTotal;

    public FormPembayaranController() {
        System.out.println("=== FORM PEMBAYARAN CONTROLLER CREATED ===");
    }

    @FXML
    public void handleBack(ActionEvent event) {
        System.out.println("Tombol Back diklik - kembali ke Pembayaran");
        App.setRoot("Pembayaran");
    }

    @FXML
    public void initialize() {
        System.out.println("=== INITIALIZE FORM PEMBAYARAN ===");
        System.out.println("Data dari PembayaranData:");
        System.out.println("  Nama: " + PembayaranData.getNama());
        System.out.println("  Bank: " + PembayaranData.getBank());
        System.out.println("  NoRek: " + PembayaranData.getNoRek());
        
        cbBank.getItems().addAll("SmartPay", "BNI", "BCA", "BSI");
        System.out.println("ComboBox diisi dengan " + cbBank.getItems().size() + " bank");
        
        if (PembayaranData.getNama() != null && !PembayaranData.getNama().isEmpty()) {
            tfNama.setText(PembayaranData.getNama());
            System.out.println("Nama di-set: " + PembayaranData.getNama());
        }
        
        if (PembayaranData.getBank() != null && !PembayaranData.getBank().isEmpty()) {
            cbBank.setValue(PembayaranData.getBank());
            lblAdmin.setText("Rp " + String.format("%,d", PembayaranData.getAdmin()));
            System.out.println("Bank di-set: " + PembayaranData.getBank());
        }
        
        if (PembayaranData.getNoRek() != null && !PembayaranData.getNoRek().isEmpty()) {
            tfNoRek.setText(PembayaranData.getNoRek());
            System.out.println("NoRek di-set: " + PembayaranData.getNoRek());
        }
        
        cbBank.setOnAction(e -> {
            String selectedBank = cbBank.getValue();
            if (selectedBank != null) {
                System.out.println("Bank dipilih: " + selectedBank);
                PembayaranData.setBank(selectedBank);
                lblAdmin.setText("Rp " + String.format("%,d", PembayaranData.getAdmin()));
                updateTotal();
            }
        });
        
        tfNominal.textProperty().addListener((obs, oldValue, newValue) -> {
            System.out.println("Nominal berubah: " + oldValue + " -> " + newValue);
            updateTotal();
        });
        
        lblAdmin.setText("Rp 0");
        lblTotal.setText("Rp 0");
        
        System.out.println("=== INITIALIZE SELESAI ===");
    }

    private void updateTotal() {
        try {
            if (!tfNominal.getText().isEmpty()) {
                String nominalText = tfNominal.getText().replace(".", "").replace("Rp", "").trim();
                int nominal = Integer.parseInt(nominalText);
                System.out.println("Nominal parsed: " + nominal);
                PembayaranData.setNominal(nominal);
                String total = "Rp " + String.format("%,d", PembayaranData.getTotal());
                lblTotal.setText(total);
                System.out.println("Total di-update: " + total);
            } else {
                lblTotal.setText("Rp 0");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error parsing nominal: " + e.getMessage());
            lblTotal.setText("Rp 0");
        }
    }

    @FXML
    private void handleBayar(ActionEvent event) {
        System.out.println("=== TOMBOL BAYAR DIKLIK ===");
        System.out.println("Event source: " + event.getSource());
        
        // Cek saldo pengirim terlebih dahulu
        long saldoPengirim = SaldoManager.getSaldo();
        System.out.println("Saldo pengirim: " + SaldoManager.formatSaldo());
        
        if (saldoPengirim == 0) {
            System.out.println("Validasi gagal: Saldo pengirim 0");
            showError("Saldo Anda Rp 0!\n\n" +
                     "⚠️  Lakukan TOP UP terlebih dahulu sebelum melakukan transaksi.");
            return;
        }
        
        if (tfNama.getText().isEmpty()) {
            System.out.println("Validasi gagal: Nama kosong");
            showError("Nama penerima belum diisi!");
            return;
        }
        
        if (cbBank.getValue() == null) {
            System.out.println("Validasi gagal: Bank belum dipilih");
            showError("Pilih bank tujuan terlebih dahulu!");
            return;
        }
        
        if (tfNoRek.getText().isEmpty()) {
            System.out.println("Validasi gagal: NoRek kosong");
            showError("Nomor rekening belum diisi!");
            return;
        }
        
        if (tfNominal.getText().isEmpty()) {
            System.out.println("Validasi gagal: Nominal kosong");
            showError("Nominal transfer belum diisi!");
            return;
        }
        
        long nominal;
        try {
            String nominalText = tfNominal.getText().replace(".", "").replace("Rp", "").trim();
            nominal = Long.parseLong(nominalText);
            System.out.println("Nominal valid: " + nominal);
        } catch (NumberFormatException e) {
            System.out.println("Validasi gagal: Nominal bukan angka");
            showError("Nominal harus berupa angka!");
            return;
        }
        
        if (nominal > PembayaranData.LIMIT_TRANSFER) {
            System.out.println("Validasi gagal: Melebihi limit");
            showError("Melebihi limit transfer!\nMaksimum Rp " + 
                     String.format("%,d", PembayaranData.LIMIT_TRANSFER));
            return;
        }
        
        if (nominal < PembayaranData.MIN_TRANSFER) {
            System.out.println("Validasi gagal: Kurang dari minimum");
            showError("Nominal terlalu kecil!\nMinimum Rp " + 
                     String.format("%,d", PembayaranData.MIN_TRANSFER));
            return;
        }
        
        String bank = cbBank.getValue();
        String noRek = tfNoRek.getText().trim();
        String kodeAwal = PembayaranData.getKodeAwalRek();
        
        System.out.println("Validasi NoRek:");
        System.out.println("  Bank: " + bank);
        System.out.println("  NoRek: " + noRek);
        System.out.println("  Kode awal yang diharapkan: " + kodeAwal);
        
        if (bank.equals("SmartPay")) {
            if (!PembayaranData.isValidSmartPayRekening(noRek)) {
                System.out.println("Validasi gagal: Rekening SmartPay tidak valid");
                showError("No rekening SmartPay tidak valid!\n" +
                         "Format harus: 006XXXXXXX (10 digit)\n" +
                         "Contoh: 0061234567");
                return;
            }
            
            if (noRek.equals(UserData.getNomorRekening())) {
                System.out.println("Validasi gagal: Tidak bisa transfer ke rekening sendiri");
                showError("Tidak bisa transfer ke rekening sendiri!");
                return;
            }
        } else {
            if (!noRek.startsWith(kodeAwal)) {
                System.out.println("Validasi gagal: Kode awal tidak cocok");
                showError("No rekening harus diawali dengan " + kodeAwal + 
                         "\nContoh: " + kodeAwal + "XXXXXXXXX");
                return;
            }
        }
        
        int panjang = 0;
        switch (bank) {
            case "SmartPay": panjang = 10; break;
            case "BNI": panjang = 11; break;
            case "BCA": panjang = 12; break;
            case "BSI": panjang = 13; break;
        }
        
        if (noRek.length() != panjang) {
            System.out.println("Validasi gagal: Panjang NoRek salah (" + noRek.length() + " bukan " + panjang + ")");
            showError("Panjang No Rekening " + bank + " harus " + panjang + " digit");
            return;
        }
        
        if (!noRek.matches("\\d+")) {
            System.out.println("Validasi gagal: NoRek mengandung non-digit");
            showError("No rekening harus berupa angka!");
            return;
        }
        
        System.out.println("Semua validasi berhasil!");
        
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Verifikasi PIN");
        dialog.setHeaderText("Masukkan PIN untuk melanjutkan transaksi");
        dialog.setContentText("PIN (6 digit):");
        
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String pin = result.get();
            System.out.println("PIN dimasukkan: " + pin);
            
            if (pin.length() != 6 || !pin.matches("\\d+")) {
                System.out.println("Validasi PIN gagal: format salah");
                showError("PIN harus 6 digit angka!");
                return;
            }
            
            if (UserData.validatePin(pin)) {
                System.out.println("PIN BENAR!");
                
                boolean saldoCukup = SaldoManager.kurangiSaldo(PembayaranData.getTotal());
                if (!saldoCukup) {
                    showError("Saldo tidak cukup!\n\n" +
                             "Saldo Anda: " + SaldoManager.formatSaldo() + "\n" +
                             "Total yang dibutuhkan: " + PembayaranData.getTotal());
                    return;
                }
                System.out.println("Saldo berhasil dikurangi");
                
                PembayaranData.setNama(tfNama.getText());
                PembayaranData.setBank(bank);
                PembayaranData.setNoRek(noRek);
                PembayaranData.setNominal((int) nominal);
                
                System.out.println("Data disimpan ke PembayaranData:");
                System.out.println("  Nama: " + PembayaranData.getNama());
                System.out.println("  Bank: " + PembayaranData.getBank());
                System.out.println("  NoRek: " + PembayaranData.getNoRek());
                System.out.println("  Nominal: " + PembayaranData.getNominal());
                System.out.println("  Admin: " + PembayaranData.getAdmin());
                System.out.println("  Total: " + PembayaranData.getTotal());
                
                if (bank.equals("SmartPay")) {
                    tambahSaldoKePenerima(noRek, nominal);
                }
                
                RiwayatItem newItem = new RiwayatItem(
                    PembayaranData.getNama(),
                    PembayaranData.getBank(),
                    PembayaranData.getNoRek()
                );
                RiwayatPembayaran.tambah(newItem);
                System.out.println("Ditambahkan ke riwayat: " + newItem);
                
                System.out.println("Pindah ke BuktiTransaksi...");
                App.setRoot("BuktiTransaksi");
                
            } else {
                System.out.println("PIN SALAH!");
                showError("PIN salah! Transaksi dibatalkan.");
            }
        } else {
            System.out.println("PIN dialog dibatalkan");
        }
    }
    
    private void tambahSaldoKePenerima(String nomorRekening, long nominal) {
        try {
            com.app.User penerima = com.app.UserDatabase.getUserByRekening(nomorRekening);
            if (penerima != null) {
                penerima.tambahSaldo(nominal);
                com.app.UserDatabase.updateUser(penerima);
                System.out.println("✓ Saldo penerima ditambah: " + penerima.getNama() + 
                                 " +" + nominal + ", total: " + penerima.getSaldoFormatted());
            }
        } catch (Exception e) {
            System.err.println("✗ Gagal menambah saldo penerima: " + e.getMessage());
        }
    }

    private void showError(String msg) {
        System.out.println("Menampilkan error: " + msg);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Gagal");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}