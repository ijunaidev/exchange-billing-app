package com.xische.exchangebilling.model;

import com.xische.exchangebilling.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private UserType type;
    private int tenureInYears;
}