package com.xische.exchangebilling.service;

import com.xische.exchangebilling.dto.BillRequest;
import com.xische.exchangebilling.dto.BillResponse;
import com.xische.exchangebilling.model.Item;
import com.xische.exchangebilling.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillCalculationService {

    private final CurrencyExchangeService currencyService;
    private final DiscountService discountService;

    public BillResponse calculate(BillRequest request) {
        List<Item> items = request.getItems().stream()
                .map(i -> new Item(i.getName(), i.getCategory(), i.getPrice()))
                .collect(Collectors.toList());

        User user = new User(request.getUserType(), request.getTenureInYears());

        double discountedAmount = discountService.applyDiscount(items, user);

        double rate = currencyService.getExchangeRate(request.getOriginalCurrency(), request.getTargetCurrency());

        return new BillResponse(discountedAmount * rate, request.getTargetCurrency());
    }
}