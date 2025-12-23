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
    private static HomeController homeController; // 🔹 simpan instance HomeController

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
        Parent root = loadFXML("Login");
        scene = new Scene(root, 400, 700);

        stage.setScene(scene);
        stage.show();

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
            Parent root = loadFXML(fxml);
            long loadTime = System.currentTimeMillis() - startTime;

            System.out.println("✅ FXML dimuat dalam " + loadTime + "ms");
            scene.setRoot(root);
            stage.sizeToScene();

            System.out.println("✓ Navigasi ke " + fxml + " berhasil!");

        } catch (IOException e) {
            System.err.println("\n❌ GAGAL MEMUAT FXML: " + fxml);
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();

            // Coba load file lain untuk testing
            if (!fxml.equals("Login")) {
                System.out.println("🔄 Coba kembali ke Login...");
                try {
                    Parent root = loadFXML("Login");
                    scene.setRoot(root);
                } catch (IOException ex) {
                    System.err.println("Gagal kembali ke Login!");
                }
            }
        } catch (Exception e) {
            System.err.println("\n⚠️ ERROR TIDAK TERDUGA: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ===================== HOME =====================
    public static void showHome() {
        if (homeController != null) {
            scene.setRoot(homeController.getRootNode());
            stage.sizeToScene();
            System.out.println("✓ Navigasi ke Home menggunakan instance lama berhasil!");
        } else {
            setRoot("Home"); // fallback jika HomeController belum di-load
        }
    }

    // ===================== ALFAMART =====================
    public static void showAlfamart() {
        try {
            setRoot("AlfaTopup"); // navigasi ke halaman Alfamart
            System.out.println("✓ Navigasi ke Alfamart berhasil!");
        } catch (Exception e) {
            System.err.println("✗ Gagal navigasi ke Alfamart: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ===================== RIWAYAT =====================
    public static void navigateToRiwayat() {
        try {
            System.out.println("\n=== NAVIGASI KE RIWAYAT TRANSAKSI ===");
            Parent root = loadFXML("Riwayat");
            scene.setRoot(root);
            stage.sizeToScene();
        } catch (IOException e) {
            System.err.println("Gagal navigasi ke Riwayat: " + e.getMessage());
        }
    }

    // ===================== LOGOUT =====================
    public static void logout() {
        try {
            System.out.println("\n=== MELAKUKAN LOGOUT ===");
            Parent root = loadFXML("Login");
            scene.setRoot(root);
            stage.sizeToScene();
            System.out.println("✓ Logout berhasil, kembali ke Login");
        } catch (IOException e) {
            System.err.println("✗ Gagal logout: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ===================== LOAD FXML DENGAN CONTROLLER CACHE =====================
    private static Parent loadFXML(String fxml) throws IOException {
        System.out.println("📂 Loading file: " + fxml + ".fxml");

        // Cek resource URL - cari di berbagai lokasi
        String[] possiblePaths = {
            "/com/app/" + fxml + ".fxml",
            "/com/app/Riwayat/tugas/" + fxml + ".fxml",
            "/" + fxml + ".fxml",
            fxml + ".fxml"
        };

        java.net.URL url = null;
        for (String path : possiblePaths) {
            url = App.class.getResource(path);
            if (url != null) {
                System.out.println("📍 File ditemukan di: " + path);
                break;
            }
        }

        if (url == null) {
            System.err.println("❌ FILE TIDAK DITEMUKAN: " + fxml + ".fxml");
            System.err.println("   Mencari di: " + String.join(", ", possiblePaths));
            System.err.println("   Classpath: " + System.getProperty("java.class.path"));
            throw new IOException("File " + fxml + ".fxml tidak ditemukan!");
        }

        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();

        // 🔹 Simpan instance HomeController jika memuat Home
        if (fxml.equals("Home")) {
            homeController = loader.getController();
            System.out.println("✓ HomeController instance tersimpan di App");
        }

        return root;
    }

    // ===================== MAIN =====================
    public static void main(String[] args) {
        System.out.println("=== MEMULAI APLIKASI SMARTPAY TERPADU ===");
        System.out.println("Java Version: " + System.getProperty("java.version"));
        System.out.println("Working Directory: " + System.getProperty("user.dir"));
        System.out.println("=== FITUR YANG TERSEDIA ===");
        System.out.println("1. Login & Register");
        System.out.println("2. Riwayat Transaksi");
        System.out.println("3. Detail Transaksi");
        System.out.println("4. Cell Components Dinamis");
        launch();
    }
}
