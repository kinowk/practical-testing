package com.example.cafekiosk.spring.domain.order;

import com.example.cafekiosk.spring.domain.BaseEntity;
import com.example.cafekiosk.spring.domain.orderproduct.OrderProduct;
import com.example.cafekiosk.spring.domain.product.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private int totalPrice;

    private LocalDateTime registeredAt;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<OrderProduct> orderProducts = new ArrayList<>();

    @Builder
    private Order(List<Product> products, OrderStatus orderStatus, LocalDateTime registeredAt) {
        this.orderProducts = products.stream()
                .map(product -> OrderProduct
                        .builder()
                        .order(this)
                        .product(product)
                        .build()
                )
                .toList();
        this.orderStatus = orderStatus;
        this.registeredAt = registeredAt;
        this.totalPrice = calculateTotalPrice(products);
    }
    @Builder
    private Order(LocalDateTime registeredAt, List<Product> products) {
        this.orderStatus = OrderStatus.INIT;
        this.totalPrice = calculateTotalPrice(products);
        this.registeredAt = registeredAt;
        this.orderProducts = products.stream()
                .map(product ->
                        OrderProduct.builder()
                                .order(this)
                                .product(product)
                                .build()
                )
                .toList();
    }

    public static Order create(List<Product> products, LocalDateTime registeredAt) {
        return Order.builder()
                .orderStatus(OrderStatus.INIT)
                .products(products)
                .registeredAt(registeredAt)
                .build();
    }

    private int calculateTotalPrice(List<Product> orderProducts) {
        return orderProducts.stream()
                .mapToInt(Product::getPrice)
                .sum();
    }
}
