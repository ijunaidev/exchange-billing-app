package com.xische.exchangebilling.service;

import com.xische.exchangebilling.enums.ItemCategory;
import com.xische.exchangebilling.enums.UserType;
import com.xische.exchangebilling.model.Item;
import com.xische.exchangebilling.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscountService {

    public double applyDiscount(List<Item> items, User user) {
        double total = items.stream().mapToDouble(Item::getPrice).sum();

        double percentageDiscount = 0.0;
        if (containsNonGrocery(items)) {
            if (user.getType() == UserType.EMPLOYEE) {
                percentageDiscount = 0.30;
            } else if (user.getType() == UserType.AFFILIATE) {
                percentageDiscount = 0.10;
            } else if (user.getType() == UserType.CUSTOMER && user.getTenureInYears() > 2) {
                percentageDiscount = 0.05;
            }
        }

        double totalNonGrocery = items.stream()
                .filter(i -> i.getCategory() == ItemCategory.NON_GROCERY)
                .mapToDouble(Item::getPrice)
                .sum();

        double discount = totalNonGrocery * percentageDiscount;
        discount += Math.floor(total / 100) * 5;

        return total - discount;
    }

    private boolean containsNonGrocery(List<Item> items) {
        return items.stream().anyMatch(i -> i.getCategory() == ItemCategory.NON_GROCERY);
    }
}