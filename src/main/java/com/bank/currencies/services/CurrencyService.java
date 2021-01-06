package com.bank.currencies.services;

import com.bank.currencies.parameters.Parameters;
import com.bank.currencies.external.OpenExchangeRatesFeignClient;
import com.bank.currencies.model.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@Service
public class CurrencyService {

@Autowired
private OpenExchangeRatesFeignClient openExchangeRatesFeignClient;

    public ResponseEntity checkCurrency(Currency currency) {
        System.out.println("CurrencyService has started");

        // 1. Check if Request has "code" in the body
        if (!StringUtils.hasText(currency.getCode())) {
            return ResponseEntity.badRequest().body("Request should contain \"code\"");
        }

        // 2. Check if "code" is in the list of the available currencies from "openexchangerates.org"
        ResponseEntity responseEntity = openExchangeRatesFeignClient.getCurrenciesList(Parameters.OER_CURR_PRETTYPRINT,
                Parameters.OER_CURR_SHOW_ALTERNATIVE, Parameters.OER_CURR_SHOW_INACTIVE);
        HashMap<String, Object> responseBody = (HashMap) responseEntity.getBody();
        if (!responseBody.containsKey(currency.getCode())) {
            return ResponseEntity.badRequest().body("Request should contain proper \"code\". For example, \"USD\"");
        }

        // 3. Get today's
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        responseEntity = openExchangeRatesFeignClient.getCurrencyRate(today.format(formatter), Parameters.OER_APP_ID,
                currency.getCode(), Parameters.OER_HIST_SHOW_ALTERNATIVE, Parameters.OER_HIST_PRETTYPRINT);
        responseBody = (HashMap) responseEntity.getBody();
        Double todaysRate = (Double) ((HashMap) responseBody.get("rates")).get(currency.getCode());

        // and yesterday's rates
        LocalDate yesterday = LocalDate.now().minusDays(1);
        responseEntity = openExchangeRatesFeignClient.getCurrencyRate(yesterday.format(formatter), Parameters.OER_APP_ID,
                currency.getCode(), Parameters.OER_HIST_SHOW_ALTERNATIVE, Parameters.OER_HIST_PRETTYPRINT);
        responseBody = (HashMap) responseEntity.getBody();
        Double yesterdaysRate = (Double) ((HashMap) responseBody.get("rates")).get(currency.getCode());

        // 4. If today's rate equal or higher get Rich Gif. Other way get Broke Gif.
        Object gif = new Object();
        if (todaysRate >= yesterdaysRate) {
            System.out.println("RICH");
        } else {
            System.out.println("BROKE");
        }



        return ResponseEntity.ok().body("ok");
    }
}
