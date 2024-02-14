package com.shoponlineback;

import com.shoponlineback.product.ScrapProductsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@Profile("dev")
public class ShopOnlineBackApplicationDev {

    private final static Logger LOGGER = LoggerFactory.getLogger(ShopOnlineBackApplicationDev.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(ShopOnlineBackApplicationDev.class, args);
        ScrapProductsService scrapProductsService = applicationContext.getBean(ScrapProductsService.class);
        try {
            scrapProductsService.fetchExampleGames();
            scrapProductsService.markUnavailableProducts();
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }

    }

}
