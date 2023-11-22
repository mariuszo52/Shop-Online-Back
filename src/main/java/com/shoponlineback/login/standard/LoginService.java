package com.shoponlineback.login.standard;

import com.shoponlineback.email.EmailService;
import com.shoponlineback.register.RegisterService;
import com.shoponlineback.user.User;
import com.shoponlineback.user.UserRepository;
import com.shoponlineback.user.dto.UserLoginDto;
import jakarta.mail.MessagingException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    public final static String BEARER_PREFIX = "Bearer ";
    private final UserRepository userRepository;
    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final EmailService emailService;

    public LoginService(UserRepository userRepository, DaoAuthenticationProvider daoAuthenticationProvider, EmailService emailService) {
        this.userRepository = userRepository;
        this.daoAuthenticationProvider = daoAuthenticationProvider;
        this.emailService = emailService;
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

    public void sendResetPasswordConfirmEmail(String email) throws MessagingException {
        if(userRepository.existsUserByEmail(email)){
            String token = RegisterService.generateActivationToken();
            String link = "http://localhost:8080/account/login?token=" + token;
            emailService.sendPasswordResetEmail(link, email);
        }else{
            throw new RuntimeException("User with this email not exists");
        }
    }
}
