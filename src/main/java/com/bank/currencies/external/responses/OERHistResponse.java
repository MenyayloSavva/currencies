package com.bank.currencies.external.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OERHistResponse {
        private String disclaimer;
        private String license;
        private int timestamp;
        private String base;
        private Map<String, Double> rates;
}
