package com.att.tdp.bisbis10.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class OrderDTO {
    private Integer restaurantId;
    private List<OrderItemDTO> orderItems;
}
