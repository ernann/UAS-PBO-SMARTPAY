package com.app.Transfer;
import com.app.App;
import com.app.UserData;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class PembayaranController {
    
    @FXML
    private ListView<RiwayatItem> listRiwayat;
    
    public PembayaranController() {
        System.out.println("=== PEMBAYARAN CONTROLLER CREATED ===");
    }
    
    @FXML
    private void handleBack(ActionEvent event) {
        System.out.println("Tombol Back diklik - kembali ke Home");
        System.out.println("Event source: " + event.getSource());
        App.setRoot("Home");
    }

    @FXML
    private void handlePenerimaBaru(ActionEvent event) {
        System.out.println("=== TOMBOL PENERIMA BARU DIKLIK ===");
        System.out.println("Event source: " + event.getSource());
        System.out.println("Mengosongkan PembayaranData...");
        PembayaranData.clear();
        System.out.println("Pindah ke FormPembayaran...");
        App.setRoot("FormPembayaran");
    }

    @FXML
    public void initialize() {
        System.out.println("=== INITIALIZE PEMBAYARAN CONTROLLER ===");
        
        if (listRiwayat == null) {
            System.out.println("ERROR: listRiwayat is NULL!");
        } else {
            System.out.println("listRiwayat is OK");
        }
        
       String smartId = UserData.getSmartId();

        listRiwayat.getItems().setAll(
            RiwayatPembayaran.getListByUser(smartId)
        );

        System.out.println(
            "Jumlah riwayat user (" + smartId + "): " +
            RiwayatPembayaran.getListByUser(smartId).size()
        );

        System.out.println("Jumlah item di ListView: " + listRiwayat.getItems().size());
        
        listRiwayat.setOnMouseClicked(e -> {
            System.out.println("ListView diklik - Click count: " + e.getClickCount());
            RiwayatItem item = listRiwayat.getSelectionModel().getSelectedItem();
            
            if (item != null) {
                System.out.println("Item dipilih: " + item.getNama());
                
                PembayaranData.setNama(item.getNama());
                PembayaranData.setBank(item.getBank());
                PembayaranData.setNoRek(item.getNoRek());
                
                System.out.println("Data disimpan ke PembayaranData:");
                System.out.println("  Nama: " + PembayaranData.getNama());
                System.out.println("  Bank: " + PembayaranData.getBank());
                System.out.println("  NoRek: " + PembayaranData.getNoRek());
                
                App.setRoot("FormPembayaran");
            } else {
                System.out.println("Tidak ada item yang dipilih");
            }
        });
        System.out.println("LOAD RIWAYAT UNTUK: " + UserData.getSmartId());
        System.out.println("=== INITIALIZE SELESAI ===");
    }
}