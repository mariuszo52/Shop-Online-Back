package com.shoponlineback.userProducts;

import com.shoponlineback.product.dto.ProductDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/favorite-product")
    ResponseEntity<?> addProductToFavorite() {
        try {
            List<ProductDto> productDtoList = favoriteProductsService.getUserFavoriteProducts();
            return ResponseEntity.ok(productDtoList);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
