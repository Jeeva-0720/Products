package org.productsstore.products.inheritanceModelTypes.singleTable;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "st_users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn( name = "user_type", discriminatorType = DiscriminatorType.INTEGER )
@DiscriminatorValue("0")
public class User {
    @Id
    private long id;
    private String name;
    private String email;
}
