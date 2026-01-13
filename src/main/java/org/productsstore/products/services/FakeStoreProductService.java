package org.productsstore.products.services;

import org.productsstore.products.Dtos.CreateProductRequestDTO;
import org.productsstore.products.Dtos.FakeStoreProductDto;
import org.productsstore.products.models.Category;
import org.productsstore.products.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service("fakeStoreProductService")
public class FakeStoreProductService implements ProductService {

    RestTemplate restTemplate;
    RedisTemplate<String, Object> redisTemplate;

    public FakeStoreProductService(RestTemplate restTemplate, RedisTemplate<String, Object> redisTemplate) {
        this.restTemplate = restTemplate;
        this.redisTemplate = redisTemplate;
    }

    @Override
    // Implements a cache-first strategy using Redis to reduce external API calls.
    public Product getSingleProduct(Long id) {
        // Attempt to retrieve the product from Redis cache using a composite key
        Product product = (Product) redisTemplate.opsForHash().get("PRODUCTS", "PRODUCT_" + id);

        if (product != null) {
            return product;
        }

        FakeStoreProductDto fakeStoreProductDto = restTemplate.getForObject("https://fakestoreapi.com/products/" + id, FakeStoreProductDto.class);
        if(fakeStoreProductDto == null) {
            throw new NullPointerException("Product with id: " + id + " not found");
        }
        // Convert the external DTO into the internal Product domain object
        product = fakeStoreProductDto.convertFakeStoreProductToProduct(fakeStoreProductDto);
        // Store the newly fetched product in Redis cache for future requests
        redisTemplate.opsForHash().put("PRODUCTS", "PRODUCT_" + id, product);
        return product;
    }

    @Override
    // Data is retrieved from an external API and paginated in-memory
    public Page<Product> getAllProducts(int pageNumber, int pageSize) {

        FakeStoreProductDto[] dtoArray = restTemplate.getForObject("https://fakestoreapi.com/products", FakeStoreProductDto[].class);
        if (dtoArray == null || dtoArray.length == 0) {
            return Page.empty();
        }
        List<Product> allProducts = Arrays.stream(dtoArray)
                .map(dto -> dto.convertFakeStoreProductToProduct(dto))
                .collect(Collectors.toList());

        // Calculate pagination boundaries safely to avoid IndexOutOfBoundsException
        int start = Math.min(pageNumber * pageSize, allProducts.size());
        int end = Math.min(start + pageSize, allProducts.size());

        List<Product> pagedProducts = allProducts.subList(start, end);
        // Create Pageable metadata for the current page
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return new PageImpl<>(pagedProducts, pageable, allProducts.size());
    }


    @Override
    // Updates specific fields of an existing product using HTTP PATCH.
    // Delegates the update operation to the external FakeStore API.
    public Product updateSingleProduct(Long id, Product product) {
        // Prepares the HTTP request callback with the product payload
        RequestCallback requestCallback = restTemplate.httpEntityCallback(product, FakeStoreProductDto.class);
        HttpMessageConverterExtractor<FakeStoreProductDto> responseExtractor = new HttpMessageConverterExtractor(FakeStoreProductDto.class, restTemplate.getMessageConverters());
        // Executes the PATCH request against the FakeStore API
        FakeStoreProductDto fakeStoreProductDto = restTemplate.execute("https://fakestoreapi.com/products/" + id, HttpMethod.PATCH, requestCallback, responseExtractor);
        return fakeStoreProductDto.convertFakeStoreProductToProduct(fakeStoreProductDto);
    }

     @Override
     // Fetches all products belonging to a specific category from the external API.
    public List<Product> getProductsInSpecificCategory(String categoryName) {
        List<Product> products = new ArrayList<>();
         // Calls the FakeStore API to retrieve products for the given category
         FakeStoreProductDto[] fakeStoreProductsDTO = restTemplate.getForObject("https://fakestoreapi.com/products/category/" + categoryName, FakeStoreProductDto[].class);

        for (FakeStoreProductDto fakeStoreProductDTO : fakeStoreProductsDTO) {
            // Builds Product object manually using builder pattern
            var product = Product.builder()
                    .id(fakeStoreProductDTO.getId())
                    .imageURL(fakeStoreProductDTO.getImage())
                    .title(fakeStoreProductDTO.getTitle())
                    .price(fakeStoreProductDTO.getPrice())
                    .description(fakeStoreProductDTO.getDescription())
                    .category(Category.builder()
                            .name(fakeStoreProductDTO.getCategory())
                            .build())
                    .build();

            // Adds the converted product to the result list
            products.add(product);
        }

        return products;
    }

    @Override
    // Deletes a product by its ID
    public void deleteSingleProduct(Long id) {
        restTemplate.delete("https://fakestoreapi.com/products/" + id);
        return;
    }

    @Override
    // Replaces an existing product completely with new data.
    public Product replaceProduct(Long id, Product product) {
        // Builds FakeStoreProductDto from the incoming Product object
        FakeStoreProductDto fakeStoreProductDTO = FakeStoreProductDto.builder()
                .title(product.getTitle())
                .price(product.getPrice())
                .description(product.getDescription())
                .category(product.getCategory().getName())
                .build();

        // Executes HTTP PUT request to fully replace the product
        ResponseEntity<FakeStoreProductDto> responseEntity =
                restTemplate.exchange("https://fakestoreapi.com/products/" + id, HttpMethod.PUT, new HttpEntity<>(fakeStoreProductDTO), FakeStoreProductDto.class);

        // Converts the response DTO back to the internal Product domain object
        return Objects.requireNonNull(responseEntity.getBody()).toProduct();
    }

    @Override
    // Creates a new product by sending a POST request to the FakeStore API.
    public Product createProduct(CreateProductRequestDTO createProductRequestDTO) {
        // Builds the FakeStoreProductDto from the incoming request DTO
        var fakeStoreProductDTO = FakeStoreProductDto.builder()
                .image(createProductRequestDTO.getImage())
                .title(createProductRequestDTO.getTitle())
                .price(createProductRequestDTO.getPrice())
                .description(createProductRequestDTO.getDescription())
                .category(createProductRequestDTO.getCategory())
                .build();

        // Sends a POST request to create the product in the FakeStore system
        ResponseEntity<FakeStoreProductDto> responseEntity = restTemplate.postForEntity("https://fakestoreapi.com/products", fakeStoreProductDTO, FakeStoreProductDto.class);

        return Objects.requireNonNull(responseEntity.getBody()).toProduct();
    }

    @Override
    public Page<Product> getPaginatedProduct(Integer pageNo, Integer pageSize) {
        return null;
    }

    @Override
    public Product getDetailsBasedOnUserScope(Long productId, Long userId) {
        return null;
    }
}
