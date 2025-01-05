package org.productsstore.products.inheritanceModelTypes.singleTable;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "st_instructors")
@DiscriminatorValue("2")
public class Instructor extends User {
    private String performance;
}
