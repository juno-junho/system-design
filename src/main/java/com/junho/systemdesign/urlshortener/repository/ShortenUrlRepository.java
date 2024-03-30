package com.junho.systemdesign.urlshortener.repository;

import com.junho.systemdesign.urlshortener.repository.domain.ShortenUrl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShortenUrlRepository extends JpaRepository<ShortenUrl, Long> {

}
