package com.shoponlineback;

import com.shoponlineback.product.Product;
import com.shoponlineback.product.ProductRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final JsonObjectToProductMapper jsonObjectToProductMapper;

    public ProductService(ProductRepository productRepository, JsonObjectToProductMapper jsonObjectToProductMapper) {
        this.productRepository = productRepository;
        this.jsonObjectToProductMapper = jsonObjectToProductMapper;
    }
    List<Product> getAllProducts(int pages) throws IOException {
        List<Product> allProducts = new ArrayList<>();
        for (int i = 1; i <= pages; i++) {
            System.out.println("Ukończono procent: " + (i * 100/pages));
            List<Product> pageProducts = getProductsList(i);
            allProducts.addAll(pageProducts);
        }
        return allProducts;
    }



    private List<Product> getProductsList(int page) throws IOException {
        List<Product> products = new ArrayList<>();
        JSONArray responseJSONArray = createJsonArrayFromResponse(page);
        for (int i=0; i<responseJSONArray.length() ; i++) {
            JSONObject jsonObject = (JSONObject) responseJSONArray.get(i);
            Product product = JsonObjectToProductMapper.map(jsonObject);
            products.add(product);
        }
        productRepository.saveAll(products);
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
