package com.junho.systemdesign.urlshortener.repository.domain;

import com.junho.systemdesign.global.entity.BaseTimeEntity;
import com.junho.systemdesign.urlshortener.service.SnowFlakeGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Entity
public class UrlPair extends BaseTimeEntity {

    @Id
    @GeneratedValue(generator = "global_seq_id")
    @GenericGenerator(name = "global_seq_id", type = SnowFlakeGenerator.class)
    private Long id;

    protected UrlPair() {}

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
