package com.example.cafekiosk.spring.api.service.product;

import com.example.cafekiosk.spring.IntegrationTestSupport;
import com.example.cafekiosk.spring.api.controller.product.request.ProductCreateRequest;
import com.example.cafekiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import com.example.cafekiosk.spring.api.service.product.response.ProductResponse;
import com.example.cafekiosk.spring.domain.product.Product;
import com.example.cafekiosk.spring.domain.product.ProductRepository;
import com.example.cafekiosk.spring.domain.product.ProductSellingStatus;
import com.example.cafekiosk.spring.domain.product.ProductType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.example.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static com.example.cafekiosk.spring.domain.product.ProductType.HANDMADE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class ProductServiceTest extends IntegrationTestSupport {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    void tearDown() {
        productRepository.deleteAllInBatch();
    }

    @DisplayName("기존 상품이 존재하고 신규 상품을 등록하는 경우, 가장 최근 상품의 상품번호에서 1 증가한 상품번호로 등록된다.")
    @Test
    void createProduct() {
        // given
        Product product = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        productRepository.save(product);

        ProductCreateRequest request = new ProductCreateRequest(HANDMADE, SELLING, "카푸치노", 5000);
        ProductCreateServiceRequest serviceRequest = request.toServiceRequest();

        // when
        ProductResponse response = productService.createProduct(serviceRequest);

        // then
        assertThat(response)
                .extracting("productNumber", "type", "sellingStatus", "name", "price")
                .contains("002", HANDMADE, SELLING, "카푸치노", 5000);

        List<Product> products = productRepository.findAll();
        assertThat(products)
                .extracting("productNumber", "type", "sellingStatus", "name", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", HANDMADE, SELLING, "아메리카노", 4000),
                        tuple("002", HANDMADE, SELLING, "카푸치노", 5000)
                );
    }

    @DisplayName("기존 상품이 없는 경우, 신규 상품을 등록하면 상품 번호는 001로 등록된다.")
    @Test
    void createProductWhenProductsIsEmpty() {
        // given
        ProductCreateRequest request = new ProductCreateRequest(HANDMADE, SELLING, "카푸치노", 5000);
        ProductCreateServiceRequest serviceRequest = request.toServiceRequest();

        // when
        ProductResponse response = productService.createProduct(serviceRequest);

        // then
        assertThat(response)
                .extracting("productNumber", "type", "sellingStatus", "name", "price")
                .contains("001", HANDMADE, SELLING, "카푸치노", 5000);

        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(1)
                .extracting("productNumber", "type", "sellingStatus", "name", "price")
                .contains(
                        tuple("001", HANDMADE, SELLING, "카푸치노", 5000)
                );
    }

    private Product createProduct(String productNumber, ProductType type, ProductSellingStatus sellingStatus, String name, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .type(type)
                .sellingStatus(sellingStatus)
                .name(name)
                .price(price)
                .build();
    }
}