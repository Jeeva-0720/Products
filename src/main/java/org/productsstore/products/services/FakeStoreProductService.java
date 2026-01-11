package org.productsstore.products.services;

import org.productsstore.products.Dtos.FakeStoreProductDto;
import org.productsstore.products.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

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
        FakeStoreProductDto[] fakeStoreProductDto = restTemplate.getForObject("https://fakestoreapi.com/products", FakeStoreProductDto[].class);
        List<Product> products = new ArrayList<>();
        for(FakeStoreProductDto fakeStoreProductDto1 : fakeStoreProductDto) {
            products.add(fakeStoreProductDto1.convertFakeStoreProductToProduct(fakeStoreProductDto1));
        }
        return new PageImpl<>(products);
//        throw new UnsupportedOperationException("Not supported yet.");
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
        FakeStoreProductDTO[] fakeStoreProductsDTO = restTemplate.getForObject("https://fakestoreapi.com/products/category/" + categoryName, FakeStoreProductDTO[].class);

        for (FakeStoreProductDTO fakeStoreProductDTO : fakeStoreProductsDTO) {
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
    public String deleteSingleProduct(Long id) {
        return null;
    }

    @Override
    public Product replaceProduct(Long id, Product product) {
        return null;
    }

    @Override
    public Product addProduct(Product product) {
        return null;
    }

     @Override
    public Product createProduct(CreateProductRequestDTO createProductRequestDTO) {
        var fakeStoreProductDTO = FakeStoreProductDTO.builder()
                .image(createProductRequestDTO.getImage())
                .title(createProductRequestDTO.getTitle())
                .price(createProductRequestDTO.getPrice())
                .description(createProductRequestDTO.getDescription())
                .category(createProductRequestDTO.getCategory())
                .build();

        ResponseEntity<FakeStoreProductDTO> responseEntity = restTemplate.postForEntity("https://fakestoreapi.com/products", fakeStoreProductDTO, FakeStoreProductDTO.class);

        return responseEntity.getBody().toProduct();
    }
}
