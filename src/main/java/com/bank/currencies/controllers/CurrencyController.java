package com.bank.currencies.controllers;

import com.bank.currencies.model.Currency;
import com.bank.currencies.services.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @PostMapping("/evaluate/currency")
    public @ResponseBody ResponseEntity evaluateCurrency(@RequestBody Currency request) {
        return currencyService.evaluateCurrency(request);
    }

}