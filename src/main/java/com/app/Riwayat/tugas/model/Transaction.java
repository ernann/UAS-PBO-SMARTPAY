package com.app.Riwayat.tugas.model;

public class Transaction {
    private String date;
    private String time;
    private String bank;
    private String name;
    private String account;
    private int amount;
    private String type;
    private String category;
    
    public Transaction() {
        this.date = "";
        this.time = "";
        this.bank = "";
        this.name = "";
        this.account = "";
        this.amount = 0;
        this.type = "";
        this.category = "";
    }
    
    public Transaction(String date, String time, String bank, String name, 
                      String account, int amount, String type, String category) {
        this.date = date;
        this.time = time;
        this.bank = bank;
        this.name = name;
        this.account = account;
        this.amount = amount;
        this.type = type;
        this.category = category;
    }
    
    // Getters
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getBank() { return bank; }
    public String getName() { return name; }
    public String getAccount() { return account; }
    public int getAmount() { return amount; }
    public String getType() { return type; }
    public String getCategory() { return category; }
    
    // Setters
    public void setDate(String date) { this.date = date; }
    public void setTime(String time) { this.time = time; }
    public void setBank(String bank) { this.bank = bank; }
    public void setName(String name) { this.name = name; }
    public void setAccount(String account) { this.account = account; }
    public void setAmount(int amount) { this.amount = amount; }
    public void setType(String type) { this.type = type; }
    public void setCategory(String category) { this.category = category; }
    
    @Override
    public String toString() {
        return String.format("%s - %s: Rp%,d", bank, name, amount);
    }
}