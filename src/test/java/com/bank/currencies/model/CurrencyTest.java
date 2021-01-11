package com.bank.currencies.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CurrencyTest {

    @Test
    public void testCurrencyRequest() throws JsonProcessingException, JSONException {
        Currency currency = new Currency();
        currency.setCode("USD");
        JSONAssert.assertEquals("{\"code\":\"USD\"}", new ObjectMapper().writeValueAsString(currency), true);
    }

}
