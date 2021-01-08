package com.bank.currencies.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GifResponse {
    @JsonProperty(value = "rate_today")
    private Double todaysRate;
    @JsonProperty(value = "rate_yesterday")
    private Double yesterdaysRate;
    @JsonProperty(value = "is_rich")
    private Boolean isRich;
    @JsonProperty(value = "gif_url")
    private String gifUrl;
}
