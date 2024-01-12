package com.shoponlineback.exceptions.order;

public class OrderNotFoundException extends RuntimeException{
    private final static String MESSAGE = "Order not found.";
    public OrderNotFoundException() {
        super(MESSAGE);
    }

    public OrderNotFoundException(String message) {
        super(message);
    }
}
