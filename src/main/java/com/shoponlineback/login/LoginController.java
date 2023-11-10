package com.shoponlineback.login;

import com.shoponlineback.jwt.JwtService;
import com.shoponlineback.user.dto.UserLoginDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity<String> login(@RequestBody UserLoginDto userLoginDto){
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
