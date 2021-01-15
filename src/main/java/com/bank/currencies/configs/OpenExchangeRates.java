package com.bank.currencies.configs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpenExchangeRates {
    private String apiUrl;
    private String appId;
    private String baseCurrency;
    private Currencies currencies;
    private Historical historical;
}
