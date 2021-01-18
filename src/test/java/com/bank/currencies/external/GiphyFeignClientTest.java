package com.bank.currencies.external;

import com.bank.currencies.configs.YAMLConfig;
import com.bank.currencies.external.responses.Data;
import com.bank.currencies.external.responses.GiphyResponse;
import com.bank.currencies.services.CurrencyService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest
public class GiphyFeignClientTest {

    @MockBean
    private GiphyFeignClient client;

    @Autowired
    private YAMLConfig configs;

    @Autowired
    private CurrencyService service;

    @Test
    public void getRandomGifTest() {
        Data data = new Data("gif","cmypyKZAYqUKqmnFqm","https://giphy.com/gifs/BossKerati-boss-steal-bosskerati-cmypyKZAYqUKqmnFqm");
        GiphyResponse responseExpected = new GiphyResponse(new ArrayList<Data>(){{add(data);}});
        when(client.getRandomGif(
                configs.getGiphy().getApiKey(),
                configs.getGiphy().getSearch().getRich(),
                configs.getGiphy().getSearch().getLimit(),
                500,
                configs.getGiphy().getApiRandomId()
        )).thenReturn(responseExpected);

        GiphyResponse responseActual = service.getRandomGif(configs.getGiphy().getSearch().getRich(), 500);

        Assert.assertTrue(new ReflectionEquals(responseExpected).matches(responseActual));
    }
}
