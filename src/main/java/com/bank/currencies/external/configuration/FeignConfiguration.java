package com.bank.currencies.external.configuration;

import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.context.annotation.Bean;

public class FeignConfiguration {
    @Bean
    public Decoder feignDecoder() {
        return new JacksonDecoder();
    }

    @Bean
    public Encoder feignEncoder() {
        return new JacksonEncoder();
    }
}
