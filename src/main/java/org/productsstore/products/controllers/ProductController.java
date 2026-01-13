package org.productsstore.products.controllers;


import jakarta.annotation.Nullable;
import org.productsstore.products.Dtos.CreateProductRequestDTO;
import org.productsstore.products.Dtos.ProductResponseDTO;
import org.productsstore.products.Dtos.UserDto;
import org.productsstore.products.Exceptions.InvalidTokenException;
import org.productsstore.products.Exceptions.ProductNotFoundException;
import org.productsstore.products.commons.AuthenticationCommons;
import org.productsstore.products.models.Product;
import org.productsstore.products.services.ProductService;
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
    AuthenticationCommons authenticationCommons;

    public ProductController( @Qualifier("selfProductService") ProductService productService, AuthenticationCommons authenticationCommons) {
        this.productService = productService;
        this.authenticationCommons = authenticationCommons;
    }

    @GetMapping("/products/{productId}/{userId}")
    // API to fetch product details based on both productId and userId scope.
    public Product getProductDetailsBasedOnUserScope(@PathVariable Long productId, @PathVariable Long userId) {
        // Delegates the responsibility of validating user scope and access rules to the service
        return productService.getDetailsBasedOnUserScope(productId,userId);
    }

    @GetMapping("/{id}")
    // API to fetch product details by productId. Requires Authorization token for validating the requesting user.
    public ResponseEntity<Product> getProductById(@PathVariable ("id") Long id, @Nullable @RequestHeader("Authorization") String token) throws ProductNotFoundException, InvalidTokenException {
        UserDto userDto = authenticationCommons.validateToken(token);
        // If token validation fails, explicitly throw an InvalidTokenException
        if(userDto == null) {
            throw new InvalidTokenException("Provided Token is not valid, please try again");
        }
        return new ResponseEntity<>(productService.getSingleProduct(id), HttpStatus.OK);
    }

    @GetMapping
    // API to fetch all products with pagination support.
    // pageNumber and pageSize are used to control the result set size and offset.
    public ResponseEntity<Page<Product>> getAllProducts(@RequestParam("pageNumber") int pageNumber, @RequestParam("pageSize") int pageSize) {
        ResponseEntity<Page<Product>> response;
        try{
            // Delegates pagination logic to the service layer
            response = new ResponseEntity<>(productService.getAllProducts(pageNumber, pageSize), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            // Returns INTERNAL_SERVER_ERROR in case of any unexpected failure
            response = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @GetMapping("/category/{categoryName}")
    // API to fetch all products belonging to a specific category.
    public ResponseEntity<List<?>> getProductsInSpecifigCategory(@PathVariable("categoryName") String categoryName) throws ProductNotFoundException {
        // List to hold converted ProductResponseDTO objects
        List<ProductResponseDTO> productsResponseDTO = new ArrayList<>();
        List<Product> products = productService.getProductsInSpecificCategory(categoryName);
        // Convert each Product entity to ProductResponseDTO
        for (Product product : products) {
            productsResponseDTO.add(product.toProductResponseDTO());
        }

        return new ResponseEntity<>(productsResponseDTO, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    // API to partially update an existing product identified by its ID.
    public Product updateProduct( @PathVariable("id") Long id, Product product) throws ProductNotFoundException {
        return productService.updateSingleProduct(id, product);
    }

    @PutMapping("/{id}")
    // API to completely replace an existing product identified by its ID.
    // All fields of the product are expected to be provided in the request.
    public Product replaceProduct( @PathVariable("id") Long id, Product product) throws ProductNotFoundException {
        return productService.replaceProduct(id, product);
    }

    @DeleteMapping("/{id}")
    // API to delete a product based on its unique identifier.
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteSingleProduct(id);
        return "Product deleted";
    }

    @PostMapping
    // API to create a new product.
    // Expects product creation details in the request body.
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequestDTO createProductRequestDTO) {
        ResponseEntity<Product> response;
        // Returns HTTP 201 (CREATED) upon successful creation
        return new ResponseEntity<>(productService.createProduct(createProductRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{pageNo}/{pageSize}")
    // API to fetch products using pagination parameters passed as path variables.
    public ResponseEntity<List<Product>> getPaginatedProduct(@PathVariable("pageNo") Integer pageNo, @PathVariable("pageSize") Integer pageSize){
        Page<Product> products = productService.getPaginatedProduct(pageNo, pageSize);
        return ResponseEntity.ok(products.getContent());
    }
}
