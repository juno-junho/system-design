package com.junho.systemdesign.urlshortener.service;

import com.junho.systemdesign.urlshortener.repository.ShortenUrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShortenUrlService {

    private final ShortenUrlRepository shortenUrlRepository;

}
