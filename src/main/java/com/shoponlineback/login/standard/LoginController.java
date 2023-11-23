package com.shoponlineback.login.standard;

import com.shoponlineback.jwt.JwtService;
import com.shoponlineback.user.dto.UserLoginDto;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.NonNull;
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
    @GetMapping("/login/forget-password")
    public ResponseEntity<String> sendResetPasswordConfirmEmail(@RequestParam String email){
        try {
            loginService.sendResetPasswordConfirmEmail(email);
            return ResponseEntity.ok("Mail sent");
        } catch (MessagingException | RuntimeException e ) {
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
        }

    }
}
