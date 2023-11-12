package com.shoponlineback.jwt;

import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final static String AUTHORIZATION_HEADER = "Authorization";

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
       return notFilterPaths(request);
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        if(request.getMethod().equals(HttpMethod.OPTIONS.name())){
            filterChain.doFilter(request, response);
        }
        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){
            response.setStatus(HttpStatus.FORBIDDEN.value());
        }
        else  {
            String token = authorizationHeader.substring(7);
            getAuthorizationByToken(token);
            filterChain.doFilter(request, response);
        }
    }

    private static void getAuthorizationByToken(String token) {
        Jws<Claims> claims = Jwts.parser().setSigningKey("secret").parseClaimsJws(token);
        String username = claims.getBody().get("username", String.class);
        String role = claims.getBody().get("role", String.class);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, List.of(new SimpleGrantedAuthority(role)));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    private static boolean notFilterPaths(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.equals("/") || path.equals("/login") || path.equals("/register") || path.contains("/h2-console")
                || path.equals("/genre") || path.contains("/device-genres") || path.contains("/language")
                || path.contains("/platform") || path.equals("/product") || path.equals("/products")
                || path.equals("/similar-products");
    }
}
