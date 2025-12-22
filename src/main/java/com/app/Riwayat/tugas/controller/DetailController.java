package com.app.Riwayat.tugas.controller;

import com.app.Riwayat.tugas.model.Transaction;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class DetailController extends BaseClockController implements ControllerInterface {
    @FXML private Label bankNameLabel;
    @FXML private Label accountLabel;
    @FXML private Label amountLabel;
    @FXML private Label dateTimeLabel;
    @FXML private Label typeLabel;
    @FXML private Label categoryLabel;
    @FXML private Label statusTimeLabel;
    
    private Transaction transaction;

    @FXML
    public void initialize() {
        initializeClock();
    }
    
    @Override
    public void initializeClock() {
        super.initializeClock(statusTimeLabel);
    }
    
    @Override
    public void stopClock() {
        super.stopClock();
    }
    
    @Override
    public void displayContent() {
        if (transaction != null) {
            setTransaction(transaction);
        }
    }
    
    // Overloading method (Polymorphism - compile time)
    public void setTransaction(Transaction t) {
        this.transaction = t;
        updateUI(t);
    }
    
    // Overloaded version
    public void setTransaction(Transaction t, boolean showFullDate) {
        this.transaction = t;
        if (showFullDate) {
            updateUIWithFullDate(t);
        } else {
            updateUI(t);
        }
    }
    
    private void updateUI(Transaction t) {
        // 1. Bank name dengan ikon
        String bankIcon = getBankIcon(t.getBank());
        String bankPart = t.getBank().isEmpty() ? bankIcon + " " + t.getName() : 
                         bankIcon + " " + t.getBank() + " - " + t.getName();
        bankNameLabel.setText(bankPart);
        
        // 2. Account number dengan format yang lebih baik
        String accountText = t.getAccount().isEmpty() || t.getAccount().equals("-") ? 
                           "Tidak ada nomor rekening" : 
                           "No. Rek: " + formatAccountNumber(t.getAccount());
        accountLabel.setText(accountText);
        
        // 3. Amount dengan format yang lebih elegan
        String amountText = formatAmount(t.getAmount());
        amountLabel.setText(amountText);
        
        // 4. Set warna amount berdasarkan tipe transaksi dengan gradien
        if ("Pengeluaran".equals(t.getType())) {
            amountLabel.setStyle("-fx-font-size: 36px; " +
                               "-fx-font-weight: 800; " +
                               "-fx-text-fill: linear-gradient(to right,  #ff5252); " +
                               "-fx-alignment: center; " +
                               "-fx-padding: 16px 0 20px 0; " +
                               "-fx-font-family: 'SF Pro Display', -apple-system, sans-serif; " +
                               "-fx-effect: dropshadow(gaussian, rgba(229, 57, 53, 0.3), 0, 0, 0, 2);");
        } else {
            amountLabel.setStyle("-fx-font-size: 36px; " +
                               "-fx-font-weight: 800; " +
                               "-fx-text-fill: linear-gradient(to right, #4caf50); " +
                               "-fx-alignment: center; " +
                               "-fx-padding: 16px 0 20px 0; " +
                               "-fx-font-family: 'SF Pro Display', -apple-system, sans-serif; " +
                               "-fx-effect: dropshadow(gaussian, rgba(46, 125, 50, 0.3), 0, 0, 0, 2);");
        }
        
        // 5. Date time dengan format yang lebih baik: "📅 06 Oktober 2025 • 19:26:37"
        String formattedDateTime = "📅 " + formatDate(t.getDate()) + " • " + t.getTime();
        dateTimeLabel.setText(formattedDateTime);
        
        // 6. Transaction type dengan ikon
        String typeIcon = t.getType().equals("Pengeluaran") ? "⬇️" : "⬆️";
        typeLabel.setText(typeIcon + " " + t.getType());
        
        // 7. Category dengan ikon
        String categoryIcon = getCategoryIcon(t.getCategory());
        categoryLabel.setText(categoryIcon + " " + t.getCategory());
    }
    
    private void updateUIWithFullDate(Transaction t) {
        // Versi alternatif untuk display tanggal lengkap
        String formattedDateTime = "📅 " + formatDate(t.getDate()) + " pukul " + t.getTime();
        dateTimeLabel.setText(formattedDateTime);
        
        // Sisanya sama dengan updateUI
        String bankIcon = getBankIcon(t.getBank());
        String bankPart = t.getBank().isEmpty() ? bankIcon + " " + t.getName() : 
                         bankIcon + " " + t.getBank() + " - " + t.getName();
        bankNameLabel.setText(bankPart);
        
        String accountText = t.getAccount().isEmpty() || t.getAccount().equals("-") ? 
                           "Tidak ada nomor rekening" : 
                           "No. Rek: " + formatAccountNumber(t.getAccount());
        accountLabel.setText(accountText);
        
        String amountText = formatAmount(t.getAmount());
        amountLabel.setText(amountText);
        
        // Set warna amount dengan gradien
        if ("Pengeluaran".equals(t.getType())) {
            amountLabel.setStyle("-fx-font-size: 36px; " +
                               "-fx-font-weight: 800; " +
                               "-fx-text-fill: linear-gradient(to right, #e53935, #ff5252); " +
                               "-fx-alignment: center; " +
                               "-fx-padding: 16px 0 20px 0; " +
                               "-fx-font-family: 'SF Pro Display', -apple-system, sans-serif; " +
                               "-fx-effect: dropshadow(gaussian, rgba(229, 57, 53, 0.3), 0, 0, 0, 2);");
        } else {
            amountLabel.setStyle("-fx-font-size: 36px; " +
                               "-fx-font-weight: 800; " +
                               "-fx-text-fill: linear-gradient(to right, #2e7d32, #4caf50); " +
                               "-fx-alignment: center; " +
                               "-fx-padding: 16px 0 20px 0; " +
                               "-fx-font-family: 'SF Pro Display', -apple-system, sans-serif; " +
                               "-fx-effect: dropshadow(gaussian, rgba(46, 125, 50, 0.3), 0, 0, 0, 2);");
        }
        
        String typeIcon = t.getType().equals("Pengeluaran") ? "⬇️" : "⬆️";
        typeLabel.setText(typeIcon + " " + t.getType());
        
        String categoryIcon = getCategoryIcon(t.getCategory());
        categoryLabel.setText(categoryIcon + " " + t.getCategory());
    }
    
    private String getBankIcon(String bankName) {
        switch (bankName) {
            case "BCA": return "🏦";
            case "BRI": return "🏛️";
            case "BNI": return "🏢";
            case "MANDIRI": return "🏨";
            case "BJB": return "🏣";
            default: return "💳";
        }
    }
    
    private String getCategoryIcon(String category) {
        switch (category) {
            case "Transfer": return "↔️";
            case "Pembayaran": return "💳";
            case "Gaji": return "💰";
            case "Bonus": return "🎁";
            case "Hiburan": return "🎬";
            case "Makanan": return "🍽️";
            case "Belanja": return "🛍️";
            default: return "📋";
        }
    }
    
    private String formatAccountNumber(String account) {
        // Format nomor rekening menjadi lebih mudah dibaca
        if (account.length() <= 4) return account;
        
        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < account.length(); i++) {
            if (i > 0 && i % 4 == 0) {
                formatted.append(" ");
            }
            formatted.append(account.charAt(i));
        }
        return formatted.toString();
    }
    
    private String formatAmount(int amount) {
        String sign = amount < 0 ? "-" : "+";
        return sign + " Rp" + String.format("%,d", Math.abs(amount)).replace(',', '.');
    }
    
    private String formatDate(String date) {
        // Konversi format tanggal menjadi lebih readable
        String[] parts = date.split(" ");
        if (parts.length >= 3) {
            String day = parts[0];
            String month = getFullMonthName(parts[1]);
            String year = parts[2];
            return day + " " + month + " " + year;
        }
        return date;
    }
    
    private String getFullMonthName(String monthAbbr) {
        switch (monthAbbr) {
            case "Jan": return "Januari";
            case "Feb": return "Februari";
            case "Mar": return "Maret";
            case "Apr": return "April";
            case "Mei": return "Mei";
            case "Jun": return "Juni";
            case "Jul": return "Juli";
            case "Ags": return "Agustus";
            case "Sep": return "September";
            case "Okt": return "Oktober";
            case "Nov": return "November";
            case "Des": return "Desember";
            default: return monthAbbr;
        }
    }
    
    @FXML
    private void goBack() {
        stopClock();
        Stage stage = (Stage) bankNameLabel.getScene().getWindow();
        stage.close();
    }
}