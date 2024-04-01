package com.junho.systemdesign.study;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;

import static org.assertj.core.api.Assertions.assertThat;

public class BloomFilterTest {

    @Test
    void 블룸필터는_거짓_음성을_발생하지_않는다() {
        BloomFilter<CharSequence> bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()), 1000);
        bloomFilter.put("test");

        boolean mightContain = bloomFilter.mightContain("no_test");

        if (!mightContain) {
            System.out.println("확실하게 존재하지 않는 것");
            assertThat(mightContain).isFalse();
        }
    }

}
