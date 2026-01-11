package org.productsstore.products.controllers;


import org.productsstore.products.Dtos.CreateProductRequestDTO;
import org.productsstore.products.Dtos.ProductResponseDTO;
import org.productsstore.products.Exceptions.ProductNotFoundException;
import org.productsstore.products.models.Product;
import org.productsstore.products.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    ProductService productService;

    public ProductController( @Qualifier("fakeStoreProductService") ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable ("id") Long id) {
        ResponseEntity<Product> response = new ResponseEntity<>(productService.getSingleProduct(id), HttpStatus.OK);
        return response;
    }

    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(@RequestParam("pageNumber") int pageNumber, @RequestParam("pageSize") int pageSize) {
        ResponseEntity<Page<Product>> response;
        try{
            response = new ResponseEntity<>(productService.getAllProducts(pageNumber, pageSize), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @GetMapping("/category/{categoryName}")
    public ResponseEntity<List<?>> getProductsInSpecifigCategory(@PathVariable("categoryName") String categoryName) throws ProductNotFoundException {
        List<ProductResponseDTO> productsResponseDTO = new ArrayList<>();
        List<Product> products = productService.getProductsInSpecificCategory(categoryName);
        for (Product product : products) {
            productsResponseDTO.add(product.toProductResponseDTO());
        }

        return new ResponseEntity<>(productsResponseDTO, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public Product updateProduct( @PathVariable("id") Long id, Product product) throws ProductNotFoundException {
        return productService.updateSingleProduct(id, product);
    }

    @PutMapping("/{id}")
    public Product replaceProduct( @PathVariable("id") Long id, Product product) throws ProductNotFoundException {
        return productService.replaceProduct(id, product);
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteSingleProduct(id);
        return "Product deleted";
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequestDTO createProductRequestDTO) {
        ResponseEntity<Product> response;
        return new ResponseEntity<>(productService.createProduct(createProductRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{pageNo}/{pageSize}")
    public ResponseEntity<List<Product>> getPaginatedProduct(@PathVariable("pageNo") Integer pageNo, @PathVariable("pageSize") Integer pageSize){
        Page<Product> products = productService.getPaginatedProduct(pageNo, pageSize);
        return ResponseEntity.ok(products.getContent());
    }
}
