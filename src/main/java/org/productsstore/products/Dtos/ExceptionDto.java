package org.productsstore.products.Dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ExceptionDto {
    private String error;
    private String solution;
    private LocalDateTime timestamp;
}
