package com.app;

import java.util.ArrayList;
import java.util.List;

// Interface untuk rule password
interface PasswordRule {
    boolean validate(String password);
}

// Class PasswordValidator
public class PasswordValidator {

    // List aturan password
    public List<PasswordRule> rules = new ArrayList<>();

    public PasswordValidator() {
        // Aturan 1: minimal 8 karakter
        rules.add(new PasswordRule() {
            @Override
            public boolean validate(String password) {
                return password.length() >= 8;
            }
        });

        // Aturan 2: harus ada huruf besar
        rules.add(new PasswordRule() {
            @Override
            public boolean validate(String password) {
                return password.matches(".*[A-Z].*");
            }
        });

        // Aturan 3: harus ada huruf kecil
        rules.add(new PasswordRule() {
            @Override
            public boolean validate(String password) {
                return password.matches(".*[a-z].*");
            }
        });

        // Aturan 4: harus ada angka
        rules.add(new PasswordRule() {
            @Override
            public boolean validate(String password) {
                return password.matches(".*[0-9].*");
            }
        });
    }
}
