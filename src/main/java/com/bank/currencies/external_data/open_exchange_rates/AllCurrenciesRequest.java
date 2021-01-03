package com.bank.currencies.external_data.open_exchange_rates;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class AllCurrenciesRequest {
    private Boolean prettyprint = true;
    @JsonProperty("show_alternative")
    private Boolean showAlternative = false;
    @JsonProperty("show_inactive")
    private Boolean showInactive = false;
}
