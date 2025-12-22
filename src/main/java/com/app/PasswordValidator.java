package com.app;

public class PasswordValidator {
    
    public boolean validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        
        // Cek panjang minimal 8 karakter
        if (password.length() < 8) {
            return false;
        }
        
        // Cek ada huruf besar
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }
        
        // Cek ada huruf kecil
        if (!password.matches(".*[a-z].*")) {
            return false;
        }
        
        // Cek ada angka
        if (!password.matches(".*[0-9].*")) {
            return false;
        }
        
        return true;
    }
    
    // Method tambahan untuk mendapatkan detail validasi
    public String[] getPasswordRequirements() {
        return new String[] {
            "Minimal 8 karakter",
            "Harus mengandung huruf besar (A-Z)",
            "Harus mengandung huruf kecil (a-z)",
            "Harus mengandung angka (0-9)"
        };
    }
    
    // Method untuk cek kekuatan password (optional)
    public int getPasswordStrength(String password) {
        if (password == null) return 0;
        
        int strength = 0;
        
        // Panjang password
        if (password.length() >= 8) strength += 25;
        if (password.length() >= 12) strength += 10;
        
        // Kompleksitas
        if (password.matches(".*[A-Z].*")) strength += 25;
        if (password.matches(".*[a-z].*")) strength += 20;
        if (password.matches(".*[0-9].*")) strength += 20;
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) strength += 10;
        
        return Math.min(strength, 100);
    }
}