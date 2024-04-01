package com.junho.systemdesign.urlshortener.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HashGeneratorTest {

    @ParameterizedTest
    @ValueSource(ints = {1, 7, 31})
    void 올바른_길이의_해시길이_설정시_정상(int hashLength) throws NoSuchAlgorithmException {
        // given
        HashGenerator hashGenerator = new HashGenerator();
        String longUrl = "http://www.example.com";

        // when
        String result = hashGenerator.generateHash(longUrl, hashLength);

        // then
        assertThat(result).hasSize(hashLength);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -10, 32})
    void 올바르지_않은_해시_길이_설정시_예외(int hashLength) throws NoSuchAlgorithmException {
        // given
        HashGenerator hashGenerator = new HashGenerator();
        String longUrl = "http://www.example.com";

        // when, then
        assertThatThrownBy(() -> hashGenerator.generateHash(longUrl, hashLength))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
