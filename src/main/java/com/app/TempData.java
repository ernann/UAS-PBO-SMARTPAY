package com.app;

public class TempData {
    private static String kodeStruk;
    private static long nominalTopup;

    public static void setKodeStruk(String kode) { kodeStruk = kode; }
    public static String getKodeStruk() { return kodeStruk; }

    public static void setNominalTopup(long nominal) { nominalTopup = nominal; }
    public static long getNominalTopup() { return nominalTopup; }
}
