package com.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransaksiStorage {
    private static final String FILE_NAME = "transaksi_data.dat";
    private static List<TransaksiRecord> semuaTransaksi = new ArrayList<>();

    static {
        loadTransaksi();
    }

    public static class TransaksiRecord implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private String jenis;
        private String deskripsi;
        private String tipe;
        private long jumlah;
        private LocalDateTime waktu;
        private String kodeTransaksi;
        private String userId; 
        
        public TransaksiRecord(String jenis, String deskripsi, String tipe, long jumlah, 
                              LocalDateTime waktu, String kodeTransaksi, String userId) {
            this.jenis = jenis;
            this.deskripsi = deskripsi;
            this.tipe = tipe;
            this.jumlah = jumlah;
            this.waktu = waktu;
            this.kodeTransaksi = kodeTransaksi;
            this.userId = userId;
        }
        
        public String getJenis() { return jenis; }
        public String getDeskripsi() { return deskripsi; }
        public String getTipe() { return tipe; }
        public long getJumlah() { return jumlah; }
        public LocalDateTime getWaktu() { return waktu; }
        public String getKodeTransaksi() { return kodeTransaksi; }
        public String getUserId() { return userId; }
        
        public String getJumlahFormatted() {
            String tanda = tipe.equals("PENGELUARAN") ? "-" : "+";
            return tanda + " Rp" + String.format("%,d", jumlah);
        }
        
        public String getDisplayJenis() {
            return tipe.equals("PENGELUARAN") ? "💸 " + tipe : "💰 " + tipe;
        }
        
        @Override
        public String toString() {
            return String.format("%s - %s: %s", 
                waktu.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                deskripsi, getJumlahFormatted());
        }
    }

    
    public static void tambahTransaksi(String jenis, String deskripsi, String tipe, 
                                      long jumlah, String kodeTransaksi, String userId) {
        TransaksiRecord record = new TransaksiRecord(
            jenis, deskripsi, tipe, jumlah, 
            LocalDateTime.now(), kodeTransaksi, userId
        );
        semuaTransaksi.add(record);
        saveTransaksi();
        System.out.println("✓ Transaksi ditambahkan: " + deskripsi + " - " + record.getJumlahFormatted());
    }
    
    public static List<TransaksiRecord> getTransaksiByUser(String userId) {
        List<TransaksiRecord> userTransaksi = new ArrayList<>();
        for (TransaksiRecord t : semuaTransaksi) {
            if (t.getUserId().equals(userId)) {
                userTransaksi.add(t);
            }
        }
        return userTransaksi;
    }
    
    public static List<TransaksiRecord> getSemuaTransaksi() {
        return new ArrayList<>(semuaTransaksi);
    }
    
    public static void clearDummyData() {
        semuaTransaksi.clear();
        saveTransaksi();
        System.out.println("✓ Data dummy dihapus");
    }
    
    
    @SuppressWarnings("unchecked")
    private static void loadTransaksi() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("File transaksi tidak ditemukan, buat baru");
            semuaTransaksi = new ArrayList<>();
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            semuaTransaksi = (List<TransaksiRecord>) ois.readObject();
            System.out.println("✓ Loaded " + semuaTransaksi.size() + " transaksi dari file");
        } catch (Exception e) {
            System.err.println("✗ Error loading transaksi: " + e.getMessage());
            semuaTransaksi = new ArrayList<>();
        }
    }
    
    private static void saveTransaksi() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(semuaTransaksi);
        } catch (Exception e) {
            System.err.println("✗ Error saving transaksi: " + e.getMessage());
        }
    }
}