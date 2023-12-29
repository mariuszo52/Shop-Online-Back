package com.shoponlineback.exceptions.product;

public class ProductNotFoundException extends RuntimeException{
    private final static String MESSAGE = "Product not found.";
    public ProductNotFoundException() {
        super(MESSAGE);
    }

    public ProductNotFoundException(String message) {
        super(message);
    }
}
