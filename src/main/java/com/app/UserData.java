package com.app;
public class UserData {
    private static String nama = "User";
    private static String smartId = "";
    private static String email = "";
    private static String nomorRekening = "";
    private static String pin = "123456";
    private static long saldo = 0;

    private static boolean reminderNotifShown = false;

    public static String getNama() { 
        return nama; 
    }

    public static void setNama(String nama) { 
        UserData.nama = nama; 
        System.out.println("✓ UserData: Nama diupdate menjadi " + nama);
    }

    public static String getSmartId() { 
        return smartId; 
    }

    public static void setSmartId(String smartId) { 
        UserData.smartId = smartId;
        reminderNotifShown = false; 
        System.out.println("✓ UserData: Smart ID diupdate menjadi " + smartId);
    }

    public static boolean isReminderNotifShown() {
        return reminderNotifShown;
    }

    public static void setReminderNotifShown(boolean value) {
        reminderNotifShown = value;
    }
    
    public static String getEmail() { 
        return email; 
    }
    
    public static void setEmail(String email) { 
        UserData.email = email; 
        System.out.println("✓ UserData: Email diupdate menjadi " + email);
    }
    
    public static String getNomorRekening() { 
        return nomorRekening; 
    }
    
    public static void setNomorRekening(String nomorRekening) { 
        UserData.nomorRekening = nomorRekening; 
        System.out.println("✓ UserData: Nomor Rekening diupdate menjadi " + nomorRekening);
    }
    
    public static String getPin() { 
        return pin; 
    }
    
    public static void setPin(String pin) { 
        UserData.pin = pin; 
        System.out.println("✓ UserData: PIN diupdate");
    }
    
    public static long getSaldo() { 
        return saldo; 
    }
    
    public static void setSaldo(long saldo) { 
        UserData.saldo = saldo; 
        System.out.println("✓ UserData: Saldo diupdate menjadi " + formatSaldo(saldo));
    }
    
    public static String getSaldoFormatted() {
        return formatSaldo(saldo);
    }
    
    private static String formatSaldo(long amount) {
        return "Rp" + String.format("%,d", amount);
    }
    
    public static boolean validatePin(String inputPin) {
        boolean isValid = pin.equals(inputPin);
        System.out.println("Validasi PIN: input=" + inputPin + ", stored=" + pin + ", result=" + isValid);
        return isValid;
    }
    
}