package org.productsstore.products.Dtos;


import lombok.Builder;
import lombok.Data;
import org.productsstore.products.models.ProductBaseModel;
import org.productsstore.products.models.Category;

import java.io.Serializable;
import java.util.Date;

@Data
public class ProductResponseDTO extends ProductBaseModel implements Serializable {
    private String title;
    private String description;
    private double price;
    private String imageURL;
    private Category category;


    @Builder
    public ProductResponseDTO(Long id, Date createdAt, Date updatedAt, String title, String description, double price, Category category) {
        super(id, createdAt, updatedAt);
        this.title = title;
        this.description = description;
        this.price = price;
        this.category = category;
    }
}
