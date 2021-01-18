package com.bank.currencies.external;

import com.bank.currencies.configs.YAMLConfig;
import com.bank.currencies.external.responses.OERHistResponse;
import com.bank.currencies.services.CurrencyService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OpenExchangeRatesFeignClientTest {

    @MockBean
    private OpenExchangeRatesFeignClient client;

    @Autowired
    private YAMLConfig configs;

    @Autowired
    private CurrencyService service;

    @BeforeEach
    public void InitializeParams() {
        service.initializeParams();
    }

    @Test
    public void getCurrencyListTest() {
        Map<String, String> mapExpected = new LinkedHashMap<String, String>() {{
            put("AED", "United Arab Emirates Dirham");
            put("BND", "Brunei Dollar");
            put("EUR", "Euro");
            put("USD", "United States Dollar");
        }};
        when(client.getCurrencyList(
                configs.getOpenExchangeRates().getCurrencies().getPrettyPrint(),
                configs.getOpenExchangeRates().getCurrencies().getShowAlternative(),
                configs.getOpenExchangeRates().getCurrencies().getShowInactive()
        )).thenReturn(mapExpected);

        Map<String, String> mapActual = service.getCurrencyList();

        Assert.assertTrue(new ReflectionEquals(mapActual).matches(mapExpected));
    }

    @Test
    public void getCurrencyRateTest() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.now();

        OERHistResponse responseExpected = new OERHistResponse(
                "Usage subject to terms: https://openexchangerates.org/terms",
                "https://openexchangerates.org/license",
                1610884814,
                "USD",
                new LinkedHashMap<String, Double>() {{
                    put("EUR",0.810475);
                }}
        );

        when(client.getCurrencyRate(
                date.format(formatter),
                configs.getOpenExchangeRates().getAppId(),
                "EUR",
                configs.getOpenExchangeRates().getHistorical().getShowAlternative(),
                configs.getOpenExchangeRates().getHistorical().getPrettyPrint()
        )).thenReturn(responseExpected);

        OERHistResponse responseActual = service.getCurrencyRate("EUR", date);
        Assert.assertTrue(new ReflectionEquals(responseActual).matches(responseExpected));
    }
}
