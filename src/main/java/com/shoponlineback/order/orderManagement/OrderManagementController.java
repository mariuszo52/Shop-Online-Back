package com.shoponlineback.order.orderManagement;

import com.shoponlineback.order.dto.OrderDto;
import com.shoponlineback.order.dto.OrderUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/order-management")
public class OrderManagementController {
    private final OrderManagementService orderManagementService;

    public OrderManagementController(OrderManagementService orderManagementService) {
        this.orderManagementService = orderManagementService;
    }

    @GetMapping("/all-orders")
    ResponseEntity<?> getAllOrders(@RequestParam int page) {
        try {
            Page<OrderDto> orders = orderManagementService.getAllOrders(page);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/order-status")
    ResponseEntity<String> updateOrderStatus(@RequestBody OrderUpdateDto orderUpdate) {
        try {
            orderManagementService.updateOrderStatus(orderUpdate);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/order")
    ResponseEntity<?> getOrderById(@RequestParam Long orderId) {
        try {
            OrderDto orderDto = orderManagementService.getOrderById(orderId);
            return ResponseEntity.ok(orderDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/orders")
    ResponseEntity<?> getOrdersByParameter(@RequestParam String searchBy,
                                           @RequestParam String value,
                                           @RequestParam int page) {
        try {
            Page<OrderDto> orders = orderManagementService.getOrdersByParameter(searchBy, value, page);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/all-statuses")
    ResponseEntity<?> getAllOrderStatuses(){
        List<String> orderStatuses = orderManagementService.getAllOrderStatuses();
        return ResponseEntity.ok(orderStatuses);
    }
}
