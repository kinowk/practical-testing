package com.example.cafekiosk.spring.api.service.order.response;

import com.example.cafekiosk.spring.api.service.product.response.ProductResponse;
import com.example.cafekiosk.spring.domain.order.Order;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        int totalPrice,
        LocalDateTime registeredAt,
        List<ProductResponse> products
) {
    public static OrderResponse from(Order savedOrder) {
        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getTotalPrice(),
                savedOrder.getRegisteredAt(),
                savedOrder.getOrderProducts().stream()
                        .map(orderProduct -> ProductResponse.from(orderProduct.getProduct()))
                        .toList()
        );
    }
}
