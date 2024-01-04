package com.shoponlineback.userProducts;

import com.shoponlineback.product.dto.ProductDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FavoriteProductsController {
    private final FavoriteProductsService favoriteProductsService;

    public FavoriteProductsController(FavoriteProductsService favoriteProductsService) {
        this.favoriteProductsService = favoriteProductsService;
    }

    @PostMapping("/favorite-product")
    ResponseEntity<?> addProductToFavorite(@RequestBody ProductDto productDto) {
        try {
            favoriteProductsService.addProduct(productDto);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/favorite-product")
    ResponseEntity<String> deleteProductFromFavorite(@RequestParam Long productId){
        try {
            favoriteProductsService.deleteProduct(productId);
            return ResponseEntity.noContent().build();
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/favorite-product")
    ResponseEntity<?> getUserFavoriteProducts() {
        try {
            List<ProductDto> productDtoList = favoriteProductsService.getUserFavoriteProducts();
            return ResponseEntity.ok(productDtoList);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/favorite-product/all")
    ResponseEntity<String> deleteAllUserFavoriteProducts(){
        try {
            favoriteProductsService.deleteAllUserFavoriteProducts();
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
