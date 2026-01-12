package org.productsstore.products.Dtos;

import lombok.Builder;
import lombok.Data;
import org.productsstore.products.models.ProductBaseModel;

import java.util.Date;

@Data
public class CategoryResponseDTO extends ProductBaseModel {
    private String categoryName;

    @Builder
    public CategoryResponseDTO(Long id, Date createdAt, Date updatedAt, String categoryName) {
        super(id, createdAt, updatedAt);
        this.categoryName = categoryName;
    }
}
