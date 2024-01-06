package com.shoponlineback.user.userManagement;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserIsEnabledUpdateDto {
    @NotNull
    private Long userId;
    @NotNull
    private Boolean isEnabled;
}
