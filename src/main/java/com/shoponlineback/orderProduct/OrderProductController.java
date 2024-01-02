package com.shoponlineback.orderProduct;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderProductController {
    private final OrderProductService orderProductService;

    public OrderProductController(OrderProductService orderProductService) {
        this.orderProductService = orderProductService;
    }

    @GetMapping("/order-product")
    ResponseEntity<?> getUserOrderProducts(){
        try{
            List<OrderProductDto> orderProducts = orderProductService.getOrderProducts();
            return ResponseEntity.ok(orderProducts);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
