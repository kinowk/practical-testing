package com.example.cafekiosk.spring.api.service.order;

import com.example.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import com.example.cafekiosk.spring.api.service.order.response.OrderResponse;
import com.example.cafekiosk.spring.domain.order.OrderRepository;
import com.example.cafekiosk.spring.domain.orderproduct.OrderProductRepository;
import com.example.cafekiosk.spring.domain.product.Product;
import com.example.cafekiosk.spring.domain.product.ProductRepository;
import com.example.cafekiosk.spring.domain.product.ProductSellingStatus;
import com.example.cafekiosk.spring.domain.product.ProductType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@ActiveProfiles("test")
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private OrderService orderService;

    @AfterEach
    void tearDown() {
        orderProductRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
    }

    @DisplayName("상품 번호 리스트를 받아, 주문을 생성한다.")
    @Test
    void createOrder() {
        // given
        LocalDateTime registeredAt = LocalDateTime.now();
        Product product1 = createProduct("001", 1000);
        Product product2 = createProduct("002", 3000);
        Product product3 = createProduct("003", 5000);

        List<Product> products = productRepository.saveAll(List.of(product1, product2, product3));
        List<String> productNumbers = products.stream()
                .map(Product::getProductNumber)
                .toList();

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(productNumbers, registeredAt);

        // when
        OrderResponse orderResponse = orderService.createOrder(orderCreateRequest, registeredAt);

        // then
        assertThat(orderResponse.id()).isNotNull();
        assertThat(orderResponse)
                .extracting("registeredAt", "totalPrice")
                .contains(registeredAt, 9000);
        assertThat(orderResponse.products()).hasSize(3)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", 1000),
                        tuple("002", 3000),
                        tuple("003", 5000)
                );
    }

    @DisplayName("중복되는 상품번호 리스트로 주문을 생성할 수 있다.")
    @Test
    void createOrderWithDuplicateProductNumbers() {
        // given
        LocalDateTime registeredAt = LocalDateTime.now();
        Product product1 = createProduct("001", 1000);
        Product product2 = createProduct("002", 3000);
        Product product3 = createProduct("003", 5000);

        productRepository.saveAll(List.of(product1, product2, product3));

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(List.of("001", "001"), registeredAt);

        // when
        OrderResponse orderResponse = orderService.createOrder(orderCreateRequest, registeredAt);

        // then
        assertThat(orderResponse.id()).isNotNull();
        assertThat(orderResponse)
                .extracting("registeredAt", "totalPrice")
                .contains(registeredAt, 2000);
        assertThat(orderResponse.products()).hasSize(2)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", 1000),
                        tuple("001", 1000)
                );
    }

    private Product createProduct(String productNumber, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .price(price)
                .name("Product Name")
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.SELLING)
                .build();
    }
}