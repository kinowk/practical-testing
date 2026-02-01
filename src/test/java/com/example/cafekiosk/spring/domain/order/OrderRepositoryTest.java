package com.example.cafekiosk.spring.domain.order;

import com.example.cafekiosk.spring.IntegrationTestSupport;
import com.example.cafekiosk.spring.domain.orderproduct.OrderProductRepository;
import com.example.cafekiosk.spring.domain.product.Product;
import com.example.cafekiosk.spring.domain.product.ProductRepository;
import com.example.cafekiosk.spring.domain.product.ProductSellingStatus;
import com.example.cafekiosk.spring.domain.product.ProductType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.cafekiosk.spring.domain.order.OrderStatus.INIT;
import static com.example.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static com.example.cafekiosk.spring.domain.product.ProductType.*;
import static org.assertj.core.api.Assertions.assertThat;

class OrderRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @AfterEach
    void tearDown() {
        orderProductRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
    }

    @Transactional
    @DisplayName("등록일자와 주문상태를 통해, 주문을 조회한다.")
    @Test
    void findOrdersBy() {
        // given
        Product product1 = createProduct("001", BAKERY, SELLING, "소금빵", 4000);
        Product product2 = createProduct("002", BOTTLE, SELLING, "사과쥬스", 3000);
        Product product3 = createProduct("003", HANDMADE, SELLING, "아메리카노", 2000);
        List<Product> products = List.of(product1, product2, product3);
        productRepository.saveAll(products);

        List<Product> savedProducts = productRepository.findAll();
        LocalDateTime registeredAt = LocalDateTime.of(2026, 1, 1, 17, 44);
        Order order = Order.create(savedProducts, registeredAt);
        orderRepository.save(order);

        // when
        List<Order> results = orderRepository.findOrdersBy(
                LocalDateTime.of(2026, 1, 1, 0, 0),
                LocalDateTime.of(2026, 1, 2, 0, 0),
                INIT
        );

        // then
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getOrderProducts()).hasSize(3);
    }

    private Product createProduct(String productNumber, ProductType productType, ProductSellingStatus sellingStatus, String productName, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .type(productType)
                .sellingStatus(sellingStatus)
                .name(productName)
                .price(price)
                .build();
    }
}