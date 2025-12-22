package com.app.Transfer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import com.app.App;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class BuktiTransaksiController {

    @FXML private Label lblNama, lblBank, lblRekening, lblNominal, lblAdmin, lblTotal, lblTimestamp;
    
    private Random random = new Random();

    @FXML
    public void initialize() {
        System.out.println("=== INITIALIZE BUKTI TRANSAKSI CONTROLLER ===");
        
        System.out.println("Data dari PembayaranData:");
        System.out.println("  Nama: " + PembayaranData.getNama());
        System.out.println("  Bank: " + PembayaranData.getBank());
        System.out.println("  NoRek: " + PembayaranData.getNoRek());
        System.out.println("  Nominal: " + PembayaranData.getNominal());
        System.out.println("  Admin: " + PembayaranData.getAdmin());
        System.out.println("  Total: " + PembayaranData.getTotal());
        
        lblNama.setText(PembayaranData.getNama() != null ? PembayaranData.getNama() : "-");
        lblBank.setText(PembayaranData.getBank() != null ? PembayaranData.getBank() : "-");
        
        String noRek = PembayaranData.getNoRek();
        if (noRek != null) {
            lblRekening.setText(formatRekening(noRek));
        } else {
            lblRekening.setText("-");
        }
        
        String formattedNominal = formatRupiah(PembayaranData.getNominal());
        String formattedAdmin = formatRupiah(PembayaranData.getAdmin());
        String formattedTotal = formatRupiah(PembayaranData.getTotal());
        
        lblNominal.setText(formattedNominal);
        lblAdmin.setText(formattedAdmin);
        lblTotal.setText(formattedTotal);
        
        String transactionId = "TRX" + String.format("%06d", random.nextInt(1000000));
        String timestamp = LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
        );
        
        lblTimestamp.setText("ID: " + transactionId + " • " + timestamp);
        System.out.println("Transaction ID: " + transactionId);
        System.out.println("Timestamp: " + timestamp);
        
        System.out.println("=== INITIALIZE SELESAI ===");
    }
    
    private String formatRupiah(long amount) {
        return "Rp " + String.format("%,d", amount).replace(",", ".");
    }
    
    private String formatRekening(String rekening) {
        if (rekening.length() == 10) {
            return rekening.substring(0, 3) + "-" + 
                   rekening.substring(3, 6) + "-" + 
                   rekening.substring(6);
        }
        return rekening;
    }

    @FXML
    private void handleKembali() {
        System.out.println("Tombol Kembali diklik");
        PembayaranData.clear();
        App.setRoot("Home");
    }
}