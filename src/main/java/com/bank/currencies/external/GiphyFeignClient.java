package com.bank.currencies.external;

import com.bank.currencies.parameters.Parameters;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "giphy-api", url = Parameters.GIPHY_API_URL)
public interface GiphyFeignClient {

    @GetMapping("/gifs/search")
    ResponseEntity<Object> getRandomGif(@RequestParam(name = "api_key") String apiKey,
                                        @RequestParam String q,
                                        @RequestParam Integer limit,
                                        @RequestParam Integer offset,
                                        @RequestParam(name = "random_id") String randomId);
}
