// [file name]: Reminder.java
package com.app.Reminder;

import java.io.Serializable;
import java.time.LocalDate;

public class Reminder implements Serializable {
    private static final long serialVersionUID = 1L;

    private String jenis;
    private double nominal;
    private LocalDate jatuhTempo;
    private String userId; // Menyimpan ID user yang membuat reminder
    private boolean isPaid; // Status pembayaran
    private LocalDate paidDate; // Tanggal dibayar (untuk riwayat)

    public Reminder(String jenis, double nominal, LocalDate jatuhTempo, String userId) {
        this.jenis = jenis;
        this.nominal = nominal;
        this.jatuhTempo = jatuhTempo;
        this.userId = userId;
        this.isPaid = false;
        this.paidDate = null;
    }

    // Constructor untuk riwayat
    public Reminder(String jenis, double nominal, LocalDate jatuhTempo, 
                   String userId, boolean isPaid, LocalDate paidDate) {
        this.jenis = jenis;
        this.nominal = nominal;
        this.jatuhTempo = jatuhTempo;
        this.userId = userId;
        this.isPaid = isPaid;
        this.paidDate = paidDate;
    }

    // Getters and Setters
    public String getJenis() { return jenis; }
    public double getNominal() { return nominal; }
    public LocalDate getJatuhTempo() { return jatuhTempo; }
    public String getUserId() { return userId; }
    public boolean isPaid() { return isPaid; }
    public LocalDate getPaidDate() { return paidDate; }

    public void setPaid(boolean paid) { this.isPaid = paid; }
    public void setPaidDate(LocalDate paidDate) { this.paidDate = paidDate; }

    public String getFormattedNominal() {
        return String.format("Rp %,d", (int) nominal);
    }

    public String getStatus() {
        if (isPaid) {
            return "LUNAS (" + paidDate + ")";
        } else if (LocalDate.now().isAfter(jatuhTempo)) {
            return "TERLAMBAT";
        } else {
            return "AKTIF";
        }
    }

    @Override
    public String toString() {
        if (isPaid) {
            return jenis + " | " + getFormattedNominal() + " | LUNAS (" + paidDate + ")";
        } else {
            return jenis + " | " + getFormattedNominal() + " | Due: " + jatuhTempo + " [" + getStatus() + "]";
        }
    }
}