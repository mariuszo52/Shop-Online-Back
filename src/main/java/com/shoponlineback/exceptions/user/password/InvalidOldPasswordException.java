package com.shoponlineback.exceptions.user.password;

public class InvalidOldPasswordException extends RuntimeException{
private static final String MESSAGE = "Incorrect old password. Please check the correctness and try again.";
    public InvalidOldPasswordException() {
        super(MESSAGE);
    }

    public InvalidOldPasswordException(String message) {
        super(message);
    }
}
