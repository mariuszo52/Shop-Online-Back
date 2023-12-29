package com.shoponlineback.order;

import com.shoponlineback.exceptions.product.ProductNotFoundException;
import com.shoponlineback.orderProduct.OrderProduct;
import com.shoponlineback.orderProduct.OrderProductRepository;
import com.shoponlineback.product.Product;
import com.shoponlineback.product.ProductRepository;
import org.springframework.stereotype.Service;

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

    public Order saveOrder(OrderDto orderDto) {
        Order order = orderDtoMapper.map(orderDto);
        order.setOrderStatus(OrderStatus.ORDER_RECEIVED);
        Order orderEntity = orderRepository.save(order);
        orderDto.getProductList().forEach(productDto -> {
            Product product = productRepository.findById(productDto.getId()).orElseThrow(ProductNotFoundException::new);
            orderProductRepository.save(new OrderProduct(order, product, Math.toIntExact(productDto.getCartQuantity())));
        });
        return order;
    }


}
