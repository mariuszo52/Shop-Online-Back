package com.shoponlineback.user.userManagement;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleUpdateDto {
    @NotNull
    private Long userId;
    @NotNull
    private String role;
}
