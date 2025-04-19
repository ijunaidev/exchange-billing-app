package com.xische.exchangebilling.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xische.exchangebilling.dto.BillRequest;
import com.xische.exchangebilling.dto.BillResponse;
import com.xische.exchangebilling.dto.ItemDTO;
import com.xische.exchangebilling.enums.ItemCategory;
import com.xische.exchangebilling.enums.UserType;
import com.xische.exchangebilling.service.BillCalculationService;
import com.xische.exchangebilling.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class BillingControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private BillCalculationService billingService;
    @MockBean private JwtUtil jwtUtil;
    @MockBean private AuthenticationManager authenticationManager;
    @MockBean private UserDetailsService userDetailsService;



    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testCalculate_ReturnsValidBillResponse() throws Exception {
        BillRequest request = new BillRequest(
                List.of(new ItemDTO("item1", ItemCategory.NON_GROCERY, 2)),
                UserType.CUSTOMER,
                2,
                "USD",
                "AED"
        );

        BillResponse response = new BillResponse(180.0, "AED");

        Mockito.when(billingService.calculate(any(BillRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.netPayable").value(180.0))
                .andExpect(jsonPath("$.currency").value("AED"));
    }
}