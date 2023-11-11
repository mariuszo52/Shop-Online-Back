package com.shoponlineback.user.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserLoginDto {
    @NotNull
    @Size(min = 5)
    private String username;
    @NotNull
    @Size(min = 3)
    private String password;
}
