package org.productsstore.products.services;

import org.productsstore.products.Exceptions.ProductNotFoundException;
import org.productsstore.products.models.Category;
import org.productsstore.products.models.Product;
import org.productsstore.products.repositories.CategoryRepository;
import org.productsstore.products.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service("selfProductService")
public class SelfProductService implements ProductService {

    ProductRepository productRepository;
    CategoryRepository categoryRepository;

    public SelfProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product getSingleProduct(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            throw new NullPointerException("Product with id: " + id + " not found");
        }
        return product.get();
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product updateSingleProduct(Long id, Product product) throws ProductNotFoundException {
        Optional<Product> oldProduct = productRepository.getProductById(id);
        if (oldProduct.isEmpty()) {
            throw new ProductNotFoundException("Product with id: " + id + " not found");
        }
        Product updatedProduct = oldProduct.get();
        if(product.getTitle()!=null) {
            updatedProduct.setTitle(product.getTitle());
        }
        if(product.getPrice()!=null) {
            updatedProduct.setPrice(product.getPrice());
        }
        return productRepository.save(updatedProduct);
    }

    @Override
    public String deleteSingleProduct(Long id) {

        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            throw new NullPointerException("Product with id: " + id + " not found");
        }
        productRepository.deleteById(id);
        return "Product with id: " + id + " deleted";
    }

    @Override
    public Product replaceProduct(Long id, Product product) throws ProductNotFoundException {
        Optional<Product> oldProduct = productRepository.getProductById(id);
        if (oldProduct.isEmpty()) {
            throw new ProductNotFoundException("Product with id: " + id + " not found");
        }
        Product updatedProduct = oldProduct.get();
        if(product.getTitle()!=null) {
            updatedProduct.setTitle(product.getTitle());
        }
        if(product.getPrice()!=null) {
            updatedProduct.setPrice(product.getPrice());
        }

        if(product.getCategory()!=null) {
            updatedProduct.setCategory(product.getCategory());
        }
        if(product.getDescription()!=null) {
            updatedProduct.setDescription(product.getDescription());
        }

        return productRepository.save(updatedProduct);
    }

    @Override
    public Product addProduct(Product product) {

        Category category = product.getCategory();

        if(category.getId() == null) {
            category = categoryRepository.save(category);
            product.setCategory(category);
        }
        Product newProduct = productRepository.save(product);
        return newProduct;
    }
}
