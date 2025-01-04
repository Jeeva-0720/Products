package org.productsstore.products.controllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import org.productsstore.products.Dtos.ExceptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ExceptionDto> handleNullPointerException(NullPointerException e) {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setError(e.getMessage());
        exceptionDto.setSolution("Search with correct valid id");
        exceptionDto.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(exceptionDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ArithmeticException.class)
    public ResponseEntity<String> handleArithmeticException() {
        return new ResponseEntity("Arithmatic Exception happened", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException() {
        return new ResponseEntity("Runtime Exception happened", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
