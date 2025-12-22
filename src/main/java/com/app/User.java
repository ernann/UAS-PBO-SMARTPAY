package com.app;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private static Set<String> existingRekenings = new HashSet<>();
    
    private String smartId;
    private String email;
    private String password;
    private String nama;
    private String nomorRekening;
    private String pin;
    private long saldo;
    
    public User(String smartId, String email, String password, String nama) {
        this.smartId = smartId;
        this.email = email;
        this.password = password;
        this.nama = nama;
        this.nomorRekening = generateNomorRekening();
        this.saldo = 0; // SALDO AWAL DARI 0
        this.pin = "123456";
    }
    
    public User(String smartId, String email, String password, String nama, String nomorRekening, long saldo, String pin) {
        this.smartId = smartId;
        this.email = email;
        this.password = password;
        this.nama = nama;
        this.nomorRekening = nomorRekening;
        this.saldo = saldo;
        this.pin = pin;
        
        if (nomorRekening != null && nomorRekening.startsWith("006")) {
            existingRekenings.add(nomorRekening);
        }
    }

    public String getSmartId() { 
        return smartId; 
    }
    
    public void setSmartId(String smartId) { 
        this.smartId = smartId; 
    }
    
    public String getEmail() { 
        return email; 
    }
    
    public void setEmail(String email) { 
        this.email = email; 
    }
    
    public String getPassword() { 
        return password; 
    }
    
    public void setPassword(String password) { 
        this.password = password; 
    }
    
    public String getNama() { 
        return nama; 
    }
    
    public void setNama(String nama) { 
        this.nama = nama; 
    }
    
    public String getNomorRekening() { 
        return nomorRekening; 
    }
    
    public void setNomorRekening(String nomorRekening) { 
        this.nomorRekening = nomorRekening; 
    }
    
    public String getPin() { 
        return pin; 
    }
    
    public void setPin(String pin) { 
        this.pin = pin; 
    }
    
    public long getSaldo() { 
        return saldo; 
    }
    
    public void setSaldo(long saldo) { 
        this.saldo = saldo; 
    }
    
    public boolean kurangiSaldo(long jumlah) {
        if (jumlah > 0 && jumlah <= saldo) {
            saldo -= jumlah;
            return true;
        }
        return false;
    }
    
    public void tambahSaldo(long jumlah) {
        if (jumlah > 0) {
            saldo += jumlah;
        }
    }
    
    public String getSaldoFormatted() {
        return "Rp" + String.format("%,d", saldo);
    }
    
    public boolean validatePin(String inputPin) {
        return pin.equals(inputPin);
    }
    
    private String generateNomorRekening() {
        String nomorRekening;
        int attempts = 0;
        
        do {
            int random = 1000000 + (int)(Math.random() * 9000000);
            nomorRekening = "006" + String.format("%07d", random);
            attempts++;
            
            if (attempts > 100) {
                long timestamp = System.currentTimeMillis() % 10000000;
                nomorRekening = "006" + String.format("%07d", timestamp);
                break;
            }
        } while (existingRekenings.contains(nomorRekening));
        
        existingRekenings.add(nomorRekening);
        return nomorRekening;
    }
    
    @Override
    public String toString() {
        return "User{" +
               "smartId='" + smartId + '\'' +
               ", email='" + email + '\'' +
               ", nama='" + nama + '\'' +
               ", rekening='" + nomorRekening + '\'' +
               ", saldo=" + getSaldoFormatted() +
               ", pin='" + "***" + '\'' +
               '}';
    }
    
    public static boolean isValidRekeningFormat(String rekening) {
        return rekening != null && 
               rekening.matches("006\\d{7}") && 
               rekening.length() == 10;
    }
}