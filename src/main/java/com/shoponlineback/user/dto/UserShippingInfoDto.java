package com.shoponlineback.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
public class UserShippingInfoDto {
    @NotNull
    @Email
    private String email;
    @NotNull
    @Size(min = 2)
    private String name;
    @NotNull
    @Size(min = 2)
    private String lastName;
    @Size(min = 2)
    private String address;
    @Size(min = 2)
    private String city;
    @Size(min = 2)
    private String country;
    @Size(min = 5, max = 5)
    private String postalCode;
    @Size(min = 9, max = 9)
    private String phoneNumber;
}
