package org.productsstore.products.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Session extends UserBaseModel {
    private String token;

    @ManyToOne
    private User user;
}
