package com.shoponlineback.shippingAddress;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ShippingAddressDto {
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
