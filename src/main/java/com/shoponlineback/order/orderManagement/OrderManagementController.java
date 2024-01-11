package com.shoponlineback.order.orderManagement;

import com.shoponlineback.order.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/order-management")
public class OrderManagementController {
    private final OrderManagementService orderManagementService;

    public OrderManagementController(OrderManagementService orderManagementService) {
        this.orderManagementService = orderManagementService;
    }

    @GetMapping("/all-orders")
    ResponseEntity<?> getAllOrders(@RequestParam int page){
        try{
        Page<OrderDto> orders = orderManagementService.getAllOrders(page);
        return ResponseEntity.ok(orders);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
