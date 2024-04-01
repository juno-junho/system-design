package com.junho.systemdesign.urlshortener.controller.dto;

import com.junho.systemdesign.urlshortener.validation.ValidUrl;

public record UrlRequest(@ValidUrl String longUrl) {

}
