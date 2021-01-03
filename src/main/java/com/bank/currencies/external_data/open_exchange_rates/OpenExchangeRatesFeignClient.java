package com.bank.currencies.external_data.open_exchange_rates;

import com.bank.currencies.dictionary.Parameters;
import org.json.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@FeignClient(value = "open-exchange-rates-api", url = Parameters.OPEN_EXCHANGE_RATES_API_URL)
public interface OpenExchangeRatesFeignClient {
        @RequestMapping(method = RequestMethod.GET, value = "/currencies.json")
        ResponseEntity<JSONObject> getCurrenciesList(@RequestBody AllCurrenciesRequest allCurrenciesRequest);
}