package com.shoponlineback.exceptions.userRole;

public class UserRoleNotFoundException extends RuntimeException{
    private final static String MESSAGE = "User role not found.";

    public UserRoleNotFoundException() {
        super(MESSAGE);
    }
    public UserRoleNotFoundException(String message) {
        super(message);
    }
}
