package com.shoponlineback.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.shoponlineback.exceptions.user.UserNotFoundException;
import com.shoponlineback.user.User;
import com.shoponlineback.user.UserRepository;
import com.shoponlineback.user.dto.UserLoginDto;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    private final UserRepository userRepository;

    public JwtService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String generateToken(UserLoginDto userLoginDto) {
        User user = userRepository.findUserByUsername(userLoginDto.getUsername()).orElseThrow(UserNotFoundException::new);
        long now = System.currentTimeMillis();
        return JWT.create()
                .withSubject(userLoginDto.getUsername())
                .withClaim("username", user.getUsername() )
                .withClaim("password", user.getPassword())
                .withClaim("role", user.getUserRole().getName())
                .withIssuedAt(new Date(now))
                .withExpiresAt(new Date(System.currentTimeMillis() + (1000 * 60 * 20)))
                .sign(Algorithm.HMAC512("secret"));

    }


}
