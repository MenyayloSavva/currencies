package com.bank.currencies.controllers;

import com.bank.currencies.model.Currency;
import com.bank.currencies.services.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @Autowired
    private CurrencyService currencyService;

    @GetMapping("/check/currency")
    public ResponseEntity<String> checkCurrency(@RequestBody Currency currency) {
        currencyService.checkCurrency(currency);
        return ResponseEntity.ok().body("ok");
    }

}