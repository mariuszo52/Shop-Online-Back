package com.shoponlineback.product;

import com.shoponlineback.genre.Genre;
import com.shoponlineback.genre.GenreRepository;
import com.shoponlineback.language.Language;
import com.shoponlineback.language.LanguageRepository;
import com.shoponlineback.platform.Platform;
import com.shoponlineback.platform.PlatformRepository;
import com.shoponlineback.urlConnectionService.UrlConnectionService;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Service
public class ScrapProductsService {
    private final GenreRepository genreRepository;
    private final PlatformRepository platformRepository;
    private final LanguageRepository languageRepository;

    public ScrapProductsService(GenreRepository genreRepository, PlatformRepository platformRepository, LanguageRepository languageRepository) {
        this.genreRepository = genreRepository;
        this.platformRepository = platformRepository;
        this.languageRepository = languageRepository;
    }

    public void fetchPcGames() throws IOException {
        Document document = Jsoup.connect("https://www.cdkeys.com/pc?p=1")
                .header("Accept-Language", "pl_PL")
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
            Elements descriptionDiv = gamePage.getElementsByClass("product attribute description")
                    .get(0).children();
            StringBuilder description = new StringBuilder();
            for (Element divElement : descriptionDiv) {
                description.append(divElement.text()).append("\n");
            }
            String imageCover = gamePage.getElementsByClass("product media").get(0).getElementsByTag("img").get(0)
                    .attribute("src").getValue();
            int genreStartIndex = description.lastIndexOf("Genre");
            int genreLastIndex = description.lastIndexOf("Platform") - 1;
            List<Genre> genreList = Arrays.stream(description.substring(genreStartIndex, genreLastIndex).substring(6)
                            .trim().split(","))
                    .map(genreName -> genreRepository.findGenreByName(genreName)
                            .orElseGet(() -> genreRepository.save(new Genre(genreName))))
                    .toList();
            int deviceStartIndex = description.lastIndexOf("Platform");
            int deviceLastIndex = description.lastIndexOf("(");
            String deviceName = description.substring(deviceStartIndex, deviceLastIndex).substring(9);
            String platformName = gamePage.getElementsByClass("product attribute-icon attribute platforms").get(0)
                    .getElementsByClass("value").get(0).text();
            Platform platform = platformRepository.findByName(platformName)
                    .orElseGet(() -> platformRepository.save(new Platform(platformName, deviceName)));
            String releaseDate = gamePage.getElementsByClass("product attribute release_date").get(0)
                    .getElementsByClass("value").get(0).text();
            String productId = gamePage.getElementsByClass("price-box price-final_price").get(0)
                    .attribute("data-product-id").getValue();
            URL url = new URL("https://www.cdkeys.com/restrictions/index/product/id/" + productId + "/locale/PL");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            JSONObject responseObject = UrlConnectionService.getConnectionResponse(httpURLConnection)
                    .orElseThrow(() -> new RuntimeException("Error during fetching product locale restrictions."));
            String message = responseObject.optString("message");
            String regionalLimitations = message.startsWith("Warning") ? "CANNOT ACTIVATE IN POLAND" : "CAN ACTIVATE IN POLAND";
            System.out.println(title);
            Elements languageElements = gamePage.getElementsByClass("product attribute language").get(0)
                    .getElementsByClass("language-flag");
            List<Language> languages = new ArrayList<>();
            for (Element languageElement : languageElements) {
                String name = languageElement.getElementsByTag("img").get(0).attribute("alt").toString();
                String iconUrl = languageElement.getElementsByTag("img").get(0).attribute("src").toString();
                Language language = new Language(name, iconUrl);
                if(!languageRepository.existsByName(name)){
                    Language languageEntity = languageRepository.save(language);
                    languages.add(languageEntity);
                }else {
                    Language languageEntity = languageRepository.findLanguageByName(name).get();
                    languages.add(languageEntity);
                }
            }
            System.out.println(languages);

            System.out.println("stop");

        }
    }
}
