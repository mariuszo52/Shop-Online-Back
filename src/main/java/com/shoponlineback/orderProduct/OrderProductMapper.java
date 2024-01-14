package com.shoponlineback.orderProduct;

import com.shoponlineback.order.OrderDtoMapper;
import com.shoponlineback.orderProduct.activationCode.ActivationCode;
import com.shoponlineback.product.mapper.ProductDtoMapper;

import java.util.List;

public class OrderProductMapper {

   public static OrderProductDto map(OrderProduct orderProduct){
       List<String> activationCodes = orderProduct.getActivationCodes().stream()
               .map(ActivationCode::getCode).toList();
       return OrderProductDto.builder()
               .id(orderProduct.getId())
               .order(OrderDtoMapper.map(orderProduct.getOrder()))
               .product(ProductDtoMapper.map(orderProduct.getProduct()))
               .quantity(orderProduct.getQuantity())
               .activationCodes(activationCodes).build();
    }
}
