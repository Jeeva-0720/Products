package org.productsstore.products.services;

import org.productsstore.products.Exceptions.ProductNotFoundException;
import org.productsstore.products.models.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    public Product getSingleProduct(Long id);
    public Page<Product> getAllProducts(int pageNumber, int pageSize);
    public Product updateSingleProduct(Long id, Product product) throws ProductNotFoundException;
    public String deleteSingleProduct(Long id);
    public Product replaceProduct(Long id, Product product)  throws ProductNotFoundException;
    public Product addProduct(Product product);
}
