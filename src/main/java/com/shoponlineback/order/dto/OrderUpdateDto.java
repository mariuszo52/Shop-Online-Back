package com.shoponlineback.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderUpdateDto {
    private Long orderId;
    private String orderStatus;
}
