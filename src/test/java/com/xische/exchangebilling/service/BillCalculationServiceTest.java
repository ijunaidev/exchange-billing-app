package com.xische.exchangebilling.service;

import com.xische.exchangebilling.dto.*;
import com.xische.exchangebilling.enums.ItemCategory;
import com.xische.exchangebilling.enums.UserType;
import com.xische.exchangebilling.model.Item;
import com.xische.exchangebilling.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BillCalculationServiceTest {

    @Mock
    private CurrencyExchangeService currencyService;

    @Mock
    private DiscountService discountService;

    @InjectMocks
    private BillCalculationService billCalculationService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private BillRequest buildRequest(double item1Price, double item2Price, String originalCurrency, String targetCurrency, UserType userType, int tenure) {
        return new BillRequest(
                List.of(
                        new ItemDTO("TV", ItemCategory.NON_GROCERY, item1Price),
                        new ItemDTO("Milk", ItemCategory.GROCERY, item2Price)
                ),
                userType,
                tenure,
                originalCurrency,
                targetCurrency
        );
    }

    @Test
    void testBasicConversionWithDiscounts() {
        BillRequest request = buildRequest(500, 100, "USD", "EUR", UserType.EMPLOYEE, 1);

        when(discountService.applyDiscount(any(), any())).thenReturn(520.0);
        when(currencyService.getExchangeRate("USD", "EUR")).thenReturn(0.9);

        BillResponse response = billCalculationService.calculate(request);

        assertEquals(468.0, response.getNetPayable());
        assertEquals("EUR", response.getCurrency());
    }

    @Test
    void testSameCurrencyReturnsDiscountOnly() {
        BillRequest request = buildRequest(200, 0, "USD", "USD", UserType.AFFILIATE, 1);

        when(discountService.applyDiscount(any(), any())).thenReturn(180.0);
        when(currencyService.getExchangeRate("USD", "USD")).thenReturn(1.0);

        BillResponse response = billCalculationService.calculate(request);

        assertEquals(180.0, response.getNetPayable());
    }

    @Test
    void testConversionRateIsZero() {
        BillRequest request = buildRequest(200, 0, "USD", "XYZ", UserType.CUSTOMER, 3);

        when(discountService.applyDiscount(any(), any())).thenReturn(190.0);
        when(currencyService.getExchangeRate("USD", "XYZ")).thenReturn(0.0);

        BillResponse response = billCalculationService.calculate(request);

        assertEquals(0.0, response.getNetPayable());
        assertEquals("XYZ", response.getCurrency());
    }

    @Test
    void testNoDiscountScenario() {
        BillRequest request = buildRequest(50, 50, "USD", "AED", UserType.CUSTOMER, 1);

        when(discountService.applyDiscount(any(), any())).thenReturn(95.0);
        when(currencyService.getExchangeRate("USD", "AED")).thenReturn(3.67);

        BillResponse response = billCalculationService.calculate(request);

        assertEquals(348.65, response.getNetPayable());
    }

    @Test
    void testZeroRateReturnsZero() {
        BillRequest request = buildRequest(1000, 0, "USD", "XXX", UserType.EMPLOYEE, 1);

        when(discountService.applyDiscount(any(), any())).thenReturn(700.0);
        when(currencyService.getExchangeRate("USD", "XXX")).thenReturn(0.0);

        BillResponse response = billCalculationService.calculate(request);

        assertEquals(0.0, response.getNetPayable());
    }

    @Test
    void testZeroTotalReturnsZero() {
        BillRequest request = buildRequest(0, 0, "USD", "EUR", UserType.EMPLOYEE, 5);

        when(discountService.applyDiscount(any(), any())).thenReturn(0.0);
        when(currencyService.getExchangeRate("USD", "EUR")).thenReturn(0.9);

        BillResponse response = billCalculationService.calculate(request);

        assertEquals(0.0, response.getNetPayable());
    }

    @Test
    void testHighTenureCustomerDiscount() {
        BillRequest request = buildRequest(300, 50, "USD", "INR", UserType.CUSTOMER, 10);

        when(discountService.applyDiscount(any(), any())).thenReturn(320.0);
        when(currencyService.getExchangeRate("USD", "INR")).thenReturn(82.0);

        BillResponse response = billCalculationService.calculate(request);

        assertEquals(26240.0, response.getNetPayable());
    }

    @Test
    void testAllGroceriesNoPercentDiscount() {
        BillRequest request = new BillRequest(
                List.of(new ItemDTO("Apples", ItemCategory.GROCERY, 100)),
                UserType.EMPLOYEE,
                2,
                "USD",
                "AED"
        );

        when(discountService.applyDiscount(any(), any())).thenReturn(95.0);
        when(currencyService.getExchangeRate("USD", "AED")).thenReturn(3.67);

        BillResponse response = billCalculationService.calculate(request);
        assertEquals(348.65, response.getNetPayable());
    }

    @Test
    void testEmptyItemList() {
        BillRequest request = new BillRequest(
                List.of(),
                UserType.EMPLOYEE,
                2,
                "USD",
                "AED"
        );

        when(discountService.applyDiscount(any(), any())).thenReturn(0.0);
        when(currencyService.getExchangeRate("USD", "AED")).thenReturn(3.67);

        BillResponse response = billCalculationService.calculate(request);
        assertEquals(0.0, response.getNetPayable());
    }

    @Test
    void testNegativeDiscountHandledGracefully() {
        BillRequest request = buildRequest(300, 100, "USD", "EUR", UserType.AFFILIATE, 2);

        when(discountService.applyDiscount(any(), any())).thenReturn(-50.0);
        when(currencyService.getExchangeRate("USD", "EUR")).thenReturn(0.9);

        BillResponse response = billCalculationService.calculate(request);
        assertEquals(-45.0, response.getNetPayable());
    }
}