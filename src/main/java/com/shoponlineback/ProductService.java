package com.shoponlineback;

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
import java.util.Collections;
import java.util.List;

@Service
public class ProductService {

    List<Game> getGamesList() throws IOException {
        List<Game> games = new ArrayList<>();
        JSONArray responseJSONArray = createJsonArrayFromResponse();
        for (int i=0; i<responseJSONArray.length() ; i++) {
            JSONObject jsonObject = (JSONObject) responseJSONArray.get(i);
            Game game = JsonObjectToGameMapper.map(jsonObject);
            games.add(game);
        }
        return games;
    }

    private static JSONArray createJsonArrayFromResponse() throws IOException {
        URL url = new URL("https://gateway.kinguin.net/esa/api/v1/products?page=1");
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
