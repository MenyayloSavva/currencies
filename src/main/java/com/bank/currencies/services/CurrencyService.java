package com.bank.currencies.services;

import com.bank.currencies.model.Currency;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CurrencyService {

    public void checkCurrency(Currency currency) {
        if (StringUtils.hasText(currency.getCode())) {
            System.out.println("checkCurrency has started");

            // 1. Check Entered value on the availability of the currencies list. If no, throw Exception "No such currency".
            // 2. Get today's rate and yesterday's. Compare.
            // 3. If today's equal or higher get Rich Gif.
            // 4. If today's lower get Broke Gif.

        } else {
        throw new RuntimeException("Request should contain \"code\"");
        }
    }
}
