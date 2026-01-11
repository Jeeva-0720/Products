package org.productsstore.products.Dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.productsstore.products.models.Category;
import org.productsstore.products.models.Product;

@Builder
@Getter
@Setter
public class FakeStoreProductDto {
    private Long id;
    private String title;
    private String description;
    private Double price;
    private String category;
    private String image;

    public Product convertFakeStoreProductToProduct(FakeStoreProductDto fakeStoreProductDto) {
        Product product = new Product();
        product.setId(fakeStoreProductDto.getId());
        product.setTitle(fakeStoreProductDto.getTitle());
        product.setPrice(fakeStoreProductDto.getPrice());
        product.setDescription(fakeStoreProductDto.getDescription());
        Category category = new Category();
        category.setName(fakeStoreProductDto.getCategory());
        product.setCategory(category);
        return product;
    }

    public Product toProduct() {
        var product = Product.builder()
                .id(id)
                .category(Category.builder()
                        .name(category)
                        .build())
                .description(description)
                .price(price)
                .title(title)
                .build();

        return product;
    }
}
