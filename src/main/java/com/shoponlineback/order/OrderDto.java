package com.shoponlineback.order;

import com.shoponlineback.paymentMethod.PaymentMethod;
import com.shoponlineback.product.Product;
import com.shoponlineback.product.dto.ProductDto;
import com.shoponlineback.shippingAddress.ShippingAddressDto;
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
    private ShippingAddressDto shippingAddress;
    private List<ProductDto> productList;
    private BigDecimal totalPrice;
    private String paymentMethod;
}
