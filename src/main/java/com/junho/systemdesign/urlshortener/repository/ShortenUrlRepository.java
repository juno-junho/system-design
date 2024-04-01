package com.junho.systemdesign.urlshortener.repository;

import com.junho.systemdesign.urlshortener.repository.domain.UrlPair;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShortenUrlRepository extends JpaRepository<UrlPair, Long> {

    boolean existsByLongUrl(String longUrl);

    boolean existsByShortenUrl(String shortenUrl);

    UrlPair findByLongUrl(String longUrl);

    UrlPair findByShortenUrl(String shortenUrl);
}
