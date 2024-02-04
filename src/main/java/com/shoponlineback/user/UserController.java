package com.shoponlineback.user;

import com.shoponlineback.user.dto.UserShippingInfoDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            UserShippingInfoDto loggedUserInfo = userService.getLoggedUserAccountInfo();
            return ResponseEntity.ok(loggedUserInfo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/standard")
    ResponseEntity<?> deleteAccount(HttpServletRequest request) {
        try {
            userService.deleteStandardUserOrderProducts(request);
            userService.deleteStandardUser(request);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @DeleteMapping("/sm")
    ResponseEntity<?> deleteSocialMediaAccount() {
        try {
            userService.deleteSocialMediaUserOrdersProducts();
            userService.deleteSocialMediaUser();
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
