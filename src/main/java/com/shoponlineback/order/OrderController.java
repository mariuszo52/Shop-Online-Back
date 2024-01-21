package com.shoponlineback.order;

import com.shoponlineback.email.EmailService;
import com.shoponlineback.order.dto.OrderDto;
import com.shoponlineback.product.Product;
import com.shoponlineback.product.ProductService;
import com.shoponlineback.product.dto.ProductDto;
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
    private final ProductService productService;

    public OrderController(OrderService orderService, EmailService emailService, ProductService productService) {
        this.orderService = orderService;
        this.emailService = emailService;
        this.productService = productService;
    }

    @PostMapping("/order/checkout")
    ResponseEntity<?> addOrder(@RequestBody OrderDto orderDto) {
        Order order;
        try {
            order = orderService.saveOrder(orderDto);
            final String subject = "ORDER NR: " + order.getId() + " SUMMARY.";
            emailService.sendOrderConfirmationEmail(order.getId(), subject);
            productService.updateProductSellQuantity(order);
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