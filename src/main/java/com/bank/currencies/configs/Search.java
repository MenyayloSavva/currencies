package com.bank.currencies.configs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Search {
    private Integer limit;
    private Integer randomLimit;
    private String rich;
    private String broke;
}
