package org.productsstore.products.repositories;

import org.productsstore.products.models.Category;
import org.productsstore.products.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> getProductById(Long id);

    List<Product> getProductsByCategory(Category category);

    void deleteProductById(Long id);

    Product save(Product product);

}
