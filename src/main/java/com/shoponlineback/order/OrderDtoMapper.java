package com.shoponlineback.order;

import com.shoponlineback.exceptions.user.UserNotFoundException;
import com.shoponlineback.order.dto.OrderDto;
import com.shoponlineback.orderProduct.OrderProductRepository;
import com.shoponlineback.paymentMethod.PaymentMethod;
import com.shoponlineback.product.ProductRepository;
import com.shoponlineback.product.dto.ProductDto;
import com.shoponlineback.product.mapper.ProductDtoMapper;
import com.shoponlineback.shippingAddress.ShippingAddress;
import com.shoponlineback.shippingAddress.ShippingAddressDto;
import com.shoponlineback.user.User;
import com.shoponlineback.user.UserRepository;
import com.shoponlineback.user.UserService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderDtoMapper {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductDtoMapper productDtoMapper;

    public OrderDtoMapper(UserRepository userRepository, ProductRepository productRepository, OrderProductRepository orderProductRepository, ProductDtoMapper productDtoMapper) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderProductRepository = orderProductRepository;
        this.productDtoMapper = productDtoMapper;
    }

    Order map(OrderDto orderDto){
        ShippingAddressDto saDto = orderDto.getShippingAddress();
        User user = userRepository.findById(UserService.getLoggedUser().getId())
                .orElseThrow(UserNotFoundException::new);
        BigDecimal totalPrice = orderDto.getProductList().stream()
                .map(product -> product.getPrice().multiply(BigDecimal.valueOf(product.getCartQuantity())))
                .reduce(BigDecimal::add).orElseThrow(RuntimeException::new);
        return Order.builder()
                .id(orderDto.getId())
                .paymentMethod(PaymentMethod.valueOf(orderDto.getPaymentMethod()))
                .shippingAddress(new ShippingAddress(saDto.getAddress(), saDto.getCity(),
                        saDto.getCountry(), saDto.getPostalCode(), saDto.getPhoneNumber()))
                .user(user)
                .orderDate(orderDto.getOrderDate())
                .totalPrice(totalPrice).build();
    }

    public static OrderDto map(Order order){
        ShippingAddress sa = order.getShippingAddress();
        ShippingAddressDto shippingAddressDto = new ShippingAddressDto(sa.getAddress(),
                sa.getCity(), sa.getCountry(), sa.getPostalCode(), sa.getPhoneNumber());
        List<ProductDto> productDtoList = order.getProductList().stream()
                .map(ProductDtoMapper::map).toList();
        return OrderDto.builder()
                .id(order.getId())
                .paymentMethod(order.getPaymentMethod().toString())
                .shippingAddress(shippingAddressDto)
                .productList(productDtoList)
                .totalPrice(order.getTotalPrice())
                .orderDate(order.getOrderDate())
                .orderStatus(order.getOrderStatus().toString())
                .userId(order.getId()).build();
    }
}
