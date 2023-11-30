package com.shoponlineback.user.password;

import com.shoponlineback.exceptions.user.UserNotLoggedInException;
import jakarta.validation.Valid;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/password")
public class UserPasswordController {
    private final UserPasswordService userPasswordService;

    public UserPasswordController(UserPasswordService userPasswordService) {
        this.userPasswordService = userPasswordService;
    }

    @PostMapping("/")
    public ResponseEntity<?> changeUserPassword(@RequestBody @Valid ChangeUserPasswordDto passwordDto){
        try{
            userPasswordService.changePassword(passwordDto);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (UserNotLoggedInException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }
}
