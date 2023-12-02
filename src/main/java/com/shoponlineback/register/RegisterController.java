package com.shoponlineback.register;

import com.shoponlineback.exceptions.user.BadRegistrationDataException;
import com.shoponlineback.exceptions.userRole.UserRoleNotFoundException;
import com.shoponlineback.user.dto.UserRegisterDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Arrays;
import java.util.List;

@Controller
@CrossOrigin
public class RegisterController {
    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @ResponseBody
    @PostMapping("/register")
    ResponseEntity<String> registerNewUser(@Valid @RequestBody UserRegisterDto userRegisterDto) {
        try {
            registerService.registerUser(userRegisterDto);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (BadRegistrationDataException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/register/activate")
    RedirectView activateAccount(@RequestParam String activationToken) {
        try {
            registerService.activateAccount(activationToken);
            return new RedirectView("http://localhost:3000");

        } catch (RuntimeException e) {
            return new RedirectView("http://localhost:3000/error");
        }
    }

    @GetMapping("/register/suggest-pass")
    ResponseEntity<String> suggestPassword() {
        try {
            String password = registerService.suggestStrongPassword();
            return ResponseEntity.ok(password);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}