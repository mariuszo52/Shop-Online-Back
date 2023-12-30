package com.shoponlineback.order;

import com.shoponlineback.paymentMethod.PaymentMethod;
import com.shoponlineback.product.Product;
import com.shoponlineback.product.dto.ProductDto;
import com.shoponlineback.shippingAddress.ShippingAddressDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class OrderDto {
    private Long id;
    private Long userId;
    @NotNull
    private ShippingAddressDto shippingAddress;
    @NotNull
    private List<ProductDto> productList;
    @NotNull
    @Min(1)
    private BigDecimal totalPrice;
    @NotNull
    private String paymentMethod;
}
