package com.shoponlineback.orderProduct;

import com.shoponlineback.order.Order;
import com.shoponlineback.order.OrderRepository;
import com.shoponlineback.user.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.shoponlineback.user.UserService.*;

@Service
public class OrderProductService {
    private final OrderProductRepository orderProductRepository;
    private final OrderRepository orderRepository;

    public OrderProductService(OrderProductRepository orderProductRepository, OrderRepository orderRepository) {
        this.orderProductRepository = orderProductRepository;
        this.orderRepository = orderRepository;
    }

    List<OrderProductDto> getOrderProducts(){
        List<Long> userOrdersIds = orderRepository.findAllByUser_Id(getLoggedUser().getId()).stream()
                .map(Order::getId)
                .toList();
        return orderProductRepository.findOrderProductsByOrderIdIn(userOrdersIds).stream()
                .map(OrderProductMapper::map)
                .toList();

    }
}
