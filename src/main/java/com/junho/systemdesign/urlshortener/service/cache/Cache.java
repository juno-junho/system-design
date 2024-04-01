package com.junho.systemdesign.urlshortener.service.cache;

public interface Cache {

    boolean mightContain(String url);

    void put(String url);

}
