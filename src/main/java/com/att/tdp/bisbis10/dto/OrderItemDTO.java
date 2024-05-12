package com.att.tdp.bisbis10.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderItemDTO {
    private Integer dishId;
    private Integer amount;
}
