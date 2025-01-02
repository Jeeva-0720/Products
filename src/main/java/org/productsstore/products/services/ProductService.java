package org.productsstore.products.services;

import org.productsstore.products.models.Product;

import java.util.List;

public interface ProductService {
    public Product getSingleProduct(Long id);
    public List<Product> getAllProducts();
}
