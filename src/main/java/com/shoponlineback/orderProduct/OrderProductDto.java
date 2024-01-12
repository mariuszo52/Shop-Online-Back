package com.shoponlineback.orderProduct;

import com.shoponlineback.order.dto.OrderDto;
import com.shoponlineback.product.dto.ProductDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class OrderProductDto {
    private Long id;
    @NotNull
    private OrderDto order;
    @NotNull
    private ProductDto product;
    @NotNull
    private Integer quantity;

}
