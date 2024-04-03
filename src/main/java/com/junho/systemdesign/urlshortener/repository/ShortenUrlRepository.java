package com.junho.systemdesign.urlshortener.repository;

import com.junho.systemdesign.urlshortener.repository.domain.UrlPair;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShortenUrlRepository extends JpaRepository<UrlPair, Long> {

    boolean existsByLongUrl(String longUrl);

    boolean existsByShortenUrl(String shortenUrl);

    Optional<UrlPair> findByLongUrl(String longUrl);

    Optional<UrlPair> findByShortenUrl(String shortenUrl);
}
