package org.productsstore.products.controllers;


import org.productsstore.products.models.Product;
import org.productsstore.products.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable ("id") Long id) {
        ResponseEntity<Product> response = new ResponseEntity<>(productService.getSingleProduct(id), HttpStatus.OK);
        return response;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        ResponseEntity<List<Product>> response;
        try{
            response = new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @PutMapping("{id}")
    public Product updateProduct( @PathVariable("id") Long id, Product product) {
        return productService.updateSingleProduct(id, product);
    }
}
