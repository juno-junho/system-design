package com.junho.systemdesign.urlshortener.service;

import com.junho.systemdesign.urlshortener.repository.ShortenUrlRepository;
import com.junho.systemdesign.urlshortener.repository.domain.UrlPair;
import com.junho.systemdesign.urlshortener.service.cache.Cache;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@Transactional
class ShortenUrlServiceTest {

    @MockBean
    private HashGenerator hashGenerator;
    @Autowired
    private ShortenUrlService shortenUrlService;
    @Autowired
    private ShortenUrlRepository shortenUrlRepository;
    @Autowired
    private Cache bloomFilter;

    @Test
    void 해시_충돌이_일어나지_않을경우_정상적으로_shortenUrl을_생성한다() throws NoSuchAlgorithmException {
        // given
        String shortenUrl = "1234567";
        String longUrl = "http://www.example.com";
        given(hashGenerator.generateHash(anyString(), anyInt())).willReturn(shortenUrl);

        // when
        String result = shortenUrlService.shortenUrlUsingHash(longUrl);

        // then
        assertThat(result).isEqualTo(shortenUrl);
    }

    @Test
    void 해시_충돌이_일어날_경우_특정문자를_추가하여_충돌을_해결한다() throws NoSuchAlgorithmException {
        // given
        UrlPair urlPair1 = new UrlPair("http://www.example.com", "1234567");
        UrlPair urlPair2 = new UrlPair("http://www.google.com", "1111111");
        shortenUrlRepository.saveAllAndFlush(List.of(urlPair1, urlPair2));

        String firstShortenUrl = "1234567";
        String secondShortUrl = "1111111";
        String thirdShortenUrl = "abcdefg";
        String longUrl = "https://www.tinyurl.com";

        given(hashGenerator.generateHash(eq(longUrl), anyInt())).willReturn(firstShortenUrl);
        given(hashGenerator.generateHash(eq(longUrl.concat("*")), anyInt())).willReturn(secondShortUrl);
        given(hashGenerator.generateHash(eq(longUrl.concat("**")), anyInt())).willReturn(thirdShortenUrl);

        // when
        String result = shortenUrlService.shortenUrlUsingHash(longUrl);

        // then
        assertThat(result).isEqualTo(thirdShortenUrl);
    }

    @Test
    void 블룸_필터를_사용해_해시_충돌이_일어날_경우_DB조회없이_충돌을_해결한다() throws NoSuchAlgorithmException {
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
        String result = shortenUrlService.shortenUrlUsingHashWithBloomFilter(longUrl);

        // then
        assertThat(result).isEqualTo(thirdShortenUrl);
    }

}
