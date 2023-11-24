package com.shoponlineback.login.google;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@CrossOrigin
@RequestMapping("/login/google")
public class GoogleLoginController {
    private final GoogleLoginService googleLoginService;

    public GoogleLoginController(GoogleLoginService googleLoginService) {
        this.googleLoginService = googleLoginService;
    }

    @GetMapping()
    public ResponseEntity<String> googleLogin(HttpServletRequest request) {
        try {
            googleLoginService.googleLogin(request);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (RuntimeException | GeneralSecurityException | IOException e) {
            return ResponseEntity.badRequest().body("Invalid Id token");
        }
    }
}
