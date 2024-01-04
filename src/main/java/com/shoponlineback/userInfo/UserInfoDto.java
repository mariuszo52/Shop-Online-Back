package com.shoponlineback.userInfo;

import com.shoponlineback.shippingAddress.ShippingAddressDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class UserInfoDto {
    @NotNull
    @Size(min = 2)
    private String name;
    @NotNull
    @Size(min = 2)
    private String lastName;
    private ShippingAddressDto shippingAddressDto;
}
