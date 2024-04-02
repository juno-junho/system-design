package com.junho.systemdesign.urlshortener.service.bloomfilter;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

@Component
public class MemoryBloomFilter {

    private final BloomFilter<String> bloomFilter =
            BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()), 1000000);

    public boolean mightContain(String url) {
        return bloomFilter.mightContain(url);
    }

    public void put(String url) {
        bloomFilter.put(url);
    }

}
