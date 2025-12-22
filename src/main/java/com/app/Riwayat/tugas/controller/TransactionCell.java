package com.app.Riwayat.tugas.controller;

import java.util.function.Consumer;

import com.app.Riwayat.tugas.model.Transaction;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class TransactionCell {
    
    // INTERFACE untuk Cell Action
    public interface CellAction {
        void onCellClick(Transaction transaction);
        void onCellHover(Transaction transaction, boolean isHovering);
        void onCellUpdate(Transaction transaction);
    }
    
    // DEFAULT Cell Action Implementation
    public static class DefaultCellAction implements CellAction {
        @Override
        public void onCellClick(Transaction transaction) {
            System.out.println("Cell clicked: " + transaction.getName());
        }
        
        @Override
        public void onCellHover(Transaction transaction, boolean isHovering) {
            // Default implementation does nothing
        }
        
        @Override
        public void onCellUpdate(Transaction transaction) {
            // Default implementation does nothing
        }
    }
    
    // CUSTOM Cell Action untuk demonstrasi polymorphism
    public static class CustomCellAction implements CellAction {
        private int clickCount = 0;
        
        @Override
        public void onCellClick(Transaction transaction) {
            clickCount++;
            System.out.println("Custom Cell clicked #" + clickCount + ": " + transaction.getName());
        }
        
        @Override
        public void onCellHover(Transaction transaction, boolean isHovering) {
            if (isHovering) {
                System.out.println("Hovering over: " + transaction.getName());
            }
        }
        
        @Override
        public void onCellUpdate(Transaction transaction) {
            System.out.println("Cell updated: " + transaction.getName());
        }
    }
    
    // UTILITY Class dengan static methods
    public static class CellUtils {
        public static String formatAmount(int amount, boolean showSign) {
            String sign = amount < 0 ? "-" : (showSign ? "+" : "");
            return sign + "Rp" + String.format("%,d", Math.abs(amount)).replace(',', '.');
        }
        
        public static String getDisplayText(Transaction t) {
            return t.getBank().isEmpty() ? t.getName() : t.getBank() + " - " + t.getName();
        }
        
        public static boolean isExpense(Transaction t) {
            return "Pengeluaran".equals(t.getType());
        }
    }
    
    // MAIN TransactionCell class
    private final HBox root = new HBox();
    private final VBox left = new VBox();
    private final Label typeLabel = new Label();
    private final Label detailLabel = new Label();
    private final Label amountLabel = new Label();
    private Consumer<Transaction> onOpenDetail;
    private CellAction cellAction = new DefaultCellAction();

    // Constructor overloading
    public TransactionCell() {
        this(null);
    }
    
    public TransactionCell(Consumer<Transaction> onOpenDetail) {
        this.onOpenDetail = onOpenDetail;
        initialize();
    }
    
    private void initialize() {
        // Setup layout yang lebih elegan - BACKGROUND BIRU MUDA
        root.setStyle("-fx-background-color: #e6f3ff; " +
                     "-fx-padding: 18px 20px; " +
                     "-fx-alignment: center-left; " +
                     "-fx-cursor: hand; " +
                     "-fx-background-radius: 16px; " +
                     "-fx-border-radius: 16px; " +
                     "-fx-border-color: #c2e0ff; " +
                     "-fx-border-width: 1.5px; " +
                     "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 6, 0.1, 0, 2);");
        
        left.setStyle("-fx-spacing: 6px;");
        
        // Styling untuk labels yang lebih modern
        typeLabel.setStyle("-fx-font-size: 13px; " +
                          "-fx-text-fill: #666666; " +
                          "-fx-font-weight: 500; " +
                          "-fx-font-family: 'SF Pro Text', -apple-system, sans-serif;");
        
        detailLabel.setStyle("-fx-font-size: 16px; " +
                           "-fx-text-fill: #1a1a1a; " +
                           "-fx-font-weight: 600; " +
                           "-fx-wrap-text: true; " +
                           "-fx-font-family: 'SF Pro Text', -apple-system, sans-serif;");
        
        amountLabel.setStyle("-fx-font-size: 16px; " +
                           "-fx-font-weight: 700; " +
                           "-fx-alignment: center-right; " +
                           "-fx-font-family: 'SF Pro Display', -apple-system, sans-serif;");

        left.getChildren().addAll(detailLabel, typeLabel);
        root.getChildren().addAll(left, amountLabel);
        
        HBox.setHgrow(left, Priority.ALWAYS);
        HBox.setHgrow(amountLabel, Priority.NEVER);
        root.setAlignment(Pos.CENTER_LEFT);
        amountLabel.setAlignment(Pos.CENTER_RIGHT);

        // Event handlers
        setupEventHandlers();
    }
    
    private void setupEventHandlers() {
        root.setOnMouseClicked(e -> {
            if (root.getUserData() instanceof Transaction) {
                Transaction t = (Transaction) root.getUserData();
                System.out.println("TransactionCell clicked: " + t.getName());
                
                cellAction.onCellClick(t);
                
                if (onOpenDetail != null) {
                    onOpenDetail.accept(t);
                }
            }
        });
        
        root.setOnMouseEntered(e -> {
            if (root.getUserData() instanceof Transaction) {
                Transaction t = (Transaction) root.getUserData();
                cellAction.onCellHover(t, true);
                root.setStyle("-fx-background-color: #d0e7ff; " +
                            "-fx-padding: 18px 20px; " +
                            "-fx-alignment: center-left; " +
                            "-fx-cursor: hand; " +
                            "-fx-background-radius: 16px; " +
                            "-fx-border-radius: 16px; " +
                            "-fx-border-color: #99ccff; " +
                            "-fx-border-width: 1.5px; " +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0.2, 0, 3);");
            }
        });
        
        root.setOnMouseExited(e -> {
            if (root.getUserData() instanceof Transaction) {
                Transaction t = (Transaction) root.getUserData();
                cellAction.onCellHover(t, false);
                root.setStyle("-fx-background-color: #e6f3ff; " +
                            "-fx-padding: 18px 20px; " +
                            "-fx-alignment: center-left; " +
                            "-fx-cursor: hand; " +
                            "-fx-background-radius: 16px; " +
                            "-fx-border-radius: 16px; " +
                            "-fx-border-color: #c2e0ff; " +
                            "-fx-border-width: 1.5px; " +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 6, 0.1, 0, 2);");
            }
        });
    }
    
    // Method overloading
    public void update(Transaction t) {
        update(t, true);
    }
    
    public void update(Transaction t, boolean showSign) {
        root.setUserData(t);
        
        cellAction.onCellUpdate(t);
        
        // Set type (Transfer/Pembayaran) dengan ikon
        String categoryIcon = getCategoryIcon(t.getCategory());
        typeLabel.setText(categoryIcon + " " + t.getCategory());
        
        // Set detail: "BANK - NAMA" atau hanya "NAMA"
        String detailText = CellUtils.getDisplayText(t);
        detailLabel.setText(detailText);
        
        // Format amount dengan +/- dan warna
        String amountText = CellUtils.formatAmount(t.getAmount(), showSign);
        amountLabel.setText(amountText);
        
        // Set style berdasarkan tipe transaksi
        if (CellUtils.isExpense(t)) {
            amountLabel.setStyle("-fx-font-size: 16px; " +
                               "-fx-font-weight: 700; " +
                               "-fx-text-fill: #e53935; " +
                               "-fx-alignment: center-right; " +
                               "-fx-font-family: 'SF Pro Display', -apple-system, sans-serif;");
        } else {
            amountLabel.setStyle("-fx-font-size: 16px; " +
                               "-fx-font-weight: 700; " +
                               "-fx-text-fill: #2e7d32; " +
                               "-fx-alignment: center-right; " +
                               "-fx-font-family: 'SF Pro Display', -apple-system, sans-serif;");
        }
    }
    
    private String getCategoryIcon(String category) {
        switch (category) {
            case "Transfer": return "↔";
            case "Pembayaran": return "💳";
            case "Gaji": return "💰";
            case "Bonus": return "🎁";
            case "Hiburan": return "🎬";
            case "Makanan": return "🍽️";
            case "Belanja": return "🛍️";
            default: return "📋";
        }
    }
    
    // Setter untuk polymorphism runtime
    public void setCellAction(CellAction cellAction) {
        this.cellAction = cellAction != null ? cellAction : new DefaultCellAction();
    }
    
    public void setOnOpenDetail(Consumer<Transaction> consumer) {
        this.onOpenDetail = consumer;
    }
    
    public HBox getRoot() {
        return root;
    }
    
    // Factory method
    public static TransactionCell createCell(Consumer<Transaction> onOpenDetail) {
        return new TransactionCell(onOpenDetail);
    }
}