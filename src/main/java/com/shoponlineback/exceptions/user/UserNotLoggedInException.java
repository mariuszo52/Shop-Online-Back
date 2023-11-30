package com.shoponlineback.exceptions.user;

public class UserNotLoggedInException extends RuntimeException{
    private static final String MESSAGE = "User is not logged in. Please log in and try again.";
    public UserNotLoggedInException() {
        super(MESSAGE);
    }

    public UserNotLoggedInException(String message) {
        super(message);
    }
}
