package com.shoponlineback.login.standard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class ResetPasswordDto {
    private String token;
    private String newPassword;
}
