package com.shoponlineback.user.userManagement;

import com.shoponlineback.user.User;
import com.shoponlineback.user.UserDto;
import com.shoponlineback.user.dto.UserEmailUpdateDto;
import com.shoponlineback.user.dto.UsernameUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/user-management")
public class UserManagementController {
    private final UserManagementService userManagementService;

    public UserManagementController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @GetMapping("/all-users")
    ResponseEntity<?> getAllUsers(@RequestParam int page){
        Page<UserDto> allUsers = userManagementService.getAllUsers(page);
        return ResponseEntity.ok().body(allUsers);

    }
    @PutMapping("/username")
    ResponseEntity<?> updateUsername(@RequestBody UsernameUpdateDto usernameDto){
        try {
            userManagementService.updateUsername(usernameDto);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/email")
    ResponseEntity<?> updateEmail(@RequestBody UserEmailUpdateDto emailUpdateDto){
        try {
            userManagementService.updateEmail(emailUpdateDto);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/is-enabled")
    ResponseEntity<?> updateIsEnabled(@RequestBody UserIsEnabledUpdateDto isEnabledUpdateDto){
        try {
            userManagementService.updateIsEnabled(isEnabledUpdateDto);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/role")
    ResponseEntity<?> updateIsEnabled(@RequestBody UserRoleUpdateDto roleUpdateDto){
        try {
            userManagementService.updateUserRole(roleUpdateDto);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/user")
    ResponseEntity<?> deleteUser(Long userId) {
        try {
            userManagementService.deleteUser(userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/user")
    ResponseEntity<?> getUserByValues(@RequestParam String searchBy, String value){
        try {
            UserDto userDto = userManagementService.getUserByValues(searchBy, value);
            return ResponseEntity.ok(userDto);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
