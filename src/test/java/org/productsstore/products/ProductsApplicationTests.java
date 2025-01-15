package org.productsstore.products;

import org.junit.jupiter.api.Test;
import org.productsstore.products.models.Category;
import org.productsstore.products.models.Product;
import org.productsstore.products.projections.ProductWithIdAndTitle;
import org.productsstore.products.repositories.CategoryRepository;
import org.productsstore.products.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.List;
import java.util.Optional;


@SpringBootTest
class ProductsApplicationTests {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void testDBQueries() {
        List<ProductWithIdAndTitle> productWithIdAndTitles =
                productRepository.randomSearchMethod2(1L);

        for (ProductWithIdAndTitle product : productWithIdAndTitles) {
            System.out.println(product.getId() + " " + product.getTitle());
        }

        Optional<Product> product = productRepository.findById(1L);

        Optional<Category> optionalCategory = categoryRepository.findById(1L);

        Category category = optionalCategory.get();

        System.out.println("Getting Products");

        List<Product> products = category.getProducts();

        System.out.println("DEBUG");
    }

}
