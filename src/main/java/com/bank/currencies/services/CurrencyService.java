package com.bank.currencies.services;

import com.bank.currencies.external.GiphyFeignClient;
import com.bank.currencies.external.OpenExchangeRatesFeignClient;
import com.bank.currencies.model.Currency;
import com.bank.currencies.model.Gif;
import com.bank.currencies.parameters.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;

@SuppressWarnings("unchecked")
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
    private Gif gif;

    public ResponseEntity evaluateCurrency(Currency currency) {

        // 0. Initializing params.
        initializeParams();

        // 1. Check if request has "code" in the body.
        if (!StringUtils.hasText(currency.getCode())) {
            return ResponseEntity.badRequest().body("Request should contain \"code\"");
        }

        // 2. Check if "code" is in the list of the available currencies from "openexchangerates.org".
        try {
            ResponseEntity responseEntity = getCurrencyList();
            Map<String, Object> responseBody = (Map) responseEntity.getBody();

            if (!responseBody.containsKey(currency.getCode())) {
                return ResponseEntity.badRequest().body("Request should contain proper \"code\". For example, \"RUB\", \"UAH\", \"JPY\", \"KRW\"");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Caught Exception during requesting list of all currencies.\n" + e.getMessage());
        }

        // 3. Check if "code" doesn't match with the base currency provided for free acc by "openexchangerates.org"
        if (currency.getCode().equals(Parameters.OER_BASE_CURR)) {
            return ResponseEntity.badRequest().body("Currency code shouldn't match with \"openexchangerates.org\" base currency (" +
                    Parameters.OER_BASE_CURR + "). For example, \"RUB\", \"UAH\", \"JPY\", \"KRW\"");
        }

        // 4.1. Get today's rate.
        try {
            ResponseEntity responseEntity = getCurrencyRate(currency.getCode(), dateFirst);
            Map<String, Object> responseBody = (Map) responseEntity.getBody();
            gif.setTodaysRate((Double) ((Map) responseBody.get("rates")).get(currency.getCode()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Caught Exception during requesting today's rate.\n" + e.getMessage());
        }

        // 4.2. Get yesterday's rate.
        try {
            ResponseEntity responseEntity = getCurrencyRate(currency.getCode(), dateLast);
            Map<String, Object> responseBody = (Map) responseEntity.getBody();
            gif.setYesterdaysRate((Double) ((Map) responseBody.get("rates")).get(currency.getCode()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Caught Exception during requesting yesterday's rate.\n" + e.getMessage());
        }

        // 5. If today's rate is equal or higher, get Rich Gif. Other way get Broke Gif.
        ResponseEntity responseEntity;
        try {
            if (gif.getTodaysRate() >= gif.getYesterdaysRate()) {
                gif.setIsRich(true);
                responseEntity = getRandomGif(Parameters.GIPHY_SEARCH_RICH);
            } else {
                gif.setIsRich(false);
                responseEntity = getRandomGif(Parameters.GIPHY_SEARCH_BROKE);
            }
            Map<String, Object> responseBody = (Map) responseEntity.getBody();
            gif.setGifUrl(((Map) ((ArrayList) responseBody.get("data")).get(0)).get("url").toString());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Caught Exception during getting gif from GIPHY.\n" + e.getMessage());
        }

        // 6. Returning a response.
        return ResponseEntity.ok().body(gif);
    }


    private void initializeParams() {
        this.gif = new Gif();
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.dateFirst = LocalDate.now();             // Today by BRD.
        this.dateLast = LocalDate.now().minusDays(1); // Yesterday by BRD.
        this.giphySearchOffset = (int) (Math.random() * Parameters.GIPHY_SEARCH_RANDOM_LIMIT);
    }

    private ResponseEntity getCurrencyList() {
        return openExchangeRatesFeignClient.getCurrencyList(Parameters.OER_CURR_PP, Parameters.OER_CURR_SHOW_ALT, Parameters.OER_CURR_SHOW_INACT);
    }

    private ResponseEntity getCurrencyRate(String currencyCode, LocalDate date) {
        return openExchangeRatesFeignClient.getCurrencyRate(date.format(formatter), Parameters.OER_APP_ID,
                currencyCode, Parameters.OER_HIST_SHOW_ALT, Parameters.OER_HIST_PP);
    }

    private ResponseEntity getRandomGif(String searchQuery) {
        return giphyFeignClient.getRandomGif(Parameters.GIPHY_API_KEY, searchQuery, Parameters.GIPHY_SEARCH_LIMIT,
                giphySearchOffset, Parameters.GIPHY_API_RANDOM_ID);
    }

}