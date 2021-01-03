package com.bank.currencies.services;

import com.bank.currencies.external_data.open_exchange_rates.AllCurrenciesRequest;
import com.bank.currencies.external_data.open_exchange_rates.OpenExchangeRatesFeignClient;
import com.bank.currencies.model.Currency;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CurrencyService {

@Autowired
private OpenExchangeRatesFeignClient openExchangeRatesFeignClient;

    public void checkCurrency(Currency currency) {
        if (StringUtils.hasText(currency.getCode())) {
            System.out.println("checkCurrency has started");

            // 1. Check Entered value on the availability of the currencies list. If no, throw Exception "No such currency".
            ResponseEntity<JSONObject> responseEntity = openExchangeRatesFeignClient.getCurrenciesList(new AllCurrenciesRequest());
            System.out.println(responseEntity.getBody());

            //JSONObject jsonObject = new JSONObject(openExchangeRatesFeignClient.getCurrenciesList(allCurrenciesRequest).getBody());
            //System.out.println(jsonObject);

            // 2. Get today's rate and yesterday's. Compare.
            // 3. If today's equal or higher get Rich Gif.
            // 4. If today's lower get Broke Gif.

        } else {
        throw new RuntimeException("Request should contain \"code\"");
        }
    }
}
