package com.shoponlineback.product;

import com.shoponlineback.product.dto.ProductDto;
import com.shoponlineback.product.mapper.JsonObjectToProductMapper;
import com.shoponlineback.product.mapper.ProductDtoMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class ProductService {
    private final ProductRepository productRepository;


    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    List<ProductDto> getAllProducts(int pages) throws IOException {
        List<ProductDto> allProducts = new ArrayList<>();
        for (int i = 1; i <= pages; i++) {
            System.out.println("Procent: " + (i * 100 / pages));
            List<ProductDto> pageProducts = getProductsList(i);
            allProducts.addAll(pageProducts);
        }
        return allProducts;
    }


    private List<ProductDto> getProductsList(int page) throws IOException {
        List<ProductDto> products = new ArrayList<>();
        JSONArray responseJSONArray = createJsonArrayFromResponse(page);
        for (int i = 0; i < responseJSONArray.length(); i++) {
            JSONObject jsonObject = (JSONObject) responseJSONArray.get(i);
            ProductDto productDto = JsonObjectToProductMapper.map(jsonObject);
            Product product = ProductDtoMapper.map(productDto);
            products.add(productDto);
            //productRepository.save(product);
        }

        return products;
    }

    private static JSONArray createJsonArrayFromResponse(int page) throws IOException {
        URL url = new URL("https://gateway.kinguin.net/esa/api/v1/products?page=" + page + "&limit=100");
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
