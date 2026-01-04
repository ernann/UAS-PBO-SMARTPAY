package com.app.Registrasi;

public class LoginSession {
    private static String lastSmartId;
    private static String lastEmail;
    private static String lastPassword;
    
    public static void setLastRegisteredUser(String smartId, String email, String password) {
        lastSmartId = smartId;
        lastEmail = email;
        lastPassword = password;
        System.out.println("✓ LoginSession: Data register terakhir disimpan");
        System.out.println("  Smart ID: " + smartId);
        System.out.println("  Email: " + email);
    }
    
    public static String getLastSmartId() { 
        return lastSmartId != null ? lastSmartId : ""; 
    }
    
    public static String getLastEmail() { 
        return lastEmail != null ? lastEmail : ""; 
    }
    
    public static String getLastPassword() { 
        return lastPassword != null ? lastPassword : ""; 
    }
    
    public static boolean hasLastUser() {
        boolean hasUser = lastSmartId != null && !lastSmartId.isEmpty() &&
                         lastEmail != null && !lastEmail.isEmpty();
        System.out.println("LoginSession.hasLastUser() = " + hasUser);
        return hasUser;
    }
    
    public static void clear() {
        lastSmartId = null;
        lastEmail = null;
        lastPassword = null;
        System.out.println("LoginSession cleared");
    }
}