package com.xische.exchangebilling.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CurrencyExchangeServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CurrencyExchangeService currencyExchangeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        currencyExchangeService = new CurrencyExchangeService(restTemplate);
    }

    @Test
    void testSameCurrencyReturnsOne() {
        double rate = currencyExchangeService.getExchangeRate("USD", "USD");
        assertEquals(1.0, rate);
        verifyNoInteractions(restTemplate);
    }

    @Test
    void testValidExchangeRate() {
        Map<String, Double> rates = new HashMap<>();
        rates.put("EUR", 0.92);

        Map<String, Object> response = new HashMap<>();
        response.put("conversion_rates", rates);

        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(response);

        double rate = currencyExchangeService.getExchangeRate("USD", "EUR");
        assertEquals(0.92, rate);
    }

    @Test
    void testMissingTargetCurrencyReturnsDefaultOne() {
        Map<String, Double> rates = new HashMap<>();
        rates.put("INR", 82.0); // EUR not included

        Map<String, Object> response = new HashMap<>();
        response.put("conversion_rates", rates);

        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(response);

        double rate = currencyExchangeService.getExchangeRate("USD", "EUR");
        assertEquals(1.0, rate);
    }

    @Test
    void testNullResponseReturnsOne() {
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(null);
        double rate = currencyExchangeService.getExchangeRate("USD", "EUR");
        assertEquals(1.0, rate);
    }

    @Test
    void testEmptyConversionRatesReturnsOne() {
        Map<String, Object> response = new HashMap<>();
        response.put("conversion_rates", new HashMap<>());

        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(response);

        double rate = currencyExchangeService.getExchangeRate("USD", "EUR");
        assertEquals(1.0, rate);
    }

    @Test
    void testCurrencyCaseInsensitivity() {
        Map<String, Double> rates = new HashMap<>();
        rates.put("EUR", 0.85);

        Map<String, Object> response = new HashMap<>();
        response.put("conversion_rates", rates);

        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(response);

        double rate = currencyExchangeService.getExchangeRate("usd", "eur");

        assertEquals(0.85, rate);
    }

    @Test
    void testInvalidResponseType() {
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(new HashMap<>());
        double rate = currencyExchangeService.getExchangeRate("USD", "EUR");
        assertEquals(1.0, rate);
    }

    @Test
    void testMultipleCallsToSameCurrency() {
        currencyExchangeService.getExchangeRate("USD", "USD");
        currencyExchangeService.getExchangeRate("EUR", "EUR");
        currencyExchangeService.getExchangeRate("PKR", "PKR");
        verifyNoInteractions(restTemplate);
    }

    @Test
    void testApiUrlIsBuiltCorrectly() {
        // This test would be better in an integration test, but we can confirm the call happens
        Map<String, Object> response = new HashMap<>();
        response.put("conversion_rates", Map.of("EUR", 0.8));
        when(restTemplate.getForObject(contains("USD"), eq(Map.class))).thenReturn(response);

        double rate = currencyExchangeService.getExchangeRate("USD", "EUR");
        assertEquals(0.8, rate);
    }

    @Test
    void testFallbackWhenConversionRatesMissing() {
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(Map.of());
        double rate = currencyExchangeService.getExchangeRate("USD", "EUR");
        assertEquals(1.0, rate);
    }
}