package com.shoponlineback.jwt;

import com.shoponlineback.exceptions.user.UserNotFoundException;
import com.shoponlineback.user.User;
import com.shoponlineback.user.UserRepository;
import com.shoponlineback.user.dto.UserLoginDto;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    private final UserRepository userRepository;

    public JwtService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String generateToken(String email, long accessTimeMinutes, String secret) {
        User user = userRepository.findUserByEmail(email).orElseThrow(UserNotFoundException::new);
        long now = System.currentTimeMillis();
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("role", user.getUserRole().getName());

        return Jwts.builder()
                .setHeaderParam("alg", "HS512")
                .setSubject(email)
                .addClaims(claims)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(System.currentTimeMillis() + (1000 * 15 * accessTimeMinutes)))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();


    }


}
