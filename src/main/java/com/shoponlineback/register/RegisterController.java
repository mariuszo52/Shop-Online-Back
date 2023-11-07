package com.shoponlineback.register;

import com.shoponlineback.exceptions.user.BadRegistrationDataException;
import com.shoponlineback.exceptions.userRole.UserRoleNotFoundException;
import com.shoponlineback.user.dto.UserRegisterDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class RegisterController {
    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping("/register")
    ResponseEntity<String> registerNewUser(@RequestBody UserRegisterDto userRegisterDto) {
        try {
            registerService.registerUser(userRegisterDto);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (BadRegistrationDataException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UserRoleNotFoundException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }
}
