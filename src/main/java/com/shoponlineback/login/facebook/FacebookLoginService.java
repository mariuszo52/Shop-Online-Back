package com.shoponlineback.login.facebook;

import com.shoponlineback.cart.Cart;
import com.shoponlineback.exceptions.userRole.UserRoleNotFoundException;
import com.shoponlineback.shippingAddress.ShippingAddress;
import com.shoponlineback.shippingAddress.ShippingAddressRepository;
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

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

@Service
public class FacebookLoginService {
    private final UserRepository userRepository;
    private final ShippingAddressRepository shippingAddressRepository;
    private final UserRoleRepository userRoleRepository;
    public final static String FB_HEADER_PREFIX = "FB ";
    @Value("${FACEBOOK_APP_ID}")
    private String facebookAppId;
    @Value("${FACEBOOK_SECRET_KEY}")
    private String facebookSecretKey;

    public FacebookLoginService(UserRepository userRepository,
                                ShippingAddressRepository shippingAddressRepository,
                                UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.shippingAddressRepository = shippingAddressRepository;
        this.userRoleRepository = userRoleRepository;
    }
    public String getAppId() {
        return facebookAppId;
    }

    void facebookLogin(FacebookLoginDto facebookLoginDto, HttpServletRequest request) throws LoginException {
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(FB_HEADER_PREFIX.length());
        boolean tokenIsValid = verifyAccessToken(token);
        boolean userExist = userRepository.existsUserByEmail(facebookLoginDto.getEmail());
        if (tokenIsValid && !userExist) {
            ShippingAddress shippingAddress = shippingAddressRepository.save(new ShippingAddress());
            UserRole userRole = userRoleRepository.findUserRoleByName("USER").orElseThrow(UserRoleNotFoundException::new);
            UserInfo userInfo = new UserInfo(facebookLoginDto.getFirstName(), facebookLoginDto.getLastName(), shippingAddress);
            User user = new User(facebookLoginDto.getUserId(), facebookLoginDto.getEmail(), userRole, userInfo, true, new Cart());
            userRepository.save(user);
        }
        if(userExist){
            User user = userRepository.findUserByEmail(facebookLoginDto.getEmail()).get();
            if(user.getPassword() != null){
                throw new LoginException("Your email is used in standard account.");
            }
        }
    }

    public boolean verifyAccessToken(String token) {
        try {
            URL url = new URIBuilder()
                    .setScheme("https")
                    .setHost("graph.facebook.com")
                    .setPath("/debug_token")
                    .setParameter("access_token", token)
                    .addParameter("input_token", facebookAppId + "|" + facebookSecretKey)
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

