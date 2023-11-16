package com.shoponlineback.login.google;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/login/google")
public class GoogleLoginController {
    private final GoogleLoginService googleLoginService;

    public GoogleLoginController(GoogleLoginService googleLoginService) {
        this.googleLoginService = googleLoginService;
    }

    @PostMapping
    public ResponseEntity<String> googleLogin(HttpServletRequest request) {
        try {
            googleLoginService.googleLogin(request);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (RuntimeException | GeneralSecurityException | IOException e) {
            return ResponseEntity.badRequest().body("Invalid Id token");
        }
    }
}
