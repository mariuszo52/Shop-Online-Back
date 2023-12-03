package com.shoponlineback.login.standard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LoginResponseDto {
    private String accessToken;
    private String refreshToken;
}
