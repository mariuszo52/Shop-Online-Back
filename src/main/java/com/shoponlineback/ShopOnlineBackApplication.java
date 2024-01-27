package com.shoponlineback;

import com.shoponlineback.product.ScrapProductsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ShopOnlineBackApplication {
    private final static Logger LOGGER = LoggerFactory.getLogger(ShopOnlineBackApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(ShopOnlineBackApplication.class, args);;
        ScrapProductsService scrapProductsService = applicationContext.getBean(ScrapProductsService.class);
        try {
            scrapProductsService.fetchAllGames();
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }

    }

}
