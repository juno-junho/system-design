package com.junho.systemdesign.urlshortener.service;

import com.junho.systemdesign.urlshortener.service.bloomfilter.MemoryBloomFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@Transactional
class ShortenUrlGeneratorTest {

    @Autowired
    private ShortenUrlGenerator shortenUrlGenerator;
    @MockBean
    private HashGenerator hashGenerator;
    @Autowired
    private MemoryBloomFilter bloomFilter;

    @Test
    void default_해시값과_캐릭터값_주입에_성공한다() {
        // when
        String defaultChar = shortenUrlGenerator.getDefaultChar();
        int defaultHashLength = shortenUrlGenerator.getDefaultHashLength();

        // then
        assertThat(defaultChar).isEqualTo("*");
        assertThat(defaultHashLength).isEqualTo(7);
    }

    @Test
    void 블룸_필터를_사용해_해시_충돌이_일어날_경우_DB조회없이_충돌을_해결한다() {
        // given
        bloomFilter.put("1234567");
        bloomFilter.put("1111111");

        String firstShortenUrl = "1234567"; // 충돌 발생 1회
        String secondShortUrl = "1111111"; // 충돌 발생 2회
        String thirdShortenUrl = "abcdefg";
        String longUrl = "https://www.tinyurl.com";

        given(hashGenerator.generateHash(eq(longUrl), anyInt())).willReturn(firstShortenUrl);
        given(hashGenerator.generateHash(eq(longUrl.concat("*")), anyInt())).willReturn(secondShortUrl);
        given(hashGenerator.generateHash(eq(longUrl.concat("**")), anyInt())).willReturn(thirdShortenUrl);

        // when
        String result = shortenUrlGenerator.generateUniqueShortenUrl(longUrl);

        // then
        assertThat(result).isEqualTo(thirdShortenUrl);
    }

}
