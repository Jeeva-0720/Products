package org.productsstore.products.services;

import org.productsstore.products.Dtos.CreateProductRequestDTO;
import org.productsstore.products.Exceptions.ProductNotFoundException;
import org.productsstore.products.models.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    public Product getSingleProduct(Long id);
    public Page<Product> getAllProducts(int pageNumber, int pageSize);
    List<Product> getProductsInSpecificCategory(String categoryName);
    public Product updateSingleProduct(Long id, Product product) throws ProductNotFoundException;
    public void deleteSingleProduct(Long id);
    public Product replaceProduct(Long id, Product product)  throws ProductNotFoundException;
    Product createProduct(CreateProductRequestDTO createProductRequestDTO);
    Page<Product> getPaginatedProduct(Integer pageNo, Integer pageSize);
}
