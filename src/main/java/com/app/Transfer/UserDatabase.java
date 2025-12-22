package com.app.Transfer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.Properties;

// KELAS INI AKAN DIGUNAKAN BERSAMA OLEH REGISTER DAN LOGIN
public class UserDatabase {
    private static final String DB_FILE = "smartpay_users.db";
    private static final String BACKUP_FILE = "smartpay_users_backup.txt";
    
    // Simpan user ke database
    public static boolean saveUser(String smartId, String email, String password) {
        try {
            System.out.println("\n=== MENYIMPAN USER KE DATABASE ===");
            System.out.println("Smart ID: " + smartId);
            System.out.println("Email: " + email);
            
            // Baca existing users
            Properties users = loadUsers();
            
            // Cek apakah user sudah ada
            String existingEmail = users.getProperty("user." + smartId + ".email");
            if (existingEmail != null) {
                System.out.println("✗ User dengan Smart ID ini sudah ada");
                return false;
            }
            
            // Cek apakah email sudah digunakan
            String existingSmartId = users.getProperty("email." + email);
            if (existingSmartId != null) {
                System.out.println("✗ Email ini sudah digunakan");
                return false;
            }
            
            // Tambahkan user baru
            users.setProperty("user." + smartId + ".smartId", smartId);
            users.setProperty("user." + smartId + ".email", email);
            users.setProperty("user." + smartId + ".password", password);
            users.setProperty("user." + smartId + ".timestamp", String.valueOf(System.currentTimeMillis()));
            
            // Mapping email -> smartId untuk login dengan email
            users.setProperty("email." + email, smartId);
            
            // Simpan ke file utama
            saveUsers(users);
            
            // Buat backup ke file teks
            saveToTextBackup(smartId, email, password);
            
            System.out.println("✓ User BERHASIL disimpan ke database");
            printAllUsers(); // Debug: tampilkan semua user
            return true;
            
        } catch (Exception e) {
            System.err.println("✗ Gagal save user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Authenticate user
    public static boolean authenticate(String username, String password) {
        try {
            Properties users = loadUsers();
            
            System.out.println("\n=== CEK AUTHENTIKASI ===");
            System.out.println("Username: " + username);
            
            // Cek jika username adalah smartId
            String storedPassword = users.getProperty("user." + username + ".password");
            if (storedPassword != null) {
                boolean match = storedPassword.equals(password);
                System.out.println("Cek dengan Smart ID: " + match);
                if (match) return true;
            }
            
            // Cek jika username adalah email
            String smartId = users.getProperty("email." + username);
            if (smartId != null) {
                storedPassword = users.getProperty("user." + smartId + ".password");
                if (storedPassword != null) {
                    boolean match = storedPassword.equals(password);
                    System.out.println("Cek dengan Email: " + match);
                    return match;
                }
            }
            
            System.out.println("✗ User tidak ditemukan");
            return false;
            
        } catch (Exception e) {
            System.err.println("✗ Error auth: " + e.getMessage());
            return false;
        }
    }
    
    // Get user data by username (smartId or email)
    public static UserData getUserData(String username) {
        try {
            Properties users = loadUsers();
            
            String smartId = username;
            String email = users.getProperty("user." + username + ".email");
            
            if (email == null) {
                // Mungkin username adalah email
                smartId = users.getProperty("email." + username);
                if (smartId == null) {
                    return null;
                }
                email = users.getProperty("user." + smartId + ".email");
            }
            
            String password = users.getProperty("user." + smartId + ".password");
            
            if (smartId != null && email != null && password != null) {
                return new UserData(smartId, email, password);
            }
            
        } catch (Exception e) {
            System.err.println("Error get user data: " + e.getMessage());
        }
        return null;
    }
    
    // Load users dari file
    private static Properties loadUsers() {
        Properties users = new Properties();
        try {
            File file = new File(DB_FILE);
            if (file.exists()) {
                FileInputStream in = new FileInputStream(file);
                users.load(in);
                in.close();
                System.out.println("✓ Database loaded: " + file.getAbsolutePath());
            } else {
                System.out.println("⚠ Database file belum ada, akan dibuat baru");
            }
        } catch (Exception e) {
            System.err.println("✗ Error loading database: " + e.getMessage());
        }
        return users;
    }
    
    // Save users ke file
    private static void saveUsers(Properties users) {
        try {
            FileOutputStream out = new FileOutputStream(DB_FILE);
            users.store(out, "SmartPay Users Database - " + new java.util.Date());
            out.close();
            System.out.println("✓ Database saved: " + DB_FILE);
        } catch (Exception e) {
            System.err.println("✗ Error saving database: " + e.getMessage());
        }
    }
    
    // Backup ke file teks
    private static void saveToTextBackup(String smartId, String email, String password) {
        try {
            FileWriter writer = new FileWriter(BACKUP_FILE, true);
            writer.write(String.format(
                "SmartID: %s | Email: %s | Password: %s | Time: %s%n",
                smartId, email, "***masked***", new java.util.Date()
            ));
            writer.close();
            System.out.println("✓ Backup saved: " + BACKUP_FILE);
        } catch (Exception e) {
            System.err.println("✗ Error backup: " + e.getMessage());
        }
    }
    
    // Print all users (debug)
    public static void printAllUsers() {
        try {
            System.out.println("\n=== DAFTAR USER DALAM DATABASE ===");
            Properties users = loadUsers();
            
            boolean found = false;
            for (String key : users.stringPropertyNames()) {
                if (key.startsWith("user.") && key.endsWith(".smartId")) {
                    String smartId = users.getProperty(key);
                    String email = users.getProperty(key.replace(".smartId", ".email"));
                    System.out.println("📋 SmartID: " + smartId + " | Email: " + email);
                    found = true;
                }
            }
            
            if (!found) {
                System.out.println("Tidak ada user dalam database");
            }
            System.out.println("================================\n");
        } catch (Exception e) {
            System.err.println("✗ Error print users: " + e.getMessage());
        }
    }
    
    // Inner class untuk user data
    public static class UserData {
        private String smartId;
        private String email;
        private String password;
        
        public UserData(String smartId, String email, String password) {
            this.smartId = smartId;
            this.email = email;
            this.password = password;
        }
        
        public String getSmartId() { return smartId; }
        public String getEmail() { return email; }
        public String getPassword() { return password; }
    }
}