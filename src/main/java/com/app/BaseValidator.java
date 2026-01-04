package com.app;

import java.util.ArrayList;
import java.util.List;

interface PasswordRule {
    boolean validate(String text);
    String getDescription();
}

public abstract class BaseValidator {

    protected List<PasswordRule> rules = new ArrayList<>();

    protected interface Validation {
        boolean check(String text);
    }

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
