package org.productsstore.products.controllers;

import org.productsstore.products.Exceptions.PasswordMismatchException;
import org.productsstore.products.Exceptions.UserAlreadySignedException;
import org.productsstore.products.Exceptions.UserNotRegisteredException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler({PasswordMismatchException.class, UserNotRegisteredException.class, UserAlreadySignedException.class})
    public ResponseEntity<String> handleExceptions(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
