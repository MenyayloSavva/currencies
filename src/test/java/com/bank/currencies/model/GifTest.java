package com.bank.currencies.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GifTest {

    @Test
    public void testGifResponse() throws JsonProcessingException, JSONException {
        Gif gif = new Gif();
        gif.setTodaysRate(35.6);
        gif.setYesterdaysRate(34.6);
        gif.setIsRich(gif.getTodaysRate() > gif.getYesterdaysRate());
        gif.setGifUrl("https://giphy.com/gifs/make-it-rain-get-paid-LdOyjZ7io5Msw");

        JSONAssert.assertEquals("{\"rate_today\":35.6,\"rate_yesterday\":34.6,\"is_rich\":true," +
                "\"gif_url\":\"https://giphy.com/gifs/make-it-rain-get-paid-LdOyjZ7io5Msw\"}", new ObjectMapper().writeValueAsString(gif), true);
    }

}
