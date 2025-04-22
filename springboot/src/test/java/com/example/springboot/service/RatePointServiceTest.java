package com.example.springboot.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RatePointServiceTest {

    @InjectMocks
    private RatePointService ratePointService;

    @Test
    @DisplayName("Earn 100 points per 10,000 won")
    void earnHundredPerTenThousand() {
        // given
        final int price = 10000;

        // when
        final int result = ratePointService.calculateAmount(price);

        // then
        assertThat(result).isEqualTo(100);
    }

    @ParameterizedTest
    @MethodSource("calculateOnePercent")
    @DisplayName("Earn 1% of amount")
    void earnOnePercentOfPrice(final int amount, final int expectedValue) {
        // given

        // when
        final int result = ratePointService.calculateAmount(amount);

        // then
        assertThat(result).isEqualTo(expectedValue);
    }

    private static Stream<Arguments> calculateOnePercent() {
        return Stream.of(
                Arguments.of(10000, 100),
                Arguments.of(20000, 200),
                Arguments.of(12345, 123));
    }
}
