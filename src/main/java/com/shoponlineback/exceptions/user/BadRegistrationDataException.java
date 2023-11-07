package com.shoponlineback.exceptions.user;

public class BadRegistrationDataException extends RuntimeException{
private static final String MESSAGE = "Bad registration data.";
    public BadRegistrationDataException() {
        super(MESSAGE);
    }
    public BadRegistrationDataException(String message) {
        super(message);
    }
}
