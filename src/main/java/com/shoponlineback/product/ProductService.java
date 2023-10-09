package com.shoponlineback.product;

import com.shoponlineback.product.dto.ProductDto;
import com.shoponlineback.product.mapper.JsonObjectToProductMapper;
import com.shoponlineback.product.mapper.ProductDtoMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ProductService {
    private final ProductDtoMapper productDtoMapper;
    private final ProductRepository productRepository;
    private final ProductPagingRepository productPagingRepository;
    private final static int REGION_EUROPE = 1;
    private final static int REGION_FREE = 3;

    public ProductService(ProductDtoMapper productDtoMapper, ProductRepository productRepository, ProductPagingRepository productPagingRepository) {
        this.productDtoMapper = productDtoMapper;
        this.productRepository = productRepository;
        this.productPagingRepository = productPagingRepository;
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


    private void saveProductsListPage(int page) throws IOException {
        JSONArray responseJSONArray = createJsonArrayFromResponse(page);
        for (int i = 0; i < responseJSONArray.length(); i++) {
            JSONObject jsonObject = (JSONObject) responseJSONArray.get(i);
            ProductDto productDto = JsonObjectToProductMapper.map(jsonObject);
            Product product = productDtoMapper.map(productDto);
            productRepository.save(product);
        }

    }


    private static JSONArray createJsonArrayFromResponse(int page) throws IOException {
        URL url = new URL("https://gateway.kinguin.net/esa/api/v1/products?regionId="
                + REGION_EUROPE + "&regionId=" + REGION_FREE + "&languages=Polish&limit=100&page=" +page);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestProperty("X-Api-Key", "98d4916a75acf3834bb87c6d223d5337");
        InputStream inputStream = httpURLConnection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        StringBuilder stringBuilder = new StringBuilder();
        int c = 0;
        while ((c = bufferedReader.read()) != -1) {
            stringBuilder.append((char) c);
        }
        String JSONArrayString = stringBuilder.substring(stringBuilder.indexOf("["), stringBuilder.lastIndexOf("]") + 1);
        return new JSONArray(JSONArrayString);
    }



}
