package com.shoponlineback.user.password;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChangeUserPasswordDto {
    @NotNull
    private String oldPassword;
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{8,}$",
            message = "Your password needs to be at least 8 characters long and use 4 different types of character" +
                    " (Lower Case, Upper Case, Digits, Special Characters).")
    private String newPassword;
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{8,}$",
            message = "Your password needs to be at least 8 characters long and use 4 different types of character" +
                    " (Lower Case, Upper Case, Digits, Special Characters).")
    private String confirmNewPassword;
}
