package com.shoponlineback.exceptions.user;

public class UserNotFoundException extends RuntimeException{
    private final static String MESSAGE = "User not found.";

    public UserNotFoundException() {
        super(MESSAGE);
    }
    public UserNotFoundException(String message) {
        super(message);
    }
}
