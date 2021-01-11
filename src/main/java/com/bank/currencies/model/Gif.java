package com.bank.currencies.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Gif {
    @JsonProperty(value = "rate_today")
    private Double todaysRate;
    @JsonProperty(value = "rate_yesterday")
    private Double yesterdaysRate;
    @JsonProperty(value = "is_rich")
    private Boolean isRich;
    @JsonProperty(value = "gif_url")
    private String gifUrl;
}
