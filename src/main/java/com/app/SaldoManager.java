package com.app;

public class SaldoManager {
    private static long saldo = 0;

    // Ambil saldo saat ini
    public static long getSaldo() {
        return saldo;
    }

    // Set saldo secara langsung
    public static void setSaldo(long newSaldo) {
        saldo = newSaldo;
        System.out.println("✓ SaldoManager: Saldo diupdate menjadi " + formatSaldo(newSaldo));
        updateDatabaseSaldo();
    }

    // Kurangi saldo
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

    // Tambah saldo
    public static void tambahSaldo(long jumlah) {
        if (jumlah > 0) {
            saldo += jumlah;
            updateDatabaseSaldo();

            System.out.println("✓ SaldoManager: Saldo ditambah " + formatSaldo(jumlah) +
                    ", total: " + formatSaldo(saldo));
        }
    }

    // Format saldo
    public static String formatSaldo() {
        return "Rp" + String.format("%,d", saldo);
    }

    private static String formatSaldo(long amount) {
        return "Rp" + String.format("%,d", amount);
    }

    // Update database sesuai UserData (AMAN)
    private static void updateDatabaseSaldo() {
        try {
            String rekening = UserData.getNomorRekening();
            if (rekening == null) {
                System.out.println("⚠️ SaldoManager: User belum login, skip update DB");
                return;
            }

            User currentUser = UserDatabase.getUserByRekening(rekening);
            if (currentUser != null) {
                currentUser.setSaldo(saldo);      
                UserDatabase.updateUser(currentUser);  
                System.out.println("✓ SaldoManager: Database berhasil diupdate");
            } else {
                System.err.println("✗ SaldoManager: User tidak ditemukan di database, skip update");
            }

        } catch (Exception e) {
            System.err.println("❌ SaldoManager: Error saat update database");
            e.printStackTrace();
        }
    }
}
