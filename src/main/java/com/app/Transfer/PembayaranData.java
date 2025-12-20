package com.app.Transfer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PembayaranData {
   public static final int LIMIT_TRANSFER = 25_000_000; 
   public static final int MIN_TRANSFER = 20_000;  
    private static String nama;
    private static String bank;
    private static String noRek;
    private static int nominal;

    private static ObservableList<String> riwayat =
            FXCollections.observableArrayList();    
    
    public static void setNama(String nama) {
        PembayaranData.nama = nama;
    }

    public static void setBank(String bank) {
        PembayaranData.bank = bank;
    }

    public static void setNoRek(String noRek) {
        PembayaranData.noRek = noRek;
    }

    public static void setNominal(int nominal) {
        PembayaranData.nominal = nominal;
    }

    public static String getNama() {
        return nama;
    }

    public static String getBank() {
        return bank;
    }

    public static String getNoRek() {
        return noRek;
    }

    public static int getNominal() {
        return nominal;
    }

    public static int getAdmin() {
        if (bank == null) return 0;

        if (bank.equals("SmartPay")) {
            return 0;
        } else if (bank.equals("BNI")) {
            return 2500;
        } else if (bank.equals("BCA")) {
            return 3000;
        } else if (bank.equals("BSI")) {
            return 1500;
        }

        return 0;
    }

    public static int getTotal() {
        return nominal + getAdmin();
    }

    public static int getKodeTujuan() {
        if (bank == null) return 0;

        if (bank.equals("SmartPay")) {
            return 10;
        } else if (bank.equals("BNI")) {
            return 11;
        } else if (bank.equals("BCA")) {
            return 12;
        } else if (bank.equals("BSI")) {
            return 13;
        }

        return 0;
    }

    public static String getKodeAwalRek() {
        if (bank == null) return "";

        if (bank.equals("SmartPay")) {
            return "006";
        } else if (bank.equals("BCA")) {
            return "007";
        } else if (bank.equals("BNI")) {
            return "008";
        } else if (bank.equals("BSI")) {
            return "009";
        }

        return "";
    }

    public static void clear() {
        nama = bank = noRek = null;
        nominal = 0;
    }
}
