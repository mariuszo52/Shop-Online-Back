package com.shoponlineback.product;

import com.shoponlineback.genre.Genre;
import com.shoponlineback.genre.GenreRepository;
import com.shoponlineback.language.Language;
import com.shoponlineback.language.LanguageRepository;
import com.shoponlineback.platform.Platform;
import com.shoponlineback.platform.PlatformRepository;
import com.shoponlineback.productGenres.ProductGenres;
import com.shoponlineback.productGenres.ProductGenresRepository;
import com.shoponlineback.productLanguage.ProductLanguage;
import com.shoponlineback.productLanguage.ProductLanguageRepository;
import com.shoponlineback.screenshot.Screenshot;
import com.shoponlineback.screenshot.ScreenshotRepository;
import com.shoponlineback.urlConnectionService.UrlConnectionService;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class ScrapProductsService {
    private final GenreRepository genreRepository;
    private final PlatformRepository platformRepository;
    private final LanguageRepository languageRepository;
    private final ProductRepository productRepository;
    private final ProductGenresRepository productGenresRepository;
    private final ScreenshotRepository screenshotRepository;
    private final ProductLanguageRepository productLanguageRepository;

    public ScrapProductsService(GenreRepository genreRepository,
                                PlatformRepository platformRepository,
                                LanguageRepository languageRepository,
                                ProductRepository productRepository,
                                ProductGenresRepository productGenresRepository,
                                ScreenshotRepository screenshotRepository,
                                ProductLanguageRepository productLanguageRepository) {
        this.genreRepository = genreRepository;
        this.platformRepository = platformRepository;
        this.languageRepository = languageRepository;
        this.productRepository = productRepository;
        this.productGenresRepository = productGenresRepository;
        this.screenshotRepository = screenshotRepository;
        this.productLanguageRepository = productLanguageRepository;
    }

    @Transactional
    public void fetchPcGames() throws IOException {
        Document document = Jsoup.connect("https://www.cdkeys.com/pc?p=2")
                .header("Accept-Language", "pl_PL")
                .method(Connection.Method.GET).get();
        Elements pages = document.getElementsByClass("product-item-link");
        for (Element page : pages) {
            Attribute singleProductLink = page.attribute("href");
            Document gamePage = Jsoup.connect(singleProductLink.getValue()).get();
            String title = getTitle(gamePage);
            BigDecimal price = getPrice(gamePage);
            String description = getDescription(gamePage);
            String coverImage = getImageCover(gamePage);
            List<Genre> genres = getGenres(description);
            Platform platform = getPlatform(description, gamePage);
            String releaseDate = getReleaseDate(gamePage);
            String regionalLimitations = getRegionalLimitations(gamePage);
            List<Language> languages = getLanguages(gamePage);
            String videoUrl = getVideoUrl(gamePage);
            List<Screenshot> screenshots = getScreenshots(gamePage);
            Boolean isPreorder = isPreorder(gamePage);
            String activationDetails = getActivationDetails(gamePage);
            Product product = Product.builder()
                    .name(title)
                    .description(description)
                    .price(price)
                    .coverImage(coverImage)
                    .platform(platform)
                    .releaseDate(releaseDate)
                    .regionalLimitations(regionalLimitations)
                    .videoUrl(videoUrl)
                    .isPreorder(isPreorder)
                    .activationDetails(activationDetails).build();
            Product productEntity = productRepository.save(product);
            System.out.println(productEntity.getId() + " saved");
            genres.forEach(genre -> productGenresRepository.save(new ProductGenres(productEntity, genre)));
            screenshots.forEach(screenshot -> screenshot.setProduct(productEntity));
            languages.forEach(language -> productLanguageRepository.save(new ProductLanguage(productEntity, language)));
        }
    }

    private static String getActivationDetails(Document gamePage) {
        Elements activationListElements = gamePage.getElementsByClass("product attribute platforms").first()
                .getElementsByTag("li");
        StringBuilder activationDetails = new StringBuilder();
        for (Element activationListElement : activationListElements) {
            activationDetails.append(activationListElement.text()).append("\n");
        }
        return activationDetails.toString();
    }

    private static Boolean isPreorder(Document gamePage) {
        String buttonTitle = gamePage.getElementById("product-addtocart-button").attr("title");
        return !buttonTitle.equals("Buy Now");
    }

    private List<Screenshot> getScreenshots(Document gamePage) {
        List<Screenshot> screenshots = new ArrayList<>();
        Elements screenshotElements = gamePage.getElementsByClass("screenshot-full");
        for (Element screenshotElement : screenshotElements) {
            String screenshotUrl = screenshotElement.getElementsByTag("img").first()
                    .attr("src");
            Screenshot screenshot = screenshotRepository.save(new Screenshot(screenshotUrl));
            screenshots.add(screenshot);
        }
        return screenshots;
    }

    private static String getVideoUrl(Document gamePage) {
        Elements videoElements = gamePage.select(".product.attribute.video iframe");
        if (!videoElements.isEmpty()) {
            return videoElements.first().attr("data-src");
        } else {
            return null;
        }
    }


    private List<Language> getLanguages(Document gamePage) {
        Elements languageElements = gamePage.getElementsByClass("product attribute language").first()
                .getElementsByClass("language-flag");
        List<Language> languages = new ArrayList<>();
        for (Element languageElement : languageElements) {
            String name = languageElement.getElementsByTag("img").get(0).attribute("alt").toString();
            String iconUrl = languageElement.getElementsByTag("img").get(0).attribute("src").toString();
            Language language = new Language(name, iconUrl);
            if (!languageRepository.existsByName(name)) {
                Language languageEntity = languageRepository.save(language);
                languages.add(languageEntity);
            } else {
                Language languageEntity = languageRepository.findLanguageByName(name).get();
                languages.add(languageEntity);
            }
        }
        return languages;
    }

    private static String getRegionalLimitations(Document gamePage) throws IOException {
        String productId = gamePage.getElementsByClass("price-box price-final_price").first()
                .attr("data-product-id");
        URL url = new URL("https://www.cdkeys.com/restrictions/index/product/id/" + productId + "/locale/PL");
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        JSONObject responseObject = UrlConnectionService.getConnectionResponse(httpURLConnection)
                .orElseThrow(() -> new RuntimeException("Error during fetching product locale restrictions."));
        String message = responseObject.optString("message");
        return message.startsWith("Warning") ? "CANNOT ACTIVATE IN POLAND" : "CAN ACTIVATE IN POLAND";
    }

    private static String getReleaseDate(Document gamePage) {
        return gamePage.getElementsByClass("product attribute release_date").first()
                .getElementsByClass("value").first().text();
    }

    private Platform getPlatform(String description, Document gamePage) {
        int deviceStartIndex = description.lastIndexOf("Platform");
        int deviceLastIndex = description.lastIndexOf("(") -1;
        String deviceName = description.substring(deviceStartIndex, deviceLastIndex).substring(9);
        String platformName = gamePage.getElementsByClass("product attribute-icon attribute platforms").first()
                .getElementsByClass("value").first().text();
        return platformRepository.findByName(platformName)
                .orElseGet(() -> platformRepository.save(new Platform(platformName, deviceName)));
    }

    private List<Genre> getGenres(String description) {
        int genreStartIndex = description.lastIndexOf("Genre");
        int genreLastIndex = description.lastIndexOf("Platform") - 1;
        if (genreStartIndex != -1) {
            return Arrays.stream(description.substring(genreStartIndex, genreLastIndex).substring(6)
                            .trim().split(","))
                    .map(genreName -> genreRepository.findGenreByName(genreName)
                            .orElseGet(() -> genreRepository.save(new Genre(genreName))))
                    .toList();
        } else {
            return Collections.emptyList();
        }
    }

    private static String getImageCover(Document gamePage) {
        return gamePage.getElementsByClass("product media").get(0).getElementsByTag("img").get(0)
                .attribute("src").getValue();
    }

    private static String getDescription(Document gamePage) {
        Elements descriptionDiv = gamePage.getElementsByClass("product attribute description")
                .first().children();
        StringBuilder description = new StringBuilder();
        for (Element divElement : descriptionDiv) {
            description.append(divElement.text()).append("\n");
        }
        return description.toString();
    }

    private static BigDecimal getPrice(Document gamePage) {
        return BigDecimal.valueOf(Double.parseDouble(gamePage.getElementsByClass("price")
                .first().text().substring(3)));
    }

    private static String getTitle(Document gamePage) {
        return gamePage.getElementsByClass("page-title").first()
                .attr("data-text");
    }
}
