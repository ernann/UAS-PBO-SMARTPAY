package com.app.Registrasi;

public class PasswordValidator {
    
    public boolean validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        
        if (password.length() < 8) {
            return false;
        }
        
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }
        
        if (!password.matches(".*[a-z].*")) {
            return false;
        }
        
        if (!password.matches(".*[0-9].*")) {
            return false;
        }
        
        return true;
    }
    
    public String[] getPasswordRequirements() {
        return new String[] {
            "Minimal 8 karakter",
            "Harus mengandung huruf besar (A-Z)",
            "Harus mengandung huruf kecil (a-z)",
            "Harus mengandung angka (0-9)"
        };
    }
    
    public int getPasswordStrength(String password) {
        if (password == null) return 0;
        
        int strength = 0;
        
        if (password.length() >= 8) strength += 25;
        if (password.length() >= 12) strength += 10;
        
        if (password.matches(".*[A-Z].*")) strength += 25;
        if (password.matches(".*[a-z].*")) strength += 20;
        if (password.matches(".*[0-9].*")) strength += 20;
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) strength += 10;
        
        return Math.min(strength, 100);
    }
}