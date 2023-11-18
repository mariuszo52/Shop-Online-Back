package com.shoponlineback.urlConnectionService;

import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONObject;
import org.json.JSONPointer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class UrlConnectionService {
    public static Optional<JSONObject> getConnectionResponse(HttpURLConnection urlConnection) {
        try {
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            StringBuilder stringBuilder = new StringBuilder();
            int c = 0;
            while ((c = bufferedReader.read()) != -1) {
                stringBuilder.append((char) c);
            }
            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            return Optional.of(jsonObject);
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
