package com.app.Topup;

public class TempData {
    private static String metodeTopup = "Alfamart";
    private static long nominalTopup = 0;
    private static String kodeTransaksi = "";
    private static String judulTopup = "Top Up";
    private static String infoMetode = "Pembayaran";
    private static String detailInfo = "Masukkan nominal top up";
    private static String iconMetode = "🏪";

    // Getters and Setters dengan default value
    public static String getMetodeTopup() {
        return metodeTopup != null ? metodeTopup : "Alfamart";
    }

    public static void setMetodeTopup(String metodeTopup) {
        if (metodeTopup != null) {
            TempData.metodeTopup = metodeTopup;
        }
    }

    public static long getNominalTopup() {
        return nominalTopup;
    }

    public static void setNominalTopup(long nominalTopup) {
        TempData.nominalTopup = nominalTopup;
    }

    public static String getKodeTransaksi() {
        return kodeTransaksi != null ? kodeTransaksi : "";
    }

    public static void setKodeTransaksi(String kodeTransaksi) {
        if (kodeTransaksi != null) {
            TempData.kodeTransaksi = kodeTransaksi;
        }
    }

    public static String getJudulTopup() {
        return judulTopup != null ? judulTopup : "Top Up";
    }

    public static void setJudulTopup(String judulTopup) {
        if (judulTopup != null) {
            TempData.judulTopup = judulTopup;
        }
    }

    public static String getInfoMetode() {
        return infoMetode != null ? infoMetode : "Pembayaran";
    }

    public static void setInfoMetode(String infoMetode) {
        if (infoMetode != null) {
            TempData.infoMetode = infoMetode;
        }
    }

    public static String getDetailInfo() {
        return detailInfo != null ? detailInfo : "Masukkan nominal top up";
    }

    public static void setDetailInfo(String detailInfo) {
        if (detailInfo != null) {
            TempData.detailInfo = detailInfo;
        }
    }

    public static String getIconMetode() {
        return iconMetode != null ? iconMetode : "🏪";
    }

    public static void setIconMetode(String iconMetode) {
        if (iconMetode != null) {
            TempData.iconMetode = iconMetode;
        }
    }
    
    public static void clear() {
        metodeTopup = "Alfamart";
        nominalTopup = 0;
        kodeTransaksi = "";
        judulTopup = "Top Up";
        infoMetode = "Pembayaran";
        detailInfo = "Masukkan nominal top up";
        iconMetode = "🏪";
    }
    
    // Method untuk generate kode transaksi
    public static String generateKodeTransaksi() {
        java.util.Random rand = new java.util.Random();
        return "TRX" + String.format("%06d", rand.nextInt(1000000));
    }
}