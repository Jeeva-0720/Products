package org.productsstore.products.Dtos;

import lombok.Builder;
import lombok.Data;
import org.productsstore.products.models.Product;

@Data
@Builder
public class CreateProductRequestDTO {
    private String title;
    private String description;
    private Double price;
    private String category;
    private String image;


    public Product toProduct() {
        return Product.builder()
                .title(title)
                .price(price)
                .description(description)
                .build();
    }
}