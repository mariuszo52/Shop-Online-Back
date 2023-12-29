package com.shoponlineback.login.standard;

import com.shoponlineback.email.EmailService;
import com.shoponlineback.exceptions.AccountDisabledException;
import com.shoponlineback.exceptions.user.UserNotFoundException;
import com.shoponlineback.jwt.JwtService;
import com.shoponlineback.register.RegisterService;
import com.shoponlineback.user.User;
import com.shoponlineback.user.UserRepository;
import com.shoponlineback.user.dto.UserLoginDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountLockedException;

@Service
public class LoginService {
    public final static String BEARER_PREFIX = "Bearer ";
    public final static String REFRESH_TOKEN_HEADER = "Refresh-Token";
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public LoginService(JwtService jwtService, UserRepository userRepository, DaoAuthenticationProvider daoAuthenticationProvider,
                        EmailService emailService, PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.daoAuthenticationProvider = daoAuthenticationProvider;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean authenticateUser(UserLoginDto userLoginDto){
        User user = userRepository.findUserByEmail(userLoginDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found."));
        if(!user.getIsEnabled()){
            throw new AccountDisabledException();
        }
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
        if(user.getPassword() == null){
            throw new RuntimeException("You cannot reset password. Please log in via Facebook or Google.");
        }
        String token = RegisterService.generateActivationToken();
        user.setActivationToken(token);
        String link = "http://localhost:3000/account/login?token=" + token;
        emailService.sendPasswordResetEmail(link, email);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findUserByActivationToken(token)
                .orElseThrow(() -> new RuntimeException("Reset link not valid."));
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setActivationToken(null);
    }

    public String generateNewAccessToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(REFRESH_TOKEN_HEADER);
        if (authorizationHeader != null) {
            String refreshToken = authorizationHeader.substring(BEARER_PREFIX.length());
            Jws<Claims> claims = Jwts.parser().setSigningKey("refresh").parseClaimsJws(refreshToken);
            String email = claims.getBody().get("email", String.class);
            if (userRepository.existsUserByEmail(email)) {
                return jwtService.generateToken(email, 15, "secret");
            } else {
                throw new RuntimeException("Error during fetching new access token");
            }
        } else {
            throw new RuntimeException("Empty Authorization header.");
        }
    }
}
