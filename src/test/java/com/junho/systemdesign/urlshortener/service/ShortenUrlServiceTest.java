package com.junho.systemdesign.urlshortener.service;

import com.junho.systemdesign.global.config.ShortenUrlProperties;
import com.junho.systemdesign.urlshortener.repository.ShortenUrlRepository;
import com.junho.systemdesign.urlshortener.repository.domain.UrlPair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@Transactional
class ShortenUrlServiceTest {

    @MockBean
    private ShortenUrlGenerator shortenUrlGenerator;
    @Autowired
    private ShortenUrlService shortenUrlService;
    @Autowired
    private ShortenUrlRepository shortenUrlRepository;
    @Autowired
    private ShortenUrlProperties shortenUrlProperties;

    @Test
    void http프로토콜이_붙은_동일한_Longurl이_DB에_존재하면_true를_반환한다() {
        // given
        String protocolIncludedLongUrl = "http://www.example.com";
        String protocolExcludedLongUrl = "www.example.com";
        UrlPair urlPair = new UrlPair(protocolIncludedLongUrl, "1234567", shortenUrlProperties);
        shortenUrlRepository.saveAndFlush(urlPair);

        // when
        boolean result = shortenUrlService.isLongUrlExist(protocolExcludedLongUrl);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void http프로토콜이_아닌_다른프로토콜일_경우_같은_url이라도_false를_반환한다() {
        // given
        String httpProtocolIncludedLongUrl = "http://www.example.com";
        String httpsProtocolIncludedLongUrl = "https://www.example.com";
        UrlPair urlPair = new UrlPair(httpProtocolIncludedLongUrl, "1234567", shortenUrlProperties);
        shortenUrlRepository.saveAndFlush(urlPair);

        // when
        boolean result = shortenUrlService.isLongUrlExist(httpsProtocolIncludedLongUrl);

        // then
        assertThat(result).isFalse();
    }


    @Test
    void 해시_충돌이_일어나지_않을경우_정상적으로_shortenUrl을_생성한다() {
        // given
        String shortenUrl = "1234567";
        String longUrl = "http://www.example.com";
        given(shortenUrlGenerator.generateHash(anyString())).willReturn(shortenUrl);

        // when
        String result = shortenUrlService.generateShortenUrlUsingHash(longUrl);

        // then
        assertThat(result).isEqualTo(shortenUrl);
    }

    @Test
    void 해시_충돌이_일어날_경우_특정문자를_추가하여_충돌을_해결한다() {
        // given
        UrlPair urlPair1 = new UrlPair("http://www.example.com", "1234567", shortenUrlProperties);
        UrlPair urlPair2 = new UrlPair("http://www.google.com", "1111111", shortenUrlProperties);
        shortenUrlRepository.saveAllAndFlush(List.of(urlPair1, urlPair2));

        String firstShortenUrl = "1234567";
        String secondShortUrl = "1111111";
        String thirdShortenUrl = "abcdefg";
        String longUrl = "https://www.tinyurl.com";

        given(shortenUrlGenerator.generateHash(eq(longUrl))).willReturn(firstShortenUrl);
        given(shortenUrlGenerator.generateNewHash(eq(longUrl), eq(1))).willReturn(firstShortenUrl);
        given(shortenUrlGenerator.generateNewHash(eq(longUrl), eq(2))).willReturn(secondShortUrl);
        given(shortenUrlGenerator.generateNewHash(eq(longUrl), eq(3))).willReturn(thirdShortenUrl);

        // when
        String result = shortenUrlService.generateShortenUrlUsingHash(longUrl);

        // then
        assertThat(result).isEqualTo(thirdShortenUrl);
    }

}
