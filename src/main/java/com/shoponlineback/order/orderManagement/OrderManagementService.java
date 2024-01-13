package com.shoponlineback.order.orderManagement;

import com.shoponlineback.exceptions.order.OrderNotFoundException;
import com.shoponlineback.order.Order;
import com.shoponlineback.order.OrderDtoMapper;
import com.shoponlineback.order.OrderStatus;
import com.shoponlineback.order.dto.OrderDto;
import com.shoponlineback.order.dto.OrderUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class OrderManagementService {
    private final OrderManagementRepository orderManagementRepository;

    public OrderManagementService(OrderManagementRepository orderManagementRepository) {
        this.orderManagementRepository = orderManagementRepository;
    }

    public Page<OrderDto> getAllOrders(int page) {
        final int size = 50;
        List<OrderDto> orders = StreamSupport.stream(orderManagementRepository.findAll().spliterator(), false)
                .map(OrderDtoMapper::map).toList();
        PageRequest pageRequest = PageRequest.of(page, size);
        List<OrderDto> currentPage = orders.subList(Math.min(page * size, orders.size()),
                Math.min(page * size + size, orders.size()));
        return new PageImpl<>(currentPage, pageRequest, orders.size());
    }
    @Transactional
    public void updateOrderStatus(OrderUpdateDto orderUpdate) {
        Order order = orderManagementRepository.findById(orderUpdate.getOrderId())
                .orElseThrow(OrderNotFoundException::new);
        order.setOrderStatus(OrderStatus.valueOf(orderUpdate.getOrderStatus()));
    }

    public OrderDto getOrderById(Long orderId) {
        Order order = orderManagementRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        return OrderDtoMapper.map(order);
    }

    public Page<OrderDto> getOrdersByParameter(String searchBy, String value, int page) {
        final int size = 50;
        List<Order> orders = switch (searchBy){
            case "userId" -> orderManagementRepository.findOrdersByUserId(Long.parseLong(value));
            case "orderStatus" -> orderManagementRepository.findOrdersByOrderStatus(OrderStatus.valueOf(value));
            default -> throw new IllegalStateException("Unexpected value: " + searchBy);
        };
        List<OrderDto> currentPage = orders.subList(Math.min(page * size, orders.size()),
                Math.min(page * size + size, orders.size())).stream()
                .map(OrderDtoMapper::map).toList();
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(currentPage, pageRequest, orders.size());

    }

    public List<String> getAllOrderStatuses() {
        return Arrays.stream(OrderStatus.values())
                .map(OrderStatus::name).toList();
    }
}
