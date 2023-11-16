package com.shoponlineback.login.standard;

import com.shoponlineback.jwt.JwtService;
import com.shoponlineback.user.dto.UserLoginDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class LoginController {
    private final LoginService loginService;
    private final JwtService jwtService;

    public LoginController(LoginService loginService, JwtService jwtService) {
        this.loginService = loginService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserLoginDto userLoginDto){
        boolean isAuthenticated;
        try{
           isAuthenticated = loginService.authenticateUser(userLoginDto);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
        if(isAuthenticated){
            String token = jwtService.generateToken(userLoginDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(token);
        }else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bad credentials.");
        }
    }
}
