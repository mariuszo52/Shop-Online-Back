package com.shoponlineback.shippingAddress;

public class ShippingAddressDtoMapper {
    public static ShippingAddressDto map(ShippingAddress shippingAddress){
        return ShippingAddressDto.builder()
                .address(shippingAddress.getAddress())
                .city(shippingAddress.getCity())
                .country(shippingAddress.getCountry())
                .phoneNumber(shippingAddress.getPhoneNumber())
                .postalCode(shippingAddress.getPostalCode()).build();
    }
}
