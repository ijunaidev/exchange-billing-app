package com.xische.exchangebilling.dto;

import com.xische.exchangebilling.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillRequest {
    private List<ItemDTO> items;
    private UserType userType;
    private int tenureInYears;
    private String originalCurrency;
    private String targetCurrency;
}