package com.shoponlineback.product.productManagement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProductPriceUpdateDto {
    private Long productId;
    private BigDecimal price;
}
