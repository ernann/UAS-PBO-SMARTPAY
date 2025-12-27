package com.app.Reminder;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

public class Reminder implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String jenis;
    private double nominal;
    private LocalDate jatuhTempo;
    private String userId;

    public Reminder(String jenis, double nominal, LocalDate jatuhTempo, String userId) {
        this.id = UUID.randomUUID().toString();
        this.jenis = jenis;
        this.nominal = nominal;
        this.jatuhTempo = jatuhTempo;
        this.userId = userId;
    }

    public String getId() { return id; }
    public String getJenis() { return jenis; }
    public double getNominal() { return nominal; }
    public LocalDate getJatuhTempo() { return jatuhTempo; }
    public String getUserId() { return userId; }

    public void setJenis(String jenis) { this.jenis = jenis; }
    public void setNominal(double nominal) { this.nominal = nominal; }
    public void setJatuhTempo(LocalDate jatuhTempo) { this.jatuhTempo = jatuhTempo; }

    public String getFormattedNominal() {
        return String.format("Rp %,d", (int) nominal);
    }

    public String getStatus() {
        if (LocalDate.now().isAfter(jatuhTempo)) return "TERLAMBAT";
        return "AKTIF";
    }
}
