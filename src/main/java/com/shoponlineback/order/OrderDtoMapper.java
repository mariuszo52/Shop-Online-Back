package com.shoponlineback.order;

import com.shoponlineback.exceptions.product.ProductNotFoundException;
import com.shoponlineback.exceptions.user.UserNotFoundException;
import com.shoponlineback.orderProduct.OrderProduct;
import com.shoponlineback.orderProduct.OrderProductRepository;
import com.shoponlineback.paymentMethod.PaymentMethod;
import com.shoponlineback.product.Product;
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
import java.util.stream.Collectors;

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
                .totalPrice(totalPrice).build();
    }
}