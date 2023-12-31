package com.shoponlineback.login.facebook;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;

@RestController
@CrossOrigin
@RequestMapping("/login/facebook")
public class FacebookLoginController {
    private final FacebookLoginService facebookLoginService;

    public FacebookLoginController(FacebookLoginService facebookLoginService) {
        this.facebookLoginService = facebookLoginService;
    }
    @GetMapping("/app-id")
    public ResponseEntity<String> getAppId(){
        return ResponseEntity.ok(facebookLoginService.getAppId());
    }

    @PostMapping()
    public ResponseEntity<String> facebookLogin(@RequestBody FacebookLoginDto facebookLoginDto, HttpServletRequest request)  {
        try {
            facebookLoginService.facebookLogin(facebookLoginDto, request);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (LoginException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
