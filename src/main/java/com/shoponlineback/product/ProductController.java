package com.shoponlineback.product;

import com.shoponlineback.genre.GenreDto;
import com.shoponlineback.language.LanguageDto;
import com.shoponlineback.product.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.*;

@RestController
@RequestMapping("/product")
@CrossOrigin
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    ResponseEntity<?> getProductInfo(@RequestParam Long id) {
        try {
            ProductDto productById = productService.getProductById(id);
            return ResponseEntity.ok(productById);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/products")
    Page<ProductDto> getProducts(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "25") int size,
                                 @RequestParam Optional<String> name,
                                 @RequestParam Optional<String> device,
                                 @RequestParam Optional<String> platform,
                                 @RequestParam Optional<String> genre,
                                 @RequestParam Optional<String> language,
                                 @RequestParam Optional<BigDecimal> minPrice,
                                 @RequestParam Optional<BigDecimal> maxPrice,
                                 @RequestParam(defaultValue = "DESC,name") String sort) {
        String[] sortTable = sort.split(",");
        Sort sortBy = Sort.by(fromString(sortTable[0]),sortTable[1]);
        return productService.getProducts(page, size, sortBy, name, device, platform, genre, language, minPrice, maxPrice);
    }

    @GetMapping("/similar-products")
    ResponseEntity<List<ProductDto>> getSimilarProducts(@RequestParam Long id){
       List<ProductDto> similarProducts = productService.getSimilarProducts(id);
        return ResponseEntity.ok(similarProducts);
    }



}

