package com.junho.systemdesign.urlshortener.service;

import com.junho.systemdesign.urlshortener.repository.ShortenUrlRepository;
import com.junho.systemdesign.urlshortener.repository.domain.UrlPair;
import com.junho.systemdesign.urlshortener.service.util.Base62Converter;
import com.junho.systemdesign.urlshortener.service.util.HexStringConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class ShortenUrlService {

    private static final String HASH_ALGORITHM = "SHA-256";

    private final ShortenUrlRepository shortenUrlRepository;

    public boolean isExist(String longUrl) {
        return shortenUrlRepository.existsByLongUrl(longUrl);
    }

    public String getShortenUrl(String longUrl) {
        UrlPair byLongUrl = shortenUrlRepository.findByLongUrl(longUrl);
        return byLongUrl.getShortenUrl();
    }

    @Transactional
    public String shortenUrlByBase64(String longUrl) {
        // TODO : 임시 id autoincrement를 유일성 보장 ID 생성기로 사용 -> snowflake로 변경 예정
        UrlPair pair = new UrlPair(longUrl, null);
        Long id = shortenUrlRepository.save(pair).getId();

        String shortenUrl = Base62Converter.encode(id);
        pair.completeUrl(shortenUrl); // dirty checking으로 DB 저장
        return shortenUrl;
    }

    // TODO : 해시 후 충돌 해소 전략
    public String shortenUrlByHash(String longUrl) throws NoSuchAlgorithmException {
        MessageDigest instance = MessageDigest.getInstance(HASH_ALGORITHM);
        byte[] digest = instance.digest(longUrl.getBytes());
        String shortenUrl = HexStringConverter.toHexString(digest).substring(0, 7); // 7자리로 자르기
        // DB에 있는지 확인 ? 충돌 발생 : DB 저장
        if (shortenUrlRepository.existsByShortenUrl(shortenUrl)) {
            // 충돌 발생 -> 해시값에 충돌 해소될 때 까지 사전에 저한 문자열 해시값에 덧붙인다
            // TODO 구현 예정
        }
        shortenUrlRepository.save(new UrlPair(longUrl, shortenUrl)); // DB 저장
        return shortenUrl;
    }

}
