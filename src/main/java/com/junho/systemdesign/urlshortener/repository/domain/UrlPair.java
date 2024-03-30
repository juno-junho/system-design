package com.junho.systemdesign.urlshortener.repository.domain;

import com.junho.systemdesign.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity
public class UrlPair extends BaseTimeEntity {

    protected UrlPair() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String longUrl;

    @Column(unique = true)
    private String shortenUrl;

    public UrlPair(String longUrl, String shortenUrl) {
        this.longUrl = longUrl;
        this.shortenUrl = shortenUrl;
    }

    public void completeUrl(String shortenUrl) {
        this.shortenUrl = shortenUrl;
    }

}
