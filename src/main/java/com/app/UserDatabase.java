package com.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class UserDatabase {
    private static final String DB_FILE = "users.properties";
    private static final String SERIALIZED_FILE = "users_data.ser";
    
    private static Map<String, User> usersCache = new HashMap<>();
    private static Map<String, String> rekeningToUserId = new HashMap<>();
    
    static {
        loadFromSerialized();
    }
    
    public static boolean saveUser(User user) {
        try {
            if (!User.isValidRekeningFormat(user.getNomorRekening())) {
                System.err.println("✗ Format rekening tidak valid: " + user.getNomorRekening());
                return false;
            }
            
            if (rekeningToUserId.containsKey(user.getNomorRekening())) {
                System.err.println("✗ Nomor rekening sudah digunakan: " + user.getNomorRekening());
                return false;
            }
            
            saveToProperties(user);
            
            usersCache.put(user.getSmartId(), user);
            usersCache.put(user.getEmail().toLowerCase(), user);
            rekeningToUserId.put(user.getNomorRekening(), user.getSmartId());
            
            saveToSerialized();
            
            System.out.println("✓ User berhasil disimpan:");
            System.out.println("  Nama: " + user.getNama());
            System.out.println("  Smart ID: " + user.getSmartId());
            System.out.println("  Rekening: " + user.getNomorRekening());
            System.out.println("  Saldo: " + user.getSaldoFormatted());
            
            return true;
            
        } catch (Exception e) {
            System.err.println("✗ Gagal menyimpan user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public static User authenticate(String username, String password) {
        System.out.println("\n=== CEK AUTHENTIKASI ===");
        System.out.println("Username: " + username);
        
        User user = usersCache.get(username);
        if (user == null) {
            user = usersCache.get(username.toLowerCase());
        }
        
        if (user != null && user.getPassword().equals(password)) {
            System.out.println("✓ Login BERHASIL");
            System.out.println("  Nama: " + user.getNama());
            System.out.println("  Rekening: " + user.getNomorRekening());
            System.out.println("  Saldo: " + user.getSaldoFormatted());
            return user;
        }
        
        return authenticateFromProperties(username, password);
    }
    
    public static User getUser(String username) {
        User user = usersCache.get(username);
        if (user == null) {
            user = usersCache.get(username.toLowerCase());
        }
        return user;
    }
    
    public static User getUserByRekening(String nomorRekening) {
        String smartId = rekeningToUserId.get(nomorRekening);
        return smartId != null ? usersCache.get(smartId) : null;
    }
    
    public static void updateUser(User user) {
        usersCache.put(user.getSmartId(), user);
        usersCache.put(user.getEmail().toLowerCase(), user);
        rekeningToUserId.put(user.getNomorRekening(), user.getSmartId());
        saveToSerialized();
        saveToProperties(user);
        System.out.println("✓ User diperbarui: " + user.getNama());
    }
    
    public static boolean isUserExists(String smartId, String email) {
        boolean exists = usersCache.containsKey(smartId) || 
                        usersCache.containsKey(email.toLowerCase());
        System.out.println("Cek user exists - SmartID: " + smartId + ", Email: " + email + " = " + exists);
        return exists;
    }
    
    public static boolean isRekeningExists(String nomorRekening) {
        return rekeningToUserId.containsKey(nomorRekening);
    }
    
    public static void printAllUsers() {
        System.out.println("\n=== DAFTAR SEMUA USER ===");
        int count = 0;
        for (Map.Entry<String, User> entry : usersCache.entrySet()) {
            if (!entry.getKey().contains("@")) {
                System.out.println(entry.getValue());
                count++;
            }
        }
        System.out.println("Total: " + count + " users");
        System.out.println("=========================\n");
    }
    
    public static void printAllRekening() {
        System.out.println("\n=== DAFTAR NOMOR REKENING ===");
        for (Map.Entry<String, String> entry : rekeningToUserId.entrySet()) {
            User user = usersCache.get(entry.getValue());
            if (user != null) {
                System.out.println("Rekening: " + entry.getKey() + " -> " + user.getNama() + " (Saldo: " + user.getSaldoFormatted() + ")");
            }
        }
        System.out.println("Total: " + rekeningToUserId.size() + " rekening");
        System.out.println("=============================\n");
    }
    
    private static void saveToProperties(User user) {
        try {
            Properties props = new Properties();
            File file = new File(DB_FILE);
            if (file.exists()) {
                FileInputStream in = new FileInputStream(file);
                props.load(in);
                in.close();
            }
            
            props.setProperty("user." + user.getSmartId() + ".smartId", user.getSmartId());
            props.setProperty("user." + user.getSmartId() + ".email", user.getEmail());
            props.setProperty("user." + user.getSmartId() + ".password", user.getPassword());
            props.setProperty("user." + user.getSmartId() + ".nama", user.getNama());
            props.setProperty("user." + user.getSmartId() + ".rekening", user.getNomorRekening());
            props.setProperty("user." + user.getSmartId() + ".saldo", String.valueOf(user.getSaldo()));
            props.setProperty("user." + user.getSmartId() + ".pin", user.getPin());
            props.setProperty("email." + user.getEmail().toLowerCase(), user.getSmartId());
            props.setProperty("rekening." + user.getNomorRekening(), user.getSmartId());
            
            FileOutputStream out = new FileOutputStream(DB_FILE);
            props.store(out, "SmartPay User Database - " + new java.util.Date());
            out.close();
            
            System.out.println("✓ User disimpan ke properties file");
            
        } catch (Exception e) {
            System.err.println("✗ Error saving to properties: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void saveToSerialized() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SERIALIZED_FILE))) {
            out.writeObject(usersCache);
            out.writeObject(rekeningToUserId);
            System.out.println("✓ User disimpan ke serialized file");
        } catch (Exception e) {
            System.err.println("✗ Error saving serialized: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @SuppressWarnings("unchecked")
    private static void loadFromSerialized() {
        File file = new File(SERIALIZED_FILE);
        if (!file.exists()) {
            System.out.println("Serialized file not found, starting fresh database");
            usersCache = new HashMap<>();
            rekeningToUserId = new HashMap<>();
            
            loadFromPropertiesFile();
            return;
        }
        
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(SERIALIZED_FILE))) {
            usersCache = (Map<String, User>) in.readObject();
            rekeningToUserId = (Map<String, String>) in.readObject();
            System.out.println("✓ Loaded " + (usersCache.size() / 2) + " users from serialized file");
            System.out.println("✓ Loaded " + rekeningToUserId.size() + " rekening mappings");
        } catch (FileNotFoundException e) {
            System.out.println("Serialized file not found");
            usersCache = new HashMap<>();
            rekeningToUserId = new HashMap<>();
        } catch (Exception e) {
            System.err.println("✗ Error loading serialized: " + e.getMessage());
            e.printStackTrace();
            usersCache = new HashMap<>();
            rekeningToUserId = new HashMap<>();
        }
    }
    
    private static void loadFromPropertiesFile() {
        try {
            Properties props = new Properties();
            File file = new File(DB_FILE);
            if (!file.exists()) return;
            
            FileInputStream in = new FileInputStream(file);
            props.load(in);
            in.close();
            
            for (String key : props.stringPropertyNames()) {
                if (key.startsWith("user.") && key.endsWith(".smartId")) {
                    String smartId = props.getProperty(key);
                    String email = props.getProperty(key.replace(".smartId", ".email"));
                    String password = props.getProperty(key.replace(".smartId", ".password"));
                    String nama = props.getProperty(key.replace(".smartId", ".nama"));
                    String rekening = props.getProperty(key.replace(".smartId", ".rekening"));
                    String saldoStr = props.getProperty(key.replace(".smartId", ".saldo"));
                    String pin = props.getProperty(key.replace(".smartId", ".pin"));
                    
                    if (nama == null) nama = "User";
                    if (rekening == null) rekening = generateRandomRekening();
                    if (saldoStr == null) saldoStr = "0"; // Default saldo 0
                    if (pin == null) pin = "123456";
                    
                    long saldo = Long.parseLong(saldoStr);
                    
                    User user = new User(smartId, email, password, nama, rekening, saldo, pin);
                    usersCache.put(smartId, user);
                    usersCache.put(email.toLowerCase(), user);
                    rekeningToUserId.put(rekening, smartId);
                }
            }
            
            System.out.println("✓ Loaded " + (usersCache.size() / 2) + " users from properties file");
            System.out.println("✓ Loaded " + rekeningToUserId.size() + " rekening mappings");
            
        } catch (Exception e) {
            System.err.println("✗ Error loading from properties: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static User authenticateFromProperties(String username, String password) {
        try {
            Properties props = new Properties();
            File file = new File(DB_FILE);
            if (!file.exists()) return null;
            
            FileInputStream in = new FileInputStream(file);
            props.load(in);
            in.close();
            
            String smartId = null;
            String storedPassword = props.getProperty("user." + username + ".password");
            
            if (storedPassword != null && storedPassword.equals(password)) {
                smartId = username;
            } else {
                smartId = props.getProperty("email." + username.toLowerCase());
                if (smartId != null) {
                    storedPassword = props.getProperty("user." + smartId + ".password");
                    if (!storedPassword.equals(password)) {
                        return null;
                    }
                } else {
                    return null;
                }
            }
            
            String email = props.getProperty("user." + smartId + ".email");
            String nama = props.getProperty("user." + smartId + ".nama");
            String rekening = props.getProperty("user." + smartId + ".rekening");
            String saldoStr = props.getProperty("user." + smartId + ".saldo");
            String pin = props.getProperty("user." + smartId + ".pin");
            
            if (nama == null) nama = "User";
            if (rekening == null) rekening = generateRandomRekening();
            if (saldoStr == null) saldoStr = "0"; // Default saldo 0
            if (pin == null) pin = "123456";
            
            long saldo = Long.parseLong(saldoStr);
            
            User user = new User(smartId, email, password, nama, rekening, saldo, pin);
            
            usersCache.put(smartId, user);
            usersCache.put(email.toLowerCase(), user);
            rekeningToUserId.put(rekening, smartId);
            
            System.out.println("✓ Login BERHASIL dari properties file");
            return user;
            
        } catch (Exception e) {
            System.err.println("✗ Error auth from properties: " + e.getMessage());
            return null;
        }
    }
    
    private static String generateRandomRekening() {
        String rekening;
        do {
            int random = 1000000 + (int)(Math.random() * 9000000);
            rekening = "006" + String.format("%07d", random);
        } while (rekeningToUserId.containsKey(rekening));
        return rekening;
    }
}