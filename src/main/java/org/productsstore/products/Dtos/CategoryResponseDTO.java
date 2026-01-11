package org.productsstore.products.Dtos;

import lombok.Builder;
import lombok.Data;
import org.productsstore.products.models.BaseModel;

import java.util.Date;

@Data
public class CategoryResponseDTO extends BaseModel {
    private String categoryName;

    @Builder
    public CategoryResponseDTO(Long id, Date createdAt, Date updatedAt, String categoryName) {
        super(id, createdAt, updatedAt);
        this.categoryName = categoryName;
    }
}
