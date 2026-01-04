package com.app;

import java.io.File;
import java.util.Scanner;

public class DataCleaner {
    
    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("       CLEANER DATA SMARTPAY");
        System.out.println("=========================================");
        
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\nPilihan:");
        System.out.println("1. Hapus data dummy transaksi");
        System.out.println("2. Hapus file transaksi.dat");
        System.out.println("3. Lihat statistik data");
        System.out.println("4. Keluar");
        System.out.print("\nPilih (1-4): ");
        
        String pilihan = scanner.nextLine();
        
        switch (pilihan) {
            case "1":
                hapusDataDummy();
                break;
            case "2":
                hapusFileTransaksi();
                break;
            case "3":
                lihatStatistik();
                break;
            case "4":
                System.out.println("Keluar...");
                break;
            default:
                System.out.println("Pilihan tidak valid!");
        }
        
        scanner.close();
    }
    
    private static void hapusDataDummy() {
        System.out.println("\n=== MENGHAPUS DATA DUMMY ===");
        
        java.util.List<TransaksiStorage.TransaksiRecord> semuaData = 
            TransaksiStorage.getSemuaTransaksi();
        
        System.out.println("Total data sebelum dihapus: " + semuaData.size());
        
        if (!semuaData.isEmpty()) {
            System.out.println("\nContoh data yang akan dihapus:");
            for (int i = 0; i < Math.min(5, semuaData.size()); i++) {
                TransaksiStorage.TransaksiRecord t = semuaData.get(i);
                System.out.println((i+1) + ". " + t.getDeskripsi() + " - " + 
                                 t.getJumlahFormatted() + " (" + 
                                 t.getWaktu().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ")");
            }
        }
        
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nYakin ingin menghapus SEMUA data? (ya/tidak): ");
        String konfirmasi = scanner.nextLine();
        
        if (konfirmasi.equalsIgnoreCase("ya")) {
            TransaksiStorage.clearDummyData();
            System.out.println("✓ SEMUA DATA TRANSAKSI TELAH DIHAPUS!");
            System.out.println("  Sekarang riwayat akan kosong sampai ada transaksi real.");
        } else {
            System.out.println("Penghapusan dibatalkan.");
        }
    }
    
    private static void hapusFileTransaksi() {
        System.out.println("\n=== MENGHAPUS FILE TRANSAKSI ===");
        
        File file = new File("transaksi_data.dat");
        if (file.exists()) {
            System.out.println("File ditemukan: " + file.getAbsolutePath());
            System.out.println("Ukuran file: " + file.length() + " bytes");
            
            Scanner scanner = new Scanner(System.in);
            System.out.print("Yakin ingin menghapus file? (ya/tidak): ");
            String konfirmasi = scanner.nextLine();
            
            if (konfirmasi.equalsIgnoreCase("ya")) {
                if (file.delete()) {
                    System.out.println("✓ File transaksi_data.dat berhasil dihapus!");
                } else {
                    System.out.println("✗ Gagal menghapus file!");
                }
            } else {
                System.out.println("Penghapusan dibatalkan.");
            }
        } else {
            System.out.println("File transaksi_data.dat tidak ditemukan.");
        }
    }
    
    private static void lihatStatistik() {
        System.out.println("\n=== STATISTIK DATA ===");
        
        java.util.List<TransaksiStorage.TransaksiRecord> semuaData = 
            TransaksiStorage.getSemuaTransaksi();
        
        System.out.println("Total transaksi: " + semuaData.size());
        
        if (semuaData.isEmpty()) {
            System.out.println("Tidak ada data transaksi.");
            return;
        }
        
        java.util.Map<String, Integer> transaksiPerUser = new java.util.HashMap<>();
        java.util.Map<String, Long> saldoPerUser = new java.util.HashMap<>();
        
        for (TransaksiStorage.TransaksiRecord t : semuaData) {
            String userId = t.getUserId();
            
            transaksiPerUser.put(userId, transaksiPerUser.getOrDefault(userId, 0) + 1);
            
            long jumlah = t.getJumlah();
            if (t.getTipe().equals("PEMASUKAN")) {
                saldoPerUser.put(userId, saldoPerUser.getOrDefault(userId, 0L) + jumlah);
            } else {
                saldoPerUser.put(userId, saldoPerUser.getOrDefault(userId, 0L) - jumlah);
            }
        }
        
        System.out.println("\nDistribusi per User:");
        for (java.util.Map.Entry<String, Integer> entry : transaksiPerUser.entrySet()) {
            String userId = entry.getKey();
            int count = entry.getValue();
            long saldo = saldoPerUser.getOrDefault(userId, 0L);
            
            System.out.println("  User " + userId + ": " + count + " transaksi, Saldo: " + 
                             (saldo >= 0 ? "+" : "") + SaldoManager.formatSaldo(saldo));
        }
        
        long totalPemasukan = 0;
        long totalPengeluaran = 0;
        int countTopup = 0;
        int countTransfer = 0;
        
        for (TransaksiStorage.TransaksiRecord t : semuaData) {
            if (t.getTipe().equals("PEMASUKAN")) {
                totalPemasukan += t.getJumlah();
            } else {
                totalPengeluaran += t.getJumlah();
            }
            
            if (t.getJenis().equals("TOPUP")) {
                countTopup++;
            } else if (t.getJenis().equals("TRANSFER")) {
                countTransfer++;
            }
        }
        
        System.out.println("\nStatistik Umum:");
        System.out.println("  Total Pemasukan: " + SaldoManager.formatSaldo(totalPemasukan));
        System.out.println("  Total Pengeluaran: " + SaldoManager.formatSaldo(totalPengeluaran));
        System.out.println("  Saldo Bersih: " + SaldoManager.formatSaldo(totalPemasukan - totalPengeluaran));
        System.out.println("  Jumlah TOP UP: " + countTopup);
        System.out.println("  Jumlah TRANSFER: " + countTransfer);
        
        if (!semuaData.isEmpty()) {
            java.util.Collections.sort(semuaData, (t1, t2) -> 
                t1.getWaktu().compareTo(t2.getWaktu()));
            
            TransaksiStorage.TransaksiRecord pertama = semuaData.get(0);
            TransaksiStorage.TransaksiRecord terakhir = semuaData.get(semuaData.size() - 1);
            
            System.out.println("\nRentang Waktu:");
            System.out.println("  Transaksi pertama: " + 
                pertama.getWaktu().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            System.out.println("  Transaksi terakhir: " + 
                terakhir.getWaktu().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        }
    }
}