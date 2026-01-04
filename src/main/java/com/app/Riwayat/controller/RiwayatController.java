package com.app.Riwayat.controller;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import com.app.App;
import com.app.SaldoManager;
import com.app.TransaksiStorage;
import com.app.UserData;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RiwayatController {

    @FXML
    private Label labelTitle;
    @FXML
    private VBox riwayatContainer;
    @FXML
    private ComboBox<String> filterComboBox;

    @FXML
    public void initialize() {
        try {
            System.out.println("=== RIWAYAT CONTROLLER INITIALIZED ===");
            System.out.println("User: " + UserData.getNama());
            System.out.println("Smart ID: " + UserData.getSmartId());
            
            labelTitle.setText("Riwayat Transaksi");
            setupFilterBulan();
            tampilkanTransaksiUser();
            
            System.out.println("✓ RiwayatController berhasil diinisialisasi");
            
        } catch (Exception e) {
            System.err.println("❌ ERROR: " + e.getMessage());
            e.printStackTrace();
            
            if (riwayatContainer != null) {
                Label errorLabel = new Label("Error: " + e.getMessage());
                errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px; -fx-padding: 20;");
                riwayatContainer.getChildren().add(errorLabel);
            }
        }
    }

    private void setupFilterBulan() {
        ObservableList<String> bulanList = FXCollections.observableArrayList(
            "SEMUA BULAN",
            "JANUARI", "FEBRUARI", "MARET", "APRIL", "MEI", "JUNI",
            "JULI", "AGUSTUS", "SEPTEMBER", "OKTOBER", "NOVEMBER", "DESEMBER"
        );
        
        filterComboBox.setItems(bulanList);
        filterComboBox.setValue("SEMUA BULAN");
        
        filterComboBox.setOnAction(event -> {
            String selectedMonth = filterComboBox.getValue();
            System.out.println("Filter bulan dipilih: " + selectedMonth);
            filterTransaksiByMonth(selectedMonth);
        });
    }
    
    private void filterTransaksiByMonth(String selectedMonth) {
        riwayatContainer.getChildren().clear();
        
        String userId = UserData.getSmartId();
        if (userId == null || userId.isEmpty()) {
            Label lblKosong = new Label("Silakan login terlebih dahulu");
            lblKosong.setStyle("-fx-text-fill: #666; -fx-font-size: 16px; -fx-padding: 20;");
            riwayatContainer.getChildren().add(lblKosong);
            return;
        }
        
        List<TransaksiStorage.TransaksiRecord> transaksiList = 
            TransaksiStorage.getTransaksiByUser(userId);
        
        if (selectedMonth.equals("SEMUA BULAN")) {
            tampilkanTransaksiDenganFilter(transaksiList);
        } else {
            List<TransaksiStorage.TransaksiRecord> filteredList = 
                transaksiList.stream()
                    .filter(t -> getNamaBulan(t.getWaktu().getMonthValue()).equals(selectedMonth))
                    .collect(java.util.stream.Collectors.toList());
            
            if (filteredList.isEmpty()) {
                VBox boxKosong = new VBox(10);
                boxKosong.setStyle("-fx-alignment: center; -fx-padding: 40;");
                
                Label lblIcon = new Label("📭");
                lblIcon.setStyle("-fx-font-size: 48px; -fx-text-fill: #999;");
                
                Label lblKosong = new Label("Tidak ada transaksi di bulan " + selectedMonth);
                lblKosong.setStyle("-fx-text-fill: #666; -fx-font-size: 16px; -fx-font-weight: bold;");
                
                boxKosong.getChildren().addAll(lblIcon, lblKosong);
                riwayatContainer.getChildren().add(boxKosong);
            } else {
                buatStatistikBulan(filteredList, selectedMonth);
                
                for (int i = filteredList.size() - 1; i >= 0; i--) {
                    buatKartuTransaksi(filteredList.get(i));
                }
            }
        }
    }

    private void tampilkanTransaksiUser() {
        filterTransaksiByMonth(filterComboBox.getValue());
    }
    
    private void tampilkanTransaksiDenganFilter(List<TransaksiStorage.TransaksiRecord> transaksiList) {
        riwayatContainer.getChildren().clear();
        
        if (transaksiList.isEmpty()) {
            VBox boxKosong = new VBox(10);
            boxKosong.setStyle("-fx-alignment: center; -fx-padding: 40;");
            
            Label lblIcon = new Label("📭");
            lblIcon.setStyle("-fx-font-size: 48px; -fx-text-fill: #999;");
            
            Label lblKosong = new Label("Belum ada transaksi");
            lblKosong.setStyle("-fx-text-fill: #666; -fx-font-size: 16px; -fx-font-weight: bold;");
            
            Label lblInfo = new Label("Lakukan Top Up atau Transfer terlebih dahulu");
            lblInfo.setStyle("-fx-text-fill: #888; -fx-font-size: 14px; -fx-text-alignment: center;");
            
            boxKosong.getChildren().addAll(lblIcon, lblKosong, lblInfo);
            riwayatContainer.getChildren().add(boxKosong);
            return;
        }
        
        java.util.Map<String, List<TransaksiStorage.TransaksiRecord>> kelompok = 
            new java.util.HashMap<>();
        
        for (TransaksiStorage.TransaksiRecord t : transaksiList) {
            String bulan = getNamaBulan(t.getWaktu().getMonthValue());
            int tahun = t.getWaktu().getYear();
            String key = bulan + " " + tahun;
            
            kelompok.computeIfAbsent(key, k -> new java.util.ArrayList<>()).add(t);
        }
        
        List<String> daftarBulan = new java.util.ArrayList<>(kelompok.keySet());
        daftarBulan.sort((b1, b2) -> {
            String[] parts1 = b1.split(" ");
            String[] parts2 = b2.split(" ");
            
            int tahun1 = Integer.parseInt(parts1[1]);
            int tahun2 = Integer.parseInt(parts2[1]);
            
            if (tahun1 != tahun2) {
                return Integer.compare(tahun2, tahun1);
            }
            
            int bulanIndex1 = getBulanIndex(parts1[0]);
            int bulanIndex2 = getBulanIndex(parts2[0]);
            
            return Integer.compare(bulanIndex2, bulanIndex1);
        });
        
        for (String bulanTahun : daftarBulan) {
            List<TransaksiStorage.TransaksiRecord> transaksiBulan = kelompok.get(bulanTahun);
            
            Label lblHeader = new Label("📅 " + bulanTahun + " (" + transaksiBulan.size() + " transaksi)");
            lblHeader.setStyle("-fx-font-size: 18px; " +
                             "-fx-font-weight: bold; " +
                             "-fx-text-fill: #1a5fb4; " +
                             "-fx-padding: 20 0 10 0; " +
                             "-fx-border-color: #c2e0ff; " +
                             "-fx-border-width: 0 0 1px 0;");
            riwayatContainer.getChildren().add(lblHeader);
            
            buatStatistikBulan(transaksiBulan, bulanTahun);
            
            for (int i = transaksiBulan.size() - 1; i >= 0; i--) {
                buatKartuTransaksi(transaksiBulan.get(i));
            }
        }
    }

    private void buatKartuTransaksi(TransaksiStorage.TransaksiRecord t) {
        VBox kartu = new VBox(10);
        kartu.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 12px; " +
            "-fx-padding: 15px; " +
            "-fx-border-color: #c2e0ff; " +
            "-fx-border-radius: 12px; " +
            "-fx-border-width: 1px; " +
            "-fx-margin: 0 0 10px 0;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 3, 0, 0, 1);" +
            "-fx-cursor: hand;"
        );
        
        kartu.setOnMouseClicked(e -> {
            if (e.getClickCount() == 1) {
                showDetailTransaksi(t);
            }
        });
        
        HBox baris1 = new HBox();
        baris1.setStyle("-fx-alignment: center-left;");
        
        Label lblJenis = new Label(t.getDisplayJenis());
        lblJenis.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #1a5fb4; -fx-padding: 0 10 0 0;");
        
        Label lblJumlah = new Label(t.getJumlahFormatted());
        if (t.getTipe().equals("PENGELUARAN")) {
            lblJumlah.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #ce2a27ff;");
        } else {
            lblJumlah.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1c7e3eff;");
        }
        
        baris1.getChildren().addAll(lblJenis, lblJumlah);
        
        Label lblDeskripsi = new Label(t.getDeskripsi());
        lblDeskripsi.setStyle("-fx-font-size: 13px; -fx-text-fill: #333; -fx-font-weight: 500;");
        
        HBox baris3 = new HBox(20);
        baris3.setStyle("-fx-alignment: center-left;");
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm", new Locale("id", "ID"));
        String tanggalFormatted = t.getWaktu().format(formatter);
        Label lblTanggal = new Label("📅 " + tanggalFormatted);
        lblTanggal.setStyle("-fx-font-size: 11px; -fx-text-fill: #666;");
        
        Label lblKode = new Label("🔑 " + t.getKodeTransaksi());
        lblKode.setStyle("-fx-font-size: 11px; -fx-text-fill: #666; -fx-font-style: italic;");
        
        Label lblJenisDetail = new Label(" " + t.getJenis());
        lblJenisDetail.setStyle("-fx-font-size: 11px; -fx-text-fill: #1a5fb4; -fx-font-weight: 500;");
        
        baris3.getChildren().addAll(lblTanggal, lblKode, lblJenisDetail);
        
        Label lblDetailIcon = new Label("Lihat Detail");
        lblDetailIcon.setStyle("-fx-font-size: 11px; -fx-text-fill: #1a5fb4; -fx-font-weight: 500; -fx-padding: 5px 0 0 0;");
        
        kartu.getChildren().addAll(baris1, lblDeskripsi, baris3, lblDetailIcon);
        riwayatContainer.getChildren().add(kartu);
    }
    
    private void showDetailTransaksi(TransaksiStorage.TransaksiRecord t) {
        try {
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Bukti Transaksi");
            dialogStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            
            VBox mainContainer = new VBox(0);
            mainContainer.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #d0e7ff, #e8f4ff); " +
                "-fx-background-radius: 0;"
            );
            
                VBox headerSection = new VBox(12);
    headerSection.setStyle(
        "-fx-background-color: linear-gradient(to bottom, #1a5fb4, #1a5fb4); " +
        "-fx-padding: 15px 20px 25px 20px;"
    );

            
            // Tombol Kembali yang bagus
            Button btnKembali = new Button("Kembali");
            btnKembali.setStyle(
                "-fx-background-color: #f0f8ff; " +
                "-fx-text-fill: #1a5fb4; " +
                "-fx-font-size: 14px; " +
                "-fx-cursor: hand; " +
                "-fx-padding: 6px 10px; " +
                "-fx-background-radius: 20px; " +
                "-fx-border-width: 1px; " +
                "-fx-border-radius: 20px;" +
                "-fx-font-weight: bold"
            );
            btnKembali.setPrefWidth(100);
            btnKembali.setOnAction(e -> dialogStage.close());
            
            // Judul dan ikon di tengah
            VBox titleBox = new VBox(4);
            titleBox.setStyle("-fx-alignment: center;");
            Label mainTitle = new Label("Bukti Transaksi");
            mainTitle.setStyle(
                "-fx-font-size: 30px; " +
                "-fx-font-weight: 700; " +
                "-fx-text-fill: #e6f7ff;"
            );
            
            Label subtitle = new Label("Detail lengkap transaksi");
            subtitle.setStyle(
                "-fx-font-size: 15px; " +
                "-fx-text-fill: rgba(255, 255, 255, 0.8); " +
                "-fx-font-weight: 400;"
            );
            
            titleBox.getChildren().addAll(mainTitle, subtitle);
            headerSection.getChildren().addAll(btnKembali, titleBox);
            
            VBox contentSection = new VBox(16);
            contentSection.setStyle(
                "-fx-background-color: transparent; " +
                "-fx-padding: 20px 20px 25px 20px;"
            );

            
            // KARTU UTAMA
            VBox mainCard = new VBox(0);
            mainCard.setStyle(
                "-fx-background-color: white; " +
                "-fx-background-radius: 16px; " +
                "-fx-border-radius: 16px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0.3, 0, 3);"
            );
            
            // Header Kartu (Transaction Info & Amount)
            VBox cardHeader = new VBox(4);
            cardHeader.setStyle(
                "-fx-padding: 20px 16px 16px 16px; " +
                "-fx-background-color: linear-gradient(to right, #f5faff, #e8f4ff); " +
                "-fx-background-radius: 16px 16px 0 0; " +
                "-fx-alignment: center;"
            );
            
            String displayJenis = t.getDisplayJenis();
            Label transaksiLabel = new Label( " " + displayJenis);
            transaksiLabel.setStyle(
                "-fx-font-size: 18px; " +
                "-fx-font-weight: 700; " +
                "-fx-text-fill: #1a5fb4; " +
                "-fx-wrap-text: true;"
            );
            
            // User Account Info
            Label accountLabel = new Label("👤 " + UserData.getNama() + " • " + UserData.getSmartId());
            accountLabel.setStyle(
                "-fx-font-size: 14px; " +
                "-fx-text-fill: #666666;"
            );
            
            Label amountLabel = new Label(t.getJumlahFormatted());
        if (t.getTipe().equals("PEMASUKAN")) {
            amountLabel.setStyle(
                    "-fx-font-size: 28px;" +
                    "-fx-font-weight: 800;" +
                    "-fx-text-fill: #2e7d32;" +
                    "-fx-padding: 10 0 12 0;" +
                    "-fx-border-color: #e8f0ff;" +
                    "-fx-border-width: 0 0 1 0;"
            );
        } else {
            amountLabel.setStyle(
                    "-fx-font-size: 28px;" +
                    "-fx-font-weight: 800;" +
                    "-fx-text-fill: #e53935;" +
                    "-fx-padding: 10 0 12 0;" +
                    "-fx-border-color: #e8f0ff;" +
                    "-fx-border-width: 0 0 1 0;"
            );
        }
            
            cardHeader.getChildren().addAll(transaksiLabel, accountLabel, amountLabel);
            
            // Detail Title
            HBox detailTitleBox = new HBox();
            detailTitleBox.setStyle(
                "-fx-padding: 16px 16px 12px 16px; " +
                "-fx-background-color: #fafcff;"
            );
            
            Label detailTitle = new Label("Detail Transaksi");
            detailTitle.setStyle(
                "-fx-font-size: 16px; " +
                "-fx-font-weight: 600; " +
                "-fx-text-fill: #1a5fb4;"
            );
            
            detailTitleBox.getChildren().add(detailTitle);
            
            VBox detailItemsContainer = new VBox(0);
            
            HBox waktuRow = createDetailRow(
                "Waktu",
                t.getWaktu().format(DateTimeFormatter.ofPattern("dd MMMM yyyy • HH:mm:ss", new Locale("id", "ID")))
            );
            
            // Separator
            Region separator1 = new Region();
            separator1.setStyle("-fx-background-color: #f0f4ff; -fx-pref-height: 1px;");
            
            // 2. Tipe - tanpa icon sama sekali
            String tipeText = t.getTipe().equals("PEMASUKAN") ? "Pemasukan" : "Pengeluaran";
            HBox tipeRow = createDetailRow("Tipe", tipeText);
            
            // Separator
            Region separator2 = new Region();
            separator2.setStyle("-fx-background-color: #f0f4ff; -fx-pref-height: 1px;");
            
            // 3. Kategori/Jenis - tanpa icon
            HBox jenisRow = createDetailRow("Jenis", t.getJenis());
            
            // Separator
            Region separator3 = new Region();
            separator3.setStyle("-fx-background-color: #f0f4ff; -fx-pref-height: 1px;");
            
            // 4. ID Transaksi - tanpa icon
            HBox idRow = createDetailRow("ID Transaksi", t.getKodeTransaksi());
            
            // Separator
            Region separator4 = new Region();
            separator4.setStyle("-fx-background-color: #f0f4ff; -fx-pref-height: 1px;");
            
            // 5. Deskripsi - tanpa icon
            String deskripsiText = t.getDeskripsi();
            if (deskripsiText != null && !deskripsiText.isEmpty()) {
                HBox deskripsiRow = createDetailRow("Deskripsi", deskripsiText);
                deskripsiRow.setStyle(
                    "-fx-padding: 12px 16px 16px 16px; " +
                    "-fx-min-height: 44px; " +
                    "-fx-background-color: #fcfdff; " +
                    "-fx-background-radius: 0 0 16px 16px;"
                );
                detailItemsContainer.getChildren().addAll(
                    waktuRow, separator1, tipeRow, separator2, 
                    jenisRow, separator3, idRow, separator4, deskripsiRow
                );
            } else {
                jenisRow.setStyle(
                    "-fx-padding: 12px 16px 16px 16px; " +
                    "-fx-min-height: 44px; " +
                    "-fx-background-color: #fcfdff; " +
                    "-fx-background-radius: 0 0 16px 16px;"
                );
                detailItemsContainer.getChildren().addAll(
                    waktuRow, separator1, tipeRow, separator2, 
                    jenisRow, separator3, idRow
                );
            }
            
            // Assemble main card
            mainCard.getChildren().addAll(cardHeader, detailTitleBox, detailItemsContainer);
            
            // FOOTER
            HBox footer = new HBox();
            footer.setStyle(
                "-fx-alignment: center; " +
                "-fx-padding: 8px;"
            );
            
            Label footerLabel = new Label("Transaksi telah tercatat dengan aman");
            footerLabel.setStyle(
                "-fx-font-size: 12px; " +
                "-fx-text-fill: #666666;"
            );
            
            footer.getChildren().add(footerLabel);
            
            // Assemble content section
            contentSection.getChildren().addAll(mainCard, footer);
            
            // Assemble main container
            mainContainer.getChildren().addAll(headerSection, contentSection);
            
            // Create scene dengan ukuran tepat 400x700
            Scene scene = new Scene(mainContainer, 400, 700);
            dialogStage.setScene(scene);
            
            // Set fixed size 400x700
            dialogStage.setMinWidth(400);
            dialogStage.setMinHeight(700);
            dialogStage.setMaxWidth(400);
            dialogStage.setMaxHeight(700);
            
            // Remove window decorations
            dialogStage.initStyle(javafx.stage.StageStyle.UNDECORATED);
            
            // Add drag functionality
            final double[] xOffset = new double[1];
            final double[] yOffset = new double[1];
            headerSection.setOnMousePressed(event -> {
                xOffset[0] = event.getSceneX();
                yOffset[0] = event.getSceneY();
            });
            headerSection.setOnMouseDragged(event -> {
                dialogStage.setX(event.getScreenX() - xOffset[0]);
                dialogStage.setY(event.getScreenY() - yOffset[0]);
            });
            
            dialogStage.showAndWait();
            
        } catch (Exception e) {
            System.err.println("Error menampilkan detail transaksi: " + e.getMessage());
            e.printStackTrace();
            
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Gagal menampilkan detail transaksi");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    
    private HBox createDetailRow(String label, String value) {
        HBox row = new HBox();
        row.setStyle(
            "-fx-padding: 12px 16px; " +
            "-fx-min-height: 44px; " +
            "-fx-background-color: #fcfdff;"
        );
        
        // Label
        Label lblLabel = new Label(label);
        lblLabel.setStyle(
            "-fx-font-size: 13px; " +
            "-fx-text-fill: #555555; " +
            "-fx-min-width: 100px; " +
            "-fx-font-weight: 500;"
        );
        
        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Value
        Label lblValue = new Label(value);
        lblValue.setStyle(
            "-fx-font-size: 13px; " +
            "-fx-text-fill: #000000; " +
            "-fx-font-weight: 600; " +
            "-fx-wrap-text: true;" +
            "-fx-alignment: center-right;"
        );
        
        row.getChildren().addAll(lblLabel, spacer, lblValue);
        return row;
    }

    private String getNamaBulan(int bulanAngka) {
        switch (bulanAngka) {
            case 1: return "JANUARI";
            case 2: return "FEBRUARI";
            case 3: return "MARET";
            case 4: return "APRIL";
            case 5: return "MEI";
            case 6: return "JUNI";
            case 7: return "JULI";
            case 8: return "AGUSTUS";
            case 9: return "SEPTEMBER";
            case 10: return "OKTOBER";
            case 11: return "NOVEMBER";
            case 12: return "DESEMBER";
            default: return "UNKNOWN";
        }
    }
    
    private int getBulanIndex(String namaBulan) {
        String[] bulanOrder = {
            "JANUARI", "FEBRUARI", "MARET", "APRIL", "MEI", "JUNI",
            "JULI", "AGUSTUS", "SEPTEMBER", "OKTOBER", "NOVEMBER", "DESEMBER"
        };
        
        for (int i = 0; i < bulanOrder.length; i++) {
            if (bulanOrder[i].equals(namaBulan)) {
                return i;
            }
        }
        return 0;
    }
    
    private void buatStatistikBulan(List<TransaksiStorage.TransaksiRecord> transaksi, String periode) {
        long totalPemasukan = 0;
        long totalPengeluaran = 0;
        
        for (TransaksiStorage.TransaksiRecord t : transaksi) {
            if (t.getTipe().equals("PEMASUKAN")) {
                totalPemasukan += t.getJumlah();
            } else {
                totalPengeluaran += t.getJumlah();
            }
        }
        
        VBox statBox = new VBox(10);
        statBox.setStyle(
            "-fx-background-color: #e1f0ffff; " +
            "-fx-background-radius: 12px; " +
            "-fx-padding: 18px; " +
            "-fx-border-color: #69a7e4ff; " +
            "-fx-border-radius: 12px; " +
            "-fx-border-width: 1.5px; " +
            "-fx-margin: 0 0 15px 0;"
        );
        
        HBox headerRow = new HBox(8);
        headerRow.setStyle("-fx-alignment: center-left;");
        Label lblIcon = new Label("📊");
        lblIcon.setStyle("-fx-font-size: 16px;");
        Label lblJudul = new Label("Statistik " + periode);
        lblJudul.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill:   #1a5fb4;");
        headerRow.getChildren().addAll(lblIcon, lblJudul);
        
        HBox statRow = new HBox(20);
        statRow.setStyle("-fx-alignment: center-left; -fx-padding: 8px 0;");
        
        VBox kolomPemasukan = new VBox(4);
        kolomPemasukan.setStyle("-fx-min-width: 150px;");
        Label lblPemasukanValue = new Label("+ " + SaldoManager.formatSaldo(totalPemasukan));
        lblPemasukanValue.setStyle("-fx-font-size: 15px; -fx-text-fill: #2e7d32; -fx-font-weight: bold;");
        kolomPemasukan.getChildren().addAll(lblPemasukanValue);
        
        VBox kolomPengeluaran = new VBox(4);
        kolomPengeluaran.setStyle("-fx-min-width: 150px;");
        Label lblPengeluaranValue = new Label("- " + SaldoManager.formatSaldo(totalPengeluaran));
        lblPengeluaranValue.setStyle("-fx-font-size: 15px; -fx-text-fill: #e53935; -fx-font-weight: bold;");
        kolomPengeluaran.getChildren().addAll(lblPengeluaranValue);
        
        statRow.getChildren().addAll(kolomPemasukan, kolomPengeluaran);
        
        javafx.scene.shape.Line separator = new javafx.scene.shape.Line(0, 0, 300, 0);
        separator.setStroke(javafx.scene.paint.Color.web("#b3d9ff"));
        separator.setStrokeWidth(1);
        
        long saldoBersih = totalPemasukan - totalPengeluaran;
        HBox saldoRow = new HBox(10);
        saldoRow.setStyle("-fx-alignment: center-left; -fx-padding: 8px 0 0 0;");
        
        Label lblSaldoIcon = new Label(saldoBersih >= 0 ? "💹" : "📉");
        lblSaldoIcon.setStyle("-fx-font-size: 16px;");
        
        Label lblSaldoTitle = new Label("Saldo Bersih:");
        lblSaldoTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1a5fb4; -fx-min-width: 90px;");
        
        Label lblSaldoValue = new Label((saldoBersih >= 0 ? "+" : "-") + " " + SaldoManager.formatSaldo(Math.abs(saldoBersih)));
        lblSaldoValue.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + 
                              (saldoBersih >= 0 ? "#2e7d32" : "#e53935") + ";");
        
        saldoRow.getChildren().addAll(lblSaldoIcon, lblSaldoTitle, lblSaldoValue);
        
        statBox.getChildren().addAll(headerRow, statRow, separator, saldoRow);
        
        riwayatContainer.getChildren().add(statBox);
    }

    @FXML
    private void handleBack() {
        System.out.println("Tombol Kembali diklik di Riwayat");
        try {
            App.setRoot("Home");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Gagal kembali ke Home: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleRefresh() {
        System.out.println("Tombol Refresh diklik");
        tampilkanTransaksiUser();
        showAlert("Refresh", "Data riwayat diperbarui");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}