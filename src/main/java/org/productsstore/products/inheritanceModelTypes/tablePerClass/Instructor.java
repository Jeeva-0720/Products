package org.productsstore.products.inheritanceModelTypes.tablePerClass;


import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "tpc_instructors")
public class Instructor extends User {
    private String performance;
}
