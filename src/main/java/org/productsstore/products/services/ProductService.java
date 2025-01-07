package org.productsstore.products.services;

import org.productsstore.products.Exceptions.ProductNotFoundException;
import org.productsstore.products.models.Product;

import java.util.List;

public interface ProductService {
    public Product getSingleProduct(Long id);
    public List<Product> getAllProducts();
    public Product updateSingleProduct(Long id, Product product) throws ProductNotFoundException;
    public String deleteSingleProduct(Long id);
    public Product replaceProduct(Long id, Product product)  throws ProductNotFoundException;
    public Product addProduct(Product product);
}
