package org.productsstore.products.repositories;

import org.productsstore.products.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAll();
    Category findByName(String name);

    @Override
    Optional<Category> findById(Long id);

    Category save(Category category);

}
