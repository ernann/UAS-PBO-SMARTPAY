package com.app.Transfer;

public class RiwayatItem {

    private String nama;
    private String bank;
    private String noRek;

    public RiwayatItem(String nama, String bank, String noRek) {
        this.nama = nama;
        this.bank = bank;
        this.noRek = noRek;
    }

    // ✅ GETTER (INI YANG KURANG)
    public String getNama() {
        return nama;
    }

    public String getBank() {
        return bank;
    }

    public String getNoRek() {
        return noRek;
    }

    @Override
    public String toString() {
        return nama + " - " + bank + " (" + noRek + ")";
    }
}
