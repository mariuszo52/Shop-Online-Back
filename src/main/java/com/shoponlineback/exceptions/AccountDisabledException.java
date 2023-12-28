package com.shoponlineback.exceptions;

public class AccountDisabledException extends RuntimeException{
    private final static String MESSAGE = "Account is not activ yet.";
    public AccountDisabledException() {
        super(MESSAGE);
    }

    public AccountDisabledException(String message) {
        super(message);
    }
}
