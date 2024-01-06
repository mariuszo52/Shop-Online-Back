package com.shoponlineback.user.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UsernameUpdateDto {
    @NotNull
    private Long userId;
    @NotNull
    @Size(min = 1, max = 30)
    @Pattern(regexp = "\\S+", message = "Username needs to have 1-30 characters without blank spaces.")
    private String username;
}
