package com.example.cafekiosk.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.example.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static com.example.cafekiosk.spring.domain.product.ProductType.BAKERY;
import static com.example.cafekiosk.spring.domain.product.ProductType.HANDMADE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@ActiveProfiles("test")
@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("키오스크 화면에 보여줄 상품 목록을 조회한다.")
    @Test
    void getSellingProducts() {
        // given
        Product product1 = Product.builder()
                .productNumber("001")
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("아메리카노")
                .price(2000)
                .build();

        Product product2 = Product.builder()
                .productNumber("002")
                .type(HANDMADE)
                .sellingStatus(HOLD)
                .name("카페라떼")
                .price(3000)
                .build();

        Product product3 = Product.builder()
                .productNumber("003")
                .type(BAKERY)
                .sellingStatus(STOP_SELLING)
                .name("크루아상")
                .price(3500)
                .build();

        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        List<Product> sellingProducts = productRepository.findAllBySellingStatusIn(List.of(SELLING, HOLD));

        // then
        assertThat(sellingProducts).hasSize(2);
        assertThat(sellingProducts)
                .extracting("productNumber", "name", "sellingStatus")
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", SELLING),
                        tuple("002", "카페라떼", HOLD)
                );
    }

}