package com.shoponlineback.orderProduct;

import com.shoponlineback.order.OrderDtoMapper;
import com.shoponlineback.orderProduct.activationCode.ActivationCode;
import com.shoponlineback.orderProduct.activationCode.ActivationCodeRepository;
import com.shoponlineback.product.mapper.ProductDtoMapper;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class OrderProductMapper {
    private final ActivationCodeRepository activationCodeRepository;

    public OrderProductMapper(ActivationCodeRepository activationCodeRepository) {
        this.activationCodeRepository = activationCodeRepository;
    }

    public OrderProductDto map(OrderProduct orderProduct){
        List<String> activationCodes = activationCodeRepository.findAllByOrderProductId(orderProduct.getId()).stream()
                .map(ActivationCode::getCode).toList();
        return OrderProductDto.builder()
               .id(orderProduct.getId())
               .order(OrderDtoMapper.map(orderProduct.getOrder()))
               .product(ProductDtoMapper.map(orderProduct.getProduct()))
               .quantity(orderProduct.getQuantity())
               .activationCodes(activationCodes).build();
    }
}
