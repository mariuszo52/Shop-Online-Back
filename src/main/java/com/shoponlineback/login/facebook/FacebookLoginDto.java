package com.shoponlineback.login.facebook;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class FacebookLoginDto {
    @NotNull
    @Email
    private String email;
    @NotNull
    @Size(min = 2)
    private String firstName;
    @NotNull
    @Size(min = 2)
    private String lastName;
    @NotNull
    private String userId;
}
