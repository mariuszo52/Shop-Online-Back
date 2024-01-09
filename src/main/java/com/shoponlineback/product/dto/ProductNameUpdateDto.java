package com.shoponlineback.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductNameUpdateDto {
    private Long productId;
    private String name;
}
