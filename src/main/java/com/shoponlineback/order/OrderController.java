package com.shoponlineback.order;

import com.shoponlineback.email.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    private final OrderService orderService;
    private final EmailService emailService;

    public OrderController(OrderService orderService, EmailService emailService) {
        this.orderService = orderService;
        this.emailService = emailService;
    }

    @PostMapping("/order/checkout")
    ResponseEntity<?> addOrder(@RequestBody OrderDto orderDto){System.out.println(orderDto);
        Order order = orderService.saveOrder(orderDto);
        try {
            emailService.sendOrderConfirmationEmail(order);

        }catch (MessagingException e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }
}
