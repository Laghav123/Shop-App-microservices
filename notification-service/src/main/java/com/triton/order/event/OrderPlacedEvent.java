package com.triton.order.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPlacedEvent {
    private String orderNumber;
    private String email;

    public String getEmail() {
        return email;
    }

    public String getOrderNumber() {
        return orderNumber;
    }
}
