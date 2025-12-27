package com.example.cafekiosk.spring.api.controller.order.request;

import java.time.LocalDateTime;
import java.util.List;

public record OrderCreateRequest(
    List<String> productNumbers,
    LocalDateTime registeredAt
) {

}
