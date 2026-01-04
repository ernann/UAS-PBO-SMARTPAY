package com.app.Topup;

import java.util.Random;

import com.app.App;
import com.app.HomeController;
import com.app.SaldoManager;
import com.app.TransaksiStorage;
import com.app.UserData;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DetailTopup {

    @FXML private TextField txtNominal;
    @FXML private Label lblSaldoHeader;
    @FXML private Label labelJudul;
    @FXML private Label labelIcon;
    @FXML private Label labelInfoMetode;
    @FXML private Label labelDetailInfo;

    private String kodePembayaran;

    @FXML
    public void initialize() {
        System.out.println("=== DETAIL TOPUP CONTROLLER INITIALIZE ===");
        
        try {
            String metode = TempData.getMetodeTopup();
            String judul = TempData.getJudulTopup();
            String icon = TempData.getIconMetode();
            String info = TempData.getInfoMetode();
            String detail = TempData.getDetailInfo();
            long nominal = TempData.getNominalTopup();
            
            System.out.println("Data dari TempData:");
            System.out.println("  Metode: " + metode);
            System.out.println("  Judul: " + judul);
            System.out.println("  Nominal: " + nominal);
            
            if (labelJudul != null) labelJudul.setText(judul);
            if (labelIcon != null) labelIcon.setText(icon);
            if (labelInfoMetode != null) labelInfoMetode.setText(info);
            if (labelDetailInfo != null) labelDetailInfo.setText(detail);
            
            updateSaldoLabel();
            
            if (nominal > 0) {
                txtNominal.setText(String.valueOf(nominal));
            }
            
            txtNominal.requestFocus();
            
            generatePaymentCode(metode);
            
            System.out.println("✓ DetailTopup initialized successfully");
            
        } catch (Exception e) {
            System.err.println("❌ ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void updateSaldoLabel() {
        if (lblSaldoHeader != null) {
            lblSaldoHeader.setText("Saldo: " + SaldoManager.formatSaldo());
        }
    }

    @FXML
    private void handleNominalQuick(ActionEvent event) {
        try {
            if (event.getSource() instanceof Button) {
                Button btn = (Button) event.getSource();
                String text = btn.getText();
                String nominalStr = text.replace("Rp", "").replace(".", "").trim();
                
                long nominal = Long.parseLong(nominalStr);
                txtNominal.setText(String.valueOf(nominal));
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleNext() {
        System.out.println("Tombol Next diklik");
        
        String input = txtNominal.getText().trim();
        if (input.isEmpty()) {
            showAlert("Gagal", "Nominal harus diisi!");
            txtNominal.requestFocus();
            return;
        }

        try {
            long nominal = Long.parseLong(input);

            if (nominal < 10000) {
                showAlert("Gagal", "Minimal Top Up Rp10.000");
                txtNominal.requestFocus();
                return;
            }

            if (nominal > 10000000) {
                showAlert("Gagal", "Maksimal Top Up Rp10.000.000");
                txtNominal.requestFocus();
                return;
            }

            String metode = TempData.getMetodeTopup();
            
            TempData.setNominalTopup(nominal);

            showPaymentCodeAndInput(metode, nominal);

        } catch (NumberFormatException e) {
            showAlert("Gagal", "Nominal harus angka!");
            txtNominal.requestFocus();
        }
    }
    
    private void generatePaymentCode(String metode) {
        Random rand = new Random();
        
        if (metode.contains("Alfamart")) {
            kodePembayaran = "ALF" + String.format("%06d", rand.nextInt(1000000));
        } else if (metode.contains("Indomaret")) {
            kodePembayaran = "IND" + String.format("%06d", rand.nextInt(1000000));
        } else if (metode.contains("Lawson")) {
            kodePembayaran = "LAW" + String.format("%06d", rand.nextInt(1000000));
        } else if (metode.contains("BCA")) {
            kodePembayaran = "BCA" + String.format("%013d", rand.nextInt(1000000000));
        } else if (metode.contains("BRI")) {
            kodePembayaran = "BRI" + String.format("%013d", rand.nextInt(1000000000));
        } else if (metode.contains("BNI")) {
            kodePembayaran = "BNI" + String.format("%013d", rand.nextInt(1000000000));
        } else {
            kodePembayaran = "TOP" + String.format("%06d", rand.nextInt(1000000));
        }
        
        System.out.println("Kode Pembayaran: " + kodePembayaran);
    }
    
    private void showPaymentCodeAndInput(String metode, long nominal) {
        try {
            Stage codeStage = new Stage();
            codeStage.setTitle("SmartPay");
            codeStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            
            VBox mainBox = new VBox(20);
            mainBox.setStyle(
                "-fx-background-color: #23449dff; " +
                "-fx-padding: 25px; " +
                "-fx-alignment: center;"
            );
            
            VBox headerBox = new VBox(10);
            headerBox.setStyle("-fx-alignment: center;");
            
            Label lblHeader = new Label("📋 KODE PEMBAYARAN");
            lblHeader.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
            
            Label lblSubHeader = new Label("Gunakan kode berikut untuk pembayaran");
            lblSubHeader.setStyle("-fx-font-size: 15px; -fx-text-fill: #add1fbff;");
            
            headerBox.getChildren().addAll(lblHeader, lblSubHeader);
            
            VBox cardBox = new VBox(20);
            cardBox.setPrefWidth(400);
            cardBox.setStyle(
                "-fx-background-color: white; " +
                "-fx-background-radius: 15px; " +
                "-fx-padding: 25px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0.5, 0, 3);"
            );
            
            VBox infoBox = new VBox(15);
            
            String[][] infoData = {
                {"Metode", metode},
                {"Nominal", SaldoManager.formatSaldo(nominal)},
                {"Status", "MENUNGGU PEMBAYARAN"}
            };
            
            for (String[] row : infoData) {
                HBox rowBox = new HBox();
                rowBox.setStyle("-fx-alignment: center-left; -fx-spacing: 10;");
                
                Label lblKey = new Label(row[0] + ":");
                lblKey.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-min-width: 100px;");
                
                Label lblValue = new Label(row[1]);
                lblValue.setStyle("-fx-font-size: 13px; -fx-text-fill: #333;");
                
                rowBox.getChildren().addAll(lblKey, lblValue);
                infoBox.getChildren().add(rowBox);
            }
            
            cardBox.getChildren().add(infoBox);
            
            VBox codeDisplayBox = new VBox(10);
            codeDisplayBox.setStyle("-fx-alignment: center; -fx-padding: 20px; -fx-background-color: #f8fbff; -fx-border-radius: 10px;");
            
            Label lblCodeTitle = new Label("KODE PEMBAYARAN ANDA:");
            lblCodeTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1a5fb4;");
            
            Label lblCode = new Label(kodePembayaran);
            lblCode.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #ff8a4aff; -fx-letter-spacing: 2px;");
            
            Label lblInstruction = new Label("Salin kode ini dan gunakan untuk pembayaran");
            lblInstruction.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");
            
            codeDisplayBox.getChildren().addAll(lblCodeTitle, lblCode, lblInstruction);
            cardBox.getChildren().add(codeDisplayBox);
            
            VBox buttonBox = new VBox(10);
            buttonBox.setStyle("-fx-alignment: center; -fx-padding: 10px 0 0 0;");
            
            Button btnSalin = new Button("📋 Salin Kode");
            btnSalin.setPrefWidth(300);
            btnSalin.setPrefHeight(35);
            btnSalin.setStyle(
                "-fx-background-color: #29b098ff; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 13px; " +
                "-fx-background-radius: 8px; " +
                "-fx-cursor: hand;"
            );
            
            btnSalin.setOnAction(e -> {
                javafx.scene.input.Clipboard clipboard = javafx.scene.input.Clipboard.getSystemClipboard();
                javafx.scene.input.ClipboardContent content = new javafx.scene.input.ClipboardContent();
                content.putString(kodePembayaran);
                clipboard.setContent(content);
                
                Alert copiedAlert = new Alert(Alert.AlertType.INFORMATION);
                copiedAlert.setTitle("Berhasil");
                copiedAlert.setHeaderText(null);
                copiedAlert.setContentText("Kode berhasil disalin ke clipboard!");
                copiedAlert.showAndWait();
            });
            
            Button btnLanjut = new Button("✅ Saya Sudah Bayar, Lanjut Konfirmasi");
            btnLanjut.setPrefWidth(300);
            btnLanjut.setPrefHeight(35);
            btnLanjut.setStyle(
                "-fx-background-color: #fe8d42ff; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 14px; " +
                "-fx-background-radius: 8px; " +
                "-fx-cursor: hand;"
            );
            btnLanjut.setOnAction(e -> {
                codeStage.close();
                showInputCodeDialog(metode, nominal);
            });
            
            buttonBox.getChildren().addAll(btnSalin, btnLanjut);
            cardBox.getChildren().add(buttonBox);
            
            mainBox.getChildren().addAll(headerBox, cardBox);
            
            javafx.scene.Scene scene = new javafx.scene.Scene(mainBox, 400, 700);
            codeStage.setScene(scene);
            codeStage.showAndWait();
            
        } catch (Exception e) {
            System.err.println("Error menampilkan kode: " + e.getMessage());
            e.printStackTrace();
            showInputCodeDialog(metode, nominal);
        }
    }
    
    private void showInputCodeDialog(String metode, long nominal) {
        try {
            Alert reminderAlert = new Alert(Alert.AlertType.INFORMATION);
            reminderAlert.setTitle("Ingat Kode Anda");
            reminderAlert.setHeaderText("Kode Pembayaran Anda:");
            reminderAlert.setContentText(
                "Kode: " + kodePembayaran + "\n\n" +
                "Sekarang masukkan kode tersebut untuk konfirmasi."
            );
            reminderAlert.showAndWait();
            
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Konfirmasi Pembayaran");
            dialog.setHeaderText("Masukkan Kode Pembayaran");
            dialog.setContentText("Masukkan kode yang tadi diberikan:\n(Kode: " + kodePembayaran + ")");
            
            java.util.Optional<String> result = dialog.showAndWait();
            
            if (result.isPresent()) {
                String inputKode = result.get().trim();
                
                if (inputKode.isEmpty()) {
                    showAlert("Gagal", "Kode tidak boleh kosong!");
                    showInputCodeDialog(metode, nominal);
                    return;
                }
                
                if (inputKode.equals(kodePembayaran)) {
                    processSuccessfulPayment(metode, nominal);
                } else {
                    showAlert("Gagal", "Kode salah! Coba lagi.");
                    showInputCodeDialog(metode, nominal); 
                }
            } else {
                showAlert("Dibatalkan", "Pembayaran dibatalkan.");
                TempData.clear();
                App.setRoot("Home");
            }
            
        } catch (Exception e) {
            System.err.println("Error dalam input dialog: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Terjadi kesalahan");
        }
    }
    
    private void processSuccessfulPayment(String metode, long nominal) {
        String kodeTransaksi = "TRX" + (System.currentTimeMillis() % 1000000);
        
        SaldoManager.tambahSaldo(nominal);
        updateSaldoLabel();

        HomeController home = App.getHomeController();
        if (home != null) {
            home.updateSaldoHome();
        }

        String userId = UserData.getSmartId();
        if (userId != null && !userId.isEmpty()) {
            TransaksiStorage.tambahTransaksi(
                "TOPUP", 
                "TOP UP " + metode, 
                "PEMASUKAN", 
                nominal, 
                kodeTransaksi, 
                userId
            );
        }

        showProofOfPayment(metode, nominal, kodeTransaksi);
    }
    
    private void showProofOfPayment(String metode, long nominal, String kodeTransaksi) {
        try {
            Stage buktiStage = new Stage();
            buktiStage.setTitle("SmartPay");
            buktiStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            
            VBox mainBox = new VBox(20);
            mainBox.setStyle(
                "-fx-background-color: #1a5fb4; " +
                "-fx-padding: 25px; " +
                "-fx-alignment: center;"
            );
            
            VBox headerBox = new VBox(10);
            headerBox.setStyle("-fx-alignment: center;");
            
            javafx.scene.control.Label lblHeader = new javafx.scene.control.Label("✅ TOP UP BERHASIL");
            lblHeader.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
            
            javafx.scene.control.Label lblSub = new javafx.scene.control.Label("Pembayaran telah dikonfirmasi");
            lblSub.setStyle("-fx-font-size: 14px; -fx-text-fill: #cae3ff;");
            
            headerBox.getChildren().addAll(lblHeader, lblSub);
            
            VBox cardBox = new VBox(15);
            cardBox.setStyle(
                "-fx-background-color: white; " +
                "-fx-background-radius: 15px; " +
                "-fx-padding: 25px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0.5, 0, 2);"
            );
            cardBox.setPrefWidth(350);
            
            javafx.scene.control.Label lblTitle = new javafx.scene.control.Label("BUKTI TRANSAKSI");
            lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1a5fb4; -fx-alignment: center;");
            
            VBox detailBox = new VBox(10);
            detailBox.setStyle("-fx-padding: 15px 0;");
            
            String waktu = java.time.LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
            );
            
            String[][] data = {
                {"Tanggal", waktu},
                {"Nama", UserData.getNama()},
                {"Metode", metode},
                {"Kode Transaksi", kodeTransaksi},
                {"Kode Bayar", kodePembayaran},
                {"Nominal", SaldoManager.formatSaldo(nominal)},
                {"Status", "✅ BERHASIL"}
            };
            
            for (String[] row : data) {
                HBox rowBox = new HBox();
                rowBox.setStyle("-fx-alignment: center-left; -fx-spacing: 10;");
                
                javafx.scene.control.Label lblKey = new javafx.scene.control.Label(row[0] + ":");
                lblKey.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-min-width: 120px;");
                
                javafx.scene.control.Label lblValue = new javafx.scene.control.Label(row[1]);
                lblValue.setStyle("-fx-font-size: 13px; -fx-text-fill: #333;");
                
                rowBox.getChildren().addAll(lblKey, lblValue);
                detailBox.getChildren().add(rowBox);
            }
            
            javafx.scene.control.Button btnSelesai = new javafx.scene.control.Button("SELESAI");
            btnSelesai.setPrefWidth(300);
            btnSelesai.setPrefHeight(40);
            btnSelesai.setStyle(
                "-fx-background-color: #1a5fb4; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 14px; " +
                "-fx-background-radius: 8px; " +
                "-fx-cursor: hand;"
            );
            btnSelesai.setOnAction(e -> {
                buktiStage.close();
                TempData.clear();
                App.setRoot("Home");
            });
            
            cardBox.getChildren().addAll(lblTitle, detailBox, btnSelesai);
            mainBox.getChildren().addAll(headerBox, cardBox);
            
            javafx.scene.Scene scene = new javafx.scene.Scene(mainBox, 400, 700);
            buktiStage.setScene(scene);
            buktiStage.showAndWait();
            
        } catch (Exception e) {
            System.err.println("Error tampilkan bukti: " + e.getMessage());
            
            showAlert("Top Up Berhasil", 
                "✅ Top Up Berhasil!\n\n" +
                "Metode: " + metode + "\n" +
                "Nominal: " + SaldoManager.formatSaldo(nominal) + "\n" +
                "Kode: " + kodePembayaran + "\n\n" +
                "Saldo Anda telah bertambah."
            );
            
            TempData.clear();
            App.setRoot("Home");
        }
    }

    @FXML
    private void handleBack() {
        TempData.clear();
        App.setRoot("Topup");
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}