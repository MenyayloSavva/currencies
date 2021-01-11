package com.bank.currencies.external;

import com.bank.currencies.parameters.Parameters;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "open-exchange-rates-api", url = Parameters.OER_API_URL)
public interface OpenExchangeRatesFeignClient {

        @GetMapping("/currencies.json")
        ResponseEntity<Object> getCurrencyList(@RequestParam Boolean prettyprint,
                                               @RequestParam(name = "show_alternative") Boolean showAlternative,
                                               @RequestParam(name = "show_inactive") Boolean showInactive);

        @GetMapping("/historical/{date}.json")
        ResponseEntity<Object> getCurrencyRate(@PathVariable String date,
                                               @RequestParam(name = "app_id") String AppId,
                                               @RequestParam String symbols,
                                               @RequestParam(name = "show_alternative") Boolean showAlternative,
                                               @RequestParam Boolean prettyprint);
}