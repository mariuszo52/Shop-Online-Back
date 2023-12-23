package com.shoponlineback.cart;

import com.shoponlineback.product.Product;
import com.shoponlineback.product.dto.ProductDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/cart")
    ResponseEntity<?> addProductToCart(@RequestBody ProductDto productDto) {
        try {
            cartService.addProductToCart(productDto);
            List<ProductDto> loggedUserCart = cartService.getLoggedUserCart();
            return ResponseEntity.status(HttpStatus.CREATED).body(loggedUserCart);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/cart")
    ResponseEntity<?> getUserCartProducts(){
        try{
        List<ProductDto> productList = cartService.getLoggedUserCart();
            return ResponseEntity.ok(productList);
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PatchMapping("/cart/quantity")
    ResponseEntity<?> updateProductQuantity(@RequestBody ProductDto productDto){
        try{
            cartService.updateProductQuantity(productDto);
            return ResponseEntity.ok("Cart updated.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/cart")
    ResponseEntity<?> updateAllCart(@RequestBody List<ProductDto> cart){
        cartService.updateAllCart(cart);
        return ResponseEntity.ok(cart);

    }
    @DeleteMapping("/cart")
    ResponseEntity<String> clearCart(){
        try{
            cartService.clearCart();
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/cart/{id}")
    ResponseEntity<String> removeProductFromCart(@PathVariable Long id){
        try{
            cartService.removeProductFromCart(id);
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
