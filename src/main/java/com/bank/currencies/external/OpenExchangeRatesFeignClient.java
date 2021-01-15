package com.bank.currencies.external;

import com.bank.currencies.external.configuration.FeignConfiguration;
import com.bank.currencies.external.responses.OERHistResponse;
import com.bank.currencies.parameters.Parameters;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(value = "open-exchange-rates-api", url = Parameters.OER_API_URL, configuration = FeignConfiguration.class)
public interface OpenExchangeRatesFeignClient {

        @GetMapping("/currencies.json")
        Map<String, String> getCurrencyList(@RequestParam Boolean prettyprint,
                                            @RequestParam(name = "show_alternative") Boolean showAlternative,
                                            @RequestParam(name = "show_inactive") Boolean showInactive);

        @GetMapping("/historical/{date}.json")
        OERHistResponse getCurrencyRate(@PathVariable String date,
                                        @RequestParam(name = "app_id") String AppId,
                                        @RequestParam String symbols,
                                        @RequestParam(name = "show_alternative") Boolean showAlternative,
                                        @RequestParam Boolean prettyprint);
}