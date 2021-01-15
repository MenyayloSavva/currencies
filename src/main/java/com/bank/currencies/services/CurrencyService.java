package com.bank.currencies.services;

import com.bank.currencies.external.GiphyFeignClient;
import com.bank.currencies.external.OpenExchangeRatesFeignClient;
import com.bank.currencies.external.responses.GiphyResponse;
import com.bank.currencies.external.responses.OERHistResponse;
import com.bank.currencies.model.Currency;
import com.bank.currencies.model.Gif;
import com.bank.currencies.parameters.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
            Map<String, String> currencyMap  = getCurrencyList();
            if (!currencyMap.containsKey(currency.getCode()) || currency.getCode().equals(Parameters.OER_BASE_CURR)) {
                return ResponseEntity.badRequest().body("Request should contain proper \"code\". For example, \"RUB\", \"UAH\", \"JPY\", \"KRW\"" +
                        "\nExcluding \"openexchangerates.org\" base currency (" + Parameters.OER_BASE_CURR + ")");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Caught Exception during requesting list of all currencies.\n" + e.getMessage());
        }

        // 3. Getting rates
        try {
            OERHistResponse response = getCurrencyRate(currency.getCode(), dateFirst);
            gif.setTodaysRate(response.getRates().get(currency.getCode()));
            response = getCurrencyRate(currency.getCode(), dateLast);
            gif.setYesterdaysRate(response.getRates().get(currency.getCode()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Caught Exception during requesting rates.\n" + e.getMessage());
        }

        // 4. If today's rate is equal or higher, get Rich Gif. Other way get Broke Gif.
        try {
            GiphyResponse response;
            if (gif.getTodaysRate() >= gif.getYesterdaysRate()) {
                gif.setIsRich(true);
                response = getRandomGif(Parameters.GIPHY_SEARCH_RICH);
            } else {
                gif.setIsRich(false);
                response = getRandomGif(Parameters.GIPHY_SEARCH_BROKE);
            }
            gif.setGifUrl(response.getData().get(0).getUrl());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Caught Exception during getting gif from GIPHY.\n" + e.getMessage());
        }

        // 5. Returning a response.
        return ResponseEntity.ok().body(gif);
    }


    private void initializeParams() {
        this.gif = new Gif();
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.dateFirst = LocalDate.now();             // Today by BRD.
        this.dateLast = LocalDate.now().minusDays(1); // Yesterday by BRD.
        this.giphySearchOffset = (int) (Math.random() * Parameters.GIPHY_SEARCH_RANDOM_LIMIT);
    }

    private Map<String,String> getCurrencyList() {
        return openExchangeRatesFeignClient.getCurrencyList(Parameters.OER_CURR_PP, Parameters.OER_CURR_SHOW_ALT, Parameters.OER_CURR_SHOW_INACT);
    }

    private OERHistResponse getCurrencyRate(String currencyCode, LocalDate date) {
        return openExchangeRatesFeignClient.getCurrencyRate(date.format(formatter), Parameters.OER_APP_ID,
                currencyCode, Parameters.OER_HIST_SHOW_ALT, Parameters.OER_HIST_PP);
    }

    private GiphyResponse getRandomGif(String searchQuery) {
        return giphyFeignClient.getRandomGif(Parameters.GIPHY_API_KEY, searchQuery, Parameters.GIPHY_SEARCH_LIMIT,
                giphySearchOffset, Parameters.GIPHY_API_RANDOM_ID);
    }

}