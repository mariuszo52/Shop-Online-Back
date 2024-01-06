package com.shoponlineback.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserEmailUpdateDto {
    @NotNull
    private Long userId;
    @NotNull
    @Email
    private String email;
}
