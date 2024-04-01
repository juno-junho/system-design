package com.junho.systemdesign.urlshortener.controller;

import com.junho.systemdesign.urlshortener.controller.dto.UrlRequest;
import com.junho.systemdesign.urlshortener.controller.dto.UrlResponse;
import com.junho.systemdesign.urlshortener.service.ShortenUrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ShortenUrlController {

    private final ShortenUrlService shortenUrlService;

    @PostMapping("/api/v1/data/shorten")
    public UrlResponse shortenUrl(@Valid @RequestBody UrlRequest urlRequest) { // todo valid 확인
        log.debug("url inserted : {}", urlRequest.longUrl());
        // TODO : 1. url DB에 존재? 반환 : 새로운 id 생성 -> DB에 저장 -> 반환  =>DONE
        String longUrl = urlRequest.longUrl();

        if (shortenUrlService.isLongUrlExist(longUrl)) {
            String shortenUrl = shortenUrlService.getShortenUrl(longUrl);
            return new UrlResponse(longUrl, shortenUrl);
        }
        String shortenUrl = shortenUrlService.shortenUrlByBase62(urlRequest.longUrl());

        log.debug("shorten url : {}", shortenUrl);
        return new UrlResponse(longUrl, shortenUrl);
    }

    @GetMapping("/{shortenUrl}")
    @ResponseStatus(HttpStatus.FOUND) // 트래픽 분석을 위해 301대신 302 반환
    public String getShortenUrl(@PathVariable String shortenUrl) {
        // TODO: DB 에 있으면 redirect
        if (shortenUrlService.isShortenUrlExist(shortenUrl)) {
            // TODO long URL로 redirect -> 방법 찾아보기
            return "redirect:" + shortenUrlService.getLongUrl(shortenUrl);
        }
        // TODO : 없으면 예외 페이지로 이동
        return "shorten";
    }

}
