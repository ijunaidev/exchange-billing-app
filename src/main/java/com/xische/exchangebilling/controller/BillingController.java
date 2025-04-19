package com.xische.exchangebilling.controller;

import com.xische.exchangebilling.dto.BillRequest;
import com.xische.exchangebilling.dto.BillResponse;
import com.xische.exchangebilling.service.BillCalculationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BillingController {

    private final BillCalculationService service;

    public BillingController(BillCalculationService service) {
        this.service = service;
    }

    @PostMapping("/calculate")
    public ResponseEntity<BillResponse> calculate(@RequestBody BillRequest request) {
        return ResponseEntity.ok(service.calculate(request));
    }
}