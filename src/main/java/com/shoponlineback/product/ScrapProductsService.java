package com.shoponlineback.product;

import com.shoponlineback.ShopOnlineBackApplicationProd;
import com.shoponlineback.genre.Genre;
import com.shoponlineback.genre.GenreRepository;
import com.shoponlineback.language.Language;
import com.shoponlineback.language.LanguageRepository;
import com.shoponlineback.platform.Platform;
import com.shoponlineback.platform.PlatformRepository;
import com.shoponlineback.product.productManagement.ScrapUrl;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.core.Local;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.shoponlineback.product.productManagement.ScrapUrl.*;

@Service
public class ScrapProductsService {
    private final static Logger LOGGER = LoggerFactory.getLogger(ShopOnlineBackApplicationProd.class);
    private final GenreRepository genreRepository;
    private final PlatformRepository platformRepository;
    private final LanguageRepository languageRepository;
    private final ProductRepository productRepository;
    private final ProductGenresRepository productGenresRepository;
    private final ScreenshotRepository screenshotRepository;
    private final ProductLanguageRepository productLanguageRepository;
    private final static Integer DEV_PAGE_NUMBER = 1;
    private final static Integer GAMES_FOR_PAGE = 20;
    private final static String DEFAULT_COVER_IMAGE_URL = "https://www.prokerala.com/movies/assets/img/no-poster-available.webp";

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
    @Scheduled(cron = "0 0 0 * * *")
    @Profile("dev")
    public void fetchExampleGames() {
        for (int i = 1; i <= DEV_PAGE_NUMBER; i++) {
            fetchGamesByDevice(PC_URL.value, i);
        }
        for (int i = 1; i <= DEV_PAGE_NUMBER; i++) {
            fetchGamesByDevice(XBOX_URL.value, i);
        }
        for (int i = 1; i <= DEV_PAGE_NUMBER; i++) {
            fetchGamesByDevice(NINTENDO_URL.value, i);
        }
        for (int i = 1; i <= DEV_PAGE_NUMBER; i++) {
            fetchGamesByDevice(PSN_URL.value, i);
        }
    }
    @Scheduled(cron = "0 0 0 * * *")
    @Profile("prod")
    public void fetchAllGames() {
        for (int i = 1; i <= getPagesByDevice(PC_URL); i++) {
            System.out.println("Fetching page " + i + " of " + getPagesByDevice(PC_URL) + " category: " + PC_URL.name());
            fetchGamesByDevice(PC_URL.value, i);
        }
        for (int i = 5; i <= getPagesByDevice(XBOX_URL); i++) {
            System.out.println("Fetching page " + i + " of " + getPagesByDevice(XBOX_URL) + " category: " + XBOX_URL.name());
            fetchGamesByDevice(XBOX_URL.value, i);
        }
        for (int i = 1; i <= getPagesByDevice(NINTENDO_URL); i++) {
            System.out.println("Fetching page " + i + " of " + getPagesByDevice(NINTENDO_URL) + " category: " + NINTENDO_URL.name());
            fetchGamesByDevice(NINTENDO_URL.value, i);
        }
        for (int i = 1; i <= getPagesByDevice(PSN_URL); i++) {
            System.out.println("Fetching page " + i + " of " + getPagesByDevice(PSN_URL) + " category: " + PSN_URL.name());
            fetchGamesByDevice(PSN_URL.value, i);
        }
        for (int i = 1; i <= getPagesByDevice(TOP_UP_URL); i++) {
            System.out.println("Fetching page " + i + " of " + getPagesByDevice(TOP_UP_URL) + " category: " + TOP_UP_URL.name());
            fetchGamesByDevice(TOP_UP_URL.value, i);
        }
    }
    @Transactional
    public void markUnavailableProducts(){
        List<Product> unavailableProducts = productRepository.findAllByAddedDateIsBefore(LocalDate.now());
        unavailableProducts.forEach(product -> product.setInStock(false));
    }

