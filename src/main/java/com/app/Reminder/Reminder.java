package com.app.Reminder;
import java.io.Serializable;
import java.time.LocalDate;

public class Reminder implements Serializable {
    private static final long serialVersionUID = 1L;

    private String jenis;
    private double nominal;
    private LocalDate jatuhTempo;

    public Reminder(String jenis, double nominal, LocalDate jatuhTempo) {
        this.jenis = jenis;
        this.nominal = nominal;
        this.jatuhTempo = jatuhTempo;
    }

    public String getJenis() { return jenis; }
    public double getNominal() { return nominal; }
    public LocalDate getJatuhTempo() { return jatuhTempo; }

    @Override
    public String toString() {
        return jenis + " | Rp" + nominal + " | Due: " + jatuhTempo;
    }
}
