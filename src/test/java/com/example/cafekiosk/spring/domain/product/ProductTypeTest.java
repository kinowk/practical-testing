package com.example.cafekiosk.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTypeTest {

    @DisplayName("재고가 있는 상품 타입인 경우, true를 반환한다")
    @ParameterizedTest
    @ValueSource(strings = {"BOTTLE", "BAKERY"})
    void containsStockTypeWithValidType(ProductType productType) {
        // given
        ProductType type = productType;

        // when
        boolean result = ProductType.containsStockType(type);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("재고가 없는 타입인 경우, false를 반환한다")
    @ParameterizedTest
    @ValueSource(strings = {"HANDMADE"})
    void containsStockTypeWithInvalidType(ProductType productType) {
        // given
        ProductType type = productType;

        // when
        boolean result = ProductType.containsStockType(type);

        // then
        assertThat(result).isFalse();
    }
}