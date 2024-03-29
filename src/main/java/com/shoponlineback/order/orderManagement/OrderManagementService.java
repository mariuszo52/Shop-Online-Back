package com.shoponlineback.order.orderManagement;

import com.shoponlineback.exceptions.order.OrderNotFoundException;
import com.shoponlineback.order.Order;
import com.shoponlineback.order.OrderDtoMapper;
import com.shoponlineback.order.OrderStatus;
import com.shoponlineback.order.dto.OrderDto;
import com.shoponlineback.order.dto.OrderUpdateDto;
import com.shoponlineback.orderProduct.OrderProduct;
import com.shoponlineback.orderProduct.OrderProductDto;
import com.shoponlineback.orderProduct.OrderProductMapper;
import com.shoponlineback.orderProduct.OrderProductRepository;
import com.shoponlineback.orderProduct.activationCode.ActivationCode;
import com.shoponlineback.orderProduct.activationCode.ActivationCodeRepository;
import com.shoponlineback.orderProduct.activationCode.ActivationCodeUpdateDto;
import com.shoponlineback.orderProduct.activationCode.OrderProductQuantityUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class OrderManagementService {
    private final OrderManagementRepository orderManagementRepository;
    private final OrderProductRepository orderProductRepository;
    private final ActivationCodeRepository activationCodeRepository;
    private final OrderProductMapper orderProductMapper;

    public OrderManagementService(OrderManagementRepository orderManagementRepository, OrderProductRepository orderProductRepository, ActivationCodeRepository activationCodeRepository, OrderProductMapper orderProductMapper) {
        this.orderManagementRepository = orderManagementRepository;
        this.orderProductRepository = orderProductRepository;
        this.activationCodeRepository = activationCodeRepository;
        this.orderProductMapper = orderProductMapper;
    }

    public Page<OrderDto> getAllOrders(int page) {
        PageRequest pageRequest = PageRequest.of(page, 50);
        return orderManagementRepository.findAll(pageRequest).map(OrderDtoMapper::map);
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
        PageRequest pageRequest = PageRequest.of(page, 50);
        return switch (searchBy) {
            case "userId" -> orderManagementRepository.findOrdersByUserId(Long.parseLong(value), pageRequest)
                    .map(OrderDtoMapper::map);
            case "orderStatus" ->
                    orderManagementRepository.findOrdersByOrderStatus(OrderStatus.valueOf(value), pageRequest)
                            .map(OrderDtoMapper::map);
            default -> throw new IllegalStateException("Unexpected value: " + searchBy);
        };


    }

    public List<String> getAllOrderStatuses() {
        return Arrays.stream(OrderStatus.values())
                .map(OrderStatus::name).toList();
    }

    public List<OrderProductDto> getOrderProducts(Long orderId) {
        return orderProductRepository.findOrderProductsByOrderId(orderId).stream()
                .map(orderProductMapper::map).toList();
    }

    @Transactional
    public void updateProductOrderCode(ActivationCodeUpdateDto codeUpdateDto) {
        OrderProduct orderProduct = orderProductRepository.findById(codeUpdateDto.getOrderProductId())
                .orElseThrow(OrderNotFoundException::new);
        if (orderProduct.getActivationCodes().size() < orderProduct.getQuantity()) {
            ActivationCode activationCode = new ActivationCode(codeUpdateDto.getCode(), orderProduct);
            activationCodeRepository.save(activationCode);
        } else {
            throw new RuntimeException("Cannot add more codes to this order product.");
        }
    }

    @Transactional
    public void updateProductOrderQuantity(OrderProductQuantityUpdateDto quantityUpdateDto) {
        OrderProduct orderProduct = orderProductRepository.findById(quantityUpdateDto.getOrderProductId())
                .orElseThrow(OrderNotFoundException::new);
        orderProduct.setQuantity(quantityUpdateDto.getQuantity());
    }
}
