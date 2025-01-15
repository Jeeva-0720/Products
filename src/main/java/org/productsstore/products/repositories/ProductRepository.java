package org.productsstore.products.repositories;

import org.productsstore.products.models.Category;
import org.productsstore.products.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.productsstore.products.projections.ProductWithIdAndTitle;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> getProductById(Long id);

    @Override
    Page<Product> findAll(Pageable pageable);

    List<Product> getProductsByCategory(Category category);

    void deleteProductById(Long id);

    Product save(Product product);

    List<Product> findByPriceIsGreaterThan(Double price);

    List<Product> findProductByTitleLike(String word);

    List<Product> findByTitleLikeIgnoreCase(String word);

    List<Product> findTop5ByTitleContains(String word);

    List<Product> findProductsByTitleContainsAndPriceGreaterThan(String word, Double price );

    //HQL
    @Query("select p.id as id, p.title as title from Product p where p.id = :x")
    List<ProductWithIdAndTitle> randomSearchMethod(Long x);

    //SQL
    @Query(value = "select p.id as id, p.title as title from product p where p.id = :productId", nativeQuery = true)
    List<ProductWithIdAndTitle> randomSearchMethod2(Long productId);

}
