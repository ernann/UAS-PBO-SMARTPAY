package com.app.Riwayat.controller;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class SectionHeaderCell {
    private final HBox root = new HBox();
    private final Label title = new Label();
    private final Label dateIndicator = new Label(); 
    private final Region spacer = new Region();

    public SectionHeaderCell() {
        root.setStyle("-fx-background-color: transparent; " +
                     "-fx-padding: 24px 0 12px 0; " +
                     "-fx-alignment: center-left;");
        
        dateIndicator.setStyle("-fx-font-size: 12px; " +
                             "-fx-font-weight: 500; " +
                             "-fx-text-fill: #888888; " +
                             "-fx-padding: 0 8px 0 0; " +
                             "-fx-font-family: 'SF Pro Text', -apple-system, sans-serif;");
        
        title.setStyle("-fx-font-size: 16px; " +
                      "-fx-font-weight: 600; " +
                      "-fx-text-fill: #1a5fb4; " +
                      "-fx-font-family: 'SF Pro Display', -apple-system, sans-serif;");
        
        spacer.setStyle("-fx-background-color: linear-gradient(to right, #d0e7ff, #1a5fb4 50%, #d0e7ff); " +
                       "-fx-max-height: 2px; " +
                       "-fx-pref-height: 2px; " +
                       "-fx-min-height: 2px; " +
                       "-fx-opacity: 0.4; " +
                       "-fx-background-radius: 1px;");
        
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        root.getChildren().addAll(dateIndicator, title, spacer);
    }

    public void update(String headerText) {
        String[] parts = headerText.split(" ");
        if (parts.length >= 3) {
            String day = parts[0];
            String month = parts[1];
            String year = parts[2];
            
            dateIndicator.setText("📅");
            title.setText(day + " " + month + " " + year);
        } else {
            title.setText(headerText.toUpperCase());
        }
    }

    public HBox getRoot() {
        return root;
    }
}