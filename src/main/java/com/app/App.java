package com.app;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private static Stage stage;
    private static Scene scene;
    private static HomeController homeController;

    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;
        stage.setTitle("SmartPay - Aplikasi Transaksi & Riwayat");
        stage.setResizable(false);

        // SET UKURAN TETAP
        stage.setMinWidth(400);
        stage.setMinHeight(700);
        stage.setMaxWidth(400);
        stage.setMaxHeight(700);

        System.out.println("=== MEMUAT LOGIN SCREEN ===");
        
        // Load Login sebagai halaman pertama
        try {
            Parent root = loadFXML("Login");
            scene = new Scene(root, 400, 700);
            stage.setScene(scene);
            stage.show();
            System.out.println("✓ Login screen berhasil dimuat");
        } catch (IOException e) {
            System.err.println("❌ GAGAL MEMUAT LOGIN: " + e.getMessage());
            e.printStackTrace();
            scene = new Scene(new javafx.scene.layout.StackPane(), 400, 700);
            stage.setScene(scene);
            stage.show();
        }

        System.out.println("=== APLIKASI SMARTPAY DIMULAI ===");
    }

    // ===================== HOME CONTROLLER ACCESS =====================
    public static HomeController getHomeController() {
        return homeController;
    }

    // ===================== STAGE & ROOT =====================
    public static Stage getStage() {
        return stage;
    }

    public static void setRoot(String fxml) {
        try {
            System.out.println("\n═══════════════════════════════════════");
            System.out.println("🔄 NAVIGASI KE: " + fxml + ".fxml");
            System.out.println("═══════════════════════════════════════");

            long startTime = System.currentTimeMillis();
            Parent root = null;
            
            // SPECIAL CASE: Untuk Home, gunakan caching
            if (fxml.equals("Home") && homeController != null) {
                System.out.println("🔄 Menggunakan cached HomeController");
                try {
                    root = homeController.getRootNode();
                    if (root != null) {
                        System.out.println("✓ Root node berhasil diambil dari cache");
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ Gagal ambil dari cache, load fresh");
                    homeController = null;
                }
            }
            
            // Jika root masih null, load fresh
            if (root == null) {
                // SPECIAL CASE: Untuk Riwayat dengan package khusus
                if (fxml.equals("RiwayatDetail") || fxml.equals("Detail")) {
                    System.out.println("🔄 Loading detail riwayat dari package khusus");
                    try {
                        // Coba load dari package khusus
                        FXMLLoader loader = new FXMLLoader(
                            App.class.getResource("/com/app/Riwayat/" + fxml + ".fxml")
                        );
                        root = loader.load();
                        System.out.println("✓ Detail riwayat berhasil dimuat");
                    } catch (Exception e) {
                        System.out.println("⚠️ Gagal load dari package khusus: " + e.getMessage());
                        // Fallback ke path biasa
                        root = loadFXML(fxml);
                    }
                } else {
                    root = loadFXML(fxml);
                }
                
                // Cache HomeController jika memuat Home
                if (fxml.equals("Home")) {
                    System.out.println("🔄 Loading Home.fxml fresh");
                    java.net.URL url = App.class.getResource("/com/app/Home.fxml");
                    if (url == null) {
                        System.err.println("❌ Home.fxml tidak ditemukan!");
                        return;
                    }
                    FXMLLoader loader = new FXMLLoader(url);
                    root = loader.load();
                    homeController = loader.getController();
                    System.out.println("✓ HomeController disimpan di cache");
                }
            }
            
            long loadTime = System.currentTimeMillis() - startTime;
            System.out.println("✅ FXML dimuat dalam " + loadTime + "ms");
            
            // Set root ke scene
            if (scene == null) {
                System.out.println("⚠️ Scene null, buat baru");
                scene = new Scene(root, 400, 700);
                stage.setScene(scene);
            } else {
                scene.setRoot(root);
            }
            
            System.out.println("✓ Navigasi ke " + fxml + " berhasil!");
            System.out.println("═══════════════════════════════════════\n");

        } catch (IOException e) {
            System.err.println("\n❌ GAGAL MEMUAT FXML: " + fxml);
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();

            // Coba path alternatif
            System.out.println("🔄 Coba path alternatif...");
            try {
                Parent root = loadFXMLSimple(fxml);
                if (root != null) {
                    scene.setRoot(root);
                    System.out.println("✓ Berhasil dimuat dari path alternatif");
                    return;
                }
            } catch (Exception ex) {
                System.err.println("✗ Gagal juga dari alternatif: " + ex.getMessage());
            }

            // Fallback ke Login jika bukan Login
            if (!fxml.equals("Login")) {
                System.out.println("🔄 Fallback ke Login...");
                try {
                    Parent root = loadFXML("Login");
                    scene.setRoot(root);
                } catch (IOException ex) {
                    System.err.println("✗ Gagal fallback ke Login!");
                    javafx.scene.control.Label errorLabel = new javafx.scene.control.Label(
                        "Error: " + e.getMessage() + "\nFile: " + fxml + ".fxml"
                    );
                    errorLabel.setStyle("-fx-text-fill: red; -fx-padding: 20;");
                    scene.setRoot(new javafx.scene.layout.StackPane(errorLabel));
                }
            }
        } catch (Exception e) {
            System.err.println("\n⚠️ ERROR TIDAK TERDUGA: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ===================== LOAD FXML UTAMA (SIMPLE) =====================
    private static Parent loadFXML(String fxml) throws IOException {
        System.out.println("📂 Loading file: " + fxml + ".fxml");

        // Prioritaskan path utama
        String mainPath = "/com/app/" + fxml + ".fxml";
        java.net.URL url = App.class.getResource(mainPath);
        
        if (url != null) {
            System.out.println("📍 File ditemukan di: " + mainPath);
            return FXMLLoader.load(url);
        }

        // Jika tidak ditemukan, coba path alternatif
        System.out.println("⚠️ Tidak ditemukan di " + mainPath + ", cari alternatif...");
        
        String[] altPaths = {
            "/" + fxml + ".fxml",
            fxml + ".fxml",
            "resources/com/app/" + fxml + ".fxml",
            "src/main/resources/com/app/" + fxml + ".fxml"
        };

        for (String path : altPaths) {
            System.out.println("   Mencari: " + path);
            url = App.class.getResource(path);
            if (url != null) {
                System.out.println("✅ Ditemukan di: " + path);
                return FXMLLoader.load(url);
            }
        }

        throw new IOException("File " + fxml + ".fxml tidak ditemukan di semua lokasi!");
    }
    
    // ===================== LOAD FXML SIMPLE =====================
    private static Parent loadFXMLSimple(String fxml) throws IOException {
        System.out.println("🔄 Simple load: " + fxml);
        
        String[] allPaths = {
            "/com/app/" + fxml + ".fxml",
            "/" + fxml + ".fxml",
            fxml + ".fxml"
        };
        
        for (String path : allPaths) {
            try {
                java.net.URL url = App.class.getResource(path);
                if (url != null) {
                    System.out.println("✅ Simple load berhasil dari: " + path);
                    return FXMLLoader.load(url);
                }
            } catch (Exception e) {
                System.out.println("   Gagal dari " + path + ": " + e.getMessage());
            }
        }
        
        throw new IOException("Simple load gagal untuk " + fxml);
    }

    // ===================== NAVIGASI KE FITUR =====================
    
    public static void showHome() {
        System.out.println("\n=== SHOW HOME DIPANGGIL ===");
        setRoot("Home");
    }
    
    public static void showTopUp() {
        System.out.println("\n=== NAVIGASI KE TOP UP ===");
        setRoot("Topup");
    }
    
    public static void showPembayaran() {
        System.out.println("\n=== NAVIGASI KE PEMBAYARAN ===");
        setRoot("Pembayaran");
    }
    
    public static void showReminder() {
        System.out.println("\n=== NAVIGASI KE REMINDER ===");
        setRoot("ReminderList");
    }

    // ===================== RIWAYAT =====================
    public static void showRiwayat() {
        System.out.println("\n=== NAVIGASI KE RIWAYAT ===");
        
        // Cek login
        String smartId = UserData.getSmartId();
        if (smartId == null || smartId.isEmpty()) {
            System.out.println("⚠️ User belum login, redirect ke Login");
            showAlert("Login Required", "Silakan login terlebih dahulu!");
            setRoot("Login");
            return;
        }
        
        System.out.println("User: " + UserData.getNama() + " mengakses riwayat");
        setRoot("Riwayat");
    }
    
    public static void showRiwayatDetail() {
        System.out.println("\n=== NAVIGASI KE DETAIL RIWAYAT ===");
        setRoot("Detail");
    }
    
    // ===================== ALERT UTILITY =====================
    private static void showAlert(String title, String message) {
        javafx.application.Platform.runLater(() -> {
            try {
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.INFORMATION
                );
                alert.setTitle(title);
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.showAndWait();
            } catch (Exception e) {
                System.err.println("Gagal show alert: " + e.getMessage());
            }
        });
    }

    // ===================== LOGOUT =====================
    public static void logout() {
        try {
            System.out.println("\n=== LOGOUT ===");
            
            // Clear user data
            UserData.setNama("User");
            UserData.setSmartId("");
            UserData.setEmail("");
            UserData.setNomorRekening("");
            UserData.setSaldo(0);
            SaldoManager.setSaldo(0);
            
            // Clear cache
            homeController = null;
            
            // Kembali ke Login
            Parent root = loadFXML("Login");
            if (scene != null) {
                scene.setRoot(root);
            } else {
                scene = new Scene(root, 400, 700);
                stage.setScene(scene);
            }
            
            System.out.println("✓ Logout berhasil");
        } catch (IOException e) {
            System.err.println("✗ Gagal logout: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ===================== MAIN =====================
    public static void main(String[] args) {
        System.out.println("=== MEMULAI APLIKASI SMARTPAY ===");
        System.out.println("Java: " + System.getProperty("java.version"));
        System.out.println("OS: " + System.getProperty("os.name"));
        
        try {
            launch(args);
        } catch (Exception e) {
            System.err.println("❌ APLIKASI GAGAL DIMULAI: " + e.getMessage());
            e.printStackTrace();
        }
    }
}