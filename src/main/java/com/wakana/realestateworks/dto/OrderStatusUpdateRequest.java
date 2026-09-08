package com.wakana.realestateworks.dto;

import com.wakana.realestateworks.enums.OrderStatus;

import lombok.Data;

@Data
public class OrderStatusUpdateRequest {
    private OrderStatus status;
}
