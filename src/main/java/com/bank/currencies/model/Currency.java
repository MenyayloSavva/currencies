package com.bank.currencies.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Currency {
    private String code;
    private String fullTitle;
}
