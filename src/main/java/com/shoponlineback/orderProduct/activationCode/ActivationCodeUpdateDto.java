package com.shoponlineback.orderProduct.activationCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ActivationCodeUpdateDto {
    private Long orderProductId;
    private String code;
}
