package com.junho.systemdesign.urlshortener.service.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class Base62ConverterTest {

    @ParameterizedTest
    @MethodSource("encode_test_cases")
    void base64_encode(Long input, String expected) {
        // when
        String result = Base62Converter.encode(input);

        // then
        assertThat(result).isEqualTo(expected);
    }

    static Stream<Arguments> encode_test_cases() {
        return Stream.of(
                Arguments.of(123456789L, "8M0kX"),
                Arguments.of(1L, "1"),
                Arguments.of(62L, "10"),
                Arguments.of(3844L, "100"),
                Arguments.of(238328L, "1000"),
                Arguments.of(14776336L, "10000"),
                Arguments.of(916132832L, "100000"),
                Arguments.of(56800235584L, "1000000"),
                Arguments.of(3521614606208L, "10000000"),
                Arguments.of(218340105584896L, "100000000"),
                Arguments.of(13537086546263552L, "1000000000"),
                Arguments.of(839299365868340224L, "10000000000")
        );
    }


}
