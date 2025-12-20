package com.app.Transfer;
import com.app.App;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class PembayaranController {
    
  @FXML
    private void handleBack() {
        App.setRoot("Home");
    }

 @FXML
private ListView<RiwayatItem> listRiwayat;

@FXML
public void initialize() {

    listRiwayat.getItems().setAll(RiwayatPembayaran.getList());

    listRiwayat.setOnMouseClicked(e -> {
        RiwayatItem item =
            listRiwayat.getSelectionModel().getSelectedItem();

        if (item != null) {

        PembayaranData.setNama(item.getNama());
        PembayaranData.setBank(item.getBank());
        PembayaranData.setNoRek(item.getNoRek());


            App.setRoot("FormPembayaran");
        }
    });
}


    @FXML
    private void handlePenerimaBaru() {
        PembayaranData.clear();
        App.setRoot("FormPembayaran");
    }
}   