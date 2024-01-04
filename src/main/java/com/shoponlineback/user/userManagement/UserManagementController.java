package com.shoponlineback.user.userManagement;

import com.shoponlineback.user.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user-management")
public class UserManagementController {
    private final UserManagementService userManagementService;

    public UserManagementController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @GetMapping("/all-users")
    ResponseEntity<?> getAllUsers(){
        List<UserDto> allUsers = userManagementService.getAllUsers();
        return ResponseEntity.ok().body(allUsers);

    }
}
