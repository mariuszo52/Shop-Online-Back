package com.shoponlineback.login.standard;

import com.shoponlineback.user.User;
import com.shoponlineback.user.UserRepository;
import com.shoponlineback.user.dto.UserLoginDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    public final static String BEARER_PREFIX = "Bearer ";
    private final UserRepository userRepository;
    private final DaoAuthenticationProvider daoAuthenticationProvider;

    public LoginService(UserRepository userRepository, DaoAuthenticationProvider daoAuthenticationProvider) {
        this.userRepository = userRepository;
        this.daoAuthenticationProvider = daoAuthenticationProvider;
    }

    public boolean authenticateUser(UserLoginDto userLoginDto) {
        User user = userRepository.findUserByEmail(userLoginDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found."));
        if (user.getPassword() != null) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(userLoginDto.getEmail(), userLoginDto.getPassword());
            return daoAuthenticationProvider.authenticate(usernamePasswordAuthenticationToken).isAuthenticated();
        } else {
            throw new RuntimeException("Please login via Google or Facebook.");
        }
    }
}
