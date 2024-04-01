package com.junho.systemdesign.urlshortener.service;

import com.junho.systemdesign.urlshortener.repository.ShortenUrlRepository;
import com.junho.systemdesign.urlshortener.repository.domain.UrlPair;
import com.junho.systemdesign.urlshortener.service.util.Base62Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShortenUrlService {

    private static final String DEFAULT_CHAR = "*";
    private static final int DEFAULT_HASH_LENGTH = 7;

    private final ShortenUrlRepository shortenUrlRepository;
    private final HashGenerator hashGenerator;

    public boolean isLongUrlExist(String longUrl) {
        return shortenUrlRepository.existsByLongUrl(longUrl);
    }

    public boolean isShortenUrlExist(String shortenUrl) {
        return shortenUrlRepository.existsByShortenUrl(shortenUrl);
    }

    public String getLongUrl(String shortenUrl) {
        UrlPair byShortenUrl = shortenUrlRepository.findByShortenUrl(shortenUrl);
        return byShortenUrl.getLongUrl();
    }

    public String getShortenUrl(String longUrl) {
        UrlPair byLongUrl = shortenUrlRepository.findByLongUrl(longUrl);
        return byLongUrl.getShortenUrl();
    }

    @Transactional
    public String shortenUrlByBase62(String longUrl) {
        UrlPair pair = new UrlPair(longUrl, null);
        Long id = shortenUrlRepository.save(pair).getId(); // snowflake로 생성된 id

        String shortenUrl = Base62Converter.encode(id);
        pair.completeUrl(shortenUrl); // dirty checking으로 DB 저장
        return shortenUrl;
    }

    // 해시 후 충돌 해소 전략 TODO 궁금한점 : bloom filter 사용하면 메모리에서 관리하기에 server가 꺼지면 안된다.현재 요구사항 만족 힘들것이라 추정
    @Transactional
    public String shortenUrlUsingHash(final String longUrl) throws NoSuchAlgorithmException {
        String shortenUrl = hashGenerator.generateHash(longUrl, DEFAULT_HASH_LENGTH);
        String appendCharForHashCollision = longUrl;
        // DB에 있는지 확인 ? 충돌 발생 : DB 저장
        while (shortenUrlRepository.existsByShortenUrl(shortenUrl)) {
            // 충돌 발생 -> 해시값에 충돌 해소될 때 까지 사전에 저한 문자열 해시값에 덧붙인다
            appendCharForHashCollision = appendCharForHashCollision.concat(DEFAULT_CHAR);
            shortenUrl = hashGenerator.generateHash(appendCharForHashCollision, DEFAULT_HASH_LENGTH);
        }
        shortenUrlRepository.save(new UrlPair(longUrl, shortenUrl)); // DB 저장
        return shortenUrl;
    }

}

