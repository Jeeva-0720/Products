package org.productsstore.products.Exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductNotFoundException extends Exception{
    public String message;
    public ProductNotFoundException(String message) {
        this.message = message;
    }
}
