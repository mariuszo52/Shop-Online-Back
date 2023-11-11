package com.shoponlineback.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDto {
    @NotNull
    @Size(min = 2)
    private String name;
    @NotNull
    @Size(min = 2)
    private String lastName;
    @NotNull
    @Size(min = 5)
    private String username;
    @NotNull
    @Email
    private String email;
    @NotNull
    @Email
    private String confirmEmail;
    @NotNull
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{8,}$",
            message = "Your password needs to be at least 8 characters long and use 4 different types of character" +
                    " (Lower Case, Upper Case, Digits, Special Characters).")
    private String password;
    @NotNull
    @Size(min = 3)
    private String confirmPassword;
}