    private static int getPagesByDevice(ScrapUrl url) {
        Document document;
        int tries = 1;
        while (tries < 3) {
            try {
                document = Jsoup.connect(url.value)
                        .timeout(10000)
                        .get();
                Elements toolbarNumber = document.getElementsByClass("toolbar-number");
                if (!toolbarNumber.isEmpty()) {
                    Elements elementsPage = toolbarNumber.first().getElementsByTag("span");
                    if (!elementsPage.isEmpty()) {
                        return Integer.parseInt(elementsPage.last().text()) / GAMES_FOR_PAGE;
                    }
                }
            } catch (IOException e) {
                tries++;
                System.out.println("Cannot connect with product page. Try " + tries + " / 3");
            }
        }
        return -1;
    }

    private Document connectWithPageToScrap(String url, int page) {
        int tries = 1;
        while (tries < 3) {
            try {
                return Jsoup.connect(url + "?p=" + page)
                        .header("Accept-Language", "pl_PL")
                        .timeout(10000)
                        .method(Connection.Method.GET).get();
            } catch (IOException e) {
                tries++;
                System.out.println("Cannot connect with product. Try " + tries + " / 3");
            }
        }
        return null;
    }

    private Document connectWithProductToScrap(String url) {
        int tries = 1;
        while (tries < 3) {
            try {
                return Jsoup.connect(url)
                        .header("Accept-Language", "pl_PL")
                        .timeout(10000)
                        .method(Connection.Method.GET).get();
            } catch (IOException e) {
                tries++;
                System.out.println("Cannot connect with product. Try " + tries + " / 3");
            }
        }
        return null;
    }


    public void fetchGamesByDevice(String url, int page) {
        Document document = connectWithPageToScrap(url, page);
        if (document != null) {
            Elements products = document.getElementsByClass("product-items").first()
                    .getElementsByClass("product-item-info");
            for (Element product : products) {
                try {
                    saveSingleProduct(product);
                }catch (Exception e){
                    LOGGER.error(e.getMessage());
                }
            }
        }

    }
    public void saveSingleProduct(Element product) {
        Attribute singleProductLink = product.getElementsByTag("a").first().attribute("href");
        Document gamePage = connectWithProductToScrap(singleProductLink.getValue());
        if (gamePage != null) {
            try {
                String title = getTitle(gamePage);
                BigDecimal price = getPrice(gamePage);
                BigDecimal oldPrice = getOldPrice(gamePage);
                Boolean isPreorder = isPreorder(gamePage);
                Boolean inStock = inStock(gamePage);
                if(!productRepository.existsByName(title)) {
                    addNewProduct(gamePage, title, price, isPreorder, inStock, oldPrice);
                }else {
                    updateProduct(title, price, isPreorder, inStock, oldPrice);
                }
            }catch (IOException e){
                LOGGER.error(e.getMessage());
                System.out.println("Cannot save product " + e.getMessage());
            }
        }
    }

    private BigDecimal getOldPrice(Document gamePage) throws IOException {
        Elements oldPriceElements = gamePage.getElementsByClass("old-price");
        if(!oldPriceElements.isEmpty()){
            Elements price = oldPriceElements.first().getElementsByClass("price");
            if (!price.isEmpty()) {
                int lastIndex = price.first().text().indexOf(" ");
                return BigDecimal.valueOf(Double.parseDouble(price.first().text().substring(0, lastIndex)
                        .replace(",", ".")) + 30);
            }
        }
        throw new IOException("Cannot find class to scrap product old price.");

    }

