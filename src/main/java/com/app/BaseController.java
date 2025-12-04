package com.app;

import javafx.scene.control.Alert;

// Superclass untuk controller lain
public class BaseController {

    protected void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Bisa ditambahkan method umum lain jika dibutuhkan
}
