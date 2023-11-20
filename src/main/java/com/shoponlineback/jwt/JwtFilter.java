package com.shoponlineback.jwt;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.shoponlineback.exceptions.user.UserNotFoundException;
import com.shoponlineback.exceptions.userRole.UserRoleNotFoundException;
import com.shoponlineback.login.facebook.FacebookLoginService;
import com.shoponlineback.login.google.GoogleLoginService;
import com.shoponlineback.login.standard.LoginService;
import com.shoponlineback.user.User;
import com.shoponlineback.user.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
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

    public JwtFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
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
            final String clientId = "985874330130-mjutgkgsi961lgafhbkghnc4id8coa0r.apps.googleusercontent.com";
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singleton(clientId)).build();
            try {
                getGoogleTokenAuthorization(verifier, oauthToken);
                filterChain.doFilter(request, response);
            } catch (GeneralSecurityException e) {
                response.setStatus(FORBIDDEN.value());
            }
        } else if (authorizationHeader.startsWith(FB_HEADER_PREFIX)) {
            String oauthToken = authorizationHeader.substring(FB_HEADER_PREFIX.length());
            boolean tokenValid = verifyAccessToken(oauthToken);
            if (tokenValid) {
                String facebookUserId = getFacebookUserId(oauthToken);
                authenticateUser(facebookUserId);
            }
            filterChain.doFilter(request, response);
        } else {
            String token = authorizationHeader.substring(BEARER_PREFIX.length());
            getAuthorizationByToken(token);
            filterChain.doFilter(request, response);
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


    private void getGoogleTokenAuthorization(GoogleIdTokenVerifier verifier, String oauthToken) throws GeneralSecurityException, IOException {
        GoogleIdToken idToken = verifier.verify(oauthToken);
        GoogleIdToken.Payload payload = idToken.getPayload();
        String email = (String) payload.get("email");
        System.out.println(email);
        User user = userRepository.findUserByEmail(email).orElseThrow(UserNotFoundException::new);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(email, null,
                        List.of(new SimpleGrantedAuthority(user.getUserRole().getName())));
        authentication.setDetails(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void getAuthorizationByToken(String token) {
        Jws<Claims> claims = Jwts.parser().setSigningKey("secret").parseClaimsJws(token);
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
        return path.equals("/") || path.startsWith("/login") || path.startsWith("/register") || path.contains("/h2-console")
                || path.startsWith("/genre")
                || path.contains("/platform") || path.startsWith("/product");
    }
}
