package com.shoponlineback.login.facebook;

import com.shoponlineback.urlConnectionService.UrlConnectionService;
import io.jsonwebtoken.impl.Base64UrlCodec;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
public class FacebookLoginService {
    private static final String APP_ID = "1062927578170362";
    private static final String SECRET_KEY = "b26135e22d86c3734dbe9f63203f5153";

    void facebookLogin(HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");
        String signedRequest = request.getHeader("Signed_request");
        String token = authorizationHeader.substring(6);
        boolean isValid = verifyAccessToken(token);
        if(isValid){
            String[] splitSignedRequest = signedRequest.split("\\.");
            Base64UrlCodec base64UrlCodec = new Base64UrlCodec();
            String jwt = new String(base64UrlCodec.decode(splitSignedRequest[1]), StandardCharsets.UTF_8);
            System.out.println(jwt);


        }

    }

    private static boolean verifyAccessToken(String token) {
        try {
            URL url = new URIBuilder()
                    .setScheme("https")
                    .setHost("graph.facebook.com")
                    .setPath("/debug_token")
                    .setParameter("access_token", token)
                    .addParameter("input_token", APP_ID + "|" + SECRET_KEY)
                    .build().toURL();
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            JSONObject jsonObject = UrlConnectionService.getConnectionResponse(urlConnection)
                    .orElseThrow(() -> new RuntimeException("Error during verify access token."));
            return jsonObject.optJSONObject("data").optBoolean("is_valid", false);
        } catch (IOException | URISyntaxException e) {
            return false;
        }
    }


}

