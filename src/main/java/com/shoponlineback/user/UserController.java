package com.shoponlineback.user;

import com.shoponlineback.user.dto.UserAccountInfoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.shoponlineback.user.UserService.getLoggedUser;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("")
    public ResponseEntity<?> getLoggedUserAccountInfo(){
        try {
            UserAccountInfoDto loggedUserInfo = userService.getLoggedUserAccountInfo();
            return ResponseEntity.ok(loggedUserInfo);
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
