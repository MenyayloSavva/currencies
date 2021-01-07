package com.bank.currencies.services;

import com.bank.currencies.external.GiphyFeignClient;
import com.bank.currencies.external.OpenExchangeRatesFeignClient;
import com.bank.currencies.api.CurrencyRequest;
import com.bank.currencies.api.GifResponse;
import com.bank.currencies.parameters.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;

@Service
public class CurrencyService {

@Autowired
private OpenExchangeRatesFeignClient openExchangeRatesFeignClient;

@Autowired
private GiphyFeignClient giphyFeignClient;

    LocalDate dateFirst;
    LocalDate dateLast;
    Integer giphySearchOffset;
    private DateTimeFormatter formatter;
    private ResponseEntity responseEntity;
    private Map<String, Object> responseBody;
    private GifResponse gifResponse;

    public CurrencyService() {
        this.gifResponse = new GifResponse();
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    public ResponseEntity evaluateCurrency(CurrencyRequest currencyRequest) {

        // 0. Initializing params.
        this.dateFirst = LocalDate.now();             // Today by BRD.
        this.dateLast = LocalDate.now().minusDays(1); // Yesterday by BRD.
        this.giphySearchOffset = (int) (Math.random() * Parameters.GIPHY_SEARCH_RANDOM_LIMIT);

        // 1. Check if request has "code" in the body.
        if (!StringUtils.hasText(currencyRequest.getCode())) {
            return ResponseEntity.badRequest().body("Request should contain \"code\"");
        }

        // 2. Check if "code" is in the list of the available currencies from "openexchangerates.org".
        try {
            responseEntity = openExchangeRatesFeignClient.getCurrenciesList(Parameters.OER_CURR_PP, Parameters.OER_CURR_SHOW_ALT, Parameters.OER_CURR_SHOW_INACT);
            responseBody = (Map) responseEntity.getBody();

            if (!responseBody.containsKey(currencyRequest.getCode())) {
                return ResponseEntity.badRequest().body("Request should contain proper \"code\". For example, \"RUB\", \"UAH\", \"JPY\", \"KRW\"");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Caught Exception during requesting list of all currencies.\n" + e.getMessage());
        }

        // 3. Check if "code" doesn't match with the base currency provided for free acc by "openexchangerates.org"
        if (currencyRequest.getCode().equals(Parameters.OER_BASE_CURR)) {
            return ResponseEntity.badRequest().body("Currency code shouldn't match with \"openexchangerates.org\" base currency (" +
                    Parameters.OER_BASE_CURR + "). For example, \"RUB\", \"UAH\", \"JPY\", \"KRW\"");
        }

        // 4.1. Get today's rate.
        try {
            responseEntity = openExchangeRatesFeignClient.getCurrencyRate(dateFirst.format(formatter), Parameters.OER_APP_ID,
                    currencyRequest.getCode(), Parameters.OER_HIST_SHOW_ALT, Parameters.OER_HIST_PP);
            responseBody = (Map) responseEntity.getBody();
            gifResponse.setTodaysRate((Double) ((Map) responseBody.get("rates")).get(currencyRequest.getCode()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Caught Exception during requesting today's rate.\n" + e.getMessage());
        }

        // 4.2. Get yesterday's rate.
        try {
            responseEntity = openExchangeRatesFeignClient.getCurrencyRate(dateLast.format(formatter), Parameters.OER_APP_ID,
                    currencyRequest.getCode(), Parameters.OER_HIST_SHOW_ALT, Parameters.OER_HIST_PP);
            responseBody = (Map) responseEntity.getBody();
            gifResponse.setYesterdaysRate((Double) ((Map) responseBody.get("rates")).get(currencyRequest.getCode()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Caught Exception during requesting yesterday's rate.\n" + e.getMessage());
        }

        // 5. If today's rate is equal or higher, get Rich Gif. Other way get Broke Gif.
        try {
            if (gifResponse.getTodaysRate() >= gifResponse.getYesterdaysRate()) {
                gifResponse.setIsRich(true);
                responseEntity = giphyFeignClient.getRandomGif(Parameters.GIPHY_API_KEY, Parameters.GIPHY_SEARCH_RICH, Parameters.GIPHY_SEARCH_LIMIT,
                        giphySearchOffset, Parameters.GIPHY_API_RANDOM_ID);
            } else {
                gifResponse.setIsRich(false);
                responseEntity = giphyFeignClient.getRandomGif(Parameters.GIPHY_API_KEY, Parameters.GIPHY_SEARCH_BROKE, Parameters.GIPHY_SEARCH_LIMIT,
                        giphySearchOffset, Parameters.GIPHY_API_RANDOM_ID);
            }
            responseBody = (Map) responseEntity.getBody();
            gifResponse.setGifUrl(((Map) ((ArrayList) responseBody.get("data")).get(0)).get("url").toString());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Caught Exception during getting gif from GIPHY.\n" + e.getMessage());
        }

        // 6. Returning a response.
        return ResponseEntity.ok().body(gifResponse);
    }
}
