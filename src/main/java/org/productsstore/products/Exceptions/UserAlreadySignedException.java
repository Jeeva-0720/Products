package org.productsstore.products.Exceptions;

public class UserAlreadySignedException extends RuntimeException {
    public UserAlreadySignedException(String message) {
        super(message);
    }
}
