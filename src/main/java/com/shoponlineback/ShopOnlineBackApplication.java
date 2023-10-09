package com.shoponlineback;

import com.shoponlineback.product.ProductService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@SpringBootApplication
public class ShopOnlineBackApplication {
    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(ShopOnlineBackApplication.class, args);
        ProductService productService = applicationContext.getBean(ProductService.class);
        productService.saveAllProducts(4);
    }

}
