package com.xische.exchangebilling.dto;

import com.xische.exchangebilling.enums.ItemCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {
    private String name;
    private ItemCategory category;
    private double price;
}