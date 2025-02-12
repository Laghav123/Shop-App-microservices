package com.triton.order.dto;


import java.math.BigDecimal;

public record OrderRequestDTO(
        Long id,
        String orderNumber,
        String skuCode,
        BigDecimal price,
        Integer quantity,
        UserDetails userDetails

) {
    public record UserDetails(String email, String firstName, String lastName) {}
}
