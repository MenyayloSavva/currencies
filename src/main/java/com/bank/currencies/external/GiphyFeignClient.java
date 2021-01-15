package com.bank.currencies.external;

import com.bank.currencies.external.configuration.FeignConfiguration;
import com.bank.currencies.external.responses.GiphyResponse;
import com.bank.currencies.parameters.Parameters;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "giphy-api", url = Parameters.GIPHY_API_URL, configuration = FeignConfiguration.class)
public interface GiphyFeignClient {

    @GetMapping("/gifs/search")
    GiphyResponse getRandomGif(@RequestParam(name = "api_key") String apiKey,
                               @RequestParam String q,
                               @RequestParam Integer limit,
                               @RequestParam Integer offset,
                               @RequestParam(name = "random_id") String randomId);
}