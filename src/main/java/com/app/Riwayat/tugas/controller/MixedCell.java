package com.app.Riwayat.tugas.controller;

import java.util.function.Consumer;

import com.app.Riwayat.tugas.model.Transaction;

import javafx.scene.Node;
import javafx.scene.control.ListCell;

public class MixedCell extends ListCell<Object> {
    
    // CellFactory Interface
    public interface CellFactory {
        Node createCell(Object item, Consumer<Transaction> onOpenDetail);
    }
    
    // Default CellFactory Implementation
    public static class DefaultCellFactory implements CellFactory {
        private final Consumer<Transaction> onOpenDetail;
        
        public DefaultCellFactory(Consumer<Transaction> onOpenDetail) {
            this.onOpenDetail = onOpenDetail;
        }
        
        @Override
        public Node createCell(Object item, Consumer<Transaction> onOpenDetail) {
            if (item instanceof String) {
                SectionHeaderCell headerCell = new SectionHeaderCell();
                headerCell.update((String) item);
                return headerCell.getRoot();
            } else if (item instanceof Transaction) {
                TransactionCell txCell = new TransactionCell(onOpenDetail);
                txCell.update((Transaction) item);
                return txCell.getRoot();
            }
            return null;
        }
    }
    
    private final Consumer<Transaction> onOpenDetail;
    private CellFactory cellFactory;
    
    // Constructor overloading
    public MixedCell(Consumer<Transaction> onOpenDetail) {
        this(onOpenDetail, new DefaultCellFactory(onOpenDetail));
    }
    
    public MixedCell(Consumer<Transaction> onOpenDetail, CellFactory cellFactory) {
        this.onOpenDetail = onOpenDetail;
        this.cellFactory = cellFactory;
    }
    
    @Override
    protected void updateItem(Object item, boolean empty) {
        super.updateItem(item, empty);
        
        if (empty || item == null) {
            setGraphic(null);
            setText(null);
        } else {
            Node cell = cellFactory.createCell(item, onOpenDetail);
            setGraphic(cell);
            setText(null);
        }
    }
    
    // Setter untuk mengganti factory di runtime (polymorphism)
    public void setCellFactory(CellFactory cellFactory) {
        this.cellFactory = cellFactory;
    }
}