package com.app;

public class User {
    private String smartId;
    private String email;
    private String password;
    
    public User(String smartId, String email, String password) {
        this.smartId = smartId;
        this.email = email;
        this.password = password;
    }

    public String getSmartId() {
        return smartId;
    }

    public void setSmartId(String smartId) {
        this.smartId = smartId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
