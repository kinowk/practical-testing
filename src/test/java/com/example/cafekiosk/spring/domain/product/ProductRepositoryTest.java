package com.example.cafekiosk.spring.domain.product;

import com.example.cafekiosk.spring.IntegrationTestSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.example.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static com.example.cafekiosk.spring.domain.product.ProductType.BAKERY;
import static com.example.cafekiosk.spring.domain.product.ProductType.HANDMADE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class ProductRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    void tearDown() {
        productRepository.deleteAllInBatch();
    }

    @DisplayName("키오스크 화면에 보여줄 상품 목록을 조회한다.")
    @Test
    void getSellingProducts() {
        // given
        Product product1 = createProduct("001", 2000, HANDMADE, SELLING);
        Product product2 = createProduct("002", 3000, HANDMADE, HOLD);
        Product product3 = createProduct("003", 3500, BAKERY, STOP_SELLING);

        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        List<Product> sellingProducts = productRepository.findAllBySellingStatusIn(List.of(SELLING, HOLD));

        // then
        assertThat(sellingProducts).hasSize(2);
        assertThat(sellingProducts)
                .extracting("productNumber", "sellingStatus")
                .containsExactlyInAnyOrder(
                        tuple("001", SELLING),
                        tuple("002", HOLD)
                );
    }

    @DisplayName("상품 번호 리스트로, 상품을 조회한다.")
    @Test
    void getProductsByProductNumbers() {
        // given
        Product product1 = createProduct("001", 2000, HANDMADE, SELLING);
        Product product2 = createProduct("002", 3000, HANDMADE, HOLD);
        Product product3 = createProduct("003", 3500, BAKERY, STOP_SELLING);

        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        List<Product> sellingProducts = productRepository.findAllByProductNumberIn(List.of("001", "002"));

        // then
        assertThat(sellingProducts).hasSize(2);
        assertThat(sellingProducts)
                .extracting("productNumber", "sellingStatus")
                .containsExactlyInAnyOrder(
                        tuple("001", SELLING),
                        tuple("002", HOLD)
                );
    }

    @DisplayName("가장 마지막 상품의 상품번호를 반환한다.")
    @Test
    void findLatestProductNumber() {
        // given
        String expectedProductNumber = "001";
        Product product = createProduct(expectedProductNumber, 2000, HANDMADE, SELLING);
        productRepository.save(product);

        // when
        String productNumber = productRepository.findLatestProductNumber();

        // then
        assertThat(productNumber).isEqualTo(expectedProductNumber);
    }

    @DisplayName("상품이 없는 경우 상품번호로 null를 반환한다.")
    @Test
    void findLatestProductNumberWhenProductIsEmpty() {
        // when
        String productNumber = productRepository.findLatestProductNumber();

        // then
        assertThat(productNumber).isNull();
    }

    private Product createProduct(String productNumber, int price, ProductType type, ProductSellingStatus sellingStatus) {
        return Product.builder()
                .productNumber(productNumber)
                .type(type)
                .sellingStatus(sellingStatus)
                .price(price)
                .name("Product Name")
                .build();

    }

}