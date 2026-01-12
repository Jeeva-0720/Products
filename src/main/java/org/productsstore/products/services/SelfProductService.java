package org.productsstore.products.services;

import jakarta.persistence.EntityNotFoundException;
import org.productsstore.products.Dtos.CreateProductRequestDTO;
import org.productsstore.products.Dtos.UserDto;
import org.productsstore.products.Exceptions.ProductNotFoundException;
import org.productsstore.products.models.Category;
import org.productsstore.products.models.Product;
import org.productsstore.products.repositories.CategoryRepository;
import org.productsstore.products.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service("selfProductService")
public class SelfProductService implements ProductService {

    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    RestTemplate restTemplate;

    public SelfProductService(ProductRepository productRepository, CategoryRepository categoryRepository, RestTemplate restTemplate) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public Product getDetailsBasedOnUserScope(Long productId, Long userId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if(optionalProduct.isEmpty()) {
            System.out.println("NO PRODUCT FOUND");
            return null;
        }

        //check for product scope - public or private and
        //accordingly add if else conditions

        //Call to UserService to get User Detail
        //if user is not null, we will return product , else return null;
        UserDto userDto = restTemplate.getForObject("http://userservice/users/{userId}", UserDto.class,userId);

        if(userDto == null) {
            System.out.println("NO USER DETAIL FOUND");
            return null;
        }

        System.out.println(userDto.getEmail());
        return optionalProduct.get();
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
    public List<Product> getProductsInSpecificCategory(String categoryName) {
        return productRepository.findAllByCategory_Name(categoryName);
    }

    @Override
    public Page<Product> getAllProducts(int pageNumber, int pageSize) {

        return productRepository.findAll( PageRequest.of(pageNumber, pageSize) );
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
    public void deleteSingleProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Product with id " + id + " not found"));

        productRepository.delete(product);
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
    public Product createProduct(CreateProductRequestDTO createProductRequestDTO) {
        // Check if the Product exists
        var product = productRepository.findByTitle(createProductRequestDTO.getTitle());
        if (product == null) {
            product = createProductRequestDTO.toProduct();
            product.setCreatedAt(new Date());
        } else {
            product.setUpdatedAt(new Date());
        }

        // Check if the category exists in database if exists, don't create a new entry.
        var category = categoryRepository.findByName(createProductRequestDTO.getCategory());
        if (category == null) {
            category = Category.builder()
                    .name(createProductRequestDTO.getCategory())
                    .createdAt(new Date())
                    .name(createProductRequestDTO.getCategory())
                    .build();
        } else {
            category.setUpdatedAt(new Date());
        }
        product.setCategory(category);

        return productRepository.save(product);
    }

    @Override
    public Page<Product> getPaginatedProduct(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return productRepository.findAll(pageable);
    }

}
