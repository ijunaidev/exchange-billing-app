package com.xische.exchangebilling.service;

import com.xische.exchangebilling.enums.ItemCategory;
import com.xische.exchangebilling.enums.UserType;
import com.xische.exchangebilling.model.Item;
import com.xische.exchangebilling.model.User;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DiscountServiceTest {

    private final DiscountService discountService = new DiscountService();

    @Test
    void testEmployeeDiscountNonGrocery() {
        List<Item> items = List.of(new Item("TV", ItemCategory.NON_GROCERY, 1000));
        User user = new User(UserType.EMPLOYEE, 1);
        double result = discountService.applyDiscount(items, user);
        assertEquals(650.0, result);
    }

    @Test
    void testAffiliateDiscountNonGrocery() {
        List<Item> items = List.of(new Item("TV", ItemCategory.NON_GROCERY, 1000));
        User user = new User(UserType.AFFILIATE, 1);
        double result = discountService.applyDiscount(items, user);
        assertEquals(850.0, result);
    }

    @Test
    void testCustomerOver2YearsDiscount() {
        List<Item> items = List.of(new Item("TV", ItemCategory.NON_GROCERY, 200));
        User user = new User(UserType.CUSTOMER, 3);
        double result = discountService.applyDiscount(items, user);
        assertEquals(180.0, result);
    }

    @Test
    void testNoPercentageDiscountOnGroceryOnly() {
        List<Item> items = List.of(new Item("Milk", ItemCategory.GROCERY, 200));
        User user = new User(UserType.EMPLOYEE, 5);
        double result = discountService.applyDiscount(items, user);
        assertEquals(190.0, result);
    }

    @Test
    void testOnlyOnePercentageDiscountApplies() {
        List<Item> items = List.of(new Item("TV", ItemCategory.NON_GROCERY, 1000));
        User user = new User(UserType.EMPLOYEE, 5);
        double result = discountService.applyDiscount(items, user);
        assertEquals(650.0, result);
    }

    @Test
    void testMixedItemsEmployee() {
        List<Item> items = List.of(
                new Item("TV", ItemCategory.NON_GROCERY, 400),
                new Item("Milk", ItemCategory.GROCERY, 100)
        );
        User user = new User(UserType.EMPLOYEE, 2);
        double result = discountService.applyDiscount(items, user);
        assertEquals(355.0, result);
    }

    @Test
    void testBillExactly100() {
        List<Item> items = List.of(new Item("TV", ItemCategory.NON_GROCERY, 100));
        User user = new User(UserType.CUSTOMER, 1);
        double result = discountService.applyDiscount(items, user);
        assertEquals(95.0, result);
    }

    @Test
    void testBillBelow100() {
        List<Item> items = List.of(new Item("TV", ItemCategory.NON_GROCERY, 99));
        User user = new User(UserType.CUSTOMER, 1);
        double result = discountService.applyDiscount(items, user);
        assertEquals(99.0, result);
    }

    @Test
    void testCustomerUnder2YearsGetsNoPercentDiscount() {
        List<Item> items = List.of(new Item("TV", ItemCategory.NON_GROCERY, 300));
        User user = new User(UserType.CUSTOMER, 1);
        double result = discountService.applyDiscount(items, user);
        assertEquals(285.0, result);
    }

    @Test
    void testEmptyCart() {
        List<Item> items = List.of();
        User user = new User(UserType.EMPLOYEE, 5);
        double result = discountService.applyDiscount(items, user);
        assertEquals(0.0, result);
    }
}