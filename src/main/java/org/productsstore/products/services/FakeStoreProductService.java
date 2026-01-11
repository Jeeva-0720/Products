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
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    public Product getSingleProduct(Long id) {
        Product product = (Product) redisTemplate.opsForHash().get("PRODUCTS", "PRODUCT_" + id);

        if (product != null) {
            return product;
        }

        FakeStoreProductDto fakeStoreProductDto = restTemplate.getForObject("https://fakestoreapi.com/products/" + id, FakeStoreProductDto.class);
        if(fakeStoreProductDto == null) {
            throw new NullPointerException("Product with id: " + id + " not found");
        }
        product = fakeStoreProductDto.convertFakeStoreProductToProduct(fakeStoreProductDto);
        redisTemplate.opsForHash().put("PRODUCTS", "PRODUCT_" + id, product);
        return product;
    }

    @Override
    public Page<Product> getAllProducts(int pageNumber, int pageSize) {

        FakeStoreProductDto[] dtoArray = restTemplate.getForObject("https://fakestoreapi.com/products", FakeStoreProductDto[].class);
        if (dtoArray == null || dtoArray.length == 0) {
            return Page.empty();
        }
        List<Product> allProducts = Arrays.stream(dtoArray)
                .map(dto -> dto.convertFakeStoreProductToProduct(dto))
                .collect(Collectors.toList());

        int start = Math.min(pageNumber * pageSize, allProducts.size());
        int end = Math.min(start + pageSize, allProducts.size());

        List<Product> pagedProducts = allProducts.subList(start, end);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return new PageImpl<>(pagedProducts, pageable, allProducts.size());
    }


    @Override
    public Product updateSingleProduct(Long id, Product product) {
        RequestCallback requestCallback = restTemplate.httpEntityCallback(product, FakeStoreProductDto.class);
        HttpMessageConverterExtractor<FakeStoreProductDto> responseExtractor = new HttpMessageConverterExtractor(FakeStoreProductDto.class, restTemplate.getMessageConverters());
        FakeStoreProductDto fakeStoreProductDto = restTemplate.execute("https://fakestoreapi.com/products/" + id, HttpMethod.PATCH, requestCallback, responseExtractor);
        return fakeStoreProductDto.convertFakeStoreProductToProduct(fakeStoreProductDto);
    }

     @Override
    public List<Product> getProductsInSpecificCategory(String categoryName) {
        List<Product> products = new ArrayList<>();
         FakeStoreProductDto[] fakeStoreProductsDTO = restTemplate.getForObject("https://fakestoreapi.com/products/category/" + categoryName, FakeStoreProductDto[].class);

        for (FakeStoreProductDto fakeStoreProductDTO : fakeStoreProductsDTO) {
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

            products.add(product);
        }

        return products;
    }

    @Override
    public void deleteSingleProduct(Long id) {
        return;
    }

    @Override
    public Product replaceProduct(Long id, Product product) {
        return null;
    }

    @Override
    public Product createProduct(CreateProductRequestDTO createProductRequestDTO) {
        var fakeStoreProductDTO = FakeStoreProductDto.builder()
                .image(createProductRequestDTO.getImage())
                .title(createProductRequestDTO.getTitle())
                .price(createProductRequestDTO.getPrice())
                .description(createProductRequestDTO.getDescription())
                .category(createProductRequestDTO.getCategory())
                .build();

        ResponseEntity<FakeStoreProductDto> responseEntity = restTemplate.postForEntity("https://fakestoreapi.com/products", fakeStoreProductDTO, FakeStoreProductDto.class);

        return responseEntity.getBody().toProduct();
    }

    @Override
    public Page<Product> getPaginatedProduct(Integer pageNo, Integer pageSize) {
        return null;
    }
}
