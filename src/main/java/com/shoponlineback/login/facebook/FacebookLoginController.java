package com.shoponlineback.login.facebook;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;

@RestController
@CrossOrigin
@RequestMapping("/login/facebook")
public class FacebookLoginController {
    private final FacebookLoginService facebookLoginService;

    public FacebookLoginController(FacebookLoginService facebookLoginService) {
        this.facebookLoginService = facebookLoginService;
    }

    @PostMapping()
    public ResponseEntity<String> googleLogin(HttpServletRequest request)  {
        try {
            facebookLoginService.facebookLogin(request);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
