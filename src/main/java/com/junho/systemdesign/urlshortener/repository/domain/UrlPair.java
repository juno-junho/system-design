package com.junho.systemdesign.urlshortener.repository.domain;

import com.junho.systemdesign.global.config.ShortenUrlProperties;
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

    // TODO 조회될때 마다 하나씩 올려서 -> 자주 쓰는것 캐시 넣기 // count 수가 x 이상인것은 캐싱, 기간 설정
    private int viewCount;

    public UrlPair(String longUrl, String shortenUrl, ShortenUrlProperties properties) {
        this.longUrl = ensureUrlHasProtocol(longUrl, properties);
        this.shortenUrl = shortenUrl;
    }

    public static String verified(String longUrl, ShortenUrlProperties shortenUrlProperties) {
        UrlPair urlPair = new UrlPair(longUrl, null, shortenUrlProperties);
        return urlPair.longUrl;
    }

    public void completeUrl(String shortenUrl) {
        this.shortenUrl = shortenUrl;
    }

    public void increaseViewCount() {
        this.viewCount++;
    }

    private String ensureUrlHasProtocol(final String longUrl, final ShortenUrlProperties properties) {
        return properties.allowedProtocols().stream()
                .filter(longUrl::startsWith)
                .findFirst()
                .map(protocol -> longUrl) // 프로토콜로 시작한다면 원래 URL을 반환
                .orElseGet(() -> properties.getDefaultProtocol().concat(longUrl)); // 시작하지 않는다면 기본 프로토콜을 추가
    }

}
