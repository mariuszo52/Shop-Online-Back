package com.shoponlineback.login.standard;

import com.shoponlineback.email.EmailService;
import com.shoponlineback.register.RegisterService;
import com.shoponlineback.user.User;
import com.shoponlineback.user.UserRepository;
import com.shoponlineback.user.dto.UserLoginDto;
import jakarta.mail.MessagingException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginService {
    public final static String BEARER_PREFIX = "Bearer ";
    private final UserRepository userRepository;
    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public LoginService(UserRepository userRepository, DaoAuthenticationProvider daoAuthenticationProvider,
                        EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.daoAuthenticationProvider = daoAuthenticationProvider;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
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

    @Transactional
    public void sendResetPasswordConfirmEmail(String email) throws MessagingException {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User with this email not exists"));
        String token = RegisterService.generateActivationToken();
        user.setActivationToken(token);
        String link = "http://localhost:3000/account/login?token=" + token;
        emailService.sendPasswordResetEmail(link, email);
    }

    @Transactional
    public void resetPassword(String token, String newPassword){
        User user = userRepository.findUserByActivationToken(token)
                .orElseThrow(() -> new RuntimeException("Reset link not valid."));
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setActivationToken(null);
    }
}
