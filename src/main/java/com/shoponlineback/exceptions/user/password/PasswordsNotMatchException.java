package com.shoponlineback.exceptions.user.password;

public class PasswordsNotMatchException extends RuntimeException {
    private static final String MESSAGE = "Passwords not match";

    public PasswordsNotMatchException() {
        super(MESSAGE);
    }

    public PasswordsNotMatchException(String message) {
        super(message);
    }
}
