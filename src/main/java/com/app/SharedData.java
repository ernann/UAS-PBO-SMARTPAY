package com.app;

public class SharedData {
    // Simulasi "database" sementara untuk menyimpan user
    private static User registeredUser;

    public static void setRegisteredUser(User user) {
        registeredUser = user;
    }

    public static User getRegisteredUser() {
        return registeredUser;
    }
}
