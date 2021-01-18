package com.bank.currencies.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class YAMLConfig {
    private String name;
    private String environment;
    private OpenExchangeRates openExchangeRates;
    private Giphy giphy;
}