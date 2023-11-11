package com.shoponlineback;

import com.shoponlineback.product.ProductService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class ShopOnlineBackApplication {
    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(ShopOnlineBackApplication.class, args);
        ProductService productService = applicationContext.getBean(ProductService.class);
        productService.saveAllProducts(1);
    }

}
