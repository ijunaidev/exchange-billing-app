package com.xische.exchangebilling.model;

import com.xische.exchangebilling.enums.ItemCategory;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Item {
    private String name;
    private ItemCategory category;
    private double price;
}