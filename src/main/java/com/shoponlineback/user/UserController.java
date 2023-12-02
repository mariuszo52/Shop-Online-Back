package com.shoponlineback.user;

import com.shoponlineback.user.dto.UserAccountInfoDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.shoponlineback.user.UserService.getLoggedUser;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    ResponseEntity<?> getLoggedUserAccountInfo() {
        try {
            UserAccountInfoDto loggedUserInfo = userService.getLoggedUserAccountInfo();
            return ResponseEntity.ok(loggedUserInfo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("")
    ResponseEntity<?> deleteAccount(HttpServletRequest request) {
        try {
            userService.deleteAccount(request);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
