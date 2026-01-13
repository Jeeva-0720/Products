package org.productsstore.products.controllers;

import org.productsstore.products.Exceptions.PasswordMismatchException;
import org.productsstore.products.Exceptions.UserAlreadySignedException;
import org.productsstore.products.Exceptions.UserNotRegisteredException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
// Global exception handler to intercept and process exceptions thrown by controllers.
// Centralizes error handling logic and ensures consistent HTTP responses.
public class ControllerAdvisor {

    @ExceptionHandler({PasswordMismatchException.class, UserNotRegisteredException.class, UserAlreadySignedException.class})
    // Handles authentication and user-related validation exceptions.
    // Returns HTTP 400 (Bad Request) along with the exception message.
    public ResponseEntity<String> handleExceptions(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
