package com.shoponlineback.login.standard;

import com.shoponlineback.GlobalControllerAdvice;
import com.shoponlineback.exceptions.AccountDisabledException;
import com.shoponlineback.jwt.JwtService;
import com.shoponlineback.user.dto.UserLoginDto;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@CrossOrigin
@RestController
public class LoginController {
    private final LoginService loginService;
    private final JwtService jwtService;
    @Value("${JWT_ACCESS_SECRET}")
    private String accessTokenSecret;
    @Value("${REFRESH_TOKEN_SECRET}")
    private String refreshTokenSecret;
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);


    public LoginController(LoginService loginService, JwtService jwtService) {
        this.loginService = loginService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDto userLoginDto){
        boolean isAuthenticated;
        try{
           isAuthenticated = loginService.authenticateUser(userLoginDto);

        }catch (AccountDisabledException e){
            return ResponseEntity.status(HttpStatus.LOCKED).body(e.getLocalizedMessage());
        }
        catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getLocalizedMessage());
        }
        if(isAuthenticated){
            String accessToken = jwtService
                    .generateToken(userLoginDto.getEmail(), 15, accessTokenSecret );
            String refreshToken = jwtService
                    .generateToken(userLoginDto.getEmail(), 60 * 24 * 30, refreshTokenSecret);
            LoginResponseDto loginResponseDto = new LoginResponseDto(accessToken, refreshToken);
            return ResponseEntity.status(HttpStatus.CREATED).body(loginResponseDto);
        }else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Bad Credentials.");
        }
    }
    @GetMapping("/login/forget-password")
    public ResponseEntity<String> sendResetPasswordConfirmEmail(@RequestParam String email){
        try {
            loginService.sendResetPasswordConfirmEmail(email);
            return ResponseEntity.ok("Mail sent");
        } catch (MessagingException e){
            LOGGER.error(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @PostMapping("/login/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody @NonNull ResetPasswordDto resetPasswordDto){
        try {
            loginService.resetPassword(resetPasswordDto.getToken(), resetPasswordDto.getNewPassword());
            return ResponseEntity.ok("Password has changed.");
        } catch (RuntimeException e ) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @GetMapping("/login/access-token")
    public ResponseEntity<String> getAccessToken(HttpServletRequest request){
        try {
            String accessToken = loginService.generateNewAccessToken(request);
            return ResponseEntity.ok(accessToken);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
