package com.bank.currencies.external.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class OERHistResponse {
        private String disclaimer;
        private String license;
        private int timestamp;
        private String base;
        private Map<String, Double> rates;
}
