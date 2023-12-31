package com.shoponlineback.order;

import com.shoponlineback.email.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderController {
    private final OrderService orderService;
    private final EmailService emailService;

    public OrderController(OrderService orderService, EmailService emailService) {
        this.orderService = orderService;
        this.emailService = emailService;
    }

    @PostMapping("/order/checkout")
    ResponseEntity<?> addOrder(@RequestBody OrderDto orderDto) {
        Order order;
        try {
            order = orderService.saveOrder(orderDto);
            emailService.sendOrderConfirmationEmail(order);
            return ResponseEntity.ok().build();
        } catch (MessagingException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/orders")
    ResponseEntity<?> getUserAllOrders(){
        List<OrderDto> orders = orderService.getUserOrders();
        return ResponseEntity.ok(orders);
    }
}