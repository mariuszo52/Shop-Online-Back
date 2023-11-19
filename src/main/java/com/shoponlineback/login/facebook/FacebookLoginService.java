package com.shoponlineback.login.facebook;

import com.shoponlineback.exceptions.userRole.UserRoleNotFoundException;
import com.shoponlineback.urlConnectionService.UrlConnectionService;
import com.shoponlineback.user.User;
import com.shoponlineback.user.UserRepository;
import com.shoponlineback.user.UserService;
import com.shoponlineback.userInfo.UserInfo;
import com.shoponlineback.userRole.UserRole;
import com.shoponlineback.userRole.UserRoleRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

@Service
public class FacebookLoginService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final UserRoleRepository userRoleRepository;
    public final static String FB_HEADER_PREFIX = "FB ";
    private static final String APP_ID = "1062927578170362";
    private static final String SECRET_KEY = "b26135e22d86c3734dbe9f63203f5153";

    public FacebookLoginService(UserRepository userRepository, UserService userService, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.userRoleRepository = userRoleRepository;
    }

    void facebookLogin(FacebookLoginDto facebookLoginDto, HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(FB_HEADER_PREFIX.length());
        boolean tokenIsValid = verifyAccessToken(token);
        boolean userExist = userRepository.existsUserByEmail(facebookLoginDto.getEmail());
        if (tokenIsValid && !userExist) {
            UserRole userRole = userRoleRepository.findUserRoleByName("USER").orElseThrow(UserRoleNotFoundException::new);
            UserInfo userInfo = new UserInfo(facebookLoginDto.getFirstName(), facebookLoginDto.getLastName());
            User user = new User(facebookLoginDto.getUserId(), facebookLoginDto.getEmail(), userRole, userInfo);
            userRepository.save(user);
        }
    }

    public static boolean verifyAccessToken(String token) {
        try {
            URL url = new URIBuilder()
                    .setScheme("https")
                    .setHost("graph.facebook.com")
                    .setPath("/debug_token")
                    .setParameter("access_token", token)
                    .addParameter("input_token", APP_ID + "|" + SECRET_KEY)
                    .build().toURL();
            JSONObject jsonObject = getVerificationObject(url);
            return jsonObject.optJSONObject("data").optBoolean("is_valid", false);
        } catch (IOException | URISyntaxException e) {
            return false;
        }
    }

    public static String getFacebookUserId(String token) {
        try {
            URL url = new URIBuilder()
                    .setScheme("https")
                    .setHost("graph.facebook.com")
                    .setPath("/me")
                    .setParameter("access_token", token)
                    .build().toURL();
            return getVerificationObject(url).optString("id");
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static JSONObject getVerificationObject(URL url) throws URISyntaxException, IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        return UrlConnectionService.getConnectionResponse(urlConnection)
                .orElseThrow(() -> new RuntimeException("Error during verify access token."));
    }


}

