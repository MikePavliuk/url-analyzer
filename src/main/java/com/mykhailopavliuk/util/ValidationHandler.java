package com.mykhailopavliuk.util;

public class ValidationHandler {

    public static class Validation {
        private String regex;
        private String message;

        public Validation(String regex, String message) {
            this.regex = regex;
            this.message = message;
        }

        public String getRegex() {
            return regex;
        }

        public void setRegex(String regex) {
            this.regex = regex;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static Validation getEmailRegex() {
        return new Validation(
                "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$",
                "Email is not valid");
    }

    public static Validation getPasswordValidation() {
        return new Validation(
                "^(?=.*[0-9]).{8,}$",
                "Must have at least 1 digit and min length of 8");
    }
}
