package com.example.cafekiosk.spring.api.controller.order.request;

import com.example.cafekiosk.spring.api.service.order.request.OrderCreateServiceRequest;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record OrderCreateRequest(
        @NotEmpty(message = "상품번호 목록은 필수입니다.") List<String> productNumbers
) {

    public OrderCreateServiceRequest toServiceRequest() {
        return new OrderCreateServiceRequest(productNumbers);
    }
}
