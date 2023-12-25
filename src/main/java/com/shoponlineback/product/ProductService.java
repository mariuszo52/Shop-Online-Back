package com.shoponlineback.product;

import com.shoponlineback.genre.Genre;
import com.shoponlineback.genre.GenreDto;
import com.shoponlineback.genre.GenreRepository;
import com.shoponlineback.language.Language;
import com.shoponlineback.language.LanguageDto;
import com.shoponlineback.language.LanguageRepository;
import com.shoponlineback.product.dto.ProductDto;
import com.shoponlineback.product.mapper.JsonObjectToProductMapper;
import com.shoponlineback.product.mapper.ProductDtoMapper;
import com.shoponlineback.productGenres.ProductGenres;
import com.shoponlineback.productGenres.ProductGenresRepository;
import com.shoponlineback.productLanguage.ProductLanguage;
import com.shoponlineback.productLanguage.ProductLanguageRepository;
import com.shoponlineback.screenshot.Screenshot;
import com.shoponlineback.screenshot.ScreenshotRepository;
import com.shoponlineback.urlConnectionService.UrlConnectionService;
import com.shoponlineback.video.Video;
import com.shoponlineback.video.VideoRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ProductService {
    private final ProductDtoMapper productDtoMapper;
    private final ProductRepository productRepository;
    private final ProductPagingRepository productPagingRepository;
    private final VideoRepository videoRepository;
    private final ScreenshotRepository screenshotRepository;
    private final JsonObjectToProductMapper jsonObjectToProductMapper;
    private final LanguageRepository languageRepository;
    private final ProductLanguageRepository productLanguageRepository;
    private final GenreRepository genreRepository;
    private final ProductGenresRepository productGenresRepository;
    private final static int REGION_EUROPE = 1;
    private final static int REGION_FREE = 3;

    public ProductService(ProductDtoMapper productDtoMapper, ProductRepository productRepository,
                          ProductPagingRepository productPagingRepository, VideoRepository videoRepository,
                          ScreenshotRepository screenshotRepository,
                          JsonObjectToProductMapper jsonObjectToProductMapper,
                          LanguageRepository languageRepository, ProductLanguageRepository productLanguageRepository, GenreRepository genreRepository, ProductGenresRepository productGenresRepository) {
        this.productDtoMapper = productDtoMapper;
        this.productRepository = productRepository;
        this.productPagingRepository = productPagingRepository;
        this.videoRepository = videoRepository;
        this.screenshotRepository = screenshotRepository;
        this.jsonObjectToProductMapper = jsonObjectToProductMapper;
        this.languageRepository = languageRepository;
        this.productLanguageRepository = productLanguageRepository;
        this.genreRepository = genreRepository;
        this.productGenresRepository = productGenresRepository;
    }

   ProductDto getProductById(long id){
       Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Cannot find product."));
       return ProductDtoMapper.map(product);
   }
    Page<ProductDto> getAllProducts(Sort sort){
        List<ProductDto> allProducts = StreamSupport.stream(productPagingRepository.findAll(sort).spliterator(), false)
                .map(ProductDtoMapper::map)
                .collect(Collectors.toList());
        return new PageImpl<>(allProducts);


    }
    @Transactional
   public void saveAllProducts(int pages) throws IOException {
        for (int i = 1; i <= pages; i++) {
            System.out.println("Procent: " + (i * 100 / pages));
            saveProductsListPage(i);
        }
   }

    @Transactional
    public void saveProductsListPage(int page) throws IOException {
        JSONArray responseJSONArray = createJsonArrayFromResponse(page);
        for (int i = 0; i < responseJSONArray.length(); i++) {
            JSONObject jsonObject = (JSONObject) responseJSONArray.get(i);
            ProductDto productDto = jsonObjectToProductMapper.map(jsonObject);
            List<String> languagesNames = productDto.getLanguages().stream()
                    .map(LanguageDto::getName)
                    .toList();
            List<Language> languages = languageRepository.findAllByNameIn(languagesNames);
            Product product = productDtoMapper.map(productDto);
            Product productEntity = productRepository.save(product);
            languages.forEach(language -> {
                ProductLanguage productLanguage = new ProductLanguage(product, language);
                productLanguageRepository.save(productLanguage);
            });
            List<String> genresNames = productDto.getGenres().stream()
                    .map(GenreDto::getName)
                    .toList();
            genreRepository.findByNameIn(genresNames).forEach(genre -> {
                productGenresRepository.save(new ProductGenres(product, genre));
            });
            List<Video> videos = jsonObjectToProductMapper.getVideos(jsonObject);
            videos.forEach(video -> {
                video.setProduct(productEntity);
                videoRepository.save(video);
            });
            List<Screenshot> screenshots = jsonObjectToProductMapper.getScreenshots(jsonObject);
            screenshots.forEach(screenshot ->{
                screenshot.setProduct(productEntity);
                screenshotRepository.save(screenshot);
            });
        }

    }


    private static JSONArray createJsonArrayFromResponse(int page) throws IOException {
        URL url = new URL("https://gateway.kinguin.net/esa/api/v1/products?regionId="
                + REGION_EUROPE + "&regionId=" + REGION_FREE + "&languages=Polish&limit=100&page=" +page);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestProperty("X-Api-Key", "98d4916a75acf3834bb87c6d223d5337");
        JSONObject jsonObject = UrlConnectionService.getConnectionResponse(httpURLConnection)
                .orElseThrow(() -> new RuntimeException("Error during fetching products."));
        return jsonObject.optJSONArray("results");
    }


    public List<ProductDto> getSimilarProducts(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found."));
        List<Product> allByPlatformName = productRepository.findAllByPlatform_Name(product.getPlatform().getName());
        Collections.shuffle(allByPlatformName);
        return allByPlatformName.stream()
                .limit(5)
                .map(ProductDtoMapper::map)
                .collect(Collectors.toList());

    }
}
