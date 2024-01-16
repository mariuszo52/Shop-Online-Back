package com.shoponlineback.product;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

@Service
public class ScrapProductsService {

    public void fetchPcGames() throws IOException {
        Document document = Jsoup.connect("https://www.cdkeys.com/pc?p=1")
                .method(Connection.Method.GET).get();
        Elements elementsByClass = document.getElementsByClass("product-item-link");
        Set<Product> gameNames = new TreeSet<>(Comparator.comparing(Product::getName));
        for (Element element : elementsByClass) {
            System.out.println("start");
            Product product = new Product();
            Attribute attribute = element.attribute("href");
            Document gamePage = Jsoup.connect(attribute.getValue()).get();
            String title = gamePage.getElementsByClass("page-title").get(0)
                    .attribute("data-text").getValue();
            BigDecimal price = BigDecimal.valueOf(Double.parseDouble(gamePage.getElementsByClass("price")
                    .get(0).text().substring(3)));
            Elements descriptionDiv = gamePage.getElementsByClass("product attribute description").get(0).children();
            StringBuilder description = new StringBuilder();
            for (Element divElement : descriptionDiv) {
                description.append(divElement.text()).append("\n");
            }
            System.out.println("PRICE " + price + " " + description );
            System.out.println("stop");
        }
        System.out.println(gameNames);

    }
}
