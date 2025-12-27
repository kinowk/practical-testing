package com.example.cafekiosk.spring.domain.order;

import com.example.cafekiosk.spring.domain.product.Product;
import com.example.cafekiosk.spring.domain.product.ProductSellingStatus;
import com.example.cafekiosk.spring.domain.product.ProductType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {

    @DisplayName("주문 생성 시, 상품의 총 금액을 계산한다.")
    @Test
    void calculateTotalPrice() {
        // given
        List<Product> products = List.of(
                createProduct("001", 1000),
                createProduct("002", 3000),
                createProduct("003", 4000)
        );

        // when
        Order order = Order.create(products, LocalDateTime.now());

        // then
        assertThat(order.getTotalPrice()).isEqualTo(8000);
    }

    @DisplayName("주문 생성 시, 주문상태는 INIT(주문생성)이다")
    @Test
    void init() {
        // given
        List<Product> products = List.of(
                createProduct("001", 1000),
                createProduct("002", 3000),
                createProduct("003", 4000)
        );

        // when
        Order order = Order.create(products, LocalDateTime.now());

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.INIT);
    }

    @DisplayName("주문 생성 시, 주문 등록 시간이 기록된다.")
    @Test
    void registeredAt() {
        // given
        LocalDateTime registeredAt = LocalDateTime.now();
        List<Product> products = List.of(
                createProduct("001", 1000),
                createProduct("002", 3000),
                createProduct("003", 4000)
        );

        // when
        Order order = Order.create(products, registeredAt);

        // then
        assertThat(order.getRegisteredAt()).isEqualTo(registeredAt);
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