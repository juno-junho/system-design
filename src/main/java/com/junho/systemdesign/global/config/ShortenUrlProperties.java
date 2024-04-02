package com.junho.systemdesign.global.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "shorten-url")
public record ShortenUrlProperties(
        List<String> allowedProtocols
) {

    public String getDefaultProtocol() {
        return allowedProtocols.get(0); // http://
    }

}
