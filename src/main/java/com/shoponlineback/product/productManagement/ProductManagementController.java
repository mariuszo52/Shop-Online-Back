package com.shoponlineback.product.productManagement;

import com.shoponlineback.product.dto.ProductDto;
import com.shoponlineback.product.dto.ProductNameUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product-management")
public class ProductManagementController {
    private final ProductManagementService productManagementService;

    public ProductManagementController(ProductManagementService productManagementService) {
        this.productManagementService = productManagementService;
    }

    @GetMapping("/all-products")
    ResponseEntity<?> getAllProducts(@RequestParam  int page) {
        try {
            Page<ProductDto> products = productManagementService.getAllProducts(page);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/name")
    ResponseEntity<?> updateProductName(@RequestBody ProductNameUpdateDto productNameUpdateDto){
        try{
            productManagementService.updateProductName(productNameUpdateDto);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/price")
    ResponseEntity<?> updateProductPrice(@RequestBody ProductPriceUpdateDto productPriceUpdateDto){
        try{
            productManagementService.updateProductPrice(productPriceUpdateDto);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
