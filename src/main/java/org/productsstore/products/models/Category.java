package org.productsstore.products.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
public class Category extends ProductBaseModel {
    private String name;
    private String description;
    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<Product> products;

    @Builder
    public Category(Long id, Date createdAt, Date updatedAt, String name, List<Product> products) {
        super(id, createdAt, updatedAt);
        this.name = name;
        this.products = products;
    }

    /**
     * Instantiates a new Category.
     */
    public Category() {
        super();
    }
}
