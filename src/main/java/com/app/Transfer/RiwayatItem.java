package com.app.Transfer;

import java.io.Serializable;

public class RiwayatItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nama;
    private String bank;
    private String noRek;

    public RiwayatItem(String nama, String bank, String noRek) {
        this.nama = nama;
        this.bank = bank;
        this.noRek = noRek;
    }

    public String getNama() { return nama; }
    public String getBank() { return bank; }
    public String getNoRek() { return noRek; }

    @Override
    public String toString() {
        return nama + " - " + bank + " (" + noRek + ")";
    }
}
