package com.app.Transfer;

import com.app.LoginController;

public class SaldoManager {
    private static long saldo = 0;
    
    public static long getSaldo() {
        return saldo;
    }
    
    public static void setSaldo(long newSaldo) {
        saldo = newSaldo;
        System.out.println("✓ SaldoManager: Saldo diupdate menjadi " + formatSaldo(newSaldo));
    }
    
    public static boolean kurangiSaldo(long jumlah) {
        if (jumlah <= 0 || jumlah > saldo) {
            System.out.println("✗ SaldoManager: Gagal mengurangi saldo. Jumlah: " + 
                             formatSaldo(jumlah) + ", Saldo saat ini: " + formatSaldo(saldo));
            return false;
        }
        
        saldo -= jumlah;
        
        updateDatabaseSaldo();
        
        System.out.println("✓ SaldoManager: Saldo dikurangi " + formatSaldo(jumlah) + 
                          ", sisa: " + formatSaldo(saldo));
        return true;
    }
    
    public static void tambahSaldo(long jumlah) {
        if (jumlah > 0) {
            saldo += jumlah;
            
            updateDatabaseSaldo();
            
            System.out.println("✓ SaldoManager: Saldo ditambah " + formatSaldo(jumlah) + 
                              ", total: " + formatSaldo(saldo));
        }
    }
    
    public static String formatSaldo() {
        return "Rp" + String.format("%,d", saldo);
    }
    
    private static String formatSaldo(long amount) {
        return "Rp" + String.format("%,d", amount);
    }
    
    private static void updateDatabaseSaldo() {
        LoginController.updateUserSaldo(saldo);
    }
}