    private void addNewProduct(Document gamePage, String title,
                               BigDecimal price, Boolean isPreorder, Boolean inStock,
                               BigDecimal oldPrice) throws IOException {
        String regionalLimitations = getRegionalLimitations(gamePage);
        String description = getDescription(gamePage);
        String coverImage = getImageCover(gamePage);
        List<Genre> genres = getGenres(description);
        Platform platform = getPlatform(description, gamePage);
        String releaseDate = getReleaseDate(gamePage);
        List<Language> languages = getLanguages(gamePage);
        String videoUrl = getVideoUrl(gamePage);
        List<Screenshot> screenshots = getScreenshots(gamePage);
        String activationDetails = getActivationDetails(gamePage);
        Product productToSave = Product.builder()
                .name(title)
                .description(description)
                .price(price)
                .oldPrice(oldPrice)
                .coverImage(coverImage)
                .platform(platform)
                .releaseDate(releaseDate)
                .regionalLimitations(regionalLimitations)
                .videoUrl(videoUrl)
                .isPreorder(isPreorder)
                .activationDetails(activationDetails)
                .addedDate(LocalDate.now())
                .inStock(inStock).build();
        Product productEntity = productRepository.save(productToSave);
        System.out.println(productEntity.getId() + " saved");
        genres.forEach(genre -> productGenresRepository.save(new ProductGenres(productEntity, genre)));
        screenshots.forEach(screenshot -> {
            screenshot.setProduct(productEntity);
            screenshotRepository.save(screenshot);
        });
        languages.forEach(language -> productLanguageRepository.save(new ProductLanguage(productEntity, language)));
    }

    private void updateProduct(String title, BigDecimal price, Boolean isPreorder,
                               Boolean inStock, BigDecimal oldPrice) {
        Product productToUpdate = productRepository.findByName(title).get();
        if(!productToUpdate.getPrice().equals(price)){
            productToUpdate.setPrice(price);
        }
        if (productToUpdate.getOldPrice() != null && !productToUpdate.getOldPrice().equals(oldPrice)) {
            productToUpdate.setOldPrice(oldPrice);
        }
        if(productToUpdate.getIsPreorder() != isPreorder){
            productToUpdate.setIsPreorder(isPreorder);
        }
        if(productToUpdate.getInStock() != inStock){
            productToUpdate.setInStock(inStock);
        }
        productToUpdate.setAddedDate(LocalDate.now());
        productRepository.save(productToUpdate);
    }

    private static String getActivationDetails(Document gamePage) {
        Elements productAttributePlatforms = gamePage.getElementsByClass("product attribute platforms");
        if (!productAttributePlatforms.isEmpty()) {
            Element activationListElementFirst = productAttributePlatforms.first();
            Elements activationListElements = activationListElementFirst.getElementsByTag("li");
            StringBuilder activationDetails = new StringBuilder();
            for (Element activationListElement : activationListElements) {
                activationDetails.append(activationListElement.text()).append("\n");
            }
            return activationDetails.toString().replace("CDKeys.com", "cd-keys.com.pl");
        }
        return null;
    }

    private static Boolean isPreorder(Document gamePage) {
        Element buttonTitle = gamePage.getElementById("product-addtocart-button");
        if (buttonTitle != null) {
            return buttonTitle.attr("title").equals("Buy Now");
        }
        return null;

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
        List<Language> languages = new ArrayList<>();
        Elements productAttributesLanguage = gamePage.getElementsByClass("product attribute language");
        if (!productAttributesLanguage.isEmpty()) {
            Elements languageElements = productAttributesLanguage.first()
                    .getElementsByClass("language-flag");
            for (Element languageElement : languageElements) {
                String name = languageElement.getElementsByTag("img").first().attr("alt");
                String iconUrl = languageElement.getElementsByTag("img").first().attr("src");
                Language language = new Language(name, iconUrl);
                if (!languageRepository.existsByName(name)) {
                    Language languageEntity = languageRepository.save(language);
                    languages.add(languageEntity);
                } else {
                    Language languageEntity = languageRepository.findLanguageByName(name).get();
                    languages.add(languageEntity);
                }
            }
        }
        return languages;
    }

