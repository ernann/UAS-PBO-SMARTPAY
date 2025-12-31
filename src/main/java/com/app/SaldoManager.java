package com.app;

public class SaldoManager {
    static long saldo = 0;

    public static long getSaldo() {
        // Sinkron dengan UserData
        saldo = UserData.getSaldo();
        return saldo;
    }

    public static void setSaldo(long newSaldo) {
        saldo = newSaldo;
        UserData.setSaldo(newSaldo); // Sinkron dengan UserData
        updateDatabaseSaldo();
        System.out.println("✓ SaldoManager: Saldo diupdate menjadi " + formatSaldo(newSaldo));
    }

    public static boolean kurangiSaldo(long jumlah) {
        if (jumlah <= 0 || jumlah > saldo) {
            System.out.println("✗ SaldoManager: Gagal mengurangi saldo. Jumlah: " +
                    formatSaldo(jumlah) + ", Saldo saat ini: " + formatSaldo(saldo));
            return false;
        }

        saldo -= jumlah;
        UserData.setSaldo(saldo); // Sinkron dengan UserData
        updateDatabaseSaldo();

        System.out.println("✓ SaldoManager: Saldo dikurangi " + formatSaldo(jumlah) +
                ", sisa: " + formatSaldo(saldo));
        return true;
    }

    public static void tambahSaldo(long jumlah) {
        if (jumlah > 0) {
            saldo += jumlah;
            UserData.setSaldo(saldo); // Sinkron dengan UserData
            updateDatabaseSaldo();

            System.out.println("✓ SaldoManager: Saldo ditambah " + formatSaldo(jumlah) +
                    ", total: " + formatSaldo(saldo));
        }
    }

    public static String formatSaldo() {
        return formatSaldo(saldo);
    }

    public static String formatSaldo(long amount) {
        return "Rp" + String.format("%,d", amount);
    }

    private static void updateDatabaseSaldo() {
        try {
            String rekening = UserData.getNomorRekening();
            if (rekening == null || rekening.isEmpty()) {
                System.out.println("⚠️ SaldoManager: User belum login, skip update DB");
                return;
            }

            User currentUser = UserDatabase.getUserByRekening(rekening);
            if (currentUser != null) {
                currentUser.setSaldo(saldo);      
                UserDatabase.updateUser(currentUser);  
                System.out.println("✓ SaldoManager: Database berhasil diupdate");
            } else {
                System.err.println("✗ SaldoManager: User tidak ditemukan di database");
            }

        } catch (Exception e) {
            System.err.println("❌ SaldoManager: Error saat update database");
            e.printStackTrace();
        }
    }
    
    public static void refreshFromDatabase() {
        try {
            String rekening = UserData.getNomorRekening();
            if (rekening != null && !rekening.isEmpty()) {
                User user = UserDatabase.getUserByRekening(rekening);
                if (user != null) {
                    saldo = user.getSaldo();
                    UserData.setSaldo(saldo);
                    System.out.println("✓ SaldoManager: Saldo direfresh dari database: " + formatSaldo(saldo));
                }
            }
        } catch (Exception e) {
            System.err.println("Error refreshing saldo from database: " + e.getMessage());
        }
    }
}