package com.shoponlineback;

import com.shoponlineback.product.Product;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
public class ProductController {
    private final ProductService productService;
    private final static int ALL_PAGES = 620;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    List<Product> getProducts() throws IOException {
        return productService.getAllProducts(5);

    }
}
