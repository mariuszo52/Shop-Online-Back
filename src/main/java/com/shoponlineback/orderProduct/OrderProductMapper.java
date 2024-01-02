package com.shoponlineback.orderProduct;

import com.shoponlineback.order.OrderDto;
import com.shoponlineback.order.OrderDtoMapper;
import com.shoponlineback.product.mapper.ProductDtoMapper;

public class OrderProductMapper {

    static OrderProductDto map(OrderProduct orderProduct){
       return OrderProductDto.builder()
               .id(orderProduct.getId())
               .order(OrderDtoMapper.map(orderProduct.getOrder()))
               .product(ProductDtoMapper.map(orderProduct.getProduct()))
               .quantity(orderProduct.getQuantity()).build();
    }
}
