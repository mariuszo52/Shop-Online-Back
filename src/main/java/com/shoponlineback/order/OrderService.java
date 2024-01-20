package com.shoponlineback.order;

import com.shoponlineback.exceptions.product.ProductNotFoundException;
import com.shoponlineback.order.dto.OrderDto;
import com.shoponlineback.orderProduct.OrderProduct;
import com.shoponlineback.orderProduct.OrderProductRepository;
import com.shoponlineback.product.Product;
import com.shoponlineback.product.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.shoponlineback.user.UserService.*;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;
    private final OrderDtoMapper orderDtoMapper;

    public OrderService(OrderRepository orderRepository, OrderProductRepository orderProductRepository, ProductRepository productRepository, OrderDtoMapper orderDtoMapper) {
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
        this.productRepository = productRepository;
        this.orderDtoMapper = orderDtoMapper;
    }
//todo
    public Order saveOrder(OrderDto orderDto) {
        Order order = orderDtoMapper.map(orderDto);
        order.setOrderStatus(OrderStatus.ORDER_RECEIVED);
        order.setOrderDate(LocalDateTime.now());
        Order orderEntity = orderRepository.save(order);
        orderDto.getProductList().forEach(productDto -> {
            Product product = productRepository.findById(productDto.getId())
                    .orElseThrow(ProductNotFoundException::new);
           // OrderProduct orderProduct = new OrderProduct(order, product, Math.toIntExact(productDto.getCartQuantity()));
           // orderProductRepository.save(orderProduct);
        });
        return orderEntity;
    }


    public List<OrderDto> getUserOrders() {
       return orderRepository.findAllByUser_Id(getLoggedUser().getId()).stream()
                .map(OrderDtoMapper::map)
                .toList();
    }
}