    private static String getRegionalLimitations(Document gamePage) throws IOException {
        Elements productIdDivs = gamePage.getElementsByClass("price-box price-final_price");
        if (!productIdDivs.isEmpty()) {
            String productId = productIdDivs.first().attr("data-product-id");
            URL url = new URL("https://www.cdkeys.com/restrictions/index/product/id/" + productId + "/locale/PL");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            JSONObject responseObject = UrlConnectionService.getConnectionResponse(httpURLConnection)
                    .orElseThrow(() -> new RuntimeException("Error during fetching product locale restrictions."));
            String message = responseObject.optString("message");
            return message.startsWith("Warning") ? "CANNOT ACTIVATE IN POLAND" : "CAN ACTIVATE IN POLAND";
        }
        return null;
    }

    private static String getReleaseDate(Document gamePage) {
        Elements productAttributeReleaseDate = gamePage.getElementsByClass("product attribute release_date");
        if (!productAttributeReleaseDate.isEmpty()) {
            return productAttributeReleaseDate.first().getElementsByClass("value").first().text();
        }
        return null;
    }

    private Platform getPlatform(String description, Document gamePage) {
        int deviceStartIndex = description.lastIndexOf("Platform");
        int deviceLastIndex = description.lastIndexOf("(") - 1;
        if (deviceStartIndex > -1 && deviceLastIndex > -1) {
            String deviceName = description.substring(deviceStartIndex, deviceLastIndex).substring(9);
            String platformName = gamePage.getElementsByClass("product attribute-icon attribute platforms").first()
                    .getElementsByClass("value").first().text();
            return platformRepository.findByName(platformName)
                    .orElseGet(() -> platformRepository.save(new Platform(platformName, deviceName)));
        } else {
            return platformRepository.findByName("OTHER")
                    .orElseGet(() -> platformRepository.save(new Platform("OTHER", "OTHER")));
        }
    }

    private List<Genre> getGenres(String description) {
        int genreStartIndex = description.lastIndexOf("Genre");
        int genreLastIndex = description.lastIndexOf("Platform") - 1;
        if (genreStartIndex > -1 && genreLastIndex > -1) {
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
        Elements productMediaElements = gamePage.getElementsByClass("product media");
        if (!productMediaElements.isEmpty()) {
            Elements imgElements = productMediaElements.first().getElementsByTag("img");
            if (!imgElements.isEmpty()) {
                Attribute src = imgElements.first().attribute("src");
                if (src != null) {
                    return src.getValue();
                }
            }
        }
        return DEFAULT_COVER_IMAGE_URL;
    }

    private static String getDescription(Document gamePage) {
        Elements descriptionDiv = gamePage.getElementsByClass("product attribute description");
        if(!descriptionDiv.isEmpty()){
            Elements descriptionParagraphs = descriptionDiv.first().children();
            StringBuilder description = new StringBuilder();
            for (Element divElement : descriptionParagraphs) {
                description.append(divElement.text()).append("\n");
            }
            return description.toString().replace("CDKeys.com", "cd-keys.com.pl");
        }
        return "DESCRIPTION NOT AVAILABLE";
    }

    private static BigDecimal getPrice(Document gamePage) throws IOException {
        Elements priceElements = gamePage.getElementsByClass("price");
        if(!priceElements.isEmpty()){
            int lastIndex = priceElements.first().text().indexOf(" ");
            return BigDecimal.valueOf(Double.parseDouble(priceElements.first().text().substring(0, lastIndex)
                    .replace(",", ".")) + 30);
        }
        throw new IOException("Cannot find class to scrap product price.");
    }

    private static String getTitle(Document gamePage) throws IOException {
        Elements pageTitleElements = gamePage.getElementsByClass("page-title");
        if (!pageTitleElements.isEmpty()) {
            return pageTitleElements.first().attr("data-text");
        }
        throw new IOException("Cannot find class to scrap product title.");
    }

    private static Boolean inStock(Document gamePage) {
        Elements divElements = gamePage.getElementsByClass("product-usps-item attribute stock available");
        if (!divElements.isEmpty()) {
            Elements textElements = divElements.first().getElementsByClass("product-usps-text");
            if (!textElements.isEmpty()) {
                return textElements.first().text().equalsIgnoreCase("CURRENTLY IN STOCK");
            }
        }
        return null;
    }
}
