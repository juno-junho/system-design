package com.junho.systemdesign.urlshortener.controller;

import com.junho.systemdesign.urlshortener.controller.dto.UrlRequest;
import com.junho.systemdesign.urlshortener.service.ShortenUrlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ShortenUrlController {

    private final ShortenUrlService shortenUrlService;

    @PostMapping("/data/shorten")
    public String shortenUrl(@RequestBody UrlRequest urlRequest) {
        log.debug("url inserted : {}", urlRequest.longUrl());
        // TODO : 1. url DB에 존재? 반환 : 새로운 id 생성 -> DB에 저장 -> 반환
        return "shorten";
    }

    @GetMapping("/{shortenUrl}")
    public String getShortenUrl(@PathVariable String shortenUrl) {
        // TODO: DB 에 있으면 redirect
        // TODO : 없으면 예외 페이지
        return "shorten";
    }
}
