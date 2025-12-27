package com.example.cafekiosk.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTypeTest {

    @DisplayName("재고가 있는 상품 타입인 경우, true를 반환한다")
    @Test
    void containsStockTypeWithValidType() {
        // given
        ProductType type = ProductType.BOTTLE;

        // when
        boolean result = ProductType.containsStockType(type);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("재고가 없는 타입인 경우, false를 반환한다")
    @Test
    void containsStockTypeWithInvalidType() {
        // given
        ProductType type = ProductType.HANDMADE;

        // when
        boolean result = ProductType.containsStockType(type);

        // then
        assertThat(result).isFalse();
    }
}