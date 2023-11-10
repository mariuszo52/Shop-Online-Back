package com.shoponlineback.login;

import com.shoponlineback.exceptions.user.UserNotFoundException;
import com.shoponlineback.user.User;
import com.shoponlineback.user.UserRepository;
import com.shoponlineback.user.dto.UserLoginDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final UserRepository userRepository;

    public LoginService(DaoAuthenticationProvider daoAuthenticationProvider, UserRepository userRepository) {
        this.daoAuthenticationProvider = daoAuthenticationProvider;
        this.userRepository = userRepository;
    }

    public boolean authenticateUser(UserLoginDto userLoginDto){
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userLoginDto.getUsername(), userLoginDto.getPassword());
        Authentication authenticate = daoAuthenticationProvider.authenticate(usernamePasswordAuthenticationToken);
        return authenticate.isAuthenticated();

    }
}
