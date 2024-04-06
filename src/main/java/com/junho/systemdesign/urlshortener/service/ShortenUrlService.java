package com.junho.systemdesign.urlshortener.service;

import com.junho.systemdesign.global.config.ShortenUrlProperties;
import com.junho.systemdesign.urlshortener.repository.ShortenUrlRepository;
import com.junho.systemdesign.urlshortener.repository.domain.UrlPair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShortenUrlService {

    private final ShortenUrlRepository shortenUrlRepository;
    private final ShortenUrlProperties shortenUrlProperties;
    private final ShortenUrlGenerator shortenUrlGenerator;

    public boolean isShortenUrlExist(String shortenUrl) {
        return shortenUrlRepository.existsByShortenUrl(shortenUrl);
    }

    public boolean isLongUrlExist(String longUrl) {
        String verifiedLongUrl = UrlPair.verified(longUrl, shortenUrlProperties);
        return shortenUrlRepository.existsByLongUrl(verifiedLongUrl);
    }

    @Transactional
    public String getLongUrl(String shortenUrl) {
        UrlPair url = shortenUrlRepository.findByShortenUrl(shortenUrl)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 URL"));
        url.increaseViewCount(); // view 수 증가
        return url.getLongUrl(); // dirty checking
    }

    public String getShortenUrl(String longUrl) {
        UrlPair url = shortenUrlRepository.findByLongUrl(longUrl)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 URL"));
        return url.getShortenUrl();
    }

    // Base62 encoding
    @Transactional
    public String generateShortenUrlUsingBase62(String longUrl) {
        UrlPair pair = new UrlPair(longUrl, null, shortenUrlProperties);
        Long id = shortenUrlRepository.save(pair).getId(); // snowflake로 생성된 id
        String shortenUrl = shortenUrlGenerator.getBase62String(id);
        pair.completeUrl(shortenUrl); // dirty checking으로 DB 저장
        return shortenUrl;
    }

    // 해시 후 충돌 해소 전략 : 해시값에 충돌 해소될 때 까지 사전에 저한 문자열 해시값에 덧붙임.
    @Transactional
    public String generateShortenUrlUsingHash(final String longUrl) {
        String shortenUrl = shortenUrlGenerator.generateHash(longUrl);
        int collisionCount = 0;
        while (shortenUrlRepository.existsByShortenUrl(shortenUrl)) {// DB에 있는지 확인 ? 충돌 발생 : DB 저장
            shortenUrl = shortenUrlGenerator.generateNewHash(longUrl, ++collisionCount);
        }
        shortenUrlRepository.save(new UrlPair(longUrl, shortenUrl, shortenUrlProperties)); // DB 저장
        return shortenUrl;
    }

    // bloom filter 사용하면 DB 조회 오버헤드 줄여 성능 개선 가능. but 메모리에서 관리하기에 server가 꺼지면 안된다.현재 요구사항 만족 힘들것이라 추정
    @Transactional
    public String generateShortenUrlUsingHashWithBloomFilter(final String longUrl) {
        String shortenUrl = shortenUrlGenerator.generateUniqueShortenUrl(longUrl);
        shortenUrlRepository.save(new UrlPair(longUrl, shortenUrl, shortenUrlProperties)); // DB 저장
        return shortenUrl;
    }

}
