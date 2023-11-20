package com.shoponlineback.login.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.shoponlineback.exceptions.userRole.UserRoleNotFoundException;
import com.shoponlineback.user.User;
import com.shoponlineback.user.UserRepository;
import com.shoponlineback.user.UserService;
import com.shoponlineback.userInfo.UserInfo;
import com.shoponlineback.userRole.UserRole;
import com.shoponlineback.userRole.UserRoleRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class GoogleLoginService {
    public final static String GOOGLE_HEADER_PREFIX = "GOOGLE ";
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserService userService;

    public GoogleLoginService(UserRepository userRepository, UserRoleRepository userRoleRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.userService = userService;
    }

    void googleLogin(HttpServletRequest request) throws GeneralSecurityException, IOException {
        String authorization = request.getHeader("Authorization");
        String token = authorization.substring(GOOGLE_HEADER_PREFIX.length());
        final String clientId = "985874330130-mjutgkgsi961lgafhbkghnc4id8coa0r.apps.googleusercontent.com";
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singleton(clientId)).build();
        GoogleIdToken idToken = verifier.verify(token);
        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = (String) payload.get("email");
            registerUserIfNotExist(email, payload);
        } else {
            throw new RuntimeException("Invalid ID token.");
        }
    }

    private void registerUserIfNotExist(String email, GoogleIdToken.Payload payload) {
        boolean existsUser = userRepository.existsUserByEmail(email);
        if (!existsUser) {
            String name = (String) payload.get("name");
            String givenName = (String) payload.get("given_name");
            String username = userService.generateUsername(email);
            UserInfo userInfo = new UserInfo(name, givenName);
            UserRole userRole = userRoleRepository.findUserRoleByName("USER").orElseThrow(UserRoleNotFoundException::new);
            User user = new User(username, email, userRole, userInfo, true);
            userRepository.save(user);
        }
    }


}
