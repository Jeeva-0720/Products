package org.productsstore.products.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.productsstore.products.Dtos.ProductResponseDTO;

import java.util.Date;

@Getter
@Setter
@Entity
public class Product extends BaseModel {
    private String title;
    private String description;
    private Double price;
    @ManyToOne
    private Category category;

    @Builder
    public Product(Long id, Date createdAt, Date updatedAt, String createdByUserId, String title, String description, double price, String imageURL, Category category) {
        super(id, createdAt, updatedAt);
        this.title = title;
        this.description = description;
        this.price = price;
        this.category = category;
    }

    /**
     * Instantiates a new Product.
     */
    public Product() {
        super();
    }

    public ProductResponseDTO toProductResponseDTO() {
        return ProductResponseDTO.builder()
                .id(getId())
                .category(category)
                .description(description)
                .price(price)
                .title(title)
                .createdAt(getCreatedAt())
                .updatedAt(getUpdatedAt())
                .build();
    }
}
