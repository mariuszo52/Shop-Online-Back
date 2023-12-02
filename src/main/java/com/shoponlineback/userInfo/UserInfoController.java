package com.shoponlineback.userInfo;

import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-info")

public class UserInfoController {
    private final UserInfoService userInfoService;

    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @PatchMapping("/edit")
    ResponseEntity<?> changeUserInfo(@RequestBody @Valid UserInfoDto userInfoDto){
    try{
        userInfoService.editUserInfo(userInfoDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    } catch (RuntimeException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    }
}
