package com.junho.systemdesign.urlshortener.validation;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class UrlValidatorTest {

    private final UrlValidator validator = new UrlValidator();

    @ParameterizedTest
    @ValueSource(strings = {
            "http://www.example.com",
            "https://www.example.com",
            "http://example.com/path/to/page?name=ferret&color=purple",
            "www.example.com"})
    void 유효한_url이면_true를_반환한다(String url) {
        // then
        assertThat(validator.isValid(url, null)).isTrue();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
            "htt://www.example.com",
            "http://",
            "empty",
            "       "
    })
    void 유효하지않은_url이면_false를_반환한다(String url) {
        // then
        assertThat(validator.isValid(url, null)).isFalse();
    }

}
