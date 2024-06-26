package com.junho.systemdesign.urlshortener.controller;

import com.junho.systemdesign.urlshortener.controller.dto.UrlRequest;
import com.junho.systemdesign.urlshortener.controller.dto.UrlResponse;
import com.junho.systemdesign.urlshortener.service.ShortenUrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ShortenUrlController {

    private final ShortenUrlService shortenUrlService;

    @PostMapping("/api/v1/data/shorten")
    public UrlResponse shortenUrl(@Valid @RequestBody UrlRequest urlRequest) throws NoSuchAlgorithmException {
        String longUrl = urlRequest.longUrl();
        // Long URL DB에 존재 ? 반환 : shorten URL 생성 -> DB에 저장 -> 반환
        if (shortenUrlService.isLongUrlExist(longUrl)) {
            String shortenUrl = shortenUrlService.getShortenUrl(longUrl);
            return new UrlResponse(longUrl, shortenUrl);
        }
        String shortenUrl = shortenUrlService.generateShortenUrlUsingBase62(longUrl);
        return new UrlResponse(longUrl, shortenUrl);
    }

    @GetMapping("/{shortenUrl}")
    public ResponseEntity<String> getShortenUrl(@PathVariable(name = "shortenUrl") String shortenUrl) {
        if (shortenUrlService.isShortenUrlExist(shortenUrl)) { // DB 에 있으면 redirect
            String longUrl = shortenUrlService.getLongUrl(shortenUrl);
            return ResponseEntity.status(HttpStatus.FOUND) // 트래픽 분석을 위해 301대신 302 반환
                    .header(HttpHeaders.LOCATION, longUrl)
                    .build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // DB에 존재하지 않으면 error
    }

//    @GetMapping("/api/v1/data/shorten")
//    public List<UrlResponse> getAllShortenUrl() {
//        return shortenUrlService.getAllShortenUrl();
//    }

}
