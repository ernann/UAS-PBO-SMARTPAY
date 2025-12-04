package com.app;

import java.util.ArrayList;
import java.util.List;

// Interface (polymorphism)
interface PasswordRule {
    boolean validate(String text);
    String getDescription();
}

// Abstract class
public abstract class BaseValidator {

    protected List<PasswordRule> rules = new ArrayList<>();

    // --- FIX: interface dipindah ke sini, bukan di dalam Rule ---
    protected interface Validation {
        boolean check(String text);
    }

    // Nested Class
    protected class Rule implements PasswordRule {

        private final String description;
        private final Validation validation;

        public Rule(String description, Validation validation) {
            this.description = description;
            this.validation = validation;
        }

        @Override
        public boolean validate(String text) {
            return validation.check(text);
        }

        @Override
        public String getDescription() {
            return description;
        }
    }

    public abstract void setupRules();
}
