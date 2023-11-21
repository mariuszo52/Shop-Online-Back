package com.shoponlineback.login.facebook;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/login/facebook")
public class FacebookLoginController {
    private final FacebookLoginService facebookLoginService;

    public FacebookLoginController(FacebookLoginService facebookLoginService) {
        this.facebookLoginService = facebookLoginService;
    }

    @PostMapping()
    public ResponseEntity<String> facebookLogin(@RequestBody FacebookLoginDto facebookLoginDto, HttpServletRequest request)  {
        try {
            facebookLoginService.facebookLogin(facebookLoginDto, request);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}