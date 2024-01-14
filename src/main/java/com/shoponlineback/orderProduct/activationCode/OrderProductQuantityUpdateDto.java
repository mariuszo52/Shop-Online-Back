package com.shoponlineback.orderProduct.activationCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderProductQuantityUpdateDto {
    private Long orderProductId;
    private int quantity;
}
