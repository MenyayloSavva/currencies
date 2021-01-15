package com.bank.currencies.external;

import com.bank.currencies.external.responses.GiphyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "giphy-api", url = "${giphy.api_url}")
public interface GiphyFeignClient {

    @GetMapping("/gifs/search")
    GiphyResponse getRandomGif(@RequestParam(name = "api_key") String apiKey,
                               @RequestParam String q,
                               @RequestParam Integer limit,
                               @RequestParam Integer offset,
                               @RequestParam(name = "random_id") String randomId);
}