package com.example.cafekiosk.spring.api.controller.product.request;

import com.example.cafekiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import com.example.cafekiosk.spring.domain.product.ProductSellingStatus;
import com.example.cafekiosk.spring.domain.product.ProductType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductCreateRequest(
        @NotNull(message = "상품타입은 필수입니다.") ProductType type,
        @NotNull(message = "상품 판매상태는 필수입니다.") ProductSellingStatus sellingStatus,
        @NotBlank(message = "상품명은 필수입니다.") String name,
        @PositiveOrZero(message = "가격은 0원 이상이어야 됩니다.") int price
) {

    public ProductCreateServiceRequest toServiceRequest() {
        return new ProductCreateServiceRequest(type, sellingStatus, name, price);
    }
}
