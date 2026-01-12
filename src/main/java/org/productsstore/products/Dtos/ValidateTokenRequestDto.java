package org.productsstore.products.Dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ValidateTokenRequestDto {
    private Long userId;
    private String token;
}
