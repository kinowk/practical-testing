package com.example.cafekiosk.spring.domain.stock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StockTest {

    @DisplayName("재고의 수량이 주어진 수량보다 적은지(less than) 경우 true를 반환한다.")
    @Test
    void isQuantityLessThanWithLessThanQuantity() {
        // given
        int quantity = 2;
        Stock stock = Stock.create("001", 1);

        // when
        boolean result = stock.isQuantityLessThan(quantity);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("재고의 수량이 주어진 수량보다 많거나 같은(greater than or equal to) 경우 false를 반환한다.")
    @Test
    void isQuantityLessThanWithGreaterThanOrEqualToQuantity() {
        // given
        int quantity = 2;
        Stock stock = Stock.create("001", 2);

        // when
        boolean result = stock.isQuantityLessThan(quantity);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("재고의 수량을 주어진 개수만큼 차감할 수 있다.")
    @Test
    void deductQuantity() {
        // given
        int quantity = 3;
        Stock stock = Stock.create("001", 3);

        // when
        stock.deductQuantity(quantity);

        // then
        assertThat(stock.getQuantity()).isZero();
    }

    @DisplayName("재고의 수량보다 큰(greater than) 개수만큼 차감하는 경우 예외가 발생한다.")
    @Test
    void throwsException_whenDeductQuantityGreaterThanQuantity() {
        // given
        int quantity = 4;
        Stock stock = Stock.create("001", 3);

        // when & then
        assertThatThrownBy(() -> stock.deductQuantity(quantity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("재고가 부족합니다.");
    }

    @DisplayName("재고 차감 시나리오(DynamicTest)")
    @TestFactory
    Collection<DynamicTest> stockDeductionDynamicTest() {
        // given
        Stock stock = Stock.create("001", 1);

        return List.of(
                DynamicTest.dynamicTest("재고를 주어진 개수만큼 차감할 수 있다.", () -> {
                    // given
                    int quantity = 1;

                    // when
                    stock.deductQuantity(quantity);

                    // then
                    assertThat(stock.getQuantity()).isZero();
                }),
                DynamicTest.dynamicTest("재고보다 많은 수의 개수로 차감하는 경우 예외가 발생한다.", () -> {
                    // given
                    int quantity = 1;

                    // when & then
                    assertThatThrownBy(() -> stock.deductQuantity(quantity))
                            .isInstanceOf(IllegalArgumentException.class);
                })
        );
    }
}