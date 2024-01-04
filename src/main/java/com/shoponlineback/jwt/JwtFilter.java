package com.shoponlineback.jwt;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.shoponlineback.exceptions.user.UserNotFoundException;
import com.shoponlineback.login.facebook.FacebookLoginService;
import com.shoponlineback.user.User;
import com.shoponlineback.user.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.ConnectException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import static com.shoponlineback.login.facebook.FacebookLoginService.*;
import static com.shoponlineback.login.google.GoogleLoginService.GOOGLE_HEADER_PREFIX;
import static com.shoponlineback.login.standard.LoginService.BEARER_PREFIX;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final static String AUTHORIZATION_HEADER = "Authorization";
    private final UserRepository userRepository;
    private final FacebookLoginService facebookLoginService;
    @Value("${JWT_ACCESS_SECRET}")
    private String accessTokenSecret;
    @Value("${GOOGLE_CLIENT_ID}")
    private String googleClientId;

    public JwtFilter(UserRepository userRepository, FacebookLoginService facebookLoginService) {
        this.userRepository = userRepository;
        this.facebookLoginService = facebookLoginService;
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        return notFilterPaths(request);
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (request.getMethod().equals(HttpMethod.OPTIONS.name())) {
            filterChain.doFilter(request, response);
        } else if ((authorizationHeader == null)) {
            response.setStatus(FORBIDDEN.value());
        } else if (authorizationHeader.startsWith(GOOGLE_HEADER_PREFIX)) {
            String oauthToken = authorizationHeader.substring(GOOGLE_HEADER_PREFIX.length());
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singleton(googleClientId)).build();
            try {
                getGoogleTokenAuthorization(response, verifier, oauthToken);
                filterChain.doFilter(request, response);
            } catch (ConnectException | GeneralSecurityException e) {
                response.setStatus(FORBIDDEN.value());
            }
        } else if (authorizationHeader.startsWith(FB_HEADER_PREFIX)) {
            String oauthToken = authorizationHeader.substring(FB_HEADER_PREFIX.length());
            boolean tokenValid = facebookLoginService.verifyAccessToken(oauthToken);
            if (tokenValid) {
                String facebookUserId = getFacebookUserId(oauthToken);
                authenticateUser(facebookUserId);
            }
            filterChain.doFilter(request, response);
        } else {
            String token = authorizationHeader.substring(BEARER_PREFIX.length());
            try {
                getAuthorizationByToken(token);
                filterChain.doFilter(request, response);
            } catch (JwtException e) {
                response.setStatus(FORBIDDEN.value());
            }
        }
    }

    private void authenticateUser(String facebookUserId) {
        User user = userRepository.findUserByUsername(facebookUserId)
                .orElseThrow(UserNotFoundException::new);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user.getEmail(),
                null, List.of(new SimpleGrantedAuthority("USER")));
        authentication.setDetails(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    private void getGoogleTokenAuthorization(HttpServletResponse response,
                                             GoogleIdTokenVerifier verifier,
                                             String oauthToken) throws GeneralSecurityException, IOException {
        GoogleIdToken idToken = verifier.verify(oauthToken);
        if (idToken == null) {
            response.setStatus(403);
        } else {
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = (String) payload.get("email");
            User user = userRepository.findUserByEmail(email).orElseThrow(UserNotFoundException::new);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(email, null,
                            List.of(new SimpleGrantedAuthority(user.getUserRole().getName())));
            authentication.setDetails(user);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    private void getAuthorizationByToken(String token) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(accessTokenSecret).parseClaimsJws(token);
        String email = claims.getBody().get("email", String.class);
        String role = claims.getBody().get("role", String.class);
        User user = userRepository.findUserByEmail(email).orElseThrow(UserNotFoundException::new);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(email, null,
                        List.of(new SimpleGrantedAuthority(role)));
        authentication.setDetails(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private static boolean notFilterPaths(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.equals("/") || path.startsWith("/login") || path.startsWith("/register")
                || path.contains("/h2-console")
                || path.startsWith("/genre") || path.startsWith("/language")
                || path.contains("/platform") || path.startsWith("/product");
    }
}
