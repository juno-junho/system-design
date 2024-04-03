package com.junho.systemdesign.urlshortener.service;

import com.junho.systemdesign.urlshortener.service.bloomfilter.MemoryBloomFilter;
import com.junho.systemdesign.urlshortener.service.util.Base62Converter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShortenUrlGenerator {

    @Getter
    @Value("${shorten-url.default-char}")
    private String defaultChar;
    @Getter
    @Value("${shorten-url.default-hash-length}")
    private int defaultHashLength;

    private final HashGenerator hashGenerator;
    private final MemoryBloomFilter bloomFilter;

    public String generateUniqueShortenUrl(final String longUrl) {
        String shortenUrl = generateHash(longUrl);
        int collisionCount = 0;
        // bloom filter에 있는지 확인 ? 충돌 발생 -> 충돌 해소 : DB 저장
        while (bloomFilter.mightContain(shortenUrl)) {
            // 충돌 가능성 있음
            shortenUrl = generateNewHash(longUrl, ++collisionCount);
        }
        bloomFilter.put(shortenUrl); // bloom filter 저장
        return shortenUrl;
    }

    public String getBase62String(Long id) {
        return Base62Converter.encode(id);
    }

    public String generateHash(final String longUrl) {
        return hashGenerator.generateHash(longUrl, defaultHashLength);
    }

    public String generateNewHash(final String longUrl, int collisionCount) {
        String charAppendedLongUrl = longUrl.concat(defaultChar.repeat(collisionCount));
        return hashGenerator.generateHash(charAppendedLongUrl, defaultHashLength);
    }

}
