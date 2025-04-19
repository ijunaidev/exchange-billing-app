package com.xische.exchangebilling.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CurrencyExchangeService {

    @Value("${currency.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;
    @Cacheable("rates")
    public double getExchangeRate(String from, String to) {
        if (from.equalsIgnoreCase(to)) {
            return 1.0;
        }

        log.info("Calling Exchange Rate API for {} and {}", from, to);
        String url = apiUrl + from;
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response != null && response.containsKey("conversion_rates")) {
            Map<String, Double> rates = ((Map<String, Double>) response.get("conversion_rates"))
                    .entrySet().stream()
                    .collect(Collectors.toMap(
                            entry -> entry.getKey().toUpperCase(),
                            Map.Entry::getValue
                    ));

            return rates.getOrDefault(to.toUpperCase(), 1.0);
        }

        return 1.0;
    }
}