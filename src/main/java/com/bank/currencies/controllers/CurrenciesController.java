package com.bank.currencies.controllers;

import com.bank.currencies.api.CurrencyRequest;
import com.bank.currencies.services.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrenciesController {

    @Autowired
    private CurrencyService currencyService;

    @PostMapping("/evaluate/currency")
    public ResponseEntity<String> evaluateCurrency(@RequestBody CurrencyRequest currencyRequest) {
        return currencyService.evaluateCurrency(currencyRequest);
    }

